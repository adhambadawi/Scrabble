import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Scrabble (The Model) models the scrabble game.
 *
 * @author Adham Badawi
 * @author Amr Abdelazeem
 * @author Shahrik Amin
 * @author Abdulrahman Aldayel
 *
 * @version 1.0 October 25, 2022
 */

public class Scrabble implements Serializable{

    private static int numberOfPlayers;
    private static int numberOfAIPlayers;
    public enum touchedCardinalDirection {NORTH, SOUTH, WEST, EAST} //Contains the direction for the touching letter of passed word
    private static ArrayList<Player> players;
    private static Player currentPlayer;
    private static int currentTurn = 0; //whose player turn is it
    protected static ArrayList<String> lettersBank  = new ArrayList<>();
    protected static HashMap<String, Integer> lettersPoints = new HashMap<>();
    static Board gameBoard = new Board();
    private InputChecker inputChecker = new InputChecker();
    // these attributes must be erased and updated each round
    protected static ArrayList<Point> undidTilesCoordinates = new ArrayList<>();
    protected static ArrayList<Character> undidTiles = new ArrayList<>();
    protected static ArrayList<Point> playedTilesCoordinates = new ArrayList<>(); // contains the coordinates of the inputted letters by the user in each round
    protected static ArrayList<Character> playedTiles = new ArrayList<>(); // contains the letters inputted by the user in each round
    protected static ArrayList<Integer> playedRackIndexes = new ArrayList<>(); //Contains an arrayList of the rack indexes used in current round
    protected static ArrayList<Integer> undidPlayedRackIndexes = new ArrayList<>();
    private static boolean touchesDWCoordinates = false; //represents if the current word coordinates touches special tile of DW type
    private static boolean touchesTWCoordinates = false; //represents if the current word coordinates touches special tile of TW type

    /**
     *  create a 15 x 15 grid that has 225 spaces for 98 letters and 2 blanks and initially gives each player 7 random
     *  letters (or blanks) out of the bank of letters. Initially players should have a score of 0 (both round and total)
     *  this constructor will delegate to other classes to do the work for it. Initializes new playedTiles, playedTilesCoordinates,
     *  and playedRackIndexes.
     */
    public Scrabble(){
        this.initializingLettersPoints(); // initializes the bank of 100 tiles (containing 98 letters and 2 blanks)
        this.initializingBank();
        this.initialisePlayers(); // initializes players by taking input from the user on how many players will be playing and which of them if any will be AI players.
    }

    /**
     * Constructor used for loading
     *
     */
    public Scrabble(Board gameBoard, ArrayList<Player> players, Player currentPlayerIn, ArrayList<String> lettersBank) {
        this.initializingLettersPoints();
        this.gameBoard = gameBoard;
        this.players = players;
        this.currentPlayer = currentPlayerIn;
        this.lettersBank = lettersBank;
    }

    public static Board getGameBoard() {
        return gameBoard;
    }

    public static void setGameBoard(Board gameBoard) {
        Scrabble.gameBoard = gameBoard;
    }

    public static ArrayList<Player> getPlayers() {return players;}

    public static Player getCurrentPlayer() {return currentPlayer;}

    public static ArrayList<Point> getPlayedTilesCoordinates() {
        return playedTilesCoordinates;
    }

    public static void setPlayedTilesCoordinates(ArrayList<Point> playedTilesCoordinates) {
        Scrabble.playedTilesCoordinates = playedTilesCoordinates;
    }

    public static ArrayList<Character> getPlayedTiles() {return playedTiles;}

    public static void setPlayedTiles(ArrayList<Character> playedTiles) {
        Scrabble.playedTiles = playedTiles;
    }

    /**
     * Creates the bank of 100 pieces by initializing 26 piece representing 26 letters and the points associated with it
     * and adds the pieces in the bank depending on their number of occurrence,
     */
    public void initializingLettersPoints(){
        lettersPoints.put("A", 1);
        lettersPoints.put("B", 3);
        lettersPoints.put("C", 3);
        lettersPoints.put("D", 2);
        lettersPoints.put("E", 1);
        lettersPoints.put("F", 4);
        lettersPoints.put("G", 2);
        lettersPoints.put("H", 4);
        lettersPoints.put("I", 1);
        lettersPoints.put("J", 8);
        lettersPoints.put("K", 5);
        lettersPoints.put("L", 1);
        lettersPoints.put("M", 3);
        lettersPoints.put("N", 1);
        lettersPoints.put("O", 1);
        lettersPoints.put("P", 3);
        lettersPoints.put("Q", 10);
        lettersPoints.put("R", 1);
        lettersPoints.put("S", 1);
        lettersPoints.put("T", 1);
        lettersPoints.put("U", 1);
        lettersPoints.put("V", 4);
        lettersPoints.put("W", 4);
        lettersPoints.put("X", 8);
        lettersPoints.put("Y", 4);
        lettersPoints.put("Z", 10);
        lettersPoints.put(" ", 0);
    }

    /**
     *
     */
    public void initializingBank() {
        addIntoBank("A", 9);
        addIntoBank("B", 2);
        addIntoBank("C", 2);
        addIntoBank("D", 4);
        addIntoBank("E", 12);
        addIntoBank("F", 2);
        addIntoBank("G", 3);
        addIntoBank("H", 2);
        addIntoBank("I", 9);
        addIntoBank("J", 1);
        addIntoBank("K", 1);
        addIntoBank("L", 4);
        addIntoBank("M", 2);
        addIntoBank("N", 6);
        addIntoBank("O", 8);
        addIntoBank("P", 2);
        addIntoBank("Q", 1);
        addIntoBank("R", 6);
        addIntoBank("S", 4);
        addIntoBank("T", 6);
        addIntoBank("U", 4);
        addIntoBank("V", 2);
        addIntoBank("W", 2);
        addIntoBank("X", 1);
        addIntoBank("Y", 2);
        addIntoBank("Z", 1);
        addIntoBank(" ", 2);
    }

    /**
     * Adds the specified piece to the bank based on its number of occurrences.
     * @param letter the letter to be added to the Bank of Tiles
     * @param times the number of occurrences of the letter
     */
    public void addIntoBank(String letter, int times) {
        for (int i = 0; i < times; i++) {
            lettersBank.add(letter);
        }
    }

    /**
     * lets the player pass his turn so the turn moves to the next player
     * @throws IOException if there is no currentPlayer initialized
     */
    //
    // this method simply calls changeTurn() when a player chooses to pass his turn to give turn to the next player
    public void passTurn() throws IOException {
        if(currentTurn < players.size() - 1) {
            currentTurn++;
        }
        else {
            currentTurn = 0;
        }
        currentPlayer = players.get(currentTurn);
        ScrabbleController.theView.statisticsText.setText("Statistics for " +currentPlayer.getPlayerName() +": " + "Round score: " + currentPlayer.getRoundScore() + "       Total score: " + currentPlayer.getTotalScore() + "        TilesPlayed: " + gameBoard.numberOfOccupiedTiles);
        if(currentPlayer instanceof AIPlayer){
            ((AIPlayer) currentPlayer).playRoundAI();
        }
    }

    /**
     * Calculates the player's round score by summing the score of the word(s) generated; each word's score
     * is calculated by summing the weights of each letter of the word, if one of the input letter is on a special tile
     * (DL, TL, DW, TW) the player gets extra score accordingly (i.e. if one of the inputted tiles is on a TW tile the
     * score of the whole word is tripled)
     * @return int the integer representation of the player's round score
     */
    public int roundScore() {
        int score = 0;
        int counter = 0; //cont the number of steps moved in the validCreatedWordsCoordinates arraylist
        for(int i = 0; i < inputChecker.getValidCreatedWords().size(); i++){ // traversing over the Strings in the Arraylist validCreatedWords
            String currentWord = inputChecker.getValidCreatedWords().get(i); // stores the current word in a temporary local variable
            for(int j = 0; j < currentWord.length(); j++){ // traversing over the Character in the current word
                String temp = String.valueOf(currentWord.charAt(j)); //convert the character at position j to a string and save it at temp
                score += (lettersPoints.get(temp)); //add letter corresponding number of points to the total score
                score  = additionalScore(inputChecker.getValidCreatedWordsCoordinates().get(counter),temp, score); //add special tiles score to the total score
                counter++;
            }
            if(touchesTWCoordinates){ //if one letter of the current word touches a TW coordinates then score gets multiplied by 3
                score *= 3;
                touchesTWCoordinates = false; //set touchesTWCoordinates to its initial state
            }
            else if(touchesDWCoordinates){ //if one letter of the current word touches a DW coordinates then score gets multiplied by 2
                score *= 2;
                touchesDWCoordinates = false; //set touchesDWCoordinates  to its initial state
            }
        }
        inputChecker.setValidCreatedWords(new ArrayList<>());
        inputChecker.setValidCreatedWordsNoDuplicate(new HashSet<>());
        inputChecker.setInvalidCreatedWordsNoDuplicate(new HashSet<>());
        inputChecker.setInvalidCreatedWords(new ArrayList<>());
        return score;
    }


    /**
     * This method responsible for checking if the passed letter is laying on a special tiles coordinates,
     * It determines the type of special coordinate touched and count the extra score corresponding for it
     * updates the original score with the new score including the extra points for the special tiles
     *
     * @param letterCoordinates Point represents the coordinates of the passed letter
     * @param letter string represents the character to be checked
     * @param score int represents the calculated score as if tiles are not special
     *
     * @return score represents the updated score after the special tiles extra points got added
     */
    public static int additionalScore(Point letterCoordinates, String letter, int score){

        //check if the given letter coordinate touches any of the DL special tiles coordinates
        if((ScrabbleGUI.DL.contains(letterCoordinates)) && (playedTilesCoordinates.contains(letterCoordinates))){
            return score += lettersPoints.get(letter); //double the score for the letter touching the double letter special tiles
        }
        //check if the given letter coordinate touches any of the TL special tiles coordinates
        else if((ScrabbleGUI.TL.contains(letterCoordinates)) && (playedTilesCoordinates.contains(letterCoordinates))){
            return score += lettersPoints.get(letter) * 2; //triple the score for the letter touching the triple letter special tiles
        }

        //check if the given letter coordinate touches any of the TW special tiles coordinates
        if((ScrabbleGUI.TW.contains(letterCoordinates)) && (playedTilesCoordinates.contains(letterCoordinates))){
            touchesTWCoordinates = true;
        }

        //check if the given letter coordinate touches any of the DW special tiles coordinates
        else if((ScrabbleGUI.DW.contains(letterCoordinates)) && (playedTilesCoordinates.contains(letterCoordinates))){
            touchesDWCoordinates = true;
        }
        return score;
    }

    /**
     * Compares the score of all players at the end of the game and returns the player with the highest score
     * @return Player the winner player at the end of the game
     */
    public Player winner(){
        Player winnerPlayer = players.get(0);
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getTotalScore() > winnerPlayer.getTotalScore()){
                winnerPlayer = players.get(i);
            }
        }
        return winnerPlayer;
    }


    /**
     * Populates the current player rack after he uses tiles from his rack to play
     */
    public void populateRackGUI(){
        for(int i =0; i < currentPlayer.playerRack.size(); i++){
            ScrabbleGUI.rack[i].setText(currentPlayer.playerRack.get(i));
        }
    }

    /**
     * Shuffles the current player rack
     */
    public void shuffleRack() {
        Random randomNum = ThreadLocalRandom.current();

        for (int i = currentPlayer.playerRack.size() - 1; i > 0; i--)
        {
            int index = randomNum.nextInt(i + 1);
            String shuffle = currentPlayer.playerRack.get(index);
            currentPlayer.playerRack.set(index, currentPlayer.playerRack.get(i));
            currentPlayer.playerRack.set(i, shuffle);
        }
        populateRackGUI();
    }


    /**
     * Initialize a list of players before starting the game. Takes number of players and AIPlayers through inputDialog.
     * @throws NumberFormatException
     */
    public void initialisePlayers() throws NumberFormatException {
        numberOfPlayers = 0;
        numberOfAIPlayers = 0;
        this.players = new ArrayList<>();
        /*
        if (ScrabbleGUI.gameState == 0 || ScrabbleGUI.gameState == 1) {
            return;
        }*/
        //Asking the user to enter the number of player
        while (numberOfPlayers <= 1 || numberOfPlayers > 5) {
            try {
                numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog(ScrabbleGUI.scrabbleFrame, "Please input number of players between 2 and 5"));
                if (numberOfPlayers > 5 || numberOfPlayers <= 1) {
                    numberOfPlayers = 0;
                    JOptionPane.showMessageDialog(ScrabbleGUI.scrabbleFrame, "number of players must be between 2 of 5");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ScrabbleGUI.scrabbleFrame, "Please input a valid number!");
            }
        }
        //Asking the user to enter the number of AI Players
        while (numberOfAIPlayers <= 0 || numberOfAIPlayers >= numberOfPlayers) {
            try {
                numberOfAIPlayers = Integer.parseInt(JOptionPane.showInputDialog(ScrabbleGUI.scrabbleFrame, "How many AI players?"));
                if(numberOfAIPlayers == 0){
                    break;
                }
                if (numberOfAIPlayers >= numberOfPlayers) {
                    JOptionPane.showMessageDialog(ScrabbleGUI.scrabbleFrame, "number of AI players must be smaller than total number of players");
                }
            } catch (Exception e) {
                numberOfAIPlayers = 0;
                JOptionPane.showMessageDialog(ScrabbleGUI.scrabbleFrame, "Please input a valid number!");
            }
        }

        for (int i = 0; i < numberOfPlayers - numberOfAIPlayers; i++) {
            String s = JOptionPane.showInputDialog(ScrabbleGUI.scrabbleFrame, "Please input name for player" + (i+1) + " (real Player)");
            if(s.equals("")){
                players.add(new Player("Anonymous player" + i+1)); //give the player Anonymous name if no name passed
            }
            else {
                players.add(new Player(s));
            }
        }
        for(int i = 0; i < numberOfAIPlayers; i++){
            String s = JOptionPane.showInputDialog(ScrabbleGUI.scrabbleFrame, "Please input name for AI player" + (i+1));
            if(s.equals("")) {
                players.add(new AIPlayer("Anonymous AI player" + i, this)); //give the player Anonymous name if no name passed
            }
            else {
                players.add(new AIPlayer(s, this));
            }
        }
        currentPlayer = players.get(currentTurn);
    }

    /**
     * Undoes letters played on the board one by one.
     */
    public void undoGui() {
        int lastIndex = this.playedTilesCoordinates.size() - 1;
        this.undidTilesCoordinates.add(this.playedTilesCoordinates.get(lastIndex));
        this.undidTiles.add(this.playedTiles.get(lastIndex));
        ScrabbleGUI.tiles[Scrabble.playedTilesCoordinates.get(lastIndex).x][this.playedTilesCoordinates.get(lastIndex).y].setText("");
        Board.boxes[this.playedTilesCoordinates.get(lastIndex).x][this.playedTilesCoordinates.get(lastIndex).y] = 0;
        this.playedTilesCoordinates.remove(lastIndex);
        this.playedTiles.remove(lastIndex);
        int rackIndex = this.playedRackIndexes.get(lastIndex);
        ScrabbleGUI.rack[rackIndex].setText(this.getCurrentPlayer().playerRack.get(rackIndex));
        this.playedRackIndexes.remove(lastIndex);
        this.undidPlayedRackIndexes.add(rackIndex);
    }

    /**
     *  Redoes the letters the player undid
     */
    public void redoGui() {
        int lastIndex = Scrabble.undidTiles.size() - 1;
        ScrabbleGUI.tiles[this.undidTilesCoordinates.get(lastIndex).x][this.undidTilesCoordinates.get(lastIndex).y].setText(String.valueOf(this.undidTiles.get(lastIndex)));
        Board.boxes[this.undidTilesCoordinates.get(lastIndex).x][this.undidTilesCoordinates.get(lastIndex).y] = this.undidTiles.get(lastIndex);
        char redoneTile = this.undidTiles.remove(lastIndex);
        Point redoneTileCoordinate = this.undidTilesCoordinates.remove(lastIndex);
        this.playedTilesCoordinates.add(redoneTileCoordinate);
        this.playedTiles.add(redoneTile);
        int rackIndex = this.undidPlayedRackIndexes.get(lastIndex);
        ScrabbleGUI.rack[rackIndex].setText("");
        this.playedRackIndexes.add(rackIndex);
        this.undidPlayedRackIndexes.remove(lastIndex);
    }

    /**
     * checks if the input of the player is valid by delegating to checkConnection(), noGaps(), and wordsGenerated()
     * (which uses checkValidity()), if valid calculates the players score and moves on to the next player, else prints
     * an error message based on the type of invalid play the player did (i.e. if the word the player inserted is not
     * connected it prints "Sorry, word not connected")
     * @throws IOException if one of the generated words by the user is not found in the dictionary
     */
    public void play() throws IOException {
        if (gameBoard.occupiedCoordinates.size() == 0) { // first play of the game
            Point startingPoint = new Point(7,7);
            if (playedTilesCoordinates.contains(startingPoint)) {
                if (inputChecker.checkConnection() && inputChecker.noGaps() && inputChecker.wordsGenerated()) {
                    validInputtedInserted();
                }
                else{
                    JOptionPane.showMessageDialog(ScrabbleController.theView.scrabbleFrame, "Sorry, " + inputChecker.stringRepresentationOfInvalidWords() + "not found in dictionary", "Invalid Play", JOptionPane.INFORMATION_MESSAGE);
                    int invalidInsertedTiles = playedTilesCoordinates.size();
                    for (int i = 0; i < invalidInsertedTiles; i++) {
                        ScrabbleController.theModel.undoGui();
                    }
                    setUpNextPlay();
                }
            }
            else {
                JOptionPane.showMessageDialog(ScrabbleController.theView.scrabbleFrame, "First word must be on Cyan Box", "Invalid First Play", JOptionPane.INFORMATION_MESSAGE);
                int invalidInsertedTiles = playedTilesCoordinates.size();
                for (int i = 0; i < invalidInsertedTiles; i++) {
                    ScrabbleController.theModel.undoGui();
                }
                setUpNextPlay();
            }
            return;
        }
        try {
            if (inputChecker.checkConnection() && inputChecker.noGaps() && inputChecker.wordsGenerated()) { //the inserted letters/word by the player are connected to a previous word and all words generated are valid
                validInputtedInserted();
            }
            else {
                if(!inputChecker.checkConnection() || !inputChecker.noGaps()){
                    JOptionPane.showMessageDialog(ScrabbleController.theView.scrabbleFrame, "Sorry, word not connected", "Invalid Play", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(ScrabbleController.theView.scrabbleFrame, "Sorry, " + inputChecker.stringRepresentationOfInvalidWords() + "not found in dictionary", "Invalid Play", JOptionPane.INFORMATION_MESSAGE);
                }
                int invalidInsertedTiles = playedTilesCoordinates.size();
                for (int i = 0; i < invalidInsertedTiles; i++) {
                    ScrabbleController.theModel.undoGui();
                }
                setUpNextPlay();
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * This method is called when the player's input was valid, it calculates the player's score and sets his round and
     * total score and prints them out in the game statistics. Removes the inserted tiles from the player's rack and
     * repopulates his rack with new tiles from the bank of tiles. Moves to the next player.
     */
    public void validInputtedInserted() throws IOException {
        currentPlayer.setRoundScore(roundScore());
        currentPlayer.setTotalScore(roundScore());
        gameBoard.numberOfOccupiedTiles += playedTiles.size();
        ScrabbleController.theView.statisticsText.setText("Statistics for " +currentPlayer.getPlayerName() +": " + "Round score: " + currentPlayer.getRoundScore() + "       Total score: " + currentPlayer.getTotalScore() + "        TilesPlayed: " + gameBoard.numberOfOccupiedTiles);
        for (int i = 0; i < playedTiles.size(); i++) { //If the inputted tiles are connected and valid, they are removed from the players rack and added to the gameBoard occupiedCoordinates
            gameBoard.occupiedCoordinates.add(playedTilesCoordinates.get(i));
            Character s = playedTiles.get(i);
            currentPlayer.playerRack.remove(s.toString());
        }
        Tiles.populateRack(currentPlayer.playerRack); //refill the player's rack
        setUpNextPlay();
        passTurn(); //pass the turn to the other player
        populateRackGUI(); //update rack buttons text
    }

    /**
     * Sets up the fields for the next round by initializing new playedTiles, playedRackIndexes, playedTilesCoordinates,
     * setValidCreatedWords, setValidCreatedWordsCoordinates, setInvalidCreatedWords, setCoordinatesTouched,
     * setMoveDirection ArrayLists. And new setValidCreatedWordsNoDuplicate and setInvalidCreatedWordsNoDuplicate HashSets.
     */
    public void setUpNextPlay(){
        playedTiles = new ArrayList<>();
        undidTiles = new ArrayList<>();
        undidTilesCoordinates = new ArrayList<>();
        undidPlayedRackIndexes = new ArrayList<>();
        playedRackIndexes = new ArrayList<>();
        playedTilesCoordinates = new ArrayList<>();
        inputChecker.setValidCreatedWordsNoDuplicate(new HashSet<>());
        inputChecker.setInvalidCreatedWordsNoDuplicate(new HashSet<>());
        inputChecker.setValidCreatedWords(new ArrayList<>());
        inputChecker.setValidCreatedWordsCoordinates(new ArrayList<>());
        inputChecker.setInvalidCreatedWords(new ArrayList<>());
        inputChecker.setCoordinatesTouched(new ArrayList<>());
        inputChecker.setMoveDirection(new ArrayList<>());
    }


    public static void loadData() {
        try{
            BufferedReader br = new BufferedReader(new FileReader("saveFile.txt"));
            for (int i = 0; i < players.size(); i++) { // loop over all the players
                currentPlayer = players.get(i);
                currentPlayer.setPlayerName(br.readLine());
                currentPlayer.setRoundScore(Integer.parseInt(br.readLine())); // readLine() can read only strings so to read an integer we need to convert the int to a String
                currentPlayer.setTotalScore(Integer.parseInt(br.readLine()));

                br.close(); // after reading the data from the text file we need to close the buffered reader
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCurrentPlayer(Player currentPlayer) {
        Scrabble.currentPlayer = currentPlayer;
    }

    public static void setPlayers(ArrayList<Player> players) {
        Scrabble.players = players;
    }


    public static int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public static ArrayList<String> getLettersBank() {
        return lettersBank;
    }


    public static void main(String[] args) throws IOException {
        ScrabbleGUI titlePageView = new ScrabbleGUI();
        ScrabbleController titlePageController = new ScrabbleController(titlePageView);
    }
}
