import javax.swing.*;
import java.io.File;
import javax.swing.border.*;
import javax.accessibility.*;
import java.util.concurrent.TimeUnit;
import java.awt.event.*;
import java.awt.image.BufferedImage ;
import java.awt.Graphics2D ;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ArrayDeque;



public class BoardDisplay extends JFrame {

      private JLabel boardlabel;
      private JLayeredPane bPane;
      private JPanel outputPanel;
      private JTextArea output;
      private ArrayList<JLabel> playerLabels= new ArrayList();
      private ArrayList<JLabel> cardLabels = new ArrayList();
      public volatile static String in = "" ;

      public BoardDisplay(ArrayList<Player> players, ArrayDeque<SceneCard> activeScenes){

       super("Deadwood");
       // Set the exit option for the JFrame
       setDefaultCloseOperation(EXIT_ON_CLOSE);

       // Create the JLayeredPane to hold the display, cards, dice and buttons
       bPane = getLayeredPane();

       // Create the deadwood board
       boardlabel = new JLabel();
       ImageIcon icon =  new ImageIcon("GUIFiles/board.jpg");
       boardlabel.setIcon(icon);
       boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

       // Add the board to the lowest layer
       bPane.add(boardlabel, new Integer(0));

       // Set the size of the GUI
       setSize(icon.getIconWidth()+200,icon.getIconHeight());

      // Create Text areas (needs to be cleaned up)
       outputPanel = new JPanel() ;

       output = new JTextArea() ;
       output.setLocation(900,0) ;
       output.setEditable(false) ;
       output.setLineWrap(true) ;
       output.setColumns(30) ;
       output.setRows(31) ;
       JScrollPane jScrollPane = new JScrollPane(output) ;
       jScrollPane.setPreferredSize(new Dimension(200,480));
       jScrollPane.setMaximumSize(new Dimension(200,300)) ;
       jScrollPane.setLocation(25,0) ;
       outputPanel.add(jScrollPane) ;
       outputPanel.setOpaque(true) ;
       jScrollPane.setOpaque(true) ;

      // input text field
      JTextField input = new JTextField() ;
      input.addActionListener(
      new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event){
          in = input.getText() ; // Allows chunks of user input to be accessed by program
          output.append(in + "\n") ;
          input.setText("") ;
          //Process input
        }
      }) ;
      input.setLocation(1215,500) ;
      input.setSize(300,20) ;
      this.setVisible(true);

      // put players in trailers

      toTrailers(players);
      //setSceneLabels(activeScenes);

            // Create buttons
      JButton act = new JButton("Act") ;
      act.setBounds(1230,550,60,50) ;
      act.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        in = ("Act") ;
        output.append("Act") ;
      }
      });

      JButton rehearse = new JButton("Rehearse") ;
      rehearse.setBounds(1290,550,60,50) ;
      rehearse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        in = ("Rehearse") ;
        output.append("Rehearse") ;
      }
      });

      JButton move = new JButton("Move") ;
      move.setBounds(1350,550,60,50) ;
      move.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
         in = ("Move") ;
        output.append("Move\n") ;
        }
      });


             // Add it all
             bPane.add(input) ;
             bPane.add(act) ;
             bPane.add(rehearse) ;
             bPane.add(move) ;
             this.add(outputPanel, BorderLayout.EAST) ;
             boardlabel.setOpaque(true) ;

      }//end constructor

      public void toTrailers(ArrayList<Player> players){

            ////////////////////////////////////// Player labels for Trailers ///////////////////
      String s= "";
      String color= "";
      String rank= "";
      JLabel label;
      int offset= 50;

      for(Player player: players){


            color = Character.toString(player.color);
            rank = Integer.toString(player.getRank());
            s = new String (color+rank+".png");
            label = playerlabel(s, 950+offset, 300);
            playerLabels.add(label);
            bPane.add(label, new Integer(2));
            offset= offset+50; // offset the icons
      }



      }
      ////////////////////////////////// SCENE CARD LABELS /////////////////////////////

      /*
      public void setSceneLabels(ArrayDeque<SceneCard> activeScenes){

            int sceneNum;
            JLabel label;


            for(SceneCard scene: activeScenes){


                  sceneNum = scene.number;
                  rank = Integer.toString(player.getRank());
                  s = new String (sceneNum+".png");
                  label = cardlabel(s, );
                  cardLabels.add(label);
                  bPane.add(label, new Integer(2));
            }
      }

      // UPDATE
      public void moveToRoom(Player player){

            String filename= player.location ;

         }
      */

      public void println(String out){
       output.append(out + "\n") ;
      }
      // will be replaced with better method for handling input
      public void clearIn(){
       in = "" ;
      }

      // Waits for user input before continuing
      public static String getInput(){
        in = "" ;
          while(true){
            try{
              Thread.sleep(100) ; // Hugely reduces CPU strain
            }catch(Exception e){
              e.printStackTrace() ;
            }
            if (!(in).equals("")){
              return in ;
            }
          }
      }
      //public void change
      public void pause(){

            try
                  {
                  Thread.sleep(1000);
                  }
            catch(InterruptedException ex)
                  {
                  Thread.currentThread().interrupt();
                  }


      }
      public JLabel playerlabel(String filename, int x, int y){

            JLabel playerlabel = new JLabel();
            ImageIcon pIcon = new ImageIcon("GUIFiles/dice/"+filename);
            playerlabel.setIcon(pIcon);
            playerlabel.setBounds(x+3, y, 46, 46); // Prisoner in Cell Role
            playerlabel.setOpaque(true);
            return playerlabel;


      }

      public JLabel cardlabel( String filename, Room location){ // will add Room location - using Jail for testing

            JLabel cardlabel = new JLabel();
            ImageIcon cIcon =  new ImageIcon("GUIFiles/cards/"+ filename);
            cardlabel.setIcon(cIcon);
            cardlabel.setBounds(location.x,location.y,205,115); // Jail Room
            cardlabel.setOpaque(true);
                  return cardlabel;


      }
      public JLabel smlabel(String filename/*, Room location*/){

            JLabel smlabel = new JLabel();
            ImageIcon smIcon =  new ImageIcon("GUIFiles/"+filename);
            smlabel.setIcon(smIcon);
            smlabel.setBounds(442+10,156,39+3,46); // Jail Take 1
            smlabel.setOpaque(true);
                  return smlabel;


      }
     // public static void newPlayerLabel(Player currentPlayer, Role location,
      public static void main(String[] args){

          //   JLabel one= playerlabel("r1.png", new Role( "Crusty Prospector",  "blah blah", 1,114,227,46,46));
//             JLabel two= playerlabel("r2.png", new Role( "Dragged by Train",  "blah blah", 1,51,268,46,46));
//             JLabel seven = cardlabel("02.png", Room.roomToString("secret hideout"));
//             JLabel three = playerlabel("r3.png",new Role( "Preacher with Bag",  "blah blah", 2,114,320,46,46) );
//             JLabel four = cardlabel("03.png", Room.roomToString("jail"));
//             JLabel five = playerlabel("g5.png", new Role( "Cyrus the Gunfighter",  "blah blah", 4,49,356,46,46));
//             JLabel six = smlabel("shotmarker.png");
//

              // Player p1= new Player()
               // BoardDisplay b = new BoardDisplay();
//             b.setVisible(true);
//             b.bPane.add(one,new Integer(2));
//             pause();
//             b.bPane.add(two,new Integer(3));
//             pause();
//            // b.bPane.add(seven,new Integer(2));
//            // pause();
//             b.bPane.add(three,new Integer(4));
//             pause();
//            // b.bPane.add(four,new Integer(3));
//            // pause();
//             b.bPane.add(five,new Integer(5));
//             pause();
//             b.bPane.add(six, new Integer(2));



      }
}
