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
  private int rehearsalBonus ;
  private boolean working ;
  public Room location ;
  public String playerName ;
  public Role role ;

  ///* Main method for testing
  public static void main(String[] args){
    Player thisplayer = new Player("Geroge") ;
    thisplayer.changeRank(3) ;
    thisplayer.changeMoney(4) ;
    thisplayer.changeFame(10) ;
    System.out.println(thisplayer.playerInfo()) ;
  }
  //*/

  // Default constructor
  public Player(){
    PLAYER_COUNT ++ ;
    playerName = "Player " + PLAYER_COUNT ;
    rank = 1 ;

  }

  // Returns player's information in a String
  public String playerInfo(){
    return (playerName + "\n" +
              "Money: $" + money + "\n" +
              "Fame: " + fame + "\n" +
              "Rank: " + rank + "\n" +
              "Score: " + getScore() + "\n" +
              "Location: " + location.roomName + "\n"
    ) ;
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
      money = change ;
    }
  }

  // FAME
  public void changeFame(int change){
    if(fame + change < 0){
      fame = 0 ;
    }
    else{
      fame = change ;
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

  // Turn options \\

  // Returns a player's options for display
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
    location = destination ;
    location.enter(this) ;
  }

  // Take a role
  public void takeRole(Role newRole){
    working = true ;
    role = newRole ;
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

  }

  // Rehearse
  public void rehearse(){
    rehearsalBonus++ ;
  }

  // Upgrade (unsure about best way to implement)
  public void upgrade(int rankChange, int cost){

  }

}
