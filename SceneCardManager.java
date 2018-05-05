// SceneCardManager class for Deadwood USA

import java.util.ArrayDeque ;

public class SceneCardManager {

  private ArrayDeque<SceneCard> deck ;
  private ArrayDeque<SceneCard> activeScenes ;
  private ArrayDeque<SceneCard> discardPile ;

  // Constructor
  public SceneCardManager(){
    deck = new ArrayDeque(40) ;
    activeScenes = new ArrayDeque(10) ;
    discardPile = new ArrayDeque(40) ;
  }

  // Assigns scene cards to rooms
  public void deal(){

  }

  // Removes scene card from play when scene is wrapped
  public void discard(){

  }

  // Returns number of scenes in play
  public int activeScenes(){
    return 0 ;
  }

}
