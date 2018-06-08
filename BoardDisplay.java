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
      private volatile static JPanel outputPanel;
      private JTextArea output;
      private ArrayList<JLabel> playerLabels= new ArrayList();
      private ArrayList<JLabel> cardLabels = new ArrayList();
      private ArrayList<JLabel> smLabels= new ArrayList();
      public static JList inputList ;
      public static JScrollPane inputScroll ;
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
       outputPanel = new JPanel(new GridBagLayout()) ;

       output = new JTextArea() ;
       output.setLocation(900,0) ;
       output.setEditable(false) ;
       output.setLineWrap(true) ;
       output.setColumns(30) ;
       output.setRows(31) ;
       JScrollPane jScrollPane = new JScrollPane(output) ;
       jScrollPane.setPreferredSize(new Dimension(200,450));
       jScrollPane.setMaximumSize(new Dimension(370,500)) ;
       GridBagConstraints c = new GridBagConstraints() ;
       c.gridx = 0;
       c.gridy = 0;
       c.gridwidth = 3;
       c.gridheight = 2;
       outputPanel.setSize(370,600) ;
       outputPanel.add(jScrollPane, c) ;
       outputPanel.setOpaque(true) ;
       jScrollPane.setOpaque(true) ;

       // Make inputList
       inputList = new JList<String>();
       inputScroll = new JScrollPane(inputList);
       GridBagConstraints k = new GridBagConstraints();
       k.gridx = 0;
       k.gridy = 3;
       //outputPanel.add(inputScroll,k) ;

       // Make select button
       Button select = new Button("Select") ;
       GridBagConstraints b = new GridBagConstraints();
       b.gridx = 2;
       b.gridy = 3;
       b.fill = GridBagConstraints.BOTH;
       select.addActionListener(
       new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent event){
           if(inputList.getSelectedValue() != null){
             in = inputList.getSelectedValue().toString() ;
             output.append(in + "\n") ;
           }
         }
       }) ;
       outputPanel.add(select, b) ;

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
      input.setLocation(1215,750) ;
      input.setSize(300,20) ;
      this.setVisible(true);

      // put players in trailers

      toTrailers(players);
      setSceneLabels(activeScenes);

      // Create buttons
      JButton act = new JButton("Act") ;
      act.setBounds(1230,650,60,50) ;
      act.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        in = ("Act") ;
        output.append("Act") ;
      }
      });

      JButton rehearse = new JButton("Rehearse") ;
      rehearse.setBounds(1290,650,60,50) ;
      rehearse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        in = ("Rehearse") ;
        output.append("Rehearse") ;
      }
      });

      JButton move = new JButton("Move") ;
      move.setBounds(1350,650,60,50) ;
      move.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          in = ("Move") ;
          output.append("Move\n") ;
        }
      });


             // Add it all
             bPane.add(input) ;
             //bPane.add(act) ;
             //bPane.add(rehearse) ;
             //bPane.add(move) ;
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

      public void setSceneLabels(ArrayDeque<SceneCard> activeScenes){

            JLabel cardLabel ;
            int i = 0 ;
    // Update room-by-room

            for(Room room: Room.sets){
                  cardLabel= cardlabel(room);
                  cardLabels.add(cardLabel);
                  bPane.add(cardLabel, new Integer(1));
                  //pause();
            }
                  //this.setVisible(true) ;

      }
      // MOVE TO ROOM
      public void moveToRoom(Player player){

            // create filename

            char color= player.color;
            int rank= player.getRank();
            String filename= new String(Character.toString(color)+ Integer.toString(rank)+ ".png");
            int x;
            int offset= 50;
            int playersInRoom= player.location.playersInRoom.size();

            //coordinates
            if ( playersInRoom > 1){
                  x= player.location.x+ ((playersInRoom-1)*offset);
                  
            }
            else
             x= player.location.x ;
             
             
            int y= player.location.y + 125;
            //



            // create new playerlabel

            JLabel playerLabel = playerlabel(filename, x, y);
            bPane.add(playerLabel, new Integer(2));
            playerLabels.add(playerLabel);

         }
      public void moveToRole(Player player){

            // create filename

            char color= player.color;
            int rank= player.getRank();
            String filename= new String(Character.toString(color)+ Integer.toString(rank)+ ".png");

            //coordinates
            int x= player.role.x ;
            int y= player.role.y;

            //



            // create new playerlabel

            JLabel playerLabel = playerlabel(filename, x, y);
            bPane.add(playerLabel, new Integer(2));
            playerLabels.add(playerLabel);




      }
      
      public void addMarker(Room location){
            
           
           
           JLabel smLabel= smlabel(location);
           bPane.add(smLabel, new Integer(3));
           smLabels.add(smLabel);
           
      
      }
            
      public void println(String out){
       output.append(out + "\n") ;
      }
      // will be replaced with better method for handling input
      public void clearIn(){
       in = "" ;
      }

      // Provides the user with a list of options to select
      public static String getInputList(String[] options){
      // Make list
      outputPanel.remove(inputScroll) ;
      inputList = new JList<String>(options) ;
      inputScroll = new JScrollPane(inputList) ;
      GridBagConstraints k = new GridBagConstraints() ;
      k.gridx = 0 ;
      k.gridy = 3 ;
      outputPanel.add(inputScroll,k) ;
      outputPanel.revalidate() ;
      GameSystem.display.revalidate() ;

      // Wait for input
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
      // Waits for user input before continuing
      public static String getInput(){
        /*
        DefaultListModel listModel = new DefaultListModel();
        listModel.addElement("Move");
        inputList = new JList<String>(listModel) ;
        JScrollPane inputScroll = new JScrollPane(inputList);
        GridBagConstraints k = new GridBagConstraints();
        k.gridx = 0;
        k.gridy = 3;
        outputPanel.add(inputScroll,k) ;
        */

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
                  Thread.sleep(500);
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

      public JLabel cardlabel(Room location){ // will add Room location - using Jail for testing

            JLabel cardlabel = new JLabel();

            int sceneNum = location.currentScene.number;
            String filename;
            ImageIcon cIcon;
            if (location.currentScene.flipped == true){
            
                  if(sceneNum <10)
                     filename= new String("0"+sceneNum+".png");
                  else
                     filename= new String(sceneNum+".png");
      
            cIcon =  new ImageIcon("GUIFiles/cards/"+ filename);
            cardlabel.setIcon(cIcon);
            cardlabel.setBounds(location.x,location.y,205,115); // flipped
            cardlabel.setOpaque(true);
            }
            else {
            cIcon =  new ImageIcon("GUIFiles/cards/cardback.png");
            cardlabel.setIcon(cIcon);
            cardlabel.setBounds(location.x,location.y,205, 115); // not flipped yet
            cardlabel.setOpaque(true);
            
            }
                  return cardlabel;
            

      }
      public JLabel smlabel(Room location){
            
            JLabel smlabel = new JLabel();
            int remaining= location.shotsRemaining;
            int x= location.shotLocations[(location.shotMarkers-1)- remaining][0];
            int y= location.shotLocations[(location.shotMarkers-1)- remaining][1];
            //file name is always the same
            
            ImageIcon smIcon =  new ImageIcon("GUIFiles/shotmarker.png");
            smlabel.setIcon(smIcon); 
            smlabel.setBounds(x,y, 39, 46); 
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
