import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * ScrabbleGUI (The View) models the user interface for the scrabble game.
 *
 * @author Adham Badawi
 * @author Amr Abdelazeem
 * @author Shahrik Amin
 * @author Abdulrahman Aldayel
 *
 * @version 2.0 November 13, 2022
 */
public class ScrabbleGUI implements Serializable {

    protected static JFrame scrabbleFrame, titleFrame;
    protected static JButton[][] tiles = new JButton[15][15];
    protected static JButton[] rack = new JButton[7];
    protected static JButton[] controlButtons = new JButton[6];
    //protected static JButton titleButtons[] = new JButton[2];

    protected static JButton startButton, continueButton;

    protected JMenuItem helpWithSpecialTiles, instructions, newGame, resetStatistics, helpWithLettersScore, saveGame, quitGame;

    protected String rackButtonPressed;

    protected JLabel statisticsText, gameNameTitleLabel; // Text area to prints the statistics the top of the frame

    protected static ArrayList<Point> DL;//the ArrayList contains the coordinates of DL tiles represented as points
    protected static ArrayList<Point> DW; //the ArrayList contains the coordinates of DW tiles represented as points
    protected static ArrayList<Point> TL; //the ArrayList contains the coordinates of TL tiles represented as points
    protected static ArrayList<Point> TW; //the ArrayList contains the coordinates of DW tiles represented as points

    /**
     * Constructs the initial title page that asks the user whether he wants to start a new game or continue the previous game
     */
    public ScrabbleGUI(){
        titleFrame = new JFrame("Game Progress");
        titleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        titleFrame.setSize(800,700);
        BorderLayout titleFrameGrid = new BorderLayout();
        titleFrame.setLayout(titleFrameGrid);

        JPanel gameNameTitlePanel = new JPanel();
        gameNameTitlePanel.setBounds(300, 300, 900, 350);
        gameNameTitlePanel.setBackground(Color.BLACK);
        gameNameTitleLabel = new JLabel("SCRABBLE");
        gameNameTitleLabel.setForeground(Color.WHITE);
        gameNameTitleLabel.setFont(new Font("Title", Font.ITALIC, 80));

        gameNameTitlePanel.add(gameNameTitleLabel);

        JPanel gameTitlePanel = new JPanel();
        gameTitlePanel.setBounds(50, 100, 50, 25);
        gameTitlePanel.setLayout(new GridLayout(0,1, 2, 2));
        gameTitlePanel.setBackground(Color.black);

        startButton = new JButton("Start New Game");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(null);
        continueButton= new JButton("Continue Previous Game");
        continueButton.setBackground(Color.BLACK);
        continueButton.setForeground(Color.WHITE);
        continueButton.setFocusPainted(false);
        continueButton.setBorder(null);

        gameTitlePanel.add(startButton);
        gameTitlePanel.add(continueButton);

        titleFrame.add(gameNameTitlePanel, BorderLayout.NORTH);
        titleFrame.add(gameTitlePanel, BorderLayout.CENTER);
        titleFrame.setVisible(true);
    }

    /**
     * Constructs the 15 x 15 Scrabble grid with the game buttons.
     */
    public ScrabbleGUI(Scrabble theModel){

        scrabbleFrame = new JFrame("Scrabble Game");
        scrabbleFrame.setSize(800, 700);
        BorderLayout scrabbleFrameGrid = new BorderLayout();
        scrabbleFrame.setLayout(scrabbleFrameGrid);

        JPanel tilesPanel = new JPanel();
        //tilesPanel.setBounds(0, 0, 700, 700);
        tilesPanel.setLayout(new GridLayout(15, 15, 5, 5));
        tilesPanel.setBackground(Color.BLACK);
        System.out.println(theModel.getCurrentPlayer());
        statisticsText = new JLabel("Statistics for " + theModel.getCurrentPlayer().getPlayerName() + ": " + "Round score: " + theModel.getCurrentPlayer().getRoundScore() + "       Total score: " + theModel.getCurrentPlayer().getTotalScore() + "        TilesPlayed: " + Board.numberOfOccupiedTiles);
        statisticsText.setBackground(Color.white);

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                tiles[i][j] = new JButton();
                if((i == 7) & (j == 7)) {
                    tiles[i][j].setBackground(Color.CYAN);
                }
                else{
                    tiles[i][j].setBackground(Color.WHITE);
                }
                tilesPanel.add(tiles[i][j]);
            }
        }
        DL = new ArrayList<>();
        DW = new ArrayList<>();
        TL = new ArrayList<>();
        TW = new ArrayList<>();

        String[] choices = { "EmptyBoard", "OriginalBoard", "RapidFireBoard"};
        String customBoard = (String) JOptionPane.showInputDialog(null, "Choose Custom Board Option...",
                "Customize Your Board", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        BoardConstructor constructPremiumTiles = new BoardConstructor(this, customBoard);
        constructPremiumTiles.loadBoardFromXMLFile();

        JPanel rackPanel = new JPanel();
        rackPanel.setLayout(new GridLayout(1, 7, 5, 5));

        for(int i = 0; i < 7; i++){
            rack[i] = new JButton(theModel.getCurrentPlayer().playerRack.get(i));
            rack[i].setBackground(Color.PINK);
            //rack[i].addActionListener(this); // ActionListener
            rackPanel.add(rack[i]);
        }

        JButton undoButton = new JButton("undo");
        JButton redoButton = new JButton("redo");
        JButton submitButton = new JButton("submit");
        JButton passButton = new JButton("pass");
        JButton otherPlayerRackButton = new JButton("Show Other Player Rack");
        JButton shuffleButton = new JButton("Shuffle Rack");

        JPanel controlPanel = new JPanel();
        controlPanel.add(undoButton);
        controlPanel.add(redoButton);
        controlPanel.add(submitButton);
        controlPanel.add(passButton);
        controlPanel.add(otherPlayerRackButton);
        controlPanel.add(shuffleButton);

        controlButtons[0] = undoButton;
        controlButtons[1] = redoButton;
        controlButtons[2] = submitButton;
        controlButtons[3] = passButton;
        controlButtons[4] = otherPlayerRackButton;
        controlButtons[5] = shuffleButton;

        for (int i = 0; i < 6; i++) {
            controlButtons[i].setBackground(Color.orange);
            //controlButtons[i].addActionListener(this); // ActionListener
        }

        JMenuBar menuBar = new JMenuBar();
        scrabbleFrame.setJMenuBar(menuBar);
        JMenu file = new JMenu("File");
        menuBar.add(file);

        saveGame = new JMenuItem("Save");
        KeyStroke keyStrokeToSave = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        saveGame.setAccelerator(keyStrokeToSave);
        newGame = new JMenuItem("New game");
        KeyStroke keyStrokeToNewGame = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        newGame.setAccelerator(keyStrokeToNewGame);
        quitGame = new JMenuItem("Quit");
        KeyStroke keyStrokeToQuitGame = KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK);
        quitGame.setAccelerator(keyStrokeToQuitGame);
        helpWithSpecialTiles = new JMenuItem("Help with special Tiles");
        helpWithLettersScore = new JMenuItem("Help with letter score");
        instructions = new JMenuItem("Game Instructions");
        resetStatistics = new JMenuItem("Reset statistics");
        file.add(saveGame);
        file.add(newGame);
        file.add(instructions);
        file.add(helpWithSpecialTiles);
        file.add(helpWithLettersScore);
        file.add(resetStatistics);
        file.add(quitGame);

        scrabbleFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                int confirmDialog = JOptionPane.showConfirmDialog(scrabbleFrame, "Are you sure you want to quit in the middle of the game?",
                        "Quit?", JOptionPane.YES_NO_OPTION);

                if (confirmDialog == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    scrabbleFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });

        scrabbleFrame.add(tilesPanel, scrabbleFrameGrid.CENTER);
        scrabbleFrame.add(statisticsText, scrabbleFrameGrid.NORTH);
        JPanel southOfFramePanel = new JPanel();
        BorderLayout southOfFrameGrid = new BorderLayout();
        southOfFramePanel.setLayout(southOfFrameGrid);
        southOfFramePanel.add(controlPanel, southOfFrameGrid.SOUTH);
        southOfFramePanel.add(rackPanel, southOfFrameGrid.NORTH);
        scrabbleFrame.add(southOfFramePanel, scrabbleFrameGrid.SOUTH);
        scrabbleFrame.setVisible(true);
    }

    /**
     * Undoes all the letters played on the board.
     */
    public void removeCurrentPlayerGUI() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                for (int k = 0; k < Scrabble.playedTilesCoordinates.size(); k++) {
                    if (Scrabble.playedTilesCoordinates.get(k).x == i && Scrabble.playedTilesCoordinates.get(k).y == j) {
                        tiles[i][j].setText("");
                    }
                }
            }
        }
        Scrabble.playedTilesCoordinates = new ArrayList<>();
        Scrabble.playedTiles = new ArrayList<>();
    }


    /**
     * Is passed an ActionListener object by the controller (ScrabbleController) and adds
     * it to the 15 x 15 tile buttons on the Scrabble grid.
     *
     * @param listenForTileButton the ActionListener for the TileButton
     */
    void addTileListener(ActionListener listenForTileButton){
        for(int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                tiles[i][j].addActionListener(listenForTileButton);
            }
        }
    }

    /**
     * Is passed an ActionListener object by the controller (ScrabbleController) and adds
     * it to the 7 rack buttons beneath the Scrabble grid.
     *
     * @param listenForRackButton the ActionListener for the RackButton
     */
    void addRackListener(ActionListener listenForRackButton){
        for(int i = 0; i < 7; i++) {
            rack[i].addActionListener(listenForRackButton);
        }
    }

    /**
     * Is passed an ActionListener object by the controller (ScrabbleController) and adds
     * it to the control buttons on the scrabble GUI.
     *
     * @param listenForControlButton the ActionListener for the control buttons
     */
    void addControlListener(ActionListener listenForControlButton){
        for (int i = 0; i < 6; i++) {
            controlButtons[i].addActionListener(listenForControlButton); // ActionListener
           }
        saveGame.addActionListener(listenForControlButton);
        helpWithSpecialTiles.addActionListener(listenForControlButton);
        instructions.addActionListener(listenForControlButton);
        newGame.addActionListener(listenForControlButton);
        quitGame.addActionListener(listenForControlButton);
    }

    void addGameTitleListener(ActionListener listenForGameProgressButton) {
        /*for (int i = 0; i < 2; i++) {
            titleButtons[i].addActionListener(listenForGameProgressButton); // ActionListener
        }*/
        startButton.addActionListener(listenForGameProgressButton);
        continueButton.addActionListener(listenForGameProgressButton);
    }


    /**
     * Contains the coordinates for all special tiles (DW, DL, TW, TL)
     * Responsible for setting coloring each type of sepecial tiles with a distinctive color
     */
    public void initializeSpecialTiles(int DLx[], int DLy[], int DWx[], int DWy[], int TLx[], int TLy[], int TWx[], int TWy[]){
        setColor(DLx, DLy, Color.GREEN);
        convertSpecialCoordinatesToPoints(DLx, DLy, DL);

        setColor(DWx, DWy, Color.YELLOW);
        convertSpecialCoordinatesToPoints(DWx, DWy, DW);

        setColor(TLx, TLy, Color.BLUE);
        convertSpecialCoordinatesToPoints(TLx, TLy, TL);

        setColor(TWx, TWy, Color.ORANGE);
        convertSpecialCoordinatesToPoints(TWx, TWy, TW);
    }

    /**
     * Takes two array of ints that contains x nad y of the special tiles coordinate, then it creates a new point
     * and save it in the given ArrayList parameter
     *
     * @param xCoordinates array of int contains the x of the special tiles coordinates
     * @param yCoordinates array of int contains the y of the special tiles coordinates
     * @param finalListOfPoints arrayList of Points where the new created points representing the tiles coordinates get saved
     */
    public void convertSpecialCoordinatesToPoints(int[] xCoordinates, int[] yCoordinates, ArrayList<Point> finalListOfPoints){
        for(int i = 0; i < xCoordinates.length; i++){
            finalListOfPoints.add(new Point(xCoordinates[i], yCoordinates[i]));
        }
    }

    private static void setColor(int[] x, int[] y, Color color) {
        for(int i = 0; i < x.length; i++) {
            tiles[x[i]][y[i]].setBackground(color); //set the color for the TW corresponding tiles with green
        }
    }
}
