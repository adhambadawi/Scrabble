import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.ArrayList;


public class BoardConstructor {

    private ScrabbleGUI board;
    private String customBoard;
    private ArrayList<int[]> customizedCoordinates;

    public BoardConstructor(ScrabbleGUI scrabbleGUI, String customBoard){
        this.board = scrabbleGUI;
        this.customBoard = customBoard;
        customizedCoordinates = new ArrayList<>();
    }

    public void loadBoardFromXMLFile() {

        if (customBoard.equals("EmptyBoard")) {return;}
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc;

            if(customBoard.equals("OriginalBoard")){
//                FileInputStream fis = new FileInputStream("CustomizableBoards/CustomBoard-1.xml");
//                InputSource is = new InputSource(fis);
//                doc = db.parse(is);
                doc = db.parse(this.getClass().getClassLoader().getResourceAsStream("CustomizableBoards/CustomBoard-1.xml"));
                System.out.println(doc);
            }
            else if(customBoard.equals("RapidFireBoard")){
                doc = db.parse(this.getClass().getClassLoader().getResourceAsStream("CustomizableBoards/CustomBoard-2.xml"));
            }
            else if(customBoard.equals("CustomBoard-3")){
                doc = db.parse(this.getClass().getClassLoader().getResourceAsStream("CustomizableBoards/CustomBoard-3.xml"));
            }
            else{
                doc = db.parse(this.getClass().getClassLoader().getResourceAsStream("CustomizableBoards/CustomBoard-4.xml"));
            }

            NodeList specialTileList = doc.getElementsByTagName("special-tile");
//            System.out.println(specialTileList.getLength());

            for (int i = 0; i < specialTileList.getLength(); i++) {
//                System.out.println("reached");
                Node specialTile = specialTileList.item(i);
                if (specialTile.getNodeType() == Node.ELEMENT_NODE) {
                    Element tileElement = (Element) specialTile;

                    String coordinatesxString = tileElement.getElementsByTagName("coordinatesx").item(0).getTextContent();
                    String[] coordinatesx = coordinatesxString.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\s", "").split(",");

                    int[] coordinatesxArray = new int[coordinatesx.length];

                    for (int j = 0; j < coordinatesx.length; j++) {
                        try {
                            coordinatesxArray[j] = Integer.parseInt(coordinatesx[j]);
                        } catch (NumberFormatException nfe) {
                            //NOTE: write something here if you need to recover from formatting errors
                        };
                    }

                    String coordinatesyString = tileElement.getElementsByTagName("coordinatesy").item(0).getTextContent();
                    String[] coordinatesy = coordinatesyString.replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\s", "").split(",");

                    int[] coordinatesyArray = new int[coordinatesx.length];

                    for (int j = 0; j < coordinatesy.length; j++) {
                        try {
                            coordinatesyArray[j] = Integer.parseInt(coordinatesy[j]);
                        } catch (NumberFormatException nfe) {
                            //NOTE: write something here if you need to recover from formatting errors
                        };
                    }

                    customizedCoordinates.add(coordinatesxArray);
                    customizedCoordinates.add(coordinatesyArray);

                }
//                System.out.println(Arrays.toString(coordinates.get(0)));
//                System.out.println(Arrays.toString(coordinates.get(1)));
            }

            board.initializeSpecialTiles(customizedCoordinates.get(0), customizedCoordinates.get(1), customizedCoordinates.get(2), customizedCoordinates.get(3), customizedCoordinates.get(4), customizedCoordinates.get(5), customizedCoordinates.get(6), customizedCoordinates.get(7));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
