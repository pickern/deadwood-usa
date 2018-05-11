// SceneCardManager class for Deadwood USA
// TODO:
//  - Shuffle
//  - Test once SceneCard is running

import java.util.ArrayDeque ;

public class SceneCardManager {

  static private ArrayDeque<SceneCard> deck ;
  static private ArrayDeque<SceneCard> activeScenes ;
  static private ArrayDeque<SceneCard> discardPile ;

  // Constructor
  public SceneCardManager(){
    deck = new ArrayDeque(40) ;
    activeScenes = new ArrayDeque(10) ;
    discardPile = new ArrayDeque(40) ;
  }

  // Assigns scene cards to rooms
  public void deal(){
    for(Room room: Room.sets){
      // Sweep up any remaining scenes
      if(!discardPile.contains(room.currentScene)){
        discard(room.currentScene) ;
      }

      SceneCard temp = deck.poll() ;
      activeScenes.add(temp) ; // Should end up with 10 cards
      room.setScene(temp) ;
    }
  }

  // To ensure that cards are random every time the game is played
  public void shuffle(){

  }

  // Removes scene card from play when scene is wrapped
  static public void discard(SceneCard card){
    discardPile.add(card) ;
    activeScenes.remove(card) ;
  }

  // Returns number of scenes in play
  public int activeScenes(){
    return activeScenes.size() ;
  }

}
