import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * ScrabbleController (The Controller) mediates between Scrabble (The Model) and
 * ScrabbleGUI (The View) as per the MVC design pattern.
 *
 * @author Adham Badawi
 * @author Amr Abdelazeem
 * @author Shahrik Amin
 * @author Abdulrahman Aldayel
 *
 * @version 1.0 November 13, 2022
 */
public class ScrabbleController {
    protected static ScrabbleGUI theView;
    protected static Scrabble theModel;
    private SaveLoadToXML saveLoadToXML = new SaveLoadToXML();

    /**
     * Constructs the controller and adds the listeners to the view buttons.
     *
     * @param theModel the model class (Scrabble)
     * @param theView the view class (ScrabbleGUI)
     */
    public ScrabbleController(ScrabbleGUI theView, Scrabble theModel) {

        this.theView = theView;
        this.theModel = theModel;

        ScrabbleListener Listener = new ScrabbleListener();

        this.theView.addTileListener(Listener);
        this.theView.addRackListener(Listener);
        this.theView.addControlListener(Listener);
    }
    public ScrabbleController(ScrabbleGUI theView){
        this.theView = theView;

        ScrabbleListener Listener = new ScrabbleListener();

        this.theView.addGameTitleListener(Listener);
    }

    class ScrabbleListener implements ActionListener { // , MenuListener

        /**
         * Performs button click functionality in response to button click.
         *
         * @param e the action event (click)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == theView.helpWithSpecialTiles) {
                JFrame specialCoordinatesHelpFrame = new JFrame("Special Coordinates Help");
                specialCoordinatesHelpFrame.setLayout(new FlowLayout());

                //Creates four Jlabels and four colored buttons. where each one represents the corresponding representation of the special tiles
                JButton DW = new JButton();
                DW.setBackground(Color.YELLOW);
                DW.setEnabled(false);
                JLabel doubleWord = new JLabel("Tiles for double word are represented with this color:");
                JButton DL = new JButton();
                DL.setBackground(Color.GREEN);
                DL.setEnabled(false);
                JLabel doubleLetter = new JLabel("Tiles for double letter are represented with this color:");
                JButton TW = new JButton();
                TW.setBackground(Color.ORANGE);
                TW.setEnabled(false);
                JLabel tripleWord = new JLabel("Tiles for triple word are represented with this color:");
                JButton TL = new JButton();
                TL.setBackground(Color.BLUE);
                TL.setEnabled(false);
                JLabel tripleLetter = new JLabel("Tiles for triple letter are represented with this color:");

                //add the buttons and Jlabels to the main frame
                specialCoordinatesHelpFrame.add(doubleWord);
                specialCoordinatesHelpFrame.add(DW);
                specialCoordinatesHelpFrame.add(doubleLetter);
                specialCoordinatesHelpFrame.add(DL);
                specialCoordinatesHelpFrame.add(tripleWord);
                specialCoordinatesHelpFrame.add(TW);
                specialCoordinatesHelpFrame.add(tripleLetter);
                specialCoordinatesHelpFrame.add(TL);

                //set frame size and make it visble
                specialCoordinatesHelpFrame.setSize(375,135);
                specialCoordinatesHelpFrame.setResizable(false);
                specialCoordinatesHelpFrame.setVisible(true);
                specialCoordinatesHelpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
            if (e.getSource() == theView.instructions) {
                JOptionPane.showMessageDialog(theView.scrabbleFrame,
                        "In this game, there are max of 5 players. Some can be selected as AI Players. There is " + "\n" +
                                "a game board. Each player is given a rack. Player 1 begins by placing tiles on to the " + "\n" +
                                "board to form a word. For the first turn, the tiles must touch the turquoise tile. " + "\n" +
                                "Players can Undo, Submit, Pass, Show Rack or Shuffle Rack. Undo goes back to the " + "\n" +
                                "previous tile played. Submit submits and checks the tiles played for validity and " + "\n" +
                                "increments the player's score and passes to next player. Pass just passes to next " + "\n" +
                                "player. Shuffle rack shuffle's the current player's rack. The player with the most " + "\n" +
                                "points wins. Good luck and have fun!");
            }

            if (e.getSource() == theView.saveGame) {
                saveLoadToXML.saveGameToXMLFile();
                JOptionPane.showMessageDialog(theView.scrabbleFrame, "The progress has been saved");
                // SaveLoadToXML.save();
            }

            if (e.getSource() == theView.newGame) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to start a new in the middle of the game?", "New Game",
                        JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    theView.scrabbleFrame.dispose();
                    theModel = new Scrabble();
                    theView = new ScrabbleGUI(theModel);
                    theView.scrabbleFrame.setVisible(true);
                    //SwingUtilities.updateComponentTreeUI(theModel);
                    ScrabbleController scrabbleController = new ScrabbleController(theView, theModel);
                }
            }

            if (e.getSource() == theView.quitGame) {
                int confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to quit in the middle of the game?", "Game Quit",
                        JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (e.getSource() == theView.tiles[i][j]) {
                        if (theView.rackButtonPressed != null && !theView.rackButtonPressed.equals("") && theView.tiles[i][j].getText().equals("")) {
                            theModel.playedTilesCoordinates.add(new Point(i, j));

                            theView.tiles[i][j].setText(theView.rackButtonPressed);
                            theView.rackButtonPressed = "";
                            theModel.playedTiles.add(theView.tiles[i][j].getText().charAt(0));
                            Board.boxes[i][j] = theView.tiles[i][j].getText().charAt(0);
                        }
                    }
                }
            }
            for (int i = 0; i < 7; i++) {
                if (e.getSource() == theView.rack[i]) {
                    if (theView.rackButtonPressed == null || theView.rackButtonPressed.equals("")) {
                        theView.rackButtonPressed = theView.rack[i].getText();
                        theView.rack[i].setText("");
                        theModel.playedRackIndexes.add(i);
                    }
                }
            }

            if (e.getSource() == theView.startButton) { // Start a new game
                //theModel.gameState = theModel.PLAYING_STATE;
                JOptionPane.showMessageDialog(theView.scrabbleFrame, "Starting New Game");
                theView.titleFrame.dispose();
                Scrabble theModel = new Scrabble();
                ScrabbleGUI gameView = new ScrabbleGUI(theModel);
                ScrabbleController theController = new ScrabbleController(gameView,theModel);
                //theView.scrabbleFrame.setVisible(true);
            }

            if (e.getSource() == theView.continueButton) { // Continue previous game
                //theModel.load();
                JOptionPane.showMessageDialog(theView.scrabbleFrame, "Starting Previous Game");
                theView.titleFrame.dispose();
                saveLoadToXML.loadGameFromXMLFile(); // check implementation
                System.out.println(saveLoadToXML.getCurrentPlayer());
                Scrabble theModel = new Scrabble(new Board(saveLoadToXML.getBoxes(), saveLoadToXML.getOccupiedCoordinates(), saveLoadToXML.getNumberOfOccupiedTiles()), saveLoadToXML.getPlayerArrayList(), saveLoadToXML.getCurrentPlayer(), saveLoadToXML.getLettersBank());
                ScrabbleGUI gameView = new ScrabbleGUI(theModel);

                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if (saveLoadToXML.getBoxes()[i][j] != '\0') {
                            ScrabbleGUI.tiles[i][j].setText(String.valueOf(saveLoadToXML.getBoxes()[i][j]));
                        }
                    }
                }
//                ScrabbleGUI.tiles = saveLoadToXML.getTiles();
                ScrabbleController theController = new ScrabbleController(gameView,theModel);
//                saveLoadToXML.loadGameFromXMLFile(); // check implementation
                //theView.scrabbleFrame.setVisible(true);
            }

            if (e.getSource() == theView.controlButtons[0]) { // undo button
                theModel.undoGui();
            }

            if (e.getSource() == theView.controlButtons[1]) { // redo button
                theModel.redoGui();
                // JOptionPane.showMessageDialog(theView.scrabbleFrame, "redo button to be implemented");
            }

            if (e.getSource() == theView.controlButtons[2]) { // submit button
                try {
                    theModel.play();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (e.getSource() == theView.controlButtons[3]) { // pass button
                try {
                    theModel.passTurn();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                theView.removeCurrentPlayerGUI(); //Delete all the letters the player add to the board
                theModel.populateRackGUI();
            }

            if (e.getSource() == theView.controlButtons[4]) { // show other player rack button
                boolean AIPlays = false;
                for (int i = 0; i < theModel.getPlayers().size(); i++) {
                    if (theModel.getPlayers().get(i) instanceof AIPlayer) {
                        AIPlays = true;
                        break;
                    }
                }

                if (theModel.getPlayers().size() > 2) {
                    JOptionPane.showMessageDialog(ScrabbleGUI.scrabbleFrame, "you cannot show the rack since there is more than one player");
                }

                else if (AIPlays) {
                    JOptionPane.showMessageDialog(ScrabbleGUI.scrabbleFrame, "you cannot show the rack for the AI");
                }
                else{
                    JFrame frame = new JFrame("Other Player Rack");
                    frame.setLayout(new FlowLayout());
                    JButton rack[] = new JButton[7];
                    try {
                        theModel.passTurn();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    for (int i = 0; i < theModel.getCurrentPlayer().playerRack.size(); i++) {
                        rack[i] = new JButton(theModel.getCurrentPlayer().playerRack.get(i));
                        rack[i].setBackground(Color.PINK);
                        frame.add(rack[i]);
                    }
                    try {
                        theModel.passTurn();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.setSize(350, 90);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                }
            }

            if (e.getSource() == theView.controlButtons[5]) { // shuffle Button
                theModel.shuffleRack();
                theView.removeCurrentPlayerGUI();
            }
        }
    }
}
