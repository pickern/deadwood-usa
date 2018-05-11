// Room class for Deadwood USA
// TODO:
//  - Some methods might need to print out information to keep players updated
//    - particularly, wrapScene
//  - Test with GameSystem and SceneCard
//  - Implement roll in here instead of game system

import java.lang.StringBuilder ;
import java.util.Arrays ;
import java.util.* ;

public class Room{

  // Attributes
  public String roomName ;
  public int shotMarkers ;
  public int shotsRemaining ;
  public SceneCard currentScene ;
  public Role[] extraRoles ; // Roles should be listed highest paid to lowest
  public Room[] adjacentRooms ;
  private ArrayDeque<Player> playersInRoom ;
  static ArrayDeque<Room> sets = new ArrayDeque(); // For rooms with sceneCards

  ///* Main method for testing
  public static void main(String[] args){
    new Room("Trailers", 0, null, null) ;
    new Room("Casting Office", 10, null, null) ;
    new Room("The Barn", 10, null, null) ;
    new Room("Barn 2: The Barnening", 10, null, null) ;
    System.out.println(sets.size()) ;
    System.out.println(sets.poll().roomName) ;
    System.out.println(sets.poll().roomName) ;
  }//*/

  // Constructor
  public Room(String name, int shots, Room[] adjacents, Role[] roles){
    roomName = name ;
    shotMarkers = shots ;
    shotsRemaining = shots ;
    adjacentRooms = adjacents ;
    extraRoles = roles ;
    if(!(name.equals("Casting Office") || name.equals("Trailers"))){
      sets.add(this) ;
    }
  }

  // For tracking player movement
  // Player leaves
  public void exit(Player player){
    playersInRoom.remove(player) ;
  }

  // Player enters
  public void enter(Player player){
    playersInRoom.add(player) ;
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
      if(!role.taken){
        sb.append(role.name + " (rank " + role.reqRank + ")\n" ) ;
      }
    }

    // Add on card roles
    sb.append("\nOn-card Roles:\n ") ;
    for(Role role: currentScene.roles){
      if(!role.taken){
        sb.append(role.name + " (rank " + role.reqRank + ")\n" ) ;
      }
    }

    return sb.toString() ;
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
    for(Role role: currentScene.roles){
      if(role.taken){
        onCard = true ;
      }
    }

    // Pay wrap bonuses
    if(onCard){
      PriorityQueue<Integer> dice = new PriorityQueue(Collections.reverseOrder()) ;
      for(int i = 0; i < currentScene.budget; i++){ // roll dice
        dice.offer(roll()) ;
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
  }

}
