// C.Speckhardt // speckhc // W01240690 // CSCI 345 // 2018.05.04 //

// Changes (Nick)
//  - Made attributes static
//  - Added main
//  - Added initialize()


// Game System Class


import java.util.ArrayList ;
import java.util.Random ;
import java.io.File ;
import java.util.Scanner ;

public class GameSystem{

      // attributes
      private static int days;
      private static int currentDay;
      private static ArrayList<Player> players;
      public static Player currentPlay;
      public static Player nextPlay;

      public static void main(String[] args){
        if(args.length == 1 && Integer.parseInt(args[0]) > 1 && Integer.parseInt(args[0]) < 9){
          initialize(Integer.parseInt(args[0])) ;
        }else{
          System.out.println("Error: Invalid number of players") ;
        }
      }

      // constructor
      public GameSystem(int numPlayers){

      }
  //••••••••••••••••••••••••••••••••••••• INITIALIZE ••••••••••••••••••••••••••••••••••••

      public static void initialize(int playerCount){
        // Welcome the players
        try{
          Scanner welcome = new Scanner(new File("welcome.txt")) ;
          while (welcome.hasNextLine()){
             System.out.println(welcome.nextLine());
          }
        }catch(Exception FileNotFoundException){
          System.out.println("Welcome to Deadwood Studios, USA!");
        }

        // Build board
        Room.readRooms() ;

        // Make scene cards
        SceneCardManager.readCards() ;

        // Create Players
        players = new ArrayList<Player>(playerCount) ;
        System.out.println("\nPlayers: ") ;
        for(int i = 0; i < playerCount; i++){
          players.add(new Player()) ;
          players.get(i).move(Room.trailers) ;
          System.out.println(players.get(i).playerName) ;
        }

        // Different setups for different playerCounts
        if(playerCount == 2 || playerCount == 3){
          days = 3 ;
        }else if(playerCount == 4){
          days = 4 ;
        }else if(playerCount == 5 || playerCount == 6) {
          days = 4 ;
          // Adds 2 fame for 5 players, 4 fame for 6 players
          for(int i = 0; i < playerCount; i++){
            players.get(i).changeFame((playerCount - 4) * 2)  ;
          }
        }else{
          days = 4 ;
          // If there are 7 or 8 players, players start at rank 2
          for(int i = 0; i < playerCount; i++){
            players.get(i).changeRank(2)  ;
          }
        }

        // Determine first player
        Random rand = new Random() ;
        currentPlay = players.get(rand.nextInt(playerCount)) ;

        System.out.println("\n" + currentPlay.playerName + " will be up first. Good luck!") ;

        System.out.println(currentPlay.playerInfo()) ;

      }

 //••••••••••••••••••••••••••••••••••••• DISPLAY ••••••••••••••••••••••••••••••••••••

      public static void display(){

      }

  //••••••••••••••••••••••••••••••••••••• DAY ••••••••••••••••••••••••••••••••••••

      // Handles days
      private static void day(){


      }


  //••••••••••••••••••••••••••••••••••••• TURN ••••••••••••••••••••••••••••••••••••

      // Looks at currentPlay, presents options, handles turn decisions
      private static void turn(){

      }

  //••••••••••••••••••••••••••••••••••••• ENDGAME ••••••••••••••••••••••••••••••••••••

      // Total up score, annouce winner, prompt to restart
      private static void endGame(){

      }

}
