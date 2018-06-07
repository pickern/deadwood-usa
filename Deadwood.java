//Calls GameSystem to run the game

public class Deadwood{

  public static void main(String[] args){
    GUI.getGUI() ;
    new GameSystem(Integer.parseInt(args[0])) ;
  }
}
