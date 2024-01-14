import java.awt.*;
import java.io.*;
import java.util.*;

public class InputChecker {

    private ArrayList<Point> coordinatesTouched; // contains the coordinates that the inputted letters touched
    private ArrayList<String> validCreatedWords; // contains all valid words created in this round stored as Strings
    private HashSet<String> validCreatedWordsNoDuplicate;// contains all valid words created in this round stored as Strings with no duplicates
    private ArrayList<String> invalidCreatedWords; // contains all invalid words created this round (should be empty if checkValidity is true)
    private HashSet<String> invalidCreatedWordsNoDuplicate; // contains all invalid words created this round (should be empty if checkValidity is true) with no duplicates
    private ArrayList<Scrabble.touchedCardinalDirection> moveDirection; //contains the moving direction toward the passed word

    public ArrayList<Point> getCoordinatesTouched() {
        return coordinatesTouched;
    }

    public void setCoordinatesTouched(ArrayList<Point> coordinatesTouched) {
        this.coordinatesTouched = coordinatesTouched;
    }

    public ArrayList<String> getValidCreatedWords() {
        return validCreatedWords;
    }

    public void setValidCreatedWords(ArrayList<String> validCreatedWords) {
        this.validCreatedWords = validCreatedWords;
    }

    public HashSet<String> getValidCreatedWordsNoDuplicate() {
        return validCreatedWordsNoDuplicate;
    }

    public void setValidCreatedWordsNoDuplicate(HashSet<String> validCreatedWordsNoDuplicate) {
        this.validCreatedWordsNoDuplicate = validCreatedWordsNoDuplicate;
    }

    public ArrayList<String> getInvalidCreatedWords() {
        return invalidCreatedWords;
    }

    public void setInvalidCreatedWords(ArrayList<String> invalidCreatedWords) {
        this.invalidCreatedWords = invalidCreatedWords;
    }

    public HashSet<String> getInvalidCreatedWordsNoDuplicate() {
        return invalidCreatedWordsNoDuplicate;
    }

    public void setInvalidCreatedWordsNoDuplicate(HashSet<String> invalidCreatedWordsNoDuplicate) {
        this.invalidCreatedWordsNoDuplicate = invalidCreatedWordsNoDuplicate;
    }

    public ArrayList<Scrabble.touchedCardinalDirection> getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(ArrayList<Scrabble.touchedCardinalDirection> moveDirection) {
        this.moveDirection = moveDirection;
    }

    public ArrayList<Point> getValidCreatedWordsCoordinates() {
        return validCreatedWordsCoordinates;
    }

    public void setValidCreatedWordsCoordinates(ArrayList<Point> validCreatedWordsCoordinates) {
        this.validCreatedWordsCoordinates = validCreatedWordsCoordinates;
    }

    private ArrayList<Point> validCreatedWordsCoordinates; //Contain the coordinates of all the letters for the valid created words

    public InputChecker(){
        validCreatedWordsCoordinates = new ArrayList<>();
        validCreatedWords = new ArrayList<>();
        validCreatedWordsNoDuplicate = new HashSet<>();
        invalidCreatedWordsNoDuplicate = new HashSet<>();
        invalidCreatedWords = new ArrayList<>();

    }

    /**
     * @param w the String to be checked whether its in the dictionary or not
     * @return true if the passed word is found in the dictionary, false otherwise
     * @throws IOException if the word is not found in the dictionary
     */
    public boolean wordFoundInDictionary(String w) throws IOException {
        File f1 = new File("src/Dictionary/Words.txt"); //Creation of File Descriptor for input file: insert the destination of your file
        String[] words = null;  //Initialize the word Array
        FileReader fr = new FileReader(f1);  //Creation of File Reader object
        BufferedReader br = new BufferedReader(fr); //Creation of BufferedReader object
        String s;
        String input = w;   // Input word to be searched (inserted by player)
        boolean found = false;
        while((s = br.readLine()) != null)   //Reading Content from the file
        {
            words=s.split(" ");  //Split the word using space
            for (String word : words)
            {
                if (word.equals(input.toLowerCase()))   //Search for the given word
                {
                    fr.close();      // If present close file reader and return true
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks all the connections between all the tiles inserted by the player and the tiles already inserted
     * @return true if tiles inputted by the user are connected to tiles that where already on the board, false otherwise
     */
    public boolean checkConnection() {
        coordinatesTouched = new ArrayList<>(); //initialize an empty arrayList for holding coordinates for the tiles touched by the passed word
        moveDirection = new ArrayList<>(); //initialize an empty arrayList for holding the direction for the touched tile with respect to the passed word

        if (Scrabble.getGameBoard().occupiedCoordinates.size() == 0){ //first play, as the only coordinates on the board are the coordinates of this play
            Point northOfFirstTile = new Point((Scrabble.getPlayedTilesCoordinates().get(0).x) - 1, Scrabble.getPlayedTilesCoordinates().get(0).y); //north tile of the current tile
            Point southOfFirstTile = new Point((Scrabble.getPlayedTilesCoordinates().get(0).x) + 1, Scrabble.getPlayedTilesCoordinates().get(0).y); //south tile of the current tile
            Point eastOfFirstTile = new Point(Scrabble.getPlayedTilesCoordinates().get(0).x, (Scrabble.getPlayedTilesCoordinates().get(0).y) + 1);  //east tile of the current tile
            Point westOfFirstTile = new Point(Scrabble.getPlayedTilesCoordinates().get(0).x, (Scrabble.getPlayedTilesCoordinates().get(0).y) - 1);  //west tile of the current tile

            if (Scrabble.getPlayedTilesCoordinates().contains(northOfFirstTile)) { //north dih point, fa7na benbous lw north mawgouda fi occupied coordinates 3ashan lw yes yeb2a el inserted letters by the user are connected
                coordinatesTouched.add(northOfFirstTile);
                moveDirection.add(Scrabble.touchedCardinalDirection.NORTH);
            }
            else if (Scrabble.getPlayedTilesCoordinates().contains(southOfFirstTile)) { // for the first tile we only need to check 1 connection to generate a word
                coordinatesTouched.add(southOfFirstTile);
                moveDirection.add(Scrabble.touchedCardinalDirection.SOUTH);
            }
            else if (Scrabble.getPlayedTilesCoordinates().contains(eastOfFirstTile)) { // for the first tile we only need to check 1 connection to generate a word
                coordinatesTouched.add(eastOfFirstTile);
                moveDirection.add(Scrabble.touchedCardinalDirection.EAST);
            } else if (Scrabble.getPlayedTilesCoordinates().contains(westOfFirstTile)) {
                coordinatesTouched.add(westOfFirstTile);
                moveDirection.add(Scrabble.touchedCardinalDirection.WEST);
            }
        }
        else{ // not the first play, so check connection with previously occupied coordinates on the board
            for (int i = 0; i < Scrabble.getPlayedTiles().size(); i++) { //traverse over the passed letters and check the connections of each letter
                Point north = new Point((Scrabble.getPlayedTilesCoordinates().get(i).x) - 1, Scrabble.getPlayedTilesCoordinates().get(i).y); //north tile of the current tile
                Point south = new Point((Scrabble.getPlayedTilesCoordinates().get(i).x) + 1, Scrabble.getPlayedTilesCoordinates().get(i).y); //south tile of the current tile
                Point east = new Point(Scrabble.getPlayedTilesCoordinates().get(i).x, (Scrabble.getPlayedTilesCoordinates().get(i).y) + 1);  //east tile of the current tile
                Point west = new Point(Scrabble.getPlayedTilesCoordinates().get(i).x, (Scrabble.getPlayedTilesCoordinates().get(i).y) - 1);  //west tile of the current tile

                // check if the current tile is connected to another tile located at the north
                if (checkBounds(north)) { //checks to see if point is on the board, doesn't check connection if out of bounds
                    if (Scrabble.getGameBoard().occupiedCoordinates.contains(north)) { //north dih point, fa7na benbous lw north mawgouda fi occupied coordinates 3ashan lw yes yeb2a el inserted letters by the user are connected
                        coordinatesTouched.add(north);
                        moveDirection.add(Scrabble.touchedCardinalDirection.NORTH);
                    }
                }
                // check if the current tile is connected to another tile located at the south
                if (checkBounds(south)) {
                    if (Scrabble.getGameBoard().occupiedCoordinates.contains(south)) {
                        coordinatesTouched.add(south);
                        moveDirection.add(Scrabble.touchedCardinalDirection.SOUTH);
                    }
                }
                // check if the current tile is connected to another tile located at the east
                if (checkBounds(east)) {
                    if (Scrabble.getGameBoard().occupiedCoordinates.contains(east)) {
                        coordinatesTouched.add(east);
                        moveDirection.add(Scrabble.touchedCardinalDirection.EAST);
                    }
                }
                // check if the current tile is connected to another tile located at the west
                if (checkBounds(west)) {
                    if (Scrabble.getGameBoard().occupiedCoordinates.contains(west)) {
                        coordinatesTouched.add(west);
                        moveDirection.add(Scrabble.touchedCardinalDirection.WEST);
                    }
                }
            }
        }
        //returns true if there is any connection established, false otherwise
        return !coordinatesTouched.isEmpty();
    }


    /**
     * checks the box coordinates to see if it is on the board
     * @param boxCoordinates a Point holding the box's coordinates
     * @return true if the given box coordinates is on the board, false otherwise
     */
    public boolean checkBounds(Point boxCoordinates) {
        int x = boxCoordinates.x;
        int y = boxCoordinates.y;
        if((x >= 0 & x <= 14) & (y >= 0 & y <= 14)) {
            return true;
        }
        return false;
    }

    /**
     * checks whether the player inputted his tiles this round vertically, if vertically then all tiles should be
     * inserted on the same column, meaning they have same y point value. Sorts the playedTilesCoordinates by x.
     * @return true if the player inputted just 1 tile or if the tiles inputted by the player this round are on the same column, false otherwise
     */
    public boolean Vertical(){
        int column = Scrabble.getPlayedTilesCoordinates().get(0).y;
        for(int i = 1 ; i < Scrabble.getPlayedTiles().size(); i++){
            if (Scrabble.getPlayedTilesCoordinates().get(i).y != column){
                return false;
            }
        }
        Collections.sort(Scrabble.getPlayedTilesCoordinates(), new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return Integer.valueOf(p1.x).compareTo(p2.x);
            }
        });
        return true;
    }

    /**
     * checks whether the player inputted his tiles this round horizontally, if horizontally then all tiles should be
     * inserted on the same row, meaning they have same x point value. Sorts the playedTilesCoordinates by y.
     * @return true if the player inputted just 1 tile or if the tiles inputted by the player this round are on the same row, false otherwise
     */
    public boolean Horizontal(){
        int row = Scrabble.getPlayedTilesCoordinates().get(0).x;
        for(int i = 1 ; i < Scrabble.getPlayedTiles().size(); i++){
            if (Scrabble.getPlayedTilesCoordinates().get(i).x != row){
                return false;
            }
        }
        Collections.sort(Scrabble.getPlayedTilesCoordinates(), new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return Integer.valueOf(p1.y).compareTo(p2.y);
            }
        });
        return true;
    }

    /**
     * @return true if the inputted tiles by the player doesn't create any gaps, false otherwise
     */
    public boolean noGaps() {
        if (Scrabble.getPlayedTiles().size() == 1) {return true;} // base case, if the user inputs only 1 tile then return true

        int movingDownStepCount = 1; // use this to traverse from the first inputted tile to the last with previous tile in between them, if there is
        int lastIndex = Scrabble.getPlayedTilesCoordinates().size() - 1;
        if (Vertical()) {
            int rowNumber = Scrabble.getPlayedTilesCoordinates().get(0).x;
            int columnNumber = Scrabble.getPlayedTilesCoordinates().get(0).y;
            int greatestX = Scrabble.getPlayedTilesCoordinates().get(lastIndex).x; // highest x value in the row from the inputted tiles
            Point nextCoordinate = new Point();
            while (nextCoordinate.x < greatestX) { // traversing from the top down (of the played tiles and anything in between) so we can save time instead of going from the middle up and down all the way down like what word generated does as this method can just check all the inputted tile and everything between them
                nextCoordinate = new Point(rowNumber + movingDownStepCount, columnNumber); // north tile of the current tile
                movingDownStepCount += 1;
                char boxToBeChecked = Board.boxes[nextCoordinate.x][nextCoordinate.y];
                if (boxToBeChecked == '\u0000') { // if that tile is empty then there is a gap between inserted tiles so return false
                    return false;
                }
            }
            return true; // if we reached this part it means that there are no gaps between the top inserted tile and the lowest inserted tile
        }
        if (Horizontal()) {
            int rowNumber = Scrabble.getPlayedTilesCoordinates().get(0).x;
            int columnNumber = Scrabble.getPlayedTilesCoordinates().get(0).y;
            int greatestY = Scrabble.getPlayedTilesCoordinates().get(lastIndex).y; // highest x value in the row from the inputted tiles
            Point nextCoordinate = new Point();
            while (nextCoordinate.y < greatestY) { // traversing from the top down (of the played tiles and anything in between) so we can save time instead of going from the middle up and down all the way down like what word generated does as this method can just check all the inputted tile and everything between them
                nextCoordinate = new Point(rowNumber, columnNumber + movingDownStepCount);
                movingDownStepCount += 1;
                char boxToBeChecked = Board.boxes[nextCoordinate.x][nextCoordinate.y];
                if (boxToBeChecked == '\u0000') { // if that tile is empty then there is a gap between inserted tiles so return false
                    return false;
                }
            }
            return true; // if we reached this part it means that there are no gaps between the leftmost inserted tile and the rightmost inserted tile
        }
        return false;
    }

    /**
     * Iterates over the all the connections created by the user input and generates words accordingly, then checks if the
     * created word is found in the dictionary or not and updates validCreatedWords with all created words (if any) found
     * in the dictionary, and updates invalidCreatedWords with all created words (if any) that were not found in the dictionary
     * @return true if the word(s) generated by the player are all found in the dictionary, false otherwise.
     */
    public boolean wordsGenerated() throws IOException {
        String word; // stores the generated word
        ArrayList<Point> generatedWordCoordinates; //stores the coordinates of the letters for the created word
        for (int i = 0; i < coordinatesTouched.size(); i++) { //traversing over letters' connections to generate words from them (each connection creates a word, inefficient should change that)
            int stepCount = 0;
            if ((moveDirection.get(i).equals(Scrabble.touchedCardinalDirection.NORTH)) || (moveDirection.get(i).equals(Scrabble.touchedCardinalDirection.SOUTH))) { //Check if there is any connections from north or south
                while (checkBounds(new Point((coordinatesTouched.get(i).x) - stepCount, coordinatesTouched.get(i).y)) &
                        (Scrabble.getGameBoard().boxes[(coordinatesTouched.get(i).x) - stepCount][coordinatesTouched.get(i).y] != 0)) {
                    stepCount += 1;
                }
                word = new String(); // I don't think we need that line - Adham
                generatedWordCoordinates = new ArrayList<>();
                Point startingLetterCoordinate = new Point((coordinatesTouched.get(i).x) - stepCount + 1, coordinatesTouched.get(i).y);
                int nextStep = 0;
                while ((Scrabble.getGameBoard().boxes[(startingLetterCoordinate.x) + nextStep][startingLetterCoordinate.y] != 0)) {
                    //update boxes when the player add button (update this at the action performed for the 225 buttons)
                    word = word + Scrabble.getGameBoard().boxes[(startingLetterCoordinate.x) + nextStep][startingLetterCoordinate.y];
                    generatedWordCoordinates.add(new Point(startingLetterCoordinate.x + nextStep, startingLetterCoordinate.y));
                    nextStep += 1;
                }
            }
            //Check if there is any connections from east or west directions. and count the word generated from going at this direction
            else{
                while (checkBounds(new Point(coordinatesTouched.get(i).x, (coordinatesTouched.get(i).y) - stepCount)) &
                        (Scrabble.getGameBoard().boxes[coordinatesTouched.get(i).x][(coordinatesTouched.get(i).y) - stepCount] != 0)) {
                    stepCount += 1;
                }
                word = new String();
                generatedWordCoordinates = new ArrayList<>();
                Point startingLetterCoordinate = new Point(coordinatesTouched.get(i).x, (coordinatesTouched.get(i).y) - stepCount + 1);
                int nextStep = 0;
                while ((Scrabble.getGameBoard().boxes[startingLetterCoordinate.x][(startingLetterCoordinate.y) + nextStep] != 0)) {
                    word = word + Scrabble.getGameBoard().boxes[startingLetterCoordinate.x][(startingLetterCoordinate.y) + nextStep];
                    generatedWordCoordinates.add(new Point(startingLetterCoordinate.x, startingLetterCoordinate.y+ nextStep));
                    nextStep += 1;
                }
            }
            if (word.contains(" ")){ // checks if the word generated has a blank tile
                String[] twoWordsSeperatedByASpace = word.split(" "); // if yes, it splits the word into 2 words, the previous word and the new word
                if (wordFoundInDictionary(twoWordsSeperatedByASpace[1])){ // checks to see if the new word generated after the blank tile is valid or not
                    validCreatedWordsNoDuplicate.add(twoWordsSeperatedByASpace[1]); // adds the word to validCreatedWordsNoDuplicate if its found in the dictionary
                    int sizeOfFirstHalf = twoWordsSeperatedByASpace[0].length() + 1;
                    for(i = 0; i < sizeOfFirstHalf; i++){
                        generatedWordCoordinates.remove(0); // removes the previous word coordinates and the blank tiles coordinates from generatedWordCoordinates
                    }
                    validCreatedWordsCoordinates.addAll(generatedWordCoordinates); // adds the coordinates of the new word to generatedWordCoordinates to then be checked for special tiles
                }
                else{ // if the word is invalid
                    invalidCreatedWordsNoDuplicate.add(twoWordsSeperatedByASpace[1]);
                }
            }
            else{
                if (wordFoundInDictionary(word)) {
                    validCreatedWordsNoDuplicate.add(word);
                    validCreatedWordsCoordinates.addAll(generatedWordCoordinates); //add the coordinates for the letter of the valid created word
                } else {
                    invalidCreatedWordsNoDuplicate.add(word);
                }
            }
        }
        //Add the created words to the suitable corresponding arrayList after removing the duplicates
        validCreatedWords.addAll(validCreatedWordsNoDuplicate);
        invalidCreatedWords.addAll(invalidCreatedWordsNoDuplicate);
        if(invalidCreatedWords.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * @return a String representation of the invalid words to be printed for the user when he creates an invalid word
     */
    public String stringRepresentationOfInvalidWords(){
        String listOfInvalidWords = "";
        for (int i = 0; i < invalidCreatedWordsNoDuplicate.size(); i++){
            listOfInvalidWords += invalidCreatedWords.get(i) + ", ";
        }
        return listOfInvalidWords;
    }
}
