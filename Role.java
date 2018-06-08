// C.Speckhardt // speckhc // W01240690 // CSCI 345 // 2018.05.04 //


// Role Class

public class Role{

      // attributes
      public int x ;
      public int y ;
      public int w ;
      public int h ;
      public String img ;
      public String name;
      public String line;
      public int reqRank;
      public Boolean taken = false;
      public Player workingPlayer ; // Added this to make wrapScene easier

      // constructor
      public Role(String name, String line, int requiredRank){

            this.name= name;
            this.line= line;
            this.reqRank= requiredRank;

      }

      // Alternate constructor for GUI
      public Role(String name, String line, int requiredRank, int x, int y){

            this.name= name;
            this.line= line;
            this.reqRank= requiredRank;
            this.x = x ;
            this.y = y ;

      }

}
