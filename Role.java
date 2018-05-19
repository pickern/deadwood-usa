// C.Speckhardt // speckhc // W01240690 // CSCI 345 // 2018.05.04 //


// Role Class

public class Role{

      // attributes
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

      @Override
      public String toString(){

            String ans=" ";

           // = this.name+ " ("+ this.description+ ", $"+ this.budget+ " million) \n";

            return ans;
      }

}
