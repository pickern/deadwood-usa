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

        // GameSystem test= new GameSystem(3);
//             test.currentPlay= new Player ("Chris");
//             test.turn();
            initialize(3);
      }

      // constructor
      public GameSystem(int numPlayers){
            initialize(numPlayers); // call initialize
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
        int nextIndex= players.indexOf(currentPlay)+1;

        if (nextIndex > players.size())
                  nextIndex= nextIndex- players.size();
        nextPlay= players.get(nextIndex);// initialization of nextPlay

        System.out.println("\n" + currentPlay.playerName + " will be up first. Good luck! \n \n \n") ;

        currentDay= 1; // set day to 1

        day(); // calls day to start the game
      }

 //••••••••••••••••••••••••••••••••••••• DISPLAY ••••••••••••••••••••••••••••••••••••

      public static void display(){

      }

  //••••••••••••••••••••••••••••••••••••• DAY ••••••••••••••••••••••••••••••••••••

      // Handles days
      private static void day(){
            boolean endDay= false;
            int count= 0; // for testing


            // display day
            System.out.println("It's day "+ currentDay + "! \n");
            while (endDay== false){ // turns loop between players for as long as there are scenes

                  turn();     // calls turn
                  nextPlayer();     // updates currentPlay/ nextPlay
                  count++;

                  //check if day is over
                  //if(SceneCardManager.activeScenes() == 1)  // if the 2nd to last scene card is discarded,
                                                              // there will be only one scene card left

                  if( count == 4){// ten loops for testing (FIX)
                        endDay();
                        endDay= true;
                   }

            }
            if (currentDay == 1) // finished the last day   (FIX) (currentDay== days)
                  endGame();
            else{
            currentDay++; // increment currentDay
            day(); // call next day
            }
      }


  //••••••••••••••••••••••••••••••••••••• TURN ••••••••••••••••••••••••••••••••••••

      // Looks at currentPlay, presents options, handles turn decisions
      private static void turn(){

            System.out.println("It's "+ currentPlay.playerName+ "'s turn!"+ "\n");
            System.out.println(currentPlay.playerInfo()) ;

            int answer= turnPrompt();
            String destination;


            if(answer == 1){ // act
                  System.out.println("Acting... currentPlay.workOnRole() called.");
                  currentPlay.workOnRole();


            }
            else if (answer == 2){ // rehearse
                  System.out.println("Rehearsing... currentPlay.rehearse() called.");
                  currentPlay.rehearse();
            }
            else if (answer == 3){ // move


                  Room room= roomPrompt();   // get destination

                  System.out.println("moving you to " + room.roomName + "...");

                  currentPlay.move(room);
                  if(currentPlay.working == false){
                        rolePrompt(room);
                   }

            }
            else if (answer == 4){ // pass

                  // new currentPlay
                  // new nextPlay
                  System.out.println(currentPlay.playerName + " passed, so...");
            }
            else{
                  System.out.println("There was an error with your input, let's try again.....");

                  turn();
                }

      }

//••••••••••••••••••••••••••••••••••••• NEXT PLAYER ••••••••••••••••••••••••••••••••••••

      // next currentPlay, new nextPlay

      private static void nextPlayer(){

            int nextIndex= players.indexOf(nextPlay)+1; // indexed at 1 more than previous nextPlay
            if (nextIndex > players.size()-1){
                  nextIndex= nextIndex- players.size();
            }
                  currentPlay= nextPlay;
                  nextPlay= players.get(nextIndex);
      }

   //••••••••••••••••••••••••••••••••••••• RolePrompt ••••••••••••••••••••••••••••••••••••

   private static void rolePrompt(Room room){

      Scanner sc= new Scanner(System.in);
      boolean validInput= false;

      while(validInput == false){

      System.out.println("Would you like to pick one of the roles in "+ room.roomName+ "?");
      // list possible roles (FIX) ****

            String role= sc.next().toLowerCase();

            if(role.equals("no")){
                  validInput= true;
                  return;
            }
            else if (role.equals("yes")){
                  validInput= true;
            }
            else{
                  System.out.println("Improper input: please choose yes or no... \n Let's try again...");
            }
      }

      validInput= false;

      while( validInput== false){ // only enters here if "yes", else loops or returns before this point



            // list possible roles (FIX) ****
            System.out.println("Which role would you like to choose?");

                  role= sc.nextLine().toLowerCase();

            if(){ // change to in list of possible roles
                  validInput= true;
                  return;
            }

            else{
                  System.out.println("Improper input: please choose a role from the list... \n Let's try again...");
            }




      }
   }

   //••••••••••••••••••••••••••••••••••••• RoomPrompt ••••••••••••••••••••••••••••••••••••
      private static Room roomPrompt(){

            Scanner sc= new Scanner(System.in);
            Room room= null;
            boolean validMove= false;

            while (validMove== false){

                        // display adjacent rooms

                  System.out.println("These are your choices: \n");
                  System.out.println(currentPlay.location.getMoves());

                        // get destination
                  System.out.println("Where would you like to move to? \n");
                  String destination = sc.nextLine().toLowerCase();
                  room= Room.stringToRoom(destination);
                        if (room== null ){ // ***** OR NOT AN ADJACENT ROOM *** (FIX)
                          System.out.println("You cannot move to "+ destination+ ". Let's try again.") ;
                          return roomPrompt();
                          }
                        else
                              validMove= true;

             }
             return room;
    }

  //••••••••••••••••••••••••••••••••••••• TurnPrompt ••••••••••••••••••••••••••••••••••••

      // prompts user for turn input
      private static int turnPrompt(){
            Scanner sc= new Scanner(System.in);
            int ans= 0;


            while (ans == 0){// should loop while we don't have a proper response


                  if (currentPlay.working== true){ // if working
                   System.out.println("Would you like to act or rehearse?");
                   String answer= sc.next();

                        if(answer.toLowerCase().equals("act")){
                              ans= 1;
                        }
                        else if(answer.toLowerCase().equals("rehearse")){
                              ans= 2;
                        }

                  } // end if

                  else{ // if not working
                        System.out.println("Would you like to move or pass?");
                        String answer= sc.next();    // get destination

                        if(answer.toLowerCase().equals("move")){
                              ans= 3;
                        }
                        else if(answer.toLowerCase().equals("pass")){
                              ans= 4;
                        }

                  } // end else

                  } // end while

                  return ans;

      }

  //••••••••••••••••••••••••••••••••••••• ENDDAY ••••••••••••••••••••••••••••••••••••
  // various end of day necessities

      private static void endDay(){

            //SceneCardManager.deal(); // deal 10 new Scenes            **********        (FIX)
            for(Player player: players)  // move players back to trailers

                  player.move(Room.stringToRoom("Trailers"));

            nextPlayer(); // iterate players


      }
  //••••••••••••••••••••••••••••••••••••• ENDGAME ••••••••••••••••••••••••••••••••••••

      // Total up score, annouce winner, prompt to restart
      private static void endGame(){
            int[] scores= new int[5];
            String winner= "";
            int max=0;
            int score= 0;
            Scanner sc= new Scanner(System.in);

            System.out.println("Here are your scores: \n");
            for(Player player: players){

                score= player.getScore();
                System.out.println(player.playerName+ ": "+ score +" points")  ;
                scores[(players.indexOf(player))]= score;

                if(score == max){

                  winner= winner + ", "+ player.playerName;
                }
                else if (player.getScore() > max ){

                  max= player.getScore();
                  winner= player.playerName;

                }
            }

            System.out.println(winner + " won the game!");

            System.out.println("Would you like to play again? ( yes/no )");

                  String newGame= sc.next();

                  if(newGame.toLowerCase().equals("yes"))

                        initialize(players.size());

      }
}
