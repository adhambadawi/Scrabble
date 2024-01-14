import java.awt.*;
import java.io.*;
import java.util.*;

public class AIPlayer extends Player {
    ArrayList<String> playableWords;
    Scrabble theModel;

    public AIPlayer(String AIPlayerName, Scrabble theModel) {
        super( AIPlayerName);
        playableWords = new ArrayList<>();
        this.theModel = theModel;
    }

    public AIPlayer(String AIplayerName, int roundScore, int totalScore, ArrayList<String> playerRackIn, Scrabble theModel) {
        super(AIplayerName, roundScore, totalScore, playerRackIn);
        playableWords = new ArrayList<>();
        this.theModel = theModel;
    }


    // CALL THIS FUNCTION FOR EACH OPEN LETTER ON THE BOARD
    public ArrayList<String> determinePlayableWordsAI(String attachedLetter) throws IOException {
        playableWords = new ArrayList<>();

        Scanner f1 = new Scanner(new File("src/Dictionary/Words.txt"));

        HashMap<String, Integer> playerRackWithOccurrences = new HashMap<String, Integer>();
        for (int i = 0; i < this.playerRack.size(); i++) { // If key doesn't exist, it add's with value of 1. If does exist increments value of key by 1.
            playerRackWithOccurrences.merge(this.playerRack.get(i).toLowerCase(), 1, Integer::sum);
        }

        playerRackWithOccurrences.merge(attachedLetter.toLowerCase(), 1, Integer::sum);

        while (f1.hasNext()) {
            HashMap<String, Integer> wordWithOccurrences = new HashMap<String, Integer>();
            String word = f1.next();
            for (int i = 0; i < word.length(); i++) {
                wordWithOccurrences.merge(Character.toString(word.charAt(i)), 1, Integer::sum);
            }

            if (playerRackWithOccurrences.entrySet().containsAll(wordWithOccurrences.entrySet()) && wordWithOccurrences.containsKey(attachedLetter.toLowerCase())) {
                if (word.length() > 2) {
                    playableWords.add(word);
                }
            }
        }
        
        Collections.sort(playableWords, Comparator.comparingInt(String::length));
        Collections.reverse(playableWords);

        return playableWords;
    }


    public void playRoundAI() throws IOException {
        for (int i = 0; i < Board.occupiedCoordinates.size(); i++) {
            Boolean isVerticallyOpen = false;
            Boolean isHorizontallyOpen = false;
            String attachedLetter = Character.toString(Board.boxes[Board.occupiedCoordinates.get(i).x][Board.occupiedCoordinates.get(i).y]);
            determinePlayableWordsAI(attachedLetter);

            if (Board.boxes[Board.occupiedCoordinates.get(i).x - 1][Board.occupiedCoordinates.get(i).y] == 0 && Board.boxes[Board.occupiedCoordinates.get(i).x + 1][Board.occupiedCoordinates.get(i).y] == 0) {
                isVerticallyOpen = true;
            } else if (Board.boxes[Board.occupiedCoordinates.get(i).x][Board.occupiedCoordinates.get(i).y - 1] == 0 && Board.boxes[Board.occupiedCoordinates.get(i).x][Board.occupiedCoordinates.get(i).y + 1] == 0) {
                isHorizontallyOpen = true;
            }

            if (!isHorizontallyOpen && !isVerticallyOpen) {
                continue;
            }

            if (isHorizontallyOpen) {
                // Horizontal Check
                for (int j = 0; j < playableWords.size(); j++) {
                    String word = playableWords.get(j);
                    boolean fits = true;

                    for (int k = 0; k < word.indexOf(attachedLetter); k++) {
                        if (!checkIfUnconnectedAI(Board.occupiedCoordinates.get(i), new Point(Board.occupiedCoordinates.get(i).x, Board.occupiedCoordinates.get(i).y-1-k))) {
                            fits = false;
                        }
                    }
                    for (int k = 0; k < (word.length() - word.indexOf(attachedLetter) - 1); k++) {
                        if (!checkIfUnconnectedAI(Board.occupiedCoordinates.get(i), new Point(Board.occupiedCoordinates.get(i).x, (Board.occupiedCoordinates.get(i).y+1+k)))) {
                            fits = false;
                        }
                    }

                    if (fits) {
                        playWordAI(true, word, attachedLetter, Board.occupiedCoordinates.get(i));
                        System.out.println(word);
                        return;
                    }
                }
            } else {
                // Vertical Check
                for (int j = 0; j < playableWords.size(); j++) {
                    String word = playableWords.get(j);
                    boolean fits = true;

                    for (int k = 0; k < word.indexOf(attachedLetter); k++) {
                        if (!checkIfUnconnectedAI(Board.occupiedCoordinates.get(i), new Point(Board.occupiedCoordinates.get(i).x-1-k, Board.occupiedCoordinates.get(i).y))) {
                            fits = false;
                        }
                    }
                    for (int k = 0; k < (word.length() - word.indexOf(attachedLetter) - 1); k++) {
                        if (!checkIfUnconnectedAI(Board.occupiedCoordinates.get(i), new Point(Board.occupiedCoordinates.get(i).x+1+k, (Board.occupiedCoordinates.get(i).y)))) {
                            fits = false;
                        }
                    }

                    if (fits) {
                        playWordAI(false, word, attachedLetter, Board.occupiedCoordinates.get(i));
                        return;
                    }
                }
            }

        }
        theModel.passTurn();
        theModel.populateRackGUI();
    }

    public boolean checkIfUnconnectedAI(Point attachedLetterPoint, Point pointToCheck) {
        // MEANS OUT OF BOUNDS!
        if (pointToCheck.y - 1 <= 0 || pointToCheck.y + 1 >=14 || pointToCheck.x + 1 >=14 || pointToCheck.x-1 <=0 ) {
            return false;
        }

        int pointToTheWest = Board.boxes[pointToCheck.x][pointToCheck.y - 1];
        int pointToTheEast = Board.boxes[pointToCheck.x][pointToCheck.y + 1];
        int pointToTheNorth = Board.boxes[pointToCheck.x - 1][pointToCheck.y];
        int pointToTheSouth = Board.boxes[pointToCheck.x + 1][pointToCheck.y];

        if (pointToTheWest == 0 && pointToTheEast == 0 && pointToTheSouth == 0 && pointToTheNorth == 0) {
            return true; // return true if it's touching nothing.
        } else if (pointToTheWest == 0 && pointToTheEast == 0 && pointToTheNorth == 0 && (attachedLetterPoint.x == pointToCheck.x + 1 && attachedLetterPoint.y == pointToCheck.y)) {
            return true; // return true if it's touching the attachedLetter. That is okay!
        } else if(pointToTheWest == 0 && pointToTheEast == 0 && (attachedLetterPoint.x == pointToCheck.x - 1 && attachedLetterPoint.y == pointToCheck.y) && pointToTheSouth == 0) {
            return true;
        } else if(pointToTheWest == 0 && (attachedLetterPoint.x == pointToCheck.x && attachedLetterPoint.y == pointToCheck.y + 1) && pointToTheNorth == 0 && pointToTheSouth == 0) {
            return true;
        } else if((attachedLetterPoint.x == pointToCheck.x && attachedLetterPoint.y == pointToCheck.y - 1) && pointToTheEast == 0 && pointToTheNorth == 0 && pointToTheSouth == 0) {
            return true;
        }

        return false;
    }

    // Actually plays / places the word onto the board.
    public void playWordAI(Boolean isHorizontal, String word, String attachedLetter, Point attachedLetterPoint) {
        word = word.toUpperCase();
        if (isHorizontal) {

            //place the letters located at the left of the attached tile
            for (int k = 0; k < word.indexOf(attachedLetter); k++) {

                //Here we play as if a regular player added the tiles to the board and then call the submit method

                //Adding the coordinates of the played letter to the playedTiles Coordnates
                theModel.playedTilesCoordinates.add(new Point(attachedLetterPoint.x, attachedLetterPoint.y-1-k));
                //Adding the letter to the playedTiles
                char letterToBeAdded = Character.toUpperCase(word.charAt(word.indexOf(attachedLetter)-k-1));

                theModel.playedTiles.add(letterToBeAdded);
                //Adding the letter to Boxes
                Board.boxes[attachedLetterPoint.x][attachedLetterPoint.y-1-k] = letterToBeAdded;
                //Adding to the tiles
                ScrabbleGUI.tiles[attachedLetterPoint.x][attachedLetterPoint.y-1-k].setText(String.valueOf(letterToBeAdded));
            }

            //place the letters located at the right of the attached tile
            for (int k = 0; k < (word.length() - word.indexOf(attachedLetter) - 1); k++) {
                //Adding the coordinates of the played letter to the playedTiles Coordnates
                theModel.playedTilesCoordinates.add(new Point(attachedLetterPoint.x, attachedLetterPoint.y+1+k));
                //Adding the letter to the playedTiles
                char letterToBeAdded = Character.toUpperCase(word.charAt(word.indexOf(attachedLetter)+k+1));
                theModel.playedTiles.add(letterToBeAdded);

                //Adding the letter to Boxes
                Board.boxes[attachedLetterPoint.x][attachedLetterPoint.y+1+k] = letterToBeAdded;
                //Adding to the tiles
                ScrabbleGUI.tiles[attachedLetterPoint.x][attachedLetterPoint.y+1+k].setText(String.valueOf(letterToBeAdded));
            }
        } else {
            for (int k = 0; k < word.indexOf(attachedLetter); k++) {
                //Adding the coordinates of the played letter to the playedTiles Coordnates
                theModel.playedTilesCoordinates.add(new Point(attachedLetterPoint.x-1-k, attachedLetterPoint.y));
                //Adding the letter to the playedTiles
                char letterToBeAdded = Character.toUpperCase(word.charAt(word.indexOf(attachedLetter)-k-1));

                theModel.playedTiles.add(letterToBeAdded);
                //Adding the letter to Boxes
                Board.boxes[attachedLetterPoint.x-1-k][attachedLetterPoint.y] = letterToBeAdded;
                //Adding to the tiles
                ScrabbleGUI.tiles[attachedLetterPoint.x-1-k][attachedLetterPoint.y].setText(String.valueOf(letterToBeAdded));
            }
            for (int k = 0; k < (word.length() - word.indexOf(attachedLetter) - 1); k++) {
                //Adding the coordinates of the played letter to the playedTiles Coordnates
                theModel.playedTilesCoordinates.add(new Point(attachedLetterPoint.x+1+k, attachedLetterPoint.y));
                //Adding the letter to the playedTiles
                char letterToBeAdded = Character.toUpperCase(word.charAt(word.indexOf(attachedLetter)+k+1));

                theModel.playedTiles.add(letterToBeAdded);
                //Adding the letter to Boxes
                Board.boxes[attachedLetterPoint.x+1+k][attachedLetterPoint.y] = letterToBeAdded;
                //Adding to the tiles
                ScrabbleGUI.tiles[attachedLetterPoint.x+1+k][attachedLetterPoint.y].setText(String.valueOf(letterToBeAdded));
            }
        }
        try {
            theModel.play();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
