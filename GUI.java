// GUI for Deadwood USA V1
// Nick Pickering

import java.awt.* ;
import java.awt.event.*;
import java.awt.image.BufferedImage ;
import java.awt.Graphics2D ;
import javax.swing.* ;
import java.util.* ;

public class GUI extends JFrame{


  static GUI gui = null ;
  static JTextArea output ;
  static JTextField input ;
  static JScrollPane jScrollPane ;
  volatile static String in = "" ; // For cross - thread communication. I know it's bad

  // Constructor
  // Singelton ;)
  private GUI(){
    this.initialize() ;
  }

  // Sets up board, text areas, and main buttons
  private void initialize(){
    this.setDefaultCloseOperation(EXIT_ON_CLOSE) ;
    JPanel panel = new JPanel() ;
    panel.setLayout(new BorderLayout()) ;
    this.setSize(1600,900) ;

    // Create/resize board
    // maybe make everything scale later instead of hardcoding this, mi
    JLabel label = new JLabel() ;
    ImageIcon icon = new ImageIcon("GUIFiles/board.jpg") ;
    Image img = icon.getImage() ;

    label.setIcon(new ImageIcon(img)) ;
    label.setLocation(0,0) ;
    label.setSize(1200,900) ;

    // Create Text areas (needs to be cleaned up)
    JPanel outputPanel = new JPanel() ;

    output = new JTextArea() ;
    output.setLocation(900,0) ;
    output.setEditable(false) ;
    output.setLineWrap(true) ;
    output.setColumns(20) ;
    output.setRows(31) ;
    jScrollPane = new JScrollPane(output) ;
    jScrollPane.setPreferredSize(new Dimension(370,480));
    jScrollPane.setMaximumSize(new Dimension(370,300)) ;
    jScrollPane.setLocation(20,0) ;
    outputPanel.add(jScrollPane) ;
    outputPanel.setOpaque(true) ;
    jScrollPane.setOpaque(true) ;

    // input text field
    input = new JTextField() ;
    input.addActionListener(
    new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event){
          in = input.getText() ; // Allows chunks of user input to be accessed by program
          output.append(in + "\n") ;
          input.selectAll() ;
          //Process input
        }
    }) ;
    input.setLocation(1215,500) ;

    input.setSize(400,20) ;

    // Create buttons
    JButton act = new JButton("Act") ;
    act.setBounds(1250,550,50,50) ;
    act.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        in = ("Act") ;
        output.append("Act" + "\n") ;
        input.selectAll() ;
      }
    });

    JButton rehearse = new JButton("Rehearse") ;
    rehearse.setBounds(1300,550,50,50) ;
    rehearse.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        in = ("Rehearse") ;
        output.append("Rehearse" + "\n") ;
      }
    });

    JButton move = new JButton("Move") ;
    move.setBounds(1350,550,50,50) ;
    move.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        output.append("Move" + "\n") ;
        in = ("Move") ;
      }
    });

    JLayeredPane bPane = this.getLayeredPane() ;

    // Add it all
    bPane.add(label) ;
    bPane.add(input) ;
    bPane.add(act) ;
    bPane.add(rehearse) ;
    bPane.add(move) ;
    this.add(outputPanel, BorderLayout.EAST) ;
    label.setOpaque(true) ;

    // Display
    this.setVisible(true) ;
  }

  // will be replaced with better method for handling input
  public static void clearIn(){
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

  // Replacement for println
  public static void println(String out){
    output.append(out + "\n") ;
  }

  // Replacement for print
  public static void print(String out){
    output.append(out) ;
  }

  // Main for testing
  public static void main(String[] args){
    Room.readRooms() ;
    SceneCardManager.readCards() ;
    SceneCardManager.deal() ;
    getGUI() ;
    println("Hello world") ;
    print("Hello world") ;
    update() ;
    SceneCardManager.deal() ;
    update() ;
  }

  // Getter for GUI instance
  public static GUI getGUI(){
    if(gui == null){
      gui = new GUI() ;
    }
    return gui ;
  }

  // Displays updated GUI. Super not done
  public static void update(){
    gui.setVisible(false) ;
    JLabel cardLabel = new JLabel() ;
    JLabel playerlabel;
    JLabel shotLabel;
    JPanel panel = new JPanel() ;
    ArrayList<JLabel> labelList = new ArrayList<JLabel>() ;
    int i = 0 ;
    // Update room-by-room
    for(Room room: Room.sets){
      labelList.add(new JLabel()) ;
      //System.out.println("GUIFiles/" + room.currentScene.img) ;
      labelList.get(i).setIcon(new ImageIcon("GUIFiles/cards/" + room.currentScene.img)) ;
      labelList.get(i).setBounds(room.x,room.y,205,115) ;
      labelList.get(i).setOpaque(true) ;
      i++ ;

      // Show SceneCard
      // Show players
      // Show shot markers
    }
    for(JLabel card: labelList){
      gui.getLayeredPane().add(card, new Integer(2)) ;
    }
    gui.setVisible(true) ;

    // Handle office and trailers
      // Show players

  }


}
