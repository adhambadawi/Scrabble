import java.util.*;

/**
 * Tiles models the 100 tiles we have for the game, including 98 letters and 2 blanks.
 *
 * @author Adham Badawi
 * @author Amr Abdelazeem
 * @author Shahrik Amin
 * @author Abdelrahman Aldayel
 *
 * @version 1.0 October 25, 2022
 */
public class Tiles {
    private ArrayList<String> rack; // Rack for player

    /**
     * Constructs a new rack of 7 random tiles
     */
    public Tiles() {
        rack = new ArrayList<String>();
        populateRack(rack);
    }

    public ArrayList<String> getRack() {return rack;}

    /**
     * Adds random tiles into the player's rack, completes the number of tiles in the players rack to 7 each round (until
     * the end of the game when the bank has no enough tiles). So at the beginning of the game will populate the player's
     * rack with 7 random tiles, then whenever the player uses tiles to play he will be re-populated with tiles to get him
     * back with 7 tiles to choose from in the next round.
     *
     * @param rack the player's rack of tiles
     */
    public static void populateRack(ArrayList<String> rack) {
        for(int i = rack.size(); i < 7; i++) {
            Random rand = new Random();
            int randomNum = rand.nextInt(Scrabble.lettersBank.size());
            rack.add(Scrabble.lettersBank.get(randomNum));
            Scrabble.lettersBank.remove(randomNum);
        }
    }
}
