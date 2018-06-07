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
          GUI.println("Error: Invalid number of players") ;
        }
            //initialize(3);
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
             GUI.println(welcome.nextLine());
          }
        }catch(Exception FileNotFoundException){
          GUI.println("Welcome to Deadwood Studios, USA!");
        }

        // Build board
        Room.readRooms() ;

        // Make scene cards
        SceneCardManager.readCards() ;

        // Create Players
        players = new ArrayList<Player>(playerCount) ;
        GUI.println("\nPlayers: ") ;
        for(int i = 0; i < playerCount; i++){
          players.add(new Player()) ;
          players.get(i).move(Room.trailers) ;
          GUI.println(players.get(i).playerName) ;
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

        int nextIndex= players.indexOf(currentPlay)+1;


        if (nextIndex == players.size())

                  nextIndex= 0;

        nextPlay= players.get(nextIndex);// initialization of nextPlay

        GUI.println("\n" + currentPlay.playerName + " will be up first. Good luck! \n \n \n") ;

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

            // display day
            GUI.println("It's day "+ currentDay + "! \n");
            GUI.println("Dealing....");

            SceneCardManager.deal();

            while (endDay== false){ // turns loop between players for as long as there are scenes

                  turn();     // calls turn
                  nextPlayer();     // updates currentPlay/ nextPlay

                  //check if day is over
                  if(SceneCardManager.activeScenes() == 1)  // if the 2nd to last scene card is discarded,
                        endDay();                                            // there will be only one scene card left

            }
            if (currentDay == days) // finished the last day   (FIX) (currentDay== days)
                  endGame();
            else{
            currentDay++; // increment currentDay
            day(); // call next day
            }
      }


  //••••••••••••••••••••••••••••••••••••• TURN ••••••••••••••••••••••••••••••••••••

      // Looks at currentPlay, presents options, handles turn decisions
      private static void turn(){

            GUI.println("It's "+ currentPlay.playerName+ "'s turn!"+ "\n");
            GUI.println(currentPlay.playerInfo()) ;

            int answer= turnPrompt();
            String destination;
            Role chosenRole= null;

            if(answer == 1){ // act
                  currentPlay.workOnRole();


            }
            else if (answer == 2){ // rehearse
                  currentPlay.rehearse();
            }
            else if (answer == 3){ // move

                  Room room= roomPrompt();   // get destination

                  GUI.println("moving you to " + room.roomName + "...");

                  currentPlay.move(room);

                  if(currentPlay.working == false && !(currentPlay.location.equals(Room.trailers) || currentPlay.location.equals(Room.office))){

                        if( !(room.showAvailableRoles(currentPlay.getRank()).equals(room.showAvailableRoles(0))) ){

                              chosenRole= rolePrompt(room);

                              if(chosenRole != null)
                              currentPlay.takeRole(chosenRole);

                        }
                        else{

                        GUI.println("No roles to choose from...");
                        }
                   }

            }
            else if (answer == 4){ // pass

                  // new currentPlay
                  // new nextPlay
                  GUI.println(currentPlay.playerName + " passed, so...");
            }
            else{
                  GUI.println("There was an error with your input, let's try again.....");

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

   private static Role rolePrompt(Room currentRoom){

      Scanner sc= new Scanner(System.in);
      Scanner scb= new Scanner(System.in);
      boolean validInput= false;
      Role answer= null;
      String ans;
      String role;


      while(validInput == false){

      GUI.println("Would you like to pick one of the roles in "+ currentRoom.roomName+ "? It has a budget of $"+ currentRoom.currentScene.budget+ " million.");
      // list possible roles

            // Gets input from textfield
            ans = GUI.getInput().toLowerCase() ;


            if(ans.equals("no")){

                  validInput= true;
                  return answer;
            }
            else if (ans.equals("yes")){
                  validInput= true;
            }
            else{
                  GUI.println("Improper input: please choose yes or no... \n Let's try again...");
            }
      }

      validInput= false;

      while( validInput == false){ // only enters here if "yes", else loops or returns before this point



            // list available roles
            GUI.println(currentRoom.showAvailableRoles(currentPlay.getRank()));

            GUI.println("Which role would you like to choose?");

                  role = GUI.getInput();

            for(Role offCardRole: currentRoom.extraRoles){  // off card roles for current scene

                  if(role.equals(offCardRole.name.toLowerCase())){ // change to element of [list of possible roles]

                        answer = offCardRole;
                        validInput= true;
                        return answer;
                  }
            }
            for(Role onCardRole : currentRoom.currentScene.roles){ // on card roles for current scene

                  if(role.equals(onCardRole.name.toLowerCase())){

                        answer= onCardRole;
                        validInput= true;
                        return answer;
                  }

            }

            if (validInput == true){

                  return answer;

            }
            else{
                  GUI.println("Improper input: please choose a role from the list... \n Let's try again...");

            }


      }

      return answer;
   }

   //••••••••••••••••••••••••••••••••••••• RoomPrompt ••••••••••••••••••••••••••••••••••••
      private static Room roomPrompt(){

            Room room= null;
            boolean validMove= false;
            boolean adjacent= false;
            boolean input = false;
            String destination ;

            while (validMove== false){
                        // display adjacent rooms

                  GUI.println("These are your choices: \n");
                  GUI.println(currentPlay.location.getMoves());

                        // get destination
                  GUI.println("Where would you like to move to? \n");

                  // Wait for input
                  destination = GUI.getInput().toLowerCase();

                        for(int j=0; j< currentPlay.location.adjacentRooms.length; j++){

                              if( destination.equals(currentPlay.location.adjacentRooms[j].toLowerCase()) )

                                    adjacent= true;

                        }

                  room= Room.stringToRoom(destination);

                        if (room== null || adjacent== false ){ // ***** NULL OR NOT AN ADJACENT ROOM ***
                          GUI.println("You cannot move to "+ destination+ ". Let's try again.") ;
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
            boolean input = false;
            String in ;


            while (ans == 0){// should loop while we don't have a proper response
                  if (currentPlay.working== true){ // if working
                   GUI.println("Would you like to act or rehearse? Your current budget to beat is $" + currentPlay.location.currentScene.budget+ " million.");

                   String answer= GUI.getInput().toLowerCase();

                        if(answer.toLowerCase().equals("act")){
                              ans= 1;
                        }
                        else if(answer.toLowerCase().equals("rehearse")){
                              ans= 2;
                        }

                  } // end if

                  else{ // if not working
                        GUI.println("Would you like to move or pass?");

                        String answer = GUI.getInput().toLowerCase();    // get destination

                        if(answer.toLowerCase().equals("move")){
                              ans= 3;
                        }
                        else if(answer.toLowerCase().equals("pass")){
                              ans= 4;
                        }

                  } // end else

                  if(ans== 0)

                        GUI.println("Improper input: Let's try again...");

                  } // end while
                  return ans;

      }
  //••••••••••••••••••••••••••••••••••••• UPGRADE ••••••••••••••••••••••••••••••••••••
     public static void upgrade(){

      Scanner sc= new Scanner(System.in);
      boolean properInput= false;
      int rank=0;
      String payment="";

            while(properInput== false){

                  for(int i=0; i< 5; i++){
                  // print table
                  GUI.println("Rank: "+ (i+2) + " Money: $" +Room.upgradeTable[i][0] + " Fame: "+ Room.upgradeTable[i][1]);
                  // ask for payment method, rank
                  GUI.println("What rank would you like?");
                  rank = Integer.parseInt(GUI.getInput());
                  GUI.println("How would you like to pay?");
                  payment= GUI.getInput().toLowerCase();

                  // Check validity of input
                  if( rank > currentPlay.getRank() && rank < 7 && ( payment.equals("money") || payment.equals("fame")) ){
                        properInput= true;
                  }
                  else{
                       GUI.println("Improper input: please choose an appropriate rank and payment type.");
                  }


                  if(currentPlay.getFame() < Room.upgradeTable[rank-2][1] || currentPlay.getMoney() < Room.upgradeTable[rank-2][0]){

                        GUI.println("Improper input: you cannot afford this upgrade.");
                  }
               }// end for

            }// end while

            // validInput

            currentPlay.changeRank(rank);

                  if(payment.equals("money"))
                        currentPlay.changeMoney(Room.upgradeTable[rank-2][0]);
                  else
                        currentPlay.changeFame(Room.upgradeTable[rank-2][1]);




     }

  //••••••••••••••••••••••••••••••••••••• ENDDAY ••••••••••••••••••••••••••••••••••••
  // various end of day necessities

      private static void endDay(){


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

            GUI.println("Here are your scores: \n");
            for(Player player: players){

                score= player.getScore();
                GUI.println(player.playerName+ ": "+ score +" points")  ;
                scores[(players.indexOf(player))]= score;

                if(score == max){

                  winner= winner + ", "+ player.playerName;
                }
                else if (player.getScore() > max ){

                  max= player.getScore();
                  winner= player.playerName;

                }
            }

            GUI.println(winner + " won the game!");

            GUI.println("Would you like to play again? ( yes/no )");

                  String newGame= sc.next();

                  if(newGame.toLowerCase().equals("yes")){

                        Player.PLAYER_COUNT= 0;
                        initialize(players.size());
                  }
      }
}
