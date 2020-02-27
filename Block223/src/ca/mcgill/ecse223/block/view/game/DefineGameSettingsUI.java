package ca.mcgill.ecse223.block.view.game;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.text.NumberFormatter;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * UI for Define Game Settings sub menu.
 * 
 * TODO finish setting up componenets
 * 
 * @author Christos Cunning
 */
public class DefineGameSettingsUI extends JFrame {
  
  /**
   * Generated serialVersionUID.
   */
  private static final long serialVersionUID = -338551937297996307L;

  // Constants
  private static final String WINDOW_TITLE = "Block223 - Define Game Settings";
  
  // UI Components
  private JLabel errorMessageLabel;
  private JLabel titleLabel;
  private JLabel subTitleLabel;
  private JButton confirmButton;
  private JButton cancelButton;
  // game settings
  private JLabel numLevelsLabel;
  private JTextField numLevelsTextField;
  private JLabel numBlocksLabel;
  private JTextField numBlocksTextField;
  // ball settings
  private JLabel minXLabel;
  private JTextField minXTextField;
  private JLabel minYLabel;
  private JTextField minYTextField;
  private JLabel speedFactorLabel;
  private JTextField speedFactorTextField;
  // paddle settings
  private JLabel minPaddleLabel;
  private JTextField minPaddleTextField;
  private JLabel maxPaddleLabel;
  private JTextField maxPaddleTextField;
  // game select spinner
  private JSpinner gameSelectSpinner;
  private SpinnerListModel spinnerListModel;
  private JSpinner.DefaultEditor spinnerEditor;
  private List<TOGame> gameList;
  private List<String> gameNameList;
  
  // instance variables
  private Block223UI parent;
  private String _errorMsg;
  
  /**
   * 
   * @param parent
   */
  public DefineGameSettingsUI (Block223UI parent) {
    this.parent = parent;
    this._errorMsg  = "";
    
    // init game array list
    gameList = new ArrayList<TOGame>();
    try {
      gameList = Block223Controller.getDesignableGames();
      if (gameList.size() == 0) {
        // add empty game so spinner can still be initialized.
        _errorMsg = "No current games.";
      }
    } catch (InvalidInputException e) {
      // add empty game so spinner can still be initialized
      _errorMsg = e.getMessage();
    }
    
    // init game name list
    gameNameList = new ArrayList<String>();
    for (int i = 0; i < gameList.size(); i++) {
      gameNameList.add(gameList.get(i).getName());
    }
    // if no games, add default name to list so spinner will init
    if (gameNameList.size() == 0) {
      gameNameList.add("No games.");
    }
    
    initComponents();
    refresh();
  }
  
  /**
   * Initialize UI components.
   */
  private void initComponents() {
    // window settings
    setTitle(WINDOW_TITLE);
    
    // init error msg label
    errorMessageLabel = new JLabel();
    errorMessageLabel.setForeground(Color.RED);
    errorMessageLabel.setText(_errorMsg);
    
    // init other elements
    titleLabel = new JLabel("Select a game to change settings:");
    subTitleLabel = new JLabel("You must set a games settings before it can be played.");
    subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
    
    // init buttons
    confirmButton = new JButton("Confirm");
    confirmButton.addActionListener(al -> {
      confirmButtonPressed();
    });
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(al -> {
      cancelButtonPressed();
    });
    
    // Formatter 
    NumberFormat format = NumberFormat.getInstance();
    format.setGroupingUsed(false);
    NumberFormatter formatter = new NumberFormatter(format);
    formatter.setValueClass(Double.class);
    formatter.setAllowsInvalid(false);
    formatter.setCommitsOnValidEdit(true);
    
    // init labels and textedits
    numLevelsLabel = new JLabel("Number of levels:");
    numLevelsTextField = new JFormattedTextField(formatter);
    numBlocksLabel = new JLabel("Number of blocks per level:");
    numBlocksTextField = new JFormattedTextField(formatter);
    minXLabel = new JLabel("Minimum X speed of ball:");
    minXTextField = new JFormattedTextField(formatter);
    minYLabel = new JLabel("Minimum Y speed of ball:");
    minYTextField = new JFormattedTextField(formatter);
    speedFactorLabel = new JLabel("Ball speed increase factor:");
    speedFactorTextField = new JTextField();
    minPaddleLabel = new JLabel("Minimum paddle length:");
    minPaddleTextField = new JFormattedTextField(formatter);
    maxPaddleLabel = new JLabel("Maximum paddle length:");
    maxPaddleTextField = new JFormattedTextField(formatter);
    
    // init game select spinner
    spinnerListModel = new SpinnerListModel(gameNameList);
    gameSelectSpinner = new JSpinner(spinnerListModel);
    spinnerEditor = new JSpinner.DefaultEditor(gameSelectSpinner);
    gameSelectSpinner.setEditor(spinnerEditor);
    
    // layout
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    
    GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
    
    // horizontal group is parallel
    hGroup.addComponent(errorMessageLabel);
    hGroup.addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
            .addComponent(titleLabel)
            .addComponent(subTitleLabel)
            )
        .addComponent(gameSelectSpinner)
        );
    hGroup.addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
            // first half, general and paddle settings
            .addComponent(numLevelsLabel)
            .addComponent(numLevelsTextField)
            .addComponent(numBlocksLabel)
            .addComponent(numBlocksTextField)
            .addGap(0, 10, Short.MAX_VALUE) // add gap to separate   
            .addComponent(minPaddleLabel)
            .addComponent(minPaddleTextField)
            .addComponent(maxPaddleLabel)
            .addComponent(maxPaddleTextField)
            )
        .addGroup(layout.createParallelGroup()
            // ball settings
            .addComponent(minXLabel)
            .addComponent(minXTextField)
            .addComponent(minYLabel)
            .addComponent(minYTextField)
            .addComponent(speedFactorLabel)
            .addComponent(speedFactorTextField)
            .addGap(0,50,Short.MAX_VALUE) // fill remaining space
            )
        );
    hGroup.addGroup(layout.createSequentialGroup()
        .addComponent(confirmButton)
        .addComponent(cancelButton)
        );
    
    // vertical group is sequential
    vGroup.addComponent(errorMessageLabel);
    vGroup.addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
            .addComponent(titleLabel)
            .addComponent(subTitleLabel)
            )
        .addComponent(gameSelectSpinner)
        );
    vGroup.addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
            // first half, general and paddle settings
            .addComponent(numLevelsLabel)
            .addComponent(numLevelsTextField)
            .addComponent(numBlocksLabel)
            .addComponent(numBlocksTextField)
            .addGap(0, 10, Short.MAX_VALUE) // add gap to separate   
            .addComponent(minPaddleLabel)
            .addComponent(minPaddleTextField)
            .addComponent(maxPaddleLabel)
            .addComponent(maxPaddleTextField)
            )
        .addGroup(layout.createSequentialGroup()
            // ball settings
            .addComponent(minXLabel)
            .addComponent(minXTextField)
            .addComponent(minYLabel)
            .addComponent(minYTextField)
            .addComponent(speedFactorLabel)
            .addComponent(speedFactorTextField)
            .addGap(0,50,Short.MAX_VALUE) // fill remaining space
            )
        );
    vGroup.addGroup(layout.createParallelGroup()
        .addComponent(confirmButton)
        .addComponent(cancelButton)
        );
    
    // pack JFrame, final step
    layout.setHorizontalGroup(hGroup);
    layout.setVerticalGroup(vGroup);
    pack();
  }
  
  /**
   * Every listener must call refresh at the end!!!
   */
  private void refresh() {
    // check
    errorMessageLabel.setText(_errorMsg);
    if (_errorMsg == null || _errorMsg.length() == 0) {
      // if no error, populate page with data
      try {
        gameList = Block223Controller.getDesignableGames();
      } catch (InvalidInputException e) {
        _errorMsg = e.getMessage();
      }
      // idk refresh more stuff here or outside of loop
    } else {
      //
      _errorMsg = "";
    }
    try {
      gameList = Block223Controller.getDesignableGames();
    } catch (InvalidInputException e) {
      _errorMsg = e.getMessage();
    }
    // always update gameNameList from gameList
    // we only sometimes update gameList
    gameNameList.clear();
    if (gameList.size() != 0) {
      for (TOGame to : gameList) {
        gameNameList.add(to.getName());
      }
    } else {
      gameNameList.add("No current games.");
    }
    
    // this is needed because the size of the window changes depending on whether an error message is shown or not
    pack();
  }
  
  //****************************
  // Button Listeners
  //****************************
  
  /**
   * 
   */
  private void confirmButtonPressed() {
    // get which game was selected and select it 
    // (set to current game in block223application)
    String gameName = gameSelectSpinner.getValue().toString();
    try {
      Block223Controller.selectGame(gameName);
    } catch (InvalidInputException e1) {
      _errorMsg = e1.getMessage();
      refresh();
      return;
    }
    
    // check all text items have at least something in it
    if (numLevelsTextField.getText().isEmpty()
        || numBlocksTextField.getText().isEmpty()
        || minXTextField.getText().isEmpty()
        || minYTextField.getText().isEmpty()
        || speedFactorTextField.getText().isEmpty()
        || maxPaddleTextField.getText().isEmpty()
        || minPaddleTextField.getText().isEmpty()) {
      // handle empty
      _errorMsg = "Cannot set game settings with empty text fields. Try again";
      refresh();
      return;
    }
    
    // try to get values from JLabels, throws exception if any are not numbers.
    int nrLevels,nrBlocksPerLevel,minBallSpeedX,minBallSpeedY,maxPaddleLength, minPaddleLength;
    double ballSpeedIncreaseFactor;
    try {
      nrLevels = Integer.parseInt(numLevelsTextField.getText());
      nrBlocksPerLevel = Integer.parseInt(numBlocksTextField.getText());
      minBallSpeedX = Integer.parseInt(minXTextField.getText());
      minBallSpeedY = Integer.parseInt(minYTextField.getText());
      ballSpeedIncreaseFactor = Double.parseDouble(speedFactorTextField.getText());
      maxPaddleLength = Integer.parseInt(maxPaddleTextField.getText());
      minPaddleLength = Integer.parseInt(minPaddleTextField.getText());
    } catch (NumberFormatException e3) {
      _errorMsg = e3.getMessage();
      refresh();
      return;
    }
    // set game details using controller
    try {
      Block223Controller.setGameDetails(
          nrLevels,
          nrBlocksPerLevel,
          minBallSpeedX,
          minBallSpeedY,
          ballSpeedIncreaseFactor,
          maxPaddleLength,
          minPaddleLength);
    } catch (InvalidInputException e2) {
      _errorMsg = e2.getMessage();
      //refresh();
      return;
    }
    // Success! dispose window
    parent.refresh();
    this.dispose();
  }
  
  /**
   * Cancel button listener.
   */
  private void cancelButtonPressed() {
    refresh();
    parent.refresh();
    this.dispose();
  }
  
}
