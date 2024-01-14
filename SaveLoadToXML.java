import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class SaveLoadToXML {
    public static final String xmlFilePath = "src/CustomizableBoards/previousGame.xml";
    //    protected static char[][] boxes;
    private char[][] boxes;
    private ArrayList<Player> playerArrayList;
    private Player currentPlayer;

    //    private Board gameBoard; Flag
    private ArrayList<String> lettersBank;

    private ArrayList<Point> occupiedCoordinates;
    private int numberOfOccupiedTiles;

    private JButton[][] tiles;

    public SaveLoadToXML() {
        this.playerArrayList = new ArrayList<>();
        this.occupiedCoordinates = new ArrayList<>();
        this.boxes = new char[15][15];
        this.tiles = new JButton[15][15];
        this.lettersBank = new ArrayList<>();
    }

    public void saveGameToXMLFile() {
        try {
            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = df.newDocumentBuilder();
            Document doc = db.newDocument();

            // root element
            Element root = doc.createElement("Scrabble");
            doc.appendChild(root);

            Element currentPlayer = doc.createElement("currentPlayer");
            currentPlayer.appendChild(doc.createTextNode(Scrabble.getCurrentPlayer().getPlayerName()));
            root.appendChild(currentPlayer);

            Element lettersBank = doc.createElement("lettersBank");
            lettersBank.appendChild(doc.createTextNode(String.join(", ", Scrabble.getLettersBank())));
            root.appendChild(lettersBank);

            Element numOfRealPLayers = doc.createElement("numOfRealPLayers");
            numOfRealPLayers.appendChild(doc.createTextNode(Integer.toString(Scrabble.getNumberOfPlayers())));
            root.appendChild(numOfRealPLayers);

            Element occupiedTiles = doc.createElement("occupiedTiles");
            String input = Arrays.deepToString(Scrabble.gameBoard.getBoxes());
            input = input.replace("\0", "0");
            occupiedTiles.appendChild(doc.createTextNode(input));
            root.appendChild(occupiedTiles);

            // Player element
            ArrayList<Player> players = Scrabble.getPlayers();
            for (int i = 0; i < players.size(); i++) {
                Element player = doc.createElement("Player");
                root.appendChild(player);

                // elements under Player
                Element playerName = doc.createElement("playerName");
                playerName.appendChild(doc.createTextNode(players.get(i).getPlayerName()));
                //players.get(i).getPlayerName()
                player.appendChild(playerName);

                Element totalScore = doc.createElement("totalScore");
                totalScore.appendChild(doc.createTextNode(Integer.toString(players.get(i).getTotalScore())));
                //players.get(i).getTotalScore()
                player.appendChild(totalScore);

                Element roundScore = doc.createElement("roundScore");
                roundScore.appendChild(doc.createTextNode(Integer.toString(players.get(i).getRoundScore())));
                //players.get(i).getRoundScore()
                player.appendChild(roundScore);

                Element playerRack = doc.createElement("playerRack");
                playerRack.appendChild(doc.createTextNode(String.join(", ", players.get(i).getPlayerRack())));
                //players.get(i).getPlayerRack()
                player.appendChild(playerRack);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public void loadGameFromXMLFile() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.getClass().getClassLoader().getResourceAsStream("CustomizableBoards/previousGame.xml"));

            // HERE LOAD THE TILES ONTO THE BOARD
            NodeList occupiedTilesList = doc.getElementsByTagName("occupiedTiles");
            Node occupiedTiles = occupiedTilesList.item(0);
            if (occupiedTiles.getNodeType() == Node.ELEMENT_NODE) {
                Element tileElement = (Element) occupiedTiles;
                String test = tileElement.getTextContent();
                this.boxes = stringToDeep(test);
//                Board.boxes = this.boxes; Flag
                loadBoxesOntoGame(boxes);
            }

            // Letters Bank
            NodeList lettersBankNodeList = doc.getElementsByTagName("lettersBank");
            Node lettersBankNode = lettersBankNodeList.item(0);
            if (lettersBankNode.getNodeType() == Node.ELEMENT_NODE) {
                Element lettersBankElement = (Element) lettersBankNode;
                String string = lettersBankElement.getTextContent();
                this.lettersBank = new ArrayList<String>(Arrays.asList(string.split(", ")));
//                Board.boxes = this.boxes; Flag
                loadBoxesOntoGame(boxes);
            }

            // HERE LOAD PLAYER STUFF AND PlAYER SCORES.
            NodeList playerNodeList = doc.getElementsByTagName("Player");
            for (int i = 0; i < playerNodeList.getLength(); i++) {
//                System.out.println("reached");
                Node playerNode = playerNodeList.item(i);
                if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element playerElement = (Element) playerNode;
                    String playerName = playerElement.getElementsByTagName("playerName").item(0).getTextContent();

                    int totalScore = Integer.parseInt(playerElement.getElementsByTagName("totalScore").item(0).getTextContent());

                    int roundScore = Integer.parseInt(playerElement.getElementsByTagName("roundScore").item(0).getTextContent());

                    String playerRack = playerElement.getElementsByTagName("playerRack").item(0).getTextContent();

                    ArrayList<String> playerRackArrayList = new ArrayList<String>(Arrays.asList(playerRack.split(", ")));

                    Player currentPlayerObj = new Player(playerName, totalScore, roundScore, playerRackArrayList);
                    playerArrayList.add(currentPlayerObj);

                    NodeList currentPlayerNodeList = doc.getElementsByTagName("currentPlayer");
                    Node currentPlayerNode = currentPlayerNodeList.item(0);
                    if (currentPlayerNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element currentPlayerElement = (Element) currentPlayerNode;
                        String currentPlayerString = currentPlayerElement.getTextContent();

                        if(currentPlayerString.equals(playerName)) {
                            this.currentPlayer = currentPlayerObj;
                        }
                    }
                }
            }
//            Scrabble.setPlayers(playerArrayList); //Flag . To Remove ?
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }

    private void loadBoxesOntoGame(char [][] boxes) {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (boxes[i][j] != '\0') {
                    Point newPoint = new Point(i, j);
//                    ScrabbleGUI.tiles[i][j].setText(String.valueOf(boxes[i][j]));
//                    this.tiles[i][j].setText(String.valueOf(boxes[i][j]));
                    this.occupiedCoordinates.add(newPoint);
                    this.numberOfOccupiedTiles += 1;
                    System.out.println(numberOfOccupiedTiles);
//                    Board.occupiedCoordinates.add(newPoint); Flag
//                    Board.numberOfOccupiedTiles += 1; Flag
                }
            }
        }
    }

    private void loadPlayersOntoGame() {

    }

//    private static char[][] stringToDeep(String str) {
//
//        char[][] out = new char[15][15];
//
//        str = str.replaceAll("\\[", "").replaceAll("\\]", "");
//        String[] s1 = str.split(", ");
//
//        System.out.println(s1.toString()); // CONTINUE HERE
//        System.out.println(Arrays.toString(s1));
//
//
//        String s = "";
//        for (String n:s1)
//            s+= n;
//        char[] c = s.toCharArray();
//
//        for (int i = 0; i < 15; i++) {
//            for (int k = 0; k < 15; k++){
//                if (c[i] == '0') {
//                    out[i][k] = '\0';
//                } else {
//                    out[i][k] = c[i];
//                }
//            }
//
//            //System.out.println(s1[i] + "\t" + j + "\t" + i % col);
//        }
//        return out;
//    }

    private char[][] stringToDeep(String str) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '[') {
                row++;
            }
        }
        row--;
        for (int i = 0;; i++) {
            if (str.charAt(i) == ',') {
                col++;
            }
            if (str.charAt(i) == ']') {
                break;
            }
        }
        col++;

        char[][] out = new char[row][col];

        str = str.replaceAll("\\[", "").replaceAll("\\]", "");

        String[] s1 = str.split(", ");

        String s = "";
        for (String n:s1)
            s+= n;
        char[] c = s.toCharArray();

        int j = -1;
        for (int i = 0; i < c.length; i++) {
            if (i % col == 0) {
                j++;
            }
            if (c[i] != '0') {
                out[j][i % col] = c[i];
            }
            //System.out.println(s1[i] + "\t" + j + "\t" + i % col);
        }
        return out;
    }


    public char[][] getBoxes() {
        return boxes;
    }

    public ArrayList<Player> getPlayerArrayList() {
        return playerArrayList;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public ArrayList<Point> getOccupiedCoordinates() {
        return occupiedCoordinates;
    }

    public int getNumberOfOccupiedTiles() {
        return numberOfOccupiedTiles;
    }

    public JButton[][] getTiles() {
        return tiles;
    }

    public ArrayList<String> getLettersBank() {
        return lettersBank;
    }

}
