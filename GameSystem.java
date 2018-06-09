// C.Speckhardt // speckhc // W01240690 // CSCI 345 // 2018.05.04 //
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
      public static BoardDisplay display;

      public static void main(String[] args){

        if(args.length == 1 && Integer.parseInt(args[0]) >= 1 && Integer.parseInt(args[0]) < 9){
          initialize(Integer.parseInt(args[0])) ;
        }else{
          System.out.println("Error: Invalid number of players") ;
        }

            initialize(3);

      }

      // constructor
      public GameSystem(int numPlayers){
            initialize(numPlayers); // call initialize
      }

  //••••••••••••••••••••••••••••••••••••• INITIALIZE ••••••••••••••••••••••••••••••••••••

      public static void initialize(int playerCount){
        // Welcome the players
        // Build board
        Room.readRooms() ;

        // Make scene cards
        SceneCardManager.readCards() ;

        // Create Players
        players = new ArrayList<Player>(playerCount) ;
        for(int i = 0; i < playerCount; i++){
          players.add(new Player()) ;
          players.get(i).move(Room.trailers) ;
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
        // INITIATE DISPLAY
        SceneCardManager.deal();
        display = new BoardDisplay(players, SceneCardManager.getActiveScenes());

        // Determine first player
        Random rand = new Random() ;
        currentPlay = players.get(rand.nextInt(playerCount)) ;
        int nextIndex= players.indexOf(currentPlay)+1;
        if (nextIndex == players.size())
                  nextIndex= 0;

        // DISPLAY WELCOME MESSAGE
        display.println("Welcome to Deadwood Studios, USA!");

        nextPlay= players.get(nextIndex);// initialization of nextPlay
        display.println("\n" + currentPlay.playerName + " will be up first. Good luck! \n \n \n") ;
        currentDay= 1; // set day to 1
        day(); // calls day to start the game
      }

 //••••••••••••••••••••••••••••••••••••• DEAL ••••••••••••••••••••••••••••••••••••

      public static void deal(){
            SceneCardManager.deal();
            display.setSceneLabels(SceneCardManager.getActiveScenes());
      }

  //••••••••••••••••••••••••••••••••••••• DAY ••••••••••••••••••••••••••••••••••••

      // Handles days
      private static void day(){
            boolean endDay= false;
            
            if (currentDay != 1)
                  deal();
            
            //display.setSceneLabels(SceneCardManager.getActiveScenes());
            display.println("\n*****************************\n");
            // display day
            display.println("It's day "+ currentDay + "! \n");
            
            while (endDay== false){ // turns loop between players for as long as there are scenes

                  turn();     // calls turn
                  nextPlayer();     // updates currentPlay/ nextPlay

                  //check if day is over
                  if(SceneCardManager.activeScenes() == 1){ // if the 2nd to last scene card is discarded,
                        endDay();   // there will be only one scene card left
                        endDay = true ;
                  }

            }
            if (currentDay == days) // finished the last day
                  endGame();
            else{
            currentDay++; // increment currentDay
            day(); // call next day
            }
      }


  //••••••••••••••••••••••••••••••••••••• TURN ••••••••••••••••••••••••••••••••••••

      // Looks at currentPlay, presents options, handles turn decisions
      private static void turn(){

            display.println("\n*****************************\n");
            display.println("It's "+ currentPlay.playerName+ "'s turn!"+ "\n");
            display.println(currentPlay.playerInfo()) ;

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
                  display.println("moving you to " + room.roomName + "...");
                  currentPlay.move(room);
                  display.moveToRoom(currentPlay);

                  if(currentPlay.working == false && !(currentPlay.location.equals(Room.trailers) || currentPlay.location.equals(Room.office))){

                        if( !(room.showAvailableRoles(currentPlay.getRank()).equals(room.showAvailableRoles(0))) && room.shotsRemaining > 0){

                              chosenRole= rolePrompt(room);

                              if(chosenRole != null){
                              currentPlay.takeRole(chosenRole);
                              display.moveToRole(currentPlay);
                              }

                        }
                        else{

                        display.println("No roles to choose from...");
                        }
                   }

            }
            else if (answer == 4){ // pass

                  // new currentPlay
                  // new nextPlay
                  display.println(currentPlay.playerName + " passed, so...");
            }
            else{
                  display.println("There was an error with your input, let's try again.....");
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

      display.println("Would you like to pick one of the roles in "+ currentRoom.roomName+ "? \n It has a budget of $"+ currentRoom.currentScene.budget+ " million.");
      // list possible roles

            ans = display.getInputList(new String[]{"Yes","No"}).toLowerCase() ;

            if(ans.equals("no")){

                  validInput= true;
                  return answer;
            }
            else if (ans.equals("yes")){
                  validInput= true;
            }
            else{
                  display.println("Improper input: please choose yes or no... \n Let's try again...");
            }
            display.clearIn() ;
      }

      validInput= false;

      while( validInput == false){ // only enters here if "yes", else loops or returns before this point



            // list available roles
            display.println(currentRoom.showAvailableRoles(currentPlay.getRank()));

            display.println("Which role would you like to choose?");

            role = display.getInputList(currentRoom.availableRolesArray(currentPlay.getRank())).toLowerCase();

            for(Role offCardRole: currentRoom.extraRoles){  // off card roles for current scene

                  if(role.equals(offCardRole.name.toLowerCase())){ // change to element of [list of possible roles]

                        answer = offCardRole;
                        validInput= true;
                  }
            }
            for(Role onCardRole : currentRoom.currentScene.roles){ // on card roles for current scene

                  if(role.equals(onCardRole.name.toLowerCase())){

                        answer= onCardRole;
                        validInput= true;
                  }

            }

            if (validInput == true){

                  return answer;

            }
            else{
                  display.println("Improper input: please choose a role from the list... \n Let's try again...");

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
                  display.println("These are your choices: \n");
                  display.println(currentPlay.location.getMoves());

                  // get destination
                  display.println("Where would you like to move to? \n");

                  destination = display.getInputList(currentPlay.location.adjacentRooms).toLowerCase();

                        for(int j=0; j< currentPlay.location.adjacentRooms.length; j++){

                              if( destination.equals(currentPlay.location.adjacentRooms[j].toLowerCase()) )

                                    adjacent= true;

                        }

                  room= Room.stringToRoom(destination);

                        if (room== null || adjacent== false ){ // ***** NULL OR NOT AN ADJACENT ROOM ***
                          display.println("You cannot move to "+ destination+ ". Let's try again.") ;
                          display.clearIn() ;
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

                   display.println("Would you like to act or rehearse? Your current budget to beat is $" + currentPlay.location.currentScene.budget+ " million.");

                   String answer= display.getInputList(new String[]{"Act","Rehearse"}).toLowerCase();

                        if(answer.toLowerCase().equals("act")){
                              ans= 1;
                        }
                        else if(answer.toLowerCase().equals("rehearse")){
                              ans= 2;
                        }

                  } // end if

                  else{ // if not working

                        display.println("Would you like to move or pass?");

                        String answer = display.getInputList(new String[]{"Move","Pass"}).toLowerCase();    // get destination

                        if(answer.equals("move")){
                              ans= 3;
                        }
                        else if(answer.equals("pass")){
                              ans= 4;
                        }

                  } // end else

                  if(ans== 0)

                        display.println("Improper input: Let's try again...");

                  } // end while
                  display.clearIn() ;

                  return ans;
      }
  //••••••••••••••••••••••••••••••••••••• UPGRADE ••••••••••••••••••••••••••••••••••••
     public static void upgrade(){

      boolean cancel = false ;
      boolean properInput = false ;
      int rank=0 ;
      String payment="";
      String entry ;

            while(properInput== false){

                  display.println("\nCurrent status:\nRank:" + currentPlay.getRank() +
                                                  "\nMoney:" + currentPlay.getMoney() +
                                                  "\nFame:" + currentPlay.getFame()) ;
                  // ask for payment method, rank
                  display.println("\nWhat rank would you like?");
                  entry = display.getInputList(new String[]{"2","3","4","5","6","Actually, I'm good"});
                  if(entry.equals("Actually, I'm good")){
                    cancel = true ;
                    break ;
                  }else{
                    rank = Integer.parseInt(entry) ;
                  }
                  display.println("How would you like to pay?");
                  payment= display.getInputList(new String[]{"Money","Fame"}).toLowerCase();

                  // Check validity of input
                  if(rank > currentPlay.getRank() && rank < 7 && ( payment.equals("money") || payment.equals("fame"))){
                    if(currentPlay.getFame() < Room.upgradeTable[rank-2][1] || currentPlay.getMoney() < Room.upgradeTable[rank-2][0]){
                        display.println("Improper input: you cannot afford this upgrade.");
                    }else{
                        properInput= true;
                    }
                  }
                  else{
                       display.println("Improper input: please choose an appropriate rank and payment type.");
                  }

            }// end while

            // validInput
            if(!cancel){
              currentPlay.changeRank(rank);
                    if(payment.equals("money"))
                          currentPlay.changeMoney(-Room.upgradeTable[rank-2][0]);
                    else
                          currentPlay.changeFame(-Room.upgradeTable[rank-2][1]);
            }
     }

  //••••••••••••••••••••••••••••••••••••• ENDDAY ••••••••••••••••••••••••••••••••••••
  // various end of day necessities

      private static void endDay(){

            for(Player player: players){  // move players back to trailers

                  player.move(Room.trailers);
                  display.moveToRoom(player);
                  player.working =false;
                  }
                  display.emptySM();
                  
                  for(Room room : Room.sets){
                        
                        room.shotsRemaining = room.shotMarkers;
                        
                        for(Role role: room.extraRoles){
                              role.taken = false;
                        }
                  }
                  
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

            display.println("Here are your scores: \n");
            for(Player player: players){

                score= player.getScore();
                display.println(player.playerName+ ": "+ score +" points")  ;
                scores[(players.indexOf(player))]= score;

                if(score == max){

                  winner= winner + ", "+ player.playerName;
                }
                else if (player.getScore() > max ){

                  max= player.getScore();
                  winner= player.playerName;

                }
            }

            display.println(winner + " won the game!");

            display.println("Would you like to play again? ( yes/no )");

                  String newGame= display.getInputList(new String[]{"Yes","No"}).toLowerCase();

                  if(newGame.toLowerCase().equals("yes")){

                        Player.PLAYER_COUNT= 0;
                        initialize(players.size());
                  }
                  else{
                        
                        display.setVisible(false);
                        display.dispose();
                        System.exit(0);
                  }
                  
      }

}
