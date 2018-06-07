// Player class for Deadwood USA
//
// Responsible for managing player-related information like score and location
// TODO:
//  - Decide on how to implement upgrade
//  - Flesh out turn options
//    - also, how workOnRole and PassTurn will function
//  - Review playerInfo and make sure all necessary info can be printed when needed
//  - Potentially make move take a String for destination since that might be
//      the easiest way to store adjacentRooms in Room

import java.lang.StringBuilder ;
import java.util.Random ;

public class Player{

  static int PLAYER_COUNT ;
  static Random rand = new Random() ;
  private int money ;
  private int fame ;
  private int rank ;
  public int rehearsalBonus ;
  public boolean working = false ; // everyone starts not working
  public Room location ;
  public String playerName ;
  public Role role ;
  public char color ;
  static char[] colors = {'b','c','g','o','p','r','v','y'} ;

  ///* Main method for testing
  public static void main(String[] args){

  }
  //*/

  // Default constructor
  public Player(){
    PLAYER_COUNT ++ ;
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

  // Alternate constructor
  public Player(String name){
    PLAYER_COUNT ++ ;
    playerName = name ;
    rank = 1 ;

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

  // Returns a player's options for GameSystem.display
  public String turnOptions(){
    if(working){
      return(playerName + " may work, rehearse, or pass the turn") ;
    }else if(location.shotsRemaining > 0){
      return(playerName + " may take a role, move, or pass the turn") ;
    }else if(location.roomName.equals("Casting Office")){
      return(playerName + " may upgrade, move, or pass the turn") ;
    }else{
      return(playerName + " may move or pass the turn") ;
    }
  }

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

      boolean onCard = true;     // true if on card Role, false ihf off card Role

      //print "Line"

      GameSystem.display.println(role.line);


      for(Role role: location.extraRoles){
        if(this.role.equals(role)){
          onCard = false ;
        }
      }


      int roll = rand.nextInt(6) + 1 ;
      // compare (roll+ currPlay rehearsal bonus) to (currentPlay.location.currentScene.budget)
      // for (roles in room)

            //    if(role.taken== true && role

      if(roll + rehearsalBonus > location.currentScene.budget){
        if(onCard){
          changeFame(2) ;
        }else{
          changeFame(1) ;
          changeMoney(1) ;
        }
        location.advanceScene() ;
        GameSystem.display.println("Good job! You finished the shot. You have "+ (location.shotsRemaining -1)+ " remaining. \n");
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
