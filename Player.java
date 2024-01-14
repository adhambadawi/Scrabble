import java.util.ArrayList;

/**
 * @author Amr Abdelazeem
 * @author Shahrik Amin
 * @author Abdelrahman Aldayel
 *
 * @version 1.0 October 25, 2022
 */
public class Player{

    private int roundScore;
    private int totalScore;

    private String playerName;

    protected ArrayList<String> playerRack; // contains the 7 letters for the player in the current round

    /**
     * Constructs a player with an initial round score and total score of 0 and gives the player a rack of 7 random tiles from the
     * tiles bank.
     */
    public Player(String playerName) {
        playerRack = new ArrayList<String>();
        Tiles.populateRack(playerRack);
        this.roundScore = 0;
        this.totalScore = 0;
        this.playerName = playerName;
    }

    /**
     * Constructs a player with a loaded name, round score, total score, and rack (used for loading a previous game)
     */
    public Player(String playerName, int roundScore, int totalScore, ArrayList<String> playerRackIn) {
        this.playerName = playerName;
        this.roundScore = roundScore;
        this.totalScore = totalScore;
        this.playerRack = playerRackIn;
    }
    /**
     * gets the player's round score (which is calculated by wordScore() in Scrabble class)
     * @return int an integer representation the player's current round score
     */
    public int getRoundScore() {
        return roundScore;
    }

    /**
     * sets the player's current round score with passed parameter
     * @param roundScore in integer representation of the player's current round score
     */
    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }

    /**
     * gets the player's total score (which is calculated by summing the rounds' scores)
     * @return int an integer representation the player's total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * sets the player's total score by adding the passed parameter (current round score) to the player's total score
     * @param roundScore in integer representation of the player's current round score
     */
    public void setTotalScore(int roundScore) {
        this.totalScore += this.roundScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<String> getPlayerRack() {
        return playerRack;
    }
}
