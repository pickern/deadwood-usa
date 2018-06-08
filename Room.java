// Room class for Deadwood USA

import java.lang.StringBuilder ;
import java.util.Arrays ;
import java.util.* ;
import java.util.Random ;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;

public class Room{

  // Attributes
  public int x ;
  public int y ;
  public int w ;
  public int h ;
  public int[][] shotLocations ;
  public String roomName ;
  public int shotMarkers ;
  static Random rand = new Random() ;
  public int shotsRemaining ;
  public SceneCard currentScene ;
  public Role[] extraRoles ; // Roles should be listed highest paid to lowest
  public String[] adjacentRooms ;
  public ArrayDeque<Player> playersInRoom ;
  static Room trailers ; // Special rooms
  static Room office ;
  static int[][] upgradeTable ; //upgradeTable[][0] is money cost, [][1] is fame cost
  static ArrayDeque<Room> sets = new ArrayDeque<Room>() ; // For rooms with sceneCards

  ///* Main method for testing
  public static void main(String[] args){
    readRooms() ;
    for(Room room: sets){
      System.out.println(room.extraRoles[0].name);
      System.out.println("x1:" + room.extraRoles[0].x + " y1:" + room.extraRoles[0].y);
    }
  }//*/

  // Constructor
  public Room(String name, int shots, String[] adjacents, Role[] roles){
    roomName = name ;
    shotMarkers = shots ;
    shotsRemaining = shots ;
    adjacentRooms = adjacents ;
    extraRoles = roles ;
    playersInRoom = new ArrayDeque<Player>() ;
  }

  // Alternate contructor for BoardDisplay
  public Room(String name, int shots, String[] adjacents, Role[] roles, int x, int y, int w, int h, int[][] shotLocations){
    roomName = name ;
    shotMarkers = shots ;
    shotsRemaining = shots ;
    adjacentRooms = adjacents ;
    extraRoles = roles ;
    this.shotLocations = shotLocations ;
    this.x = x ;
    this.y = y ;
    this.w = w ;
    this.h = h ;
    playersInRoom = new ArrayDeque<Player>() ;
  }
  // Reads in info from board.xml
  public static void readRooms(){
    try{
      File cardsXML = new File("board.xml") ;
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance() ;
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder() ;
	    Document doc = dBuilder.parse(cardsXML) ;
      NodeList sets = doc.getElementsByTagName("set") ;
      NodeList parts = doc.getElementsByTagName("parts") ;

      // Room constructor parameters
      String roomName ;
      int shots ;
      String[] adjacentRooms ;
      Role[] roomRoles ;
      int[][] shotLocations ;
      int x ;
      int y ;
      int w ;
      int h ;

      // Role constructor parameters
      String roleName ;
      String line ;
      int reqRank ;
      int rx;
      int ry;

      // Create sets
      for(int i = 0; i < sets.getLength(); i++){
        // Set we're creating
        Element set = (Element) sets.item(i) ;

        roomName = set.getAttributes().getNamedItem("name").getNodeValue() ;
        shots = set.getElementsByTagName("take").getLength() ;

        // Creates String[] of adjacent rooms
        NodeList neighbors = set.getElementsByTagName("neighbor") ;
        adjacentRooms = new String[neighbors.getLength()] ;
        for(int k = 0; k < neighbors.getLength(); k++){
          adjacentRooms[k] = neighbors.item(k).getAttributes().getNamedItem("name").getNodeValue();
        }

        // Create Roles for set
        NodeList roles = set.getElementsByTagName("part") ;
        roomRoles = new Role[roles.getLength()] ;
        for(int j = 0; j < roles.getLength(); j++){
          roleName = roles.item(j).getAttributes().getNamedItem("name").getNodeValue() ;
          reqRank = Integer.parseInt(roles.item(j).getAttributes().getNamedItem("level").getNodeValue()) ;
          line = ((Element)roles.item(j)).getElementsByTagName("line").item(0).getTextContent() ;
          rx = Integer.parseInt(((Element)roles.item(j)).getElementsByTagName("area").item(0).getAttributes().getNamedItem("x").getNodeValue()) ;
          ry = Integer.parseInt(((Element)roles.item(j)).getElementsByTagName("area").item(0).getAttributes().getNamedItem("y").getNodeValue()) ;
          roomRoles[j] = new Role(roleName, line, reqRank, rx, ry) ;
        }

        // Get shot marker locations
        NodeList takes = set.getElementsByTagName("take") ;
        shotLocations = new int[takes.getLength()][2] ;
        for(int j = 0; j < takes.getLength(); j++){
          shotLocations[j][0] = Integer.parseInt(((Element)takes.item(j)).getElementsByTagName("area").item(0).getAttributes().getNamedItem("x").getNodeValue()) ;
          shotLocations[j][1] = Integer.parseInt(((Element)takes.item(j)).getElementsByTagName("area").item(0).getAttributes().getNamedItem("y").getNodeValue()) ;
        }

        // Get GUI attributes
        Node area = set.getElementsByTagName("area").item(0) ;
        x = Integer.parseInt(area.getAttributes().getNamedItem("x").getNodeValue()) ;
        y = Integer.parseInt(area.getAttributes().getNamedItem("y").getNodeValue()) ;
        w = Integer.parseInt(area.getAttributes().getNamedItem("w").getNodeValue()) ;
        h = Integer.parseInt(area.getAttributes().getNamedItem("h").getNodeValue()) ;

        // Add to sets
        Room.sets.add(new Room(roomName, shots, adjacentRooms, roomRoles, x, y, w, h, shotLocations)) ;
      }

      // Create trailers and casting office
      Element trailerE = (Element) doc.getElementsByTagName("trailer").item(0) ;
      Element officeE = (Element) doc.getElementsByTagName("office").item(0) ;
      String[] tadjacentRooms = new String[3] ;
      String[] oadjacentRooms = new String[3] ;
      NodeList tneighbors = trailerE.getElementsByTagName("neighbor") ;
      NodeList oneighbors = officeE.getElementsByTagName("neighbor") ;
      // Read in neighbors
      for(int i = 0; i < 3; i++){
        tadjacentRooms[i] = tneighbors.item(i).getAttributes().getNamedItem("name").getNodeValue() ;
        oadjacentRooms[i] = oneighbors.item(i).getAttributes().getNamedItem("name").getNodeValue() ;
      }

      Room.trailers = new Room("Trailer", -1, tadjacentRooms, null) ;
      Room.office = new Room("Office", -1, oadjacentRooms, null) ;

      // Create upgrade table
      upgradeTable = new int[5][2] ;
      for(int i = 0; i < 5; i++){
        if(i == 0){
          upgradeTable[i][0] = 4 ;
        } else{
          upgradeTable[i][0] = upgradeTable[i-1][0] + 2*(i+2) ;
        }
        upgradeTable[i][1] = 5 * i ;
      }

    }catch(Exception e){
      e.printStackTrace() ;
    }

  }

  // Returns a String with room information
  public String toString(){
    StringBuilder sb = new StringBuilder() ;
    sb.append(roomName) ;
    if(this.currentScene != null){
      // sb.append(currentScene.toString())
    }
    // Won't add roles for casting office and trailers
    if(this.extraRoles != null){
      sb.append("\n Roles") ;
      for(int i = 0; i < extraRoles.length; i++){
        sb.append("\n" + extraRoles[i].name) ;
      }
    }
    if(currentScene != null && currentScene.flipped){
      sb.append(currentScene.toString());
    }

    return sb.toString() ;
  }

  // For player movement

  // Shows available moves for player
  public String getMoves(){
    StringBuilder sb = new StringBuilder() ;
    for(int i = 0; i < adjacentRooms.length; i++){
      sb.append(adjacentRooms[i] + "\n") ;
    }
    return sb.toString() ;
  }

  // Returns a Room given String with roomName ** fixed compatability with lower case*
  public static Room stringToRoom(String name){
      name = name.toLowerCase();


    if(name.equals("trailer")){
      return Room.trailers ;
    } else if(name.equals("office")){
      return Room.office ;
    } else {
      for(Room room: sets){
        if(room.roomName.toLowerCase().equals(name)){
          return room ;
        }
      }
    }
    System.out.println("No room found") ;
    return null ;
  }

  // Player leaves
  public void exit(Player player){
    playersInRoom.remove(player) ;
  }

  // Player enters
  public void enter(Player player){
    playersInRoom.add(player) ;
    if(currentScene != null && !currentScene.flipped){
      currentScene.flipped = true ;
    }
  }

  // Assigns a new scene
  public void setScene(SceneCard scene){
    currentScene = scene ;
    shotsRemaining = shotMarkers ;
  }

  // Displays extra roles and on card roles from currentScene
  public String showAvailableRoles(int playerRank){
    StringBuilder sb = new StringBuilder() ;

    // Add off card roles
    sb.append("Off-card Roles:\n ") ;
    for(Role role: extraRoles){
      if(!role.taken && playerRank >= role.reqRank){
        sb.append(role.name + " (rank " + role.reqRank + ")\n" ) ;
      }
    }

    // Add on card roles
    sb.append("\nOn-card Roles:\n ") ;
    for(Role role: currentScene.roles){
      if(!role.taken && playerRank >= role.reqRank){
        sb.append(role.name + " (rank " + role.reqRank + ")\n" ) ;
      }
    }

    return sb.toString() ;
  }

  // Delivers an array of role names for getInputList to use
  public String[] availableRolesArray(int playerRank){
    String[] roleNames = new String[extraRoles.length + currentScene.roles.length] ;
    int i = 0 ;
    // Add off card roles
    for(Role role: extraRoles){
      if(!role.taken && playerRank >= role.reqRank){
        roleNames[i] = role.name ;
        i++ ;
      }
    }

    // Add on card roles
    for(Role role: currentScene.roles){
      if(!role.taken && playerRank >= role.reqRank){
        roleNames[i] = role.name ;
        i++ ;
      }
    }
    return roleNames ;
  }

  // Advances shot marker and pays actor
  public void advanceScene(){
    shotsRemaining-- ;
    if(shotsRemaining == 0){
      wrapScene();
    }
  }

  // Discards current scene and pays wrap bonuses
  private void wrapScene(){
    // Check to see if wrap bonuses need to be paid
    boolean onCard = false ;
    shotsRemaining = shotMarkers ;
    for(Role role: currentScene.roles){
      if(role.taken){
        onCard = true ;
      }
    }

    // Pay wrap bonuses
    if(onCard){
      PriorityQueue<Integer> dice = new PriorityQueue<Integer>(Collections.reverseOrder()) ;
      for(int i = 0; i < currentScene.budget; i++){ // roll dice
        dice.offer(rand.nextInt(6)) ;
      }

      // Pay on card actors
      for(int i = 0; i < currentScene.budget; i++){
        int diceTemp = dice.poll() ;
        // Will loop through roles higest to lowest if ordered correctly
        if(currentScene.roles[i % currentScene.roles.length].taken){
          currentScene.roles[i % currentScene.roles.length].workingPlayer.changeMoney(diceTemp) ;
        }
      }

      // Pay off card actors
      for(Role role: extraRoles){
        if(role.taken){
          role.workingPlayer.changeMoney(role.reqRank) ;
        }
      }
    }
    SceneCardManager.discard(currentScene) ;

    // Set players to not working
    for(Player player: playersInRoom){
      if(player.working){
        player.working = false ;
        player.rehearsalBonus = 0 ;
      }
    }
  }


}
