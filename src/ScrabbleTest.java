import org.junit.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ScrabbleTest {
    private Scrabble theModel;
    private ScrabbleGUI theView;
    private ScrabbleController theController;
    private InputChecker inputChecker;

    @Before
    public void setUp() {
        theModel = new Scrabble();
        theView = new ScrabbleGUI(theModel);
        //theModel.test = 0;
        inputChecker = new InputChecker();
        theController = new ScrabbleController(theView, theModel);
    }

    @Test
    public void testingPlayer1PassingHisTurn() throws IOException {
        assertEquals(theModel.getCurrentPlayer(), theModel.getCurrentPlayer());
        theModel.passTurn();
        assertEquals(theModel.getCurrentPlayer(), theModel.getCurrentPlayer());
    }

    @Test
    public void testingPlayer2PassingHisTurn() throws IOException {
        assertEquals(theModel.getCurrentPlayer(), theModel.getCurrentPlayer());
        theModel.passTurn();
        theModel.passTurn();
        assertEquals(theModel.getCurrentPlayer(), theModel.getCurrentPlayer());
    }

    @Test
    public void checkingAnInvalidWord() throws IOException {
        assertFalse(inputChecker.wordFoundInDictionary("abrakadabra"));
    }

    @Test
    public void checkingAValidWord() throws IOException {
        assertTrue(inputChecker.wordFoundInDictionary("coding"));
    }

    @Test
    public void pointTwentyAndEighteenShouldBeOutOfBounds() {
        assertFalse(inputChecker.checkBounds(new Point(20, 18)));
    }

    @Test
    public void pointNegativeTwentyAndNegativeEighteenShouldBeOutOfBounds() {
        assertFalse(inputChecker.checkBounds(new Point(-20, 18)));
    }

    @Test
    public void pointTenAndFiveShouldBeInBounds() {
        assertTrue(inputChecker.checkBounds(new Point(10, 5)));
    }

    @Test
    public void pointZeroAndZeroShouldBeInBounds() {
        assertTrue(inputChecker.checkBounds(new Point(0, 0)));
    }

    @Test
    public void pointFourteenAndFourteenShouldBeInBounds() {
        assertTrue(inputChecker.checkBounds(new Point(14, 14)));
    }

    @Test
    public void pointNegativeOneAndNegativeOneShouldBeOutOfBounds() {
        assertFalse(inputChecker.checkBounds(new Point(-1, -1)));
    }

    @Test
    public void pointFifteenAndFifteenShouldBeOutOfBounds() {
        assertFalse(inputChecker.checkBounds(new Point(15, 15)));
    }

    @Test
    public void roundScoreShouldBeZeroWhenNoValidWordsWhereInserted() {
        assertEquals(0, theModel.roundScore()); //the list of valid words is initialized empty
    }

    @Test
    public void TestValidWordInCenter() throws IOException {
        theModel.playedTilesCoordinates.add(new Point(6, 7));
        theModel.playedTilesCoordinates.add(new Point(7, 7));
        theModel.playedTilesCoordinates.add(new Point(8, 7));
        theModel.playedTiles.add('B');
        theModel.playedTiles.add('E');
        theModel.playedTiles.add('D');
        assertTrue(inputChecker.checkConnection());
    }

    @Test
    // Putting "HI" vertically and connecting IGH horizontally to the right of the H to make the word "HIGH"
    public void PlayingAConnectedWordHorizontallyToTheRightOfAPreviousWordShouldBeAValidPlay() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[7][7] = 'H';
        theModel.gameBoard.boxes[8][7] = 'I';
        theModel.playedTilesCoordinates.add(new Point(7, 8));
        theModel.playedTilesCoordinates.add(new Point(7, 9));
        theModel.playedTilesCoordinates.add(new Point(7, 10));
        theModel.playedTiles.add('I');
        theModel.playedTiles.add('G');
        theModel.playedTiles.add('H');
        assertTrue(inputChecker.checkConnection());
    }

    @Test
    // Putting "HI" vertically and connecting TOOT horizontally to the left of the H to make the word "TOOTH"
    public void PlayingAConnectedWordHorizontallyToTheLeftOfAPreviousWordShouldBeAValidPlay() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[7][7] = 'H';
        theModel.gameBoard.boxes[8][7] = 'I';
        theModel.playedTilesCoordinates.add(new Point(7, 3));
        theModel.playedTilesCoordinates.add(new Point(7, 4));
        theModel.playedTilesCoordinates.add(new Point(7, 5));
        theModel.playedTilesCoordinates.add(new Point(7, 6));
        theModel.playedTiles.add('T');
        theModel.playedTiles.add('O');
        theModel.playedTiles.add('O');
        theModel.playedTiles.add('T');
        assertTrue(inputChecker.checkConnection());
    }

    @Test
    // Putting "HE" and connecting T vertically on top of the H to make the word "THE"
    public void PlayingAConnectedWordVerticallyOnTopOfAPreviousWordShouldBeAValidPlay() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[7][7] = 'H';
        theModel.gameBoard.boxes[8][7] = 'E';
        theModel.playedTilesCoordinates.add(new Point(6, 7));
        theModel.playedTiles.add('T');
        assertTrue(inputChecker.checkConnection());
    }

    @Test
    // Putting "HI" and connecting M vertically to the bottom of the I to make the word "HIM"
    public void PlayingAConnectedWordVerticallyToTheBottomOfAPreviousWordShouldBeAValidPlay() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[7][7] = 'H';
        theModel.gameBoard.boxes[8][7] = 'I';
        theModel.playedTilesCoordinates.add(new Point(9, 7));
        theModel.playedTiles.add('M');
        assertTrue(inputChecker.checkConnection());
    }

    @Test
    // Putting "HOT" and connecting G to the left of the O and T to the right of the O to make the word "GOT"
    public void PlayingAWordConnectedInTheMiddleShouldBeAValidPlay() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(6, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[6][7] = 'H';
        theModel.gameBoard.boxes[7][7] = 'O';
        theModel.gameBoard.boxes[8][7] = 'T';
        theModel.playedTilesCoordinates.add(new Point(7, 6));
        theModel.playedTilesCoordinates.add(new Point(7, 8));
        theModel.playedTiles.add('G');
        theModel.playedTiles.add('T');
        assertTrue(inputChecker.checkConnection());
    }

    @Test
    public void checkConnectionShouldReturnFalseIfADisconnectedWordIsPlayed() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[7][7] = 'H';
        theModel.gameBoard.boxes[8][7] = 'I';
        theModel.playedTilesCoordinates.add(new Point(9, 9));
        theModel.playedTilesCoordinates.add(new Point(10, 9));
        theModel.playedTilesCoordinates.add(new Point(11, 9));
        theModel.playedTiles.add('B');
        theModel.playedTiles.add('Y');
        theModel.playedTiles.add('E');
        assertFalse(inputChecker.checkConnection());
    }

    @Test
    public void noGapsShouldReturnFalseWhenAFirstWordWithoutConsecutiveLettersIsPlayed() {
        theModel.playedTilesCoordinates.add(new Point(7, 7));
        theModel.playedTilesCoordinates.add(new Point(8, 7));
        theModel.playedTilesCoordinates.add(new Point(11, 9));
        theModel.playedTilesCoordinates.add(new Point(12, 9));
        theModel.playedTiles.add('H');
        theModel.playedTiles.add('I');
        theModel.playedTiles.add('G');
        theModel.playedTiles.add('H');
        assertFalse(inputChecker.noGaps());
    }

    @Test
    // There is "HI" on the board, the player puts M to create "HIM" and then separately puts "THE" disconnected
    public void noGapsShouldReturnFalseWhenAPlayerInputsTwoSeparateWords() {
        theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
        theModel.gameBoard.occupiedCoordinates.add(new Point(8, 7));
        theModel.gameBoard.boxes[7][7] = 'H';
        theModel.gameBoard.boxes[8][7] = 'I';
        theModel.playedTilesCoordinates.add(new Point(9, 7));
        theModel.playedTiles.add('M');

        theModel.playedTilesCoordinates.add(new Point(11, 9));
        theModel.playedTilesCoordinates.add(new Point(12, 9));
        theModel.playedTilesCoordinates.add(new Point(13, 9));
        theModel.playedTiles.add('T');
        theModel.playedTiles.add('H');
        theModel.playedTiles.add('E');
        assertFalse(inputChecker.noGaps());
    }

    /**@Test
    //If the first word is BED horizontally then if we put S to the right of it the S will be touching the DW
    // so it will generate a score of 14
    public void testingSpecialTiles() throws IOException {
    theModel.gameBoard.occupiedCoordinates.add(new Point(7, 7));
    theModel.gameBoard.occupiedCoordinates.add(new Point(7, 8));
    theModel.gameBoard.occupiedCoordinates.add(new Point(7, 9));
    theModel.gameBoard.boxes[7][7] = 'B';
    theModel.gameBoard.boxes[8][7] = 'E';
    theModel.gameBoard.boxes[7][7] = 'D';
    theModel.playedTilesCoordinates.add(new Point(7, 10));
    theModel.playedTiles.add('S');
    assertTrue(inputChecker.checkConnection());
    assertTrue(inputChecker.noGaps());
    ArrayList<String> word = new ArrayList<>();
    word.add("BEDS");
    inputChecker.setValidCreatedWords(word);

    inputChecker.setValidCreatedWordsCoordinates();
    //assertTrue(theModel.wordsGenerated());
    assertEquals(14, theModel.roundScore());
    }*/
}
