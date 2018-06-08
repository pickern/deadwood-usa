// Player class for Deadwood USA
//
// Responsible for managing player-related information like score and location

import java.lang.StringBuilder ;
import java.util.Random ;

public class Player{

  static int PLAYER_COUNT ;
  static Random rand = new Random() ;
  private int money ;
  private int fame ;
  private int rank ;
  public int playerNumber ;
  public int rehearsalBonus ; 
  public boolean working = false ; // everyone starts not working
  public Room location ;
  public String playerName ;
  public Role role ;
  public char color ;
  static char[] colors = {'b','c','g','o','p','r','v','y'} ;

  // Default constructor
  public Player(){
    PLAYER_COUNT ++ ;
    playerNumber = PLAYER_COUNT ;
    playerName = "Player " + PLAYER_COUNT ;
    rank = 1 ;
    this.playerName = "Player " + PLAYER_COUNT ;
    this.rank = 1 ;
    this.color = colors[PLAYER_COUNT - 1] ;
  }

  // Returns player's information in a String
  public String playerInfo(){
    int score = getScore();

    if(working == false){
    String ans = (playerName + "\n" +
              "Money: $" + money + "\n" +
              "Fame: " + fame + "\n" +
              "Rank: " + rank + "\n" +
              "Score: " + score + "\n" +
              "Location: " + location.roomName + "\n"+
              "Current Role: not working \n"+
              "Rehearsal Bonus: " +rehearsalBonus +
              "\nColor: " + color

    ) ;

    return ans;

    }
    else{
    String ans = (playerName + "\n" +
              "Money: $" + money + "\n" +
              "Fame: " + fame + "\n" +
              "Rank: " + rank + "\n" +
              "Score: " + score + "\n" +
              "Location: " + location.roomName + "\n"+
              "Current Role: "+ role.name+ "\n"+
              "Rehearsal Bonus : " + rehearsalBonus +
              "\nColor: " + color

    ) ;

    return ans;

    }

  }

  // Adders and setters
  public void changeMoney(int change){
    if(money + change < 0){
      money = 0 ;
    }
    else{
      money += change ;
    }
  }

  // FAME
  public void changeFame(int change){
    if(fame + change < 0){
      fame = 0 ;
    }
    else{
      fame += change ;
    }

  }

  // RANK
  public void changeRank(int newRank){
    rank = newRank ;
  }

  // Alternate call to reset rehearsalBonus
  public void rehearse(int flag){
    rehearsalBonus = 0 ;
  }

  // Getters
  public int getMoney(){
    return money ;
  }

  public int getFame(){
    return fame ;
  }

  public int getRank(){
    return rank ;
  }

  public int getScore(){
    return fame + money + 5 * rank ;
  }

  public int getRehearsalBonus(){
    return rehearsalBonus ;
  }
  public char getColor(){
      return color;
  }

  // Turn options \\

  // Move
  public void move(Room destination){
    if(location != null){
      location.exit(this) ;
    }

    if(destination.equals(Room.office)){
      location= destination;
      location.enter(this);
      GameSystem.upgrade();
    }
    // flip scene card if necessary
    if(destination != Room.trailers && destination != Room.office && destination.currentScene.flipped == false){
      destination.currentScene.flipped= true;
      GameSystem.display.setSceneLabels(SceneCardManager.getActiveScenes());
    }

    location = destination ;
    location.enter(this) ;
  }

  // Take a role
  public void takeRole(Role newRole){
    working = true ;
    role = newRole ;
    newRole.taken = true ;
    newRole.workingPlayer = this ;
  }

  // Leave role, for when a scene wraps
  public void leaveRole(){
    working = false ;
    role = null ;
  }

  // End turn
  public void passTurn(){

  }

  // Work on Role
  public void workOnRole(){

      boolean onCard = true;     // true if on card Role, false if off card Role

      GameSystem.display.println(role.line);

      for(Role role: location.extraRoles){
        if(this.role.equals(role)){
          onCard = false ;
        }
      }


      int roll = rand.nextInt(6) + 1 ;
      // compare (roll+ currPlay rehearsal bonus) to (currentPlay.location.currentScene.budget)
      if(roll + rehearsalBonus > location.currentScene.budget){
        if(onCard){
          changeFame(2) ;
        }else{
          changeFame(1) ;
          changeMoney(1) ;
        }
        location.advanceScene() ;
        GameSystem.display.println("Good job! You finished the shot. You have "+ (location.shotsRemaining)+ " remaining. \n");
        GameSystem.display.addMarker(location);

      } else if(!onCard){
        changeMoney(1) ;
        GameSystem.display.println("Better luck next time... You didn't finish the shot.\n");
      }
      else{GameSystem.display.println("Better luck next time... You didn't finish the shot.\n");}

  }

  // Rehearse
  public void rehearse(){
    
    
    rehearsalBonus++ ;
  }
}
