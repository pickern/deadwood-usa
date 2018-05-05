// Room class for Deadwood USA

public class Room{

  public String roomName ;
  public int shotMarker ;
  private int shotsRemaining ;
  public SceneCard currentScene ;
  public Role[] extraRoles ;
  public Room[] adjacentRooms ;
  public Player[] playersInRoom ;

  // Constructor
  public Room(String name, int shots, Room[] adjacents){

  }

  // Assigns a new scene
  public void setScene(SceneCard scene){

  }

  // Displays extra roles and on card roles from currentScene
  public void showAvailableRoles(int playerRank){

  }

  // Advances shot marker and pays actors
  public void advanceScene(){

  }

  // Discards current scene and pays wrap bonuses
  public void wrapScene(){

  }

}
