// C.Speckhardt // speckhc // W01240690 // CSCI 345 // 2018.05.04 //
// SceneCard Class

public class SceneCard{

      // attributes
      public String img ;
      public String name;
      public int number;
      public String description;
      public int budget;
      public Role[] roles;
      public boolean flipped= false;
      

      // constructor
      public SceneCard(String name, int num, String description, int budget, Role[] roles, String img){
            this.img = img ;
            this.name= name;
            this.number= num;
            this.description= description;
            this.budget= budget;
            this.roles= roles;

      }

      // alt constructor for GUI
      public SceneCard(String name, int num, String description, int budget, Role[] roles){
            this.name= name;
            this.number= num;
            this.description= description;
            this.budget= budget;
            this.roles= roles;

      }

      @Override
      public String toString(){

            String ans=" ";

           // = this.name+ " ("+ this.description+ ", $"+ this.budget+ " million) \n";

            return ans;
      }

}
