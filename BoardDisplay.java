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
      private static JPanel outputPanel;
      private JTextArea output;
      private ArrayList<JLabel> playerLabels = new ArrayList<JLabel>();
      private ArrayList<JLabel> cardLabels = new ArrayList<JLabel>();
      private ArrayList<JLabel> smLabels= new ArrayList<JLabel>();
      public static JList inputList ;
      public static JScrollPane inputScroll ;
      public volatile static String in = "" ;
      private JTextField input;
      private Button select;
      private JScrollPane jScrollPane;



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
       jScrollPane = new JScrollPane(output) ;
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
        select = new Button("Select") ;
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
      input = new JTextField() ;
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
      this.setVisible(true) ;

      // put players in trailers

      toTrailers(players) ;
      setSceneLabels(activeScenes) ;

      // Add it all
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
            bPane.add(label, new Integer(2));
            playerLabels.add(label);


            offset= offset+50; // offset the icons
      }


      }
      ////////////////////////////////// SCENE CARD LABELS /////////////////////////////

      public void setSceneLabels(ArrayDeque<SceneCard> activeScenes){

            JLabel cardLabel ;
            int i = 0 ;
    // Update room-by-room

            //cardLabels= new ArrayList<JLabel>();

            for(Room room: Room.sets){
                  cardLabel= cardlabel(room);

                  if(cardLabels.size() < i)
                  cardLabels.remove(i);

                  cardLabels.add(i,cardLabel);
                  i++;

            }

            Component[] scenelabels= bPane.getComponentsInLayer(1);
            for(Component scene: scenelabels)
                  bPane.remove(scene);

            for(JLabel label: cardLabels)
                  bPane.add(label, new Integer(1));


            revalidate() ;
            repaint();

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

            int y;
            if(player.location.equals(Room.office)){
              y= player.location.y;
            }else{
              y= player.location.y + 125;
            }
            //

            // create new playerlabel

            JLabel playerLabel = playerlabel(filename, x, y);
            //** delete old playerlabel for player num
            deletePlayerLabels(player.playerNumber);
            playerLabels.add(player.playerNumber-1, playerLabel);
            redrawPlayerLabels();
            revalidate() ;
            repaint();


         }

      public void moveToRole(Player player){

            // create filename
            char color= player.color;
            int rank= player.getRank();
            String filename= new String(Character.toString(color)+ Integer.toString(rank)+ ".png");

            // Check cardonality of role
            boolean onCard = true ;
            for(Role role: player.location.extraRoles){
              if(player.role.equals(role)){
                onCard = false ;
              }
            }

            // Assign coordinates
            int x;
            int y;
            if(!onCard){
              x = player.role.x ;
              y = player.role.y ;
            }else{
              x = player.role.x + player.location.x ;
              y = player.role.y + player.location.y ;
            }

            // create new playerlabel

            JLabel playerLabel = playerlabel(filename, x, y);
            bPane.add(playerLabel, new Integer(2));
            deletePlayerLabels(player.playerNumber);
            playerLabels.add(player.playerNumber-1, playerLabel);
            redrawPlayerLabels();
            revalidate() ;
            repaint();




      }

      public void addMarker(Room location){



           JLabel smLabel= smlabel(location);
           smLabels.add(smLabel);
           bPane.add(smLabel, new Integer(3));


      }
      public void deletePlayerLabels(int playernum){


            playerLabels.remove(playernum-1);

            Component[] oldPlayers= bPane.getComponentsInLayer(2);

            for( Component label: oldPlayers)
                  bPane.remove(label);

            repaint();
            revalidate();

      }
      public void redrawPlayerLabels(){

            for(JLabel playerlabel: playerLabels){
               bPane.add(playerlabel, new Integer(2));
                  }
            repaint();
            revalidate();

      }

      public void emptySM(){

            smLabels= new ArrayList<JLabel>();
            Component[] shots= bPane.getComponentsInLayer(3);
            for(Component shotmarker : shots){

                  bPane.remove(shotmarker);
            }
            repaint();
            revalidate();
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
            playerlabel.setBounds(x+3, y+3, pIcon.getIconWidth(), pIcon.getIconHeight()); // Prisoner in Cell Role
            playerlabel.setOpaque(true);
            return playerlabel;


      }

      public JLabel cardlabel(Room location){ // will add Room location - using Jail for testing

            JLabel cardlabel = new JLabel();

            int sceneNum = location.currentScene.number;
            String filename;
            ImageIcon cIcon;

            if (location.currentScene.flipped == false){

            cIcon =  new ImageIcon("GUIFiles/cards/cardback.png");
            cardlabel.setIcon(cIcon);
            cardlabel.setBounds(location.x,location.y,205, 115); // not flipped yet
            cardlabel.setOpaque(true);

            }
            else{

            filename= location.currentScene.img;
            cIcon =  new ImageIcon("GUIFiles/cards/"+ filename);
            cardlabel.setIcon(cIcon);
            cardlabel.setBounds(location.x,location.y,205,115); // flipped
            cardlabel.setOpaque(true);
                 }

            return cardlabel;


      }
      public JLabel smlabel(Room location){

            JLabel smlabel = new JLabel();
            int remaining= location.shotsRemaining;
            int x= location.shotLocations[remaining][0];
            int y= location.shotLocations[remaining][1];
            //file name is always the same

            ImageIcon smIcon =  new ImageIcon("GUIFiles/shotmarker.png");
            smlabel.setIcon(smIcon);
            smlabel.setBounds(x,y, 39, 46);
            smlabel.setOpaque(true);

                  return smlabel;


      }
     // public static void newPlayerLabel(Player currentPlayer, Role location,
      public static void main(String[] args){




      }
}
