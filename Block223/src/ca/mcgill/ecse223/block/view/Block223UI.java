package ca.mcgill.ecse223.block.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOCurrentBlock;
import ca.mcgill.ecse223.block.controller.TOCurrentlyPlayedGame;
import ca.mcgill.ecse223.block.controller.TOHallOfFame;
import ca.mcgill.ecse223.block.controller.TOHallOfFameEntry;
import ca.mcgill.ecse223.block.controller.TOPlayableGame;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Paddle;
import ca.mcgill.ecse223.block.view.block.AddBlockToGameUI;
import ca.mcgill.ecse223.block.view.block.DeleteBlockFromGameUI;
import ca.mcgill.ecse223.block.view.block.UpdateBlockFromGameUI;
import ca.mcgill.ecse223.block.view.file.ExitUI;
import ca.mcgill.ecse223.block.view.file.LogInUI;
import ca.mcgill.ecse223.block.view.file.LogOutUI;
import ca.mcgill.ecse223.block.view.file.RegisterUI;
import ca.mcgill.ecse223.block.view.file.SaveGameUI;
import ca.mcgill.ecse223.block.view.game.AddGameUI;
import ca.mcgill.ecse223.block.view.game.DefineGameSettingsUI;
import ca.mcgill.ecse223.block.view.game.DeleteGameUI;
import ca.mcgill.ecse223.block.view.game.UpdateGameUI;
import ca.mcgill.ecse223.block.view.level.MoveBlockInLevelUI;
import ca.mcgill.ecse223.block.view.level.PositionBlockInLevelUI;
import ca.mcgill.ecse223.block.view.level.RemoveBlockInLevelUI;
import ca.mcgill.ecse223.block.view.play.GameOverUI;
import ca.mcgill.ecse223.block.view.play.HallOfFameUI;
import ca.mcgill.ecse223.block.view.play.PlayGameUI;
import ca.mcgill.ecse223.block.view.play.PublishGameUI;
import ca.mcgill.ecse223.block.view.play.TestGameUI;

/**
 * This class provides the view for the main window of the Block223 application.
 * 
 * Contains a task-bar with menu options for all required features, a header section with 
 * current game data, a Hall of Fame section (HoF) with list of top players and a play
 * area section with a Ball, Paddle and Block objects represented by Java Swing objects.
 * 
 * Each feature that has its own window/menu will have a button in the task bar. Clicking
 * this button should open a new window (a new JFrame), and have the all the required
 * java swing UI elements needed to accomplish its feature.
 * 
 * This class will have a reference to the Block223Application as well as pass itself
 * as a parameter when creating new UI popup windows, so that the UI windows have
 * access to application data.
 * 
 */
public class Block223UI extends JFrame implements Block223PlayModeInterface {
  
  /**
   * Generated Serial Version UID
   */
  private static final long serialVersionUID = -7115209896515435572L;
  
  //****************************
  // UI Components
  //*****************************
  /* Menu bar */
  private JMenuBar menuBar;
  private JMenu fileMenu;
  private JMenuItem saveGameMenuItem, logInMenuItem, registerMenuItem, logOutMenuItem, exitMenuItem;
  private JMenu playMenu;
  private JMenuItem playGameMenuItem, testGameMenuItem, publishGameMenuItem;
  private JMenu gameMenu;
  private JMenuItem addGameMenuItem, defineGameSettingsMenuItem, deleteGameMenuItem, updateGameMenuItem;
  private JMenu blockMenu;
  private JMenuItem addBlockToGameMenuItem, deleteBlockFromGameMenuItem, updateBlockFromGameMenuItem;
  private JMenu levelMenu;
  private JMenuItem positionBlockInLevelMenuItem, moveBlockInLevelMenuItem, removeBlockInLevelMenuItem;
  
  /* Header */
  private JLabel headerTitleLabel;
  private JLabel headerLevelLabel;
  private JLabel headerLivesLabel;
  private JLabel headerScoreLabel;
  /* Play area */
  // Need java swing objects to represent each of the following
  // Paddle, Ball and Blocks
  private JPanel playAreaPanel;
  /* hall of fame (HoF) */ 
  private JLabel HoFTitleLabel; // Hall of fame title 
  private JLabel HoFNameLabel; // Hall of Fame name
  private JLabel[][] HoFScoreLabels;
  // some sort of Jlist for users and their score
  private JButton HoFPreviousButton;
  private JButton HoFNextButton;
  /* other UI stuff */
  private JLabel errorMessageLabel;
  private JLabel statusMessageLabel;
  
  //****************************
  // Constants
  //****************************
  // constant data elements (ie: titles etc.)
  // TODO these might not have to be constants?
  private static final String WINDOW_TITLE = "Block 223";
  private static final String HEADER_TITLE = "BLOCK 223";
  private static final String HOF_TITLE = "Hall of Fame:";
  private static final String LEVEL_TEXT = "Level: ";
  private static final String LIVES_TEXT = "Lives: ";
  private static final String SCORE_TEXT = "Score: ";
  // formatting constants (all in px)
  // these are the same for any UI element in Block223 application
  public static final int WALL_PADDING = 10; //px
  public static final int COLUMN_PADDING = 5; //px
  public static final int ROW_PADDING = 2; // px
  public static final int BLOCK_SIDE_LENGTH = 20; // px, blocks are squares
  public static final int BALL_DIAMETER = 10; //px
  public static final int PADDLE_WIDTH = 5; //px
  
  private static final int PLAY_AREA_SIZE = 390; //px
  private static final float HEADER_HEIGHT_FACTOR = 0.15f; // the height the JLabel headerTitleLabel is (this constant * this.getHeight())
  
  public static final Font TITLE_FONT = new Font(null); // not being used right now
  private static final String FAVICON_URL = "/ca/mcgill/ecse223/block/resource/block223_icon_32x32.png";
  
  //****************************
  // Data elements
  //****************************
  private JFrame popupWindow;
  private int _lives; // current number of lives remaining to display in UI
  private int _level; // current level to display in UI
  private int _score; // current players score
  private String _errorMsg; // error message displayed at top of window (just under menu bar)
  private String _status; // status message displayed at top of window (just under error msg)
  private String _hofGroup; // The name of the HoF group currently being displayed
  private HashMap<Integer, HoFEntry> _highscores; // The currently displayed HoF. Key is position on the leaderboard.
  // play area will need stuff too
  /** Storage for predefined user inputs - the value with the key 0 will be ignored */
  private Map<Integer, String> _inputs;
  /** Internal counter to keeps track how many times the takeInputs() method was called  */
  private volatile int _takeCounter;
  /** Temp storage for user input inbetween game loops. */
  private volatile String _keyString;
  /** KeyListener */
  Block223KeyListener blockKeyListener;
  
  /**
   * Defualt contructor with no params
   * 
   * @param inputs
   */
  public Block223UI () {
    this(new HashMap<Integer, String>());
  }
  
  /**
   * Creates a new Block223 game User Interface.
   */
  public Block223UI (Map<Integer, String> inputs) {
    // init instance variables
    popupWindow = new JFrame();
    _lives = 0;
    _level = 0;
    _score = 0;
    _errorMsg = "";
    _status = "";
    // init HoF with default values for now i guess?
    _hofGroup = "Default";
    _highscores = new HashMap<Integer, HoFEntry>(10);
    for (int i = 0; i < 10 /*length of HoF*/; i++) {
      _highscores.put(i,new HoFEntry("Placeholder",100));
    }
    
    _inputs = inputs;
    _takeCounter = 0;
    _keyString = "";
    
    initComponents();
    this.setVisible(true);
    refresh();
  }
  
  /**
   * Setup all UI components in this method. Call this method in the constructor.
   * Copied from Java swing tutorial BtmsPage.java class.
   */
  private void initComponents() { 
    // global settings
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set application to exit on close of main window
    setTitle(WINDOW_TITLE);
    // set favicon
    URL iconURL = getClass().getResource(FAVICON_URL);
    // iconURL is null when not found
    ImageIcon icon = new ImageIcon(iconURL);
    setIconImage(icon.getImage());
    
    // init error message
    errorMessageLabel = new JLabel();
    errorMessageLabel.setForeground(Color.RED); 
    
    // init status message
    statusMessageLabel = new JLabel();
    statusMessageLabel.setForeground(Color.BLACK);
    
    // setup error message
    errorMessageLabel.setText(_errorMsg);
    // setup status message
    statusMessageLabel.setText(_status);
    
    // TODO: move all this menu bar bull crap to a seperate method or something
    setupMenuBar();
    
    // elements for header
    headerTitleLabel = new JLabel();
    headerLevelLabel = new JLabel();
    headerLivesLabel = new JLabel();
    headerScoreLabel = new JLabel();
    
    // setup elements
    headerTitleLabel.setText(HEADER_TITLE);
    headerTitleLabel.setForeground(Color.red);
    headerTitleLabel.setSize(100, (int) (HEADER_HEIGHT_FACTOR*getHeight()));
    headerTitleLabel.setFont(new Font(headerTitleLabel.getFont().getName(), Font.BOLD, 30));
    headerLevelLabel.setText(LEVEL_TEXT + _level); 
    headerLivesLabel.setText(LIVES_TEXT + _lives);
    headerScoreLabel.setText(SCORE_TEXT + _score);
    headerLevelLabel.setForeground(Color.red);
    headerLivesLabel.setForeground(Color.red);
    headerScoreLabel.setForeground(Color.red);
    // elments for play area
    playAreaPanel = new JPanel();
    
    // square of size 390 x 390
    playAreaPanel.setSize(PLAY_AREA_SIZE, PLAY_AREA_SIZE);
    playAreaPanel.setBackground(Color.cyan);
    // add a 1px black border to play area
    playAreaPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    
    // elements for HoF area
    HoFTitleLabel = new JLabel();
    HoFNameLabel = new JLabel();
    HoFPreviousButton = new JButton();
    HoFNextButton = new JButton();

    HoFScoreLabels = new JLabel[2][10]; // 2 rows, 10 cols
    for (int i = 0; i < HoFScoreLabels.length; i++) { // 2 rows
      for (int j = 0; j < HoFScoreLabels[i].length; j++) { // 10 cols
        if (i == 0) {
          HoFScoreLabels[i][j] = new JLabel(_highscores.get(j).getName());
        } else if (i == 1) {
          HoFScoreLabels[i][j] = new JLabel(_highscores.get(j).getScore());
        }
       }
    }
    
    // setup HoF elements
    HoFTitleLabel.setText(HOF_TITLE);
    HoFTitleLabel.setForeground(Color.red);
    HoFNameLabel.setText(_hofGroup);
    HoFNameLabel.setForeground(Color.red);
    HoFPreviousButton.setText("Previous");
    HoFNextButton.setText("Next");
    
    // Horizontal Line elements
    // dont need first one i think because menu bar is a different element
    //JSeparator horz_LineTop = new JSeparator(); // Separate task bar and header
    JSeparator horz_LineMiddle = new JSeparator(); // seperate header and rest of window (hall of fame and play area)
    JSeparator vert_Line = new JSeparator(SwingConstants.VERTICAL); // seperate hall of fame and play area
    
    // layout
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    // Create horizontal and vertical groups in layout.
    // Every component must be in both the vertical and horizontal group
    // or an IllegalStateException will be thrown.
    GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
    // Each horizontal and vertical group may contain multiple parrellel groups
    // these are essentially the rows of the horizontal axis and columns of the
    // vertical axis. Refer to 
    //     https://docs.oracle.com/javase/7/docs/api/javax/swing/GroupLayout.html
    // for more information.
    // Another good tutorial for GroupLayout
    //     https://docs.oracle.com/javase/tutorial/uiswing/layout/group.html
    
    // horz group is parallel
    hGroup.addComponent(errorMessageLabel);
    hGroup.addComponent(statusMessageLabel);
    hGroup.addGroup(layout.createSequentialGroup()
        // header group
        .addComponent(headerTitleLabel)
        .addGap(0, 100, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(Alignment.TRAILING, true)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerLevelLabel)
                .addComponent(headerLivesLabel))
            .addComponent(headerScoreLabel)
        )
    );
    hGroup.addComponent(horz_LineMiddle);
    hGroup.addGroup(layout.createSequentialGroup()
        .addComponent(playAreaPanel, GroupLayout.PREFERRED_SIZE, PLAY_AREA_SIZE, GroupLayout.PREFERRED_SIZE)
        .addComponent(vert_Line)
        .addGroup(layout.createParallelGroup()
            // HoF
            .addComponent(HoFTitleLabel)
            .addComponent(HoFNameLabel)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                    .addComponent(HoFScoreLabels[0][0])
                    .addComponent(HoFScoreLabels[0][1])
                    .addComponent(HoFScoreLabels[0][2])
                    .addComponent(HoFScoreLabels[0][3])
                    .addComponent(HoFScoreLabels[0][4])
                    .addComponent(HoFScoreLabels[0][5])
                    .addComponent(HoFScoreLabels[0][6])
                    .addComponent(HoFScoreLabels[0][7])
                    .addComponent(HoFScoreLabels[0][8])
                    .addComponent(HoFScoreLabels[0][9])
                    )
                .addGap(0,20,Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(Alignment.TRAILING, true)
                    .addComponent(HoFScoreLabels[1][0])
                    .addComponent(HoFScoreLabels[1][1])
                    .addComponent(HoFScoreLabels[1][2])
                    .addComponent(HoFScoreLabels[1][3])
                    .addComponent(HoFScoreLabels[1][4])
                    .addComponent(HoFScoreLabels[1][5])
                    .addComponent(HoFScoreLabels[1][6])
                    .addComponent(HoFScoreLabels[1][7])
                    .addComponent(HoFScoreLabels[1][8])
                    .addComponent(HoFScoreLabels[1][9])
                    )
                )
            .addGroup(layout.createSequentialGroup()
                .addComponent(HoFPreviousButton)
                .addComponent(HoFNextButton)
                )
            
            )
        );
    hGroup.addGap(0,10,Short.MAX_VALUE); // add small gap at bottom
    
    // vertical group is sequential
    vGroup.addComponent(errorMessageLabel);
    vGroup.addComponent(statusMessageLabel);
    vGroup.addGroup(layout.createParallelGroup()
        // header group
        .addComponent(headerTitleLabel, GroupLayout.Alignment.LEADING)
        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(headerLevelLabel)
                .addComponent(headerLivesLabel))
            .addComponent(headerScoreLabel)
            )
        );
    vGroup.addComponent(horz_LineMiddle);
    vGroup.addGroup(layout.createParallelGroup(Alignment.LEADING)
        .addComponent(playAreaPanel, GroupLayout.PREFERRED_SIZE, PLAY_AREA_SIZE, GroupLayout.PREFERRED_SIZE)
        .addComponent(vert_Line,0,PLAY_AREA_SIZE,Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            //.addComponent(HoF_component))
            .addComponent(HoFTitleLabel)
            .addComponent(HoFNameLabel)
            .addGap(0, 30, Short.MAX_VALUE) // TODO hard coded value
            .addGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addComponent(HoFScoreLabels[0][0])
                    .addComponent(HoFScoreLabels[0][1])
                    .addComponent(HoFScoreLabels[0][2])
                    .addComponent(HoFScoreLabels[0][3])
                    .addComponent(HoFScoreLabels[0][4])
                    .addComponent(HoFScoreLabels[0][5])
                    .addComponent(HoFScoreLabels[0][6])
                    .addComponent(HoFScoreLabels[0][7])
                    .addComponent(HoFScoreLabels[0][8])
                    .addComponent(HoFScoreLabels[0][9])
                    )
                .addGroup(layout.createSequentialGroup()
                    .addComponent(HoFScoreLabels[1][0])
                    .addComponent(HoFScoreLabels[1][1])
                    .addComponent(HoFScoreLabels[1][2])
                    .addComponent(HoFScoreLabels[1][3])
                    .addComponent(HoFScoreLabels[1][4])
                    .addComponent(HoFScoreLabels[1][5])
                    .addComponent(HoFScoreLabels[1][6])
                    .addComponent(HoFScoreLabels[1][7])
                    .addComponent(HoFScoreLabels[1][8])
                    .addComponent(HoFScoreLabels[1][9])
                    )
                )
            .addGap(0, 80, Short.MAX_VALUE) // TODO hard coded value
            .addGroup(layout.createParallelGroup()
                .addComponent(HoFPreviousButton)
                .addComponent(HoFNextButton))
            )
        );
    vGroup.addGap(0,10,Short.MAX_VALUE); 
    
    setJMenuBar(menuBar);
    layout.setHorizontalGroup(hGroup);
    layout.setVerticalGroup(vGroup);
    this.setFocusable(true);
    pack(); // pack jframe, final step
  }
  
  /**
   * Refresh all data displayed by UI components.
   * Copied from Java swing tutorial BtmsPage.java class.
   * 
   * This method should be called whenever anything part of the Block223
   * UI is changed, as any UI change, including user input, could change
   * what is supposed to be displayed.
   */
  @Override
  public synchronized void refresh () {
    // get key input

    if (_keyString != null) {
      _inputs.put(_takeCounter, _keyString);
    }
    // clear input for next loop
    if (blockKeyListener != null) {
      _keyString = blockKeyListener.takeInputs();
    } else {
      _keyString = "";
    }
    
    /*
    // update _highscores
    if (Block223Application.getCurrentPlayableGame() != null) {
      try {
        List<TOHallOfFameEntry> hofs = Block223Controller.getHallOfFame(0, 10).getEntries();
        int i = 0;
        for (TOHallOfFameEntry h : hofs) {
          HoFEntry hofentry = new HoFEntry(h.getPlayername(), h.getScore());
          _highscores.put(i++,hofentry);
        }
      } catch (InvalidInputException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    */
    
    // update status message
    try {
    _status = "Logged in as " + Block223Application.getCurrentUserRole().toString() + "selected game: " + Block223Application.getCurrentGame().toString();
    _status = _status.substring(0, 200);
    } catch (Exception e) {
      _status = "current user role or current game is not set";
    }
    _status = "";
    
    // update UI components
    errorMessageLabel.setText(_errorMsg);
    statusMessageLabel.setText(_status);
    // display error message if there is one, else update all UI elements
    if (_errorMsg == null || _errorMsg.length() == 0) {
      // if no error, populate page with data
      headerLevelLabel.setText(LEVEL_TEXT + _level); // TODO 
      headerLivesLabel.setText(LIVES_TEXT + _lives);
      headerScoreLabel.setText(SCORE_TEXT + _score);
      HoFNameLabel.setText(_hofGroup);
      // update HashMap of HoF scores.
      for (int i = 0; i < HoFScoreLabels.length; i++) { // 2 rows
        for (int j = 0; j < HoFScoreLabels[i].length; j++) { // 10 columns
          if (i == 0) {
            HoFScoreLabels[i][j].setText(_highscores.get(j).getName());
          } else if (i == 1) {
            HoFScoreLabels[i][j].setText(_highscores.get(j).getScore());;
          }
         }
      }
      
      // refresh game components
      refreshGameData();
      
      // refresh more data
    } else {
      
    }
    
    // this is needed because the size of the window changes depending on whether an error message is shown or not
    pack();
  }
  
  private synchronized void refreshGameData () {
    Graphics g = playAreaPanel.getGraphics();
    playAreaPanel.paint(g);
    //playAreaPanel.paint
    TOCurrentlyPlayedGame pgame = null;
    //TOPlayableGame
    try {
       pgame = Block223Controller.getCurrentPlayableGame();
    } catch (InvalidInputException e) {
        //_errorMsg = e.getMessage();
    }
    
    if (pgame != null) {
      // update data
      _lives = pgame.getLives();
      _score = pgame.getScore();
      _level = pgame.getCurrentLevel();
      // update blocks
      List<TOCurrentBlock> blocks = pgame.getBlocks();
      for (TOCurrentBlock b : blocks) {
        float red = b.getRed()/255.0f;
        float green = b.getGreen()/255.0f;
        float blue = b.getBlue()/255.0f;
        g.setColor(new Color(red,green,blue) );
        g.fillRect(b.getX(), b.getY(), BLOCK_SIDE_LENGTH, BLOCK_SIDE_LENGTH);
      }
      // update ball and paddle
      g.setColor(Color.BLACK);
      g.fillOval((int)pgame.getCurrentBallX(), (int)pgame.getCurrentBallY(), BALL_DIAMETER, BALL_DIAMETER);
      g.fillRect((int)pgame.getCurrentPaddleX(), (PLAY_AREA_SIZE - Paddle.VERTICAL_DISTANCE),(int) pgame.getCurrentPaddleLength(), PADDLE_WIDTH);
    
      // repaint
      //playAreaPanel.paint
      //playAreaPanel.repaint();
    } else {
      // clear jpanel
      
    }
  }
  
  //****************************
  // Button Listeners
  //****************************
  
  /**
   * Listener for all the MenuItems. It passes the ActionEvent
   * to get the JMenuItem that method came from. It gets the text
   * from that JMenuItem and compares it to all the JMenuItem.getText()
   * for a match. If it matches, it creates a new Frame, gives focus to 
   * that frame and brings it to the front.
   * 
   * All the frames created this way should be singletons because we
   * don't want to have to deal with the case of a user having multiple
   * of the same windows open at the same time.
   * 
   * @param The ActionEvent that triggered this function to be called.
   */
  private void menuItemActionPerformed(ActionEvent evt) {
    // create new internal frame as popup
    JMenuItem m = (JMenuItem) evt.getSource();
    String menuitemtext = m.getText();
    popupWindow.dispose(); // this line is mostly a replacement for having the popup windows be singletons i think....
    if (saveGameMenuItem.getText().equals(menuitemtext)) {                    popupWindow = new SaveGameUI(this);
    } else if (logInMenuItem.getText().equals(menuitemtext)) {                popupWindow = new LogInUI(this);
    } else if (registerMenuItem.getText().equals(menuitemtext)) {             popupWindow = new RegisterUI(this);
    } else if (logOutMenuItem.getText().equals(menuitemtext)) {               popupWindow = new LogOutUI(this); 
    } else if (exitMenuItem.getText().equals(menuitemtext)) {                 popupWindow = new ExitUI(this);
    } else if (playGameMenuItem.getText().equals(menuitemtext)) {             popupWindow = new PlayGameUI(this);
    } else if (testGameMenuItem.getText().equals(menuitemtext)) {             popupWindow = new TestGameUI(this);
    } else if (publishGameMenuItem.getText().equals(menuitemtext)) {          popupWindow = new PublishGameUI(this);
    } else if (addGameMenuItem.getText().equals(menuitemtext)) {              popupWindow = new AddGameUI(this); 
    } else if (defineGameSettingsMenuItem.getText().equals(menuitemtext)) {   popupWindow = new DefineGameSettingsUI(this);
    } else if (deleteGameMenuItem.getText().equals(menuitemtext)) {           popupWindow = new DeleteGameUI(this);
    } else if (updateGameMenuItem.getText().equals(menuitemtext)) {           popupWindow = new UpdateGameUI(this);
    } else if (addBlockToGameMenuItem.getText().equals(menuitemtext)) {       popupWindow = new AddBlockToGameUI(this);
    } else if (deleteBlockFromGameMenuItem.getText().equals(menuitemtext)) {  popupWindow = new DeleteBlockFromGameUI(this);
    } else if (updateBlockFromGameMenuItem.getText().equals(menuitemtext)) {  popupWindow = new UpdateBlockFromGameUI(this);
    } else if (positionBlockInLevelMenuItem.getText().equals(menuitemtext)) { popupWindow = new PositionBlockInLevelUI(this);
    } else if (moveBlockInLevelMenuItem.getText().equals(menuitemtext)) {     popupWindow = new MoveBlockInLevelUI(this);
    } else if (removeBlockInLevelMenuItem.getText().equals(menuitemtext)) {   popupWindow = new RemoveBlockInLevelUI(this);
    } else { // if its none of them then wtf idk throw FAT ERROR
      _errorMsg = "Error: That menu item does not exist.";
    }
    
    // init window
    popupWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    popupWindow.setResizable(false); // simpler
    popupWindow.setAlwaysOnTop(true); // don't want this window to get lost
    popupWindow.setLocation(getX()+50,getY()+50); // hard coded offset for now
    popupWindow.toFront(); // bring new popup window frame to front
    popupWindow.setVisible(true); // last step, make the new window visible
    // update data elements
    refresh();
  }
  
  
  
  //****************************
  // Helper Methods
  //****************************
  
  /**
   * Returns  all  inputs  received  from  the  user  since  the  last  call  to  takeInputs() 
   * and removes all of these inputs from the queue
   * possible input are: l for left and r for right and space bar
   * @return
   */
  @Override
  public synchronized String takeInputs () {
    
    // Get the predefined input that mimics user interaction at loop iteration
    // "takeConter"
    String input = _inputs.get(_takeCounter++);
    // If there is no predefined input at this stage, return an empty string
    return input != null ? input : "";
  }
  
  /**
   * Method to setup the menu bar.
   * Mostly separate because its a lot of repetitive code that makes initComponents()
   * really messy.
   * 
   * WARNING: THICCCC METHOD
   */
  private void setupMenuBar () {
    // elements for menu bar
    menuBar = new JMenuBar();
    fileMenu = new JMenu("File");
    fileMenu.setMnemonic(KeyEvent.VK_F);
    playMenu = new JMenu("Play");
    playMenu.setMnemonic(KeyEvent.VK_P);
    gameMenu = new JMenu("Game");
    fileMenu.setMnemonic(KeyEvent.VK_G);
    blockMenu = new JMenu("Block");
    fileMenu.setMnemonic(KeyEvent.VK_B);
    levelMenu = new JMenu("Level");
    fileMenu.setMnemonic(KeyEvent.VK_L);
    // file menu items
    saveGameMenuItem = new JMenuItem("Save game", KeyEvent.VK_S);
    logInMenuItem = new JMenuItem("Log in", KeyEvent.VK_I);
    registerMenuItem = new JMenuItem("Register");
    logOutMenuItem = new JMenuItem("Log out", KeyEvent.VK_O);
    exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_E);
    // play game menu items
    playGameMenuItem = new JMenuItem("Play game");
    testGameMenuItem = new JMenuItem("Test game");
    publishGameMenuItem = new JMenuItem("Publish game");
    // game menu items
    addGameMenuItem = new JMenuItem("Add a game", KeyEvent.VK_A);
    defineGameSettingsMenuItem = new JMenuItem("Define game settings", KeyEvent.VK_D);
    deleteGameMenuItem = new JMenuItem("Delete a game's settings");
    updateGameMenuItem = new JMenuItem("Update a game's settings");
    // block menu items
    addBlockToGameMenuItem = new JMenuItem("Add a block to a game");
    deleteBlockFromGameMenuItem = new JMenuItem("Delete a block from a game");
    updateBlockFromGameMenuItem = new JMenuItem("Update a block from a game");
    // level menu items
    positionBlockInLevelMenuItem = new JMenuItem("Add a block to level");
    moveBlockInLevelMenuItem = new JMenuItem("Move a block in a level");
    removeBlockInLevelMenuItem = new JMenuItem("Remove a block from a level");

    // setup menu bar
    menuBar.add(fileMenu);
    menuBar.add(playMenu);
    menuBar.add(gameMenu);
    menuBar.add(blockMenu);
    menuBar.add(levelMenu);
    fileMenu.add(saveGameMenuItem);
    fileMenu.add(logInMenuItem);
    fileMenu.add(registerMenuItem);
    fileMenu.add(logOutMenuItem);
    fileMenu.add(exitMenuItem);
    playMenu.add(playGameMenuItem);
    playMenu.add(testGameMenuItem);
    playMenu.add(publishGameMenuItem);
    gameMenu.add(addGameMenuItem);
    gameMenu.add(defineGameSettingsMenuItem);
    gameMenu.add(deleteGameMenuItem);
    gameMenu.add(updateGameMenuItem);
    blockMenu.add(addBlockToGameMenuItem);
    blockMenu.add(deleteBlockFromGameMenuItem);
    blockMenu.add(updateBlockFromGameMenuItem);
    levelMenu.add(positionBlockInLevelMenuItem);
    levelMenu.add(moveBlockInLevelMenuItem);
    levelMenu.add(removeBlockInLevelMenuItem);
    
    // create new action listener for menu items.
    MenuBarActionListener al = new MenuBarActionListener();
    // setup menu items
    // TODO convert to lambda expressions (Java 1.8)
    saveGameMenuItem.addActionListener(al);
    logInMenuItem.addActionListener(al);
    registerMenuItem.addActionListener(al);
    logOutMenuItem.addActionListener(al);
    exitMenuItem.addActionListener(al);
    playGameMenuItem.addActionListener(al);
    testGameMenuItem.addActionListener(al);
    publishGameMenuItem.addActionListener(al);
    addGameMenuItem.addActionListener(al);
    defineGameSettingsMenuItem.addActionListener(al);
    deleteGameMenuItem.addActionListener(al);
    updateGameMenuItem.addActionListener(al);
    addBlockToGameMenuItem.addActionListener(al);
    deleteBlockFromGameMenuItem.addActionListener(al);
    updateBlockFromGameMenuItem.addActionListener(al);
    positionBlockInLevelMenuItem.addActionListener(al);
    moveBlockInLevelMenuItem.addActionListener(al);
    removeBlockInLevelMenuItem.addActionListener(al);
  }
  
  //****************************
  // Internal Classes
  //****************************
  
  /**
   * Class to model HoF entry. Contains data for user that has the high
   * score, as well as the high score itself. These are stored in 
   * HoFscores HashMap and then displayed in the UI.
   * 
   * The reason this is its own class it because the key for the HoF HashMap
   * is the position on the Hall of Fame, so the Value must be able to hold
   * both the user and score.
   */
  private class HoFEntry {
    
    private String _name;
    private String _score;
    public HoFEntry (String name, int score) {
      setName(name);
      setScore(Integer.toString(score));
    }
    public HoFEntry (String name, String score) {
      setName(name);
      setScore(score);
    }
    public String getName() {
      return _name;
    }
    public void setName(String _name) {
      this._name = _name;
    }
    public String getScore() {
      return _score;
    }
    public void setScore(String _score) {
      this._score = _score;
    }
  }
  
  /**
   * Very simple class just implements an ActionListener to listen for menuItem events
   * and calls the menuItemActionPerformed passing the event as a param.
   */
  private class MenuBarActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent evt) {
      menuItemActionPerformed(evt);
    }
  }

  /**
   * TODO implement method
   */
  @Override
  public void endGame(int nrOfLives, TOHallOfFameEntry hof) {
	GameOverUI goui = new GameOverUI(this);
	goui.setVisible(true);
	goui.setAlwaysOnTop(true);


	HallOfFameUI hofui = new HallOfFameUI(this);
	hofui.setVisible(true);
	
  }
  
  /**
   * Helper method so that PlayGameUI popup window can call this method
   * and then Block223UI will handle starting the game and such.
   * 
   * @param callingFrame
   * @param selectedRow
   * @throws InvalidInputException 
   */
  public void startGame(JFrame callingFrame, TOPlayableGame g2p) throws InvalidInputException {
    
    callingFrame.dispose();
    blockKeyListener = new Block223KeyListener();
    
    Block223Controller.selectPlayableGame(g2p.getName(), g2p.getNumber());
    //Block223Controller.startGame(this);
    
    Runnable r1 = new Runnable() {
      @Override
      public void run() {
          // in the actual game, add keyListener to the game window
        Block223UI.this.addKeyListener(blockKeyListener);
      }
    };
    Thread t1 = new Thread(r1);
    t1.start();
    // to be on the safe side use join to start executing thread t1 before executing
    // the next thread
    try {
        t1.join();
    } catch (InterruptedException e1) {
    }
  
    // initiating a thread to start the game loop
    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            try {
                Block223Controller.startGame(Block223UI.this);
            } catch (InvalidInputException e) {
            }
        }
    };
    Thread t2 = new Thread(r2);
    t2.start();
  }
}
