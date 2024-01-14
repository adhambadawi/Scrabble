
import java.awt.*;
import java.util.ArrayList;

/**
 * Board models the scrabble game board that consists of 225 boxes.
 *
 * @author Adham Badawi
 * @author Amr Abdelazeem
 * @author Shahrik Amin
 * @author Abdelrahman Aldayel
 *
 * @version 1.0 October 25, 2022
 */
// creates the grid of the game and keeps progress of what letters have been inserted on the grid
public class Board {

    protected static char[][] boxes;
    protected static ArrayList<Point> occupiedCoordinates; // contains the coordinates (points) of the occupied tiles of the board
    static int numberOfOccupiedTiles; // keeps track of the number of tiles containing letters/ N.B. use it to print to the users the number of tiles left: 100 - numberOfOccupiedTiles.

    // initializes the 15 x 15 grid

    /**
     * Constructs a new board of size 15 x 15 (containing 225 boxes) and since the board is still empty it sets the
     * numberOfOccupiedTilenew tiles()s to 0 and creates an array of 100 points to later on contain the position of each tile out of
     * the 100 tiles.
     */
    public Board() {
        boxes = new char[15][15];
        this.occupiedCoordinates = new ArrayList<>(); //creates an array of 100 points containing the coordinate of each tile in the board
        this.numberOfOccupiedTiles = 0;
    }

    /**
     * Constructor for loading
     *
     */
    public Board(char[][] boxes, ArrayList<Point> occupiedCoordinates, int numberOfOccupiedTiles) {
        this.boxes = boxes;
        this.occupiedCoordinates = occupiedCoordinates;
        this.numberOfOccupiedTiles = numberOfOccupiedTiles;
    }
    public static char[][] getBoxes() {
        return boxes;
    }

    public static void setBoxes(char[][] boxes) {
        Board.boxes = boxes;
    }
}
