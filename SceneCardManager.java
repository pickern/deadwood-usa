// SceneCardManager class for Deadwood USA

import java.util.ArrayDeque ;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;


public class SceneCardManager {

  static private ArrayDeque<SceneCard> deck = new ArrayDeque<SceneCard>(40) ;
  static private ArrayDeque<SceneCard> activeScenes = new ArrayDeque<SceneCard>(10) ;
  static private ArrayDeque<SceneCard> discardPile = new ArrayDeque<SceneCard>(40) ;

  // Constructor
  public SceneCardManager(){

  }

  // Main for testing
  public static void main(String[] args){
    readCards() ;
  }

  // Fills deck in SceneCardManager
  public static void readCards(){
    try{
      File cardsXML = new File("cards.xml") ;
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance() ;
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder() ;
      Document doc = dBuilder.parse(cardsXML) ;
      NodeList cards = doc.getElementsByTagName("card") ;

      // Scene card attributes
      String name;
      int number;
      String description;
      int budget;
      Role[] cardRoles;
      String img ;
      int x ;
      int y ;
      int w ;
      int h ;

      // Role attributes
      String roleName;
      String line;
      int reqRank;
      int rx;
      int ry;

      for(int i = 0; i < cards.getLength(); i++){
        Element element = (Element) cards.item(i) ;
        name = cards.item(i).getAttributes().getNamedItem("name").getNodeValue() ;
        number = Integer.parseInt(element.getElementsByTagName("scene").item(0).getAttributes().getNamedItem("number").getNodeValue()) ;
        description = element.getElementsByTagName("scene").item(0).getTextContent() ;
        budget = Integer.parseInt(cards.item(i).getAttributes().getNamedItem("budget").getNodeValue()) ;
        img = cards.item(i).getAttributes().getNamedItem("img").getNodeValue() ;

        // roles
        NodeList roles = element.getElementsByTagName("part") ;
        cardRoles = new Role[roles.getLength()] ;
        for(int j = 0; j < roles.getLength(); j++){
          roleName = roles.item(j).getAttributes().getNamedItem("name").getNodeValue() ;
          reqRank = Integer.parseInt(roles.item(j).getAttributes().getNamedItem("level").getNodeValue()) ;
          line = ((Element)roles.item(j)).getElementsByTagName("line").item(0).getTextContent() ;
          rx = Integer.parseInt(((Element)roles.item(j)).getElementsByTagName("area").item(0).getAttributes().getNamedItem("x").getNodeValue()) ;
          ry = Integer.parseInt(((Element)roles.item(j)).getElementsByTagName("area").item(0).getAttributes().getNamedItem("y").getNodeValue()) ;
          cardRoles[j] = new Role(roleName, line, reqRank, rx, ry) ;
        }
        deck.add(new SceneCard(name, number, description, budget, cardRoles, img)) ;
      }

    }catch(Exception e){
      e.printStackTrace() ;
    }
  }

  // Assigns scene cards to rooms
  public static void deal(){
    for(Room room: Room.sets){
      // Sweep up any remaining scenes
      if(!discardPile.contains(room.currentScene) && room.currentScene != null){
        discard(room.currentScene) ;
        room.setScene(null) ;
      }
      SceneCard temp = deck.pop() ;
      activeScenes.add(temp) ; // Should end up with 10 cards
      room.setScene(temp) ;
    }


  }

  // To ensure that cards are random every time the game is played
  public static void shuffle(){

  }

  // Removes scene card from play when scene is wrapped
  static public void discard(SceneCard card){
    discardPile.add(card) ;
    activeScenes.remove(card) ;
  }


  // Returns number of scenes in play
  public static int activeScenes(){
    return activeScenes.size() ;
  }

  public static ArrayDeque<SceneCard> getActiveScenes(){

      return activeScenes;
  }

}
