package ca.mcgill.ecse223.block.view.level;

import java.awt.Color;
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
 * UI for Move Block
 * 
 * @author Jeremy Chow
 */
public class MoveBlockInLevelUI extends JFrame {
  
  /**
   * Generated serialVersionUID.
   */

  // Constants
  private static final String WINDOW_TITLE = "Block223 - Move Block In Level";
  
  // UI Components
  private JLabel errorMessageLabel;
  private JLabel titleLabel;
  private JButton confirmButton;
  private JButton cancelButton;
  // level number
  private JLabel levelNumberLabel;
  private JTextField levelNumberTextField;
  // old grid location
  private JLabel oldHorizontalGridPositionLabel;
  private JTextField oldHorizontalGridPositionTextField;
  private JLabel oldVerticalGridPositionLabel;
  private JTextField oldVerticalGridPositionTextField;
  // new grid location
  private JLabel newHorizontalGridPositionLabel;
  private JTextField newHorizontalGridPositionTextField;
  private JLabel newVerticalGridPositionLabel;
  private JTextField newVerticalGridPositionTextField;

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
  public MoveBlockInLevelUI (Block223UI parent) {
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
    titleLabel = new JLabel("Select a game to move a block: ");
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
    levelNumberLabel = new JLabel("Level Number:");
    levelNumberTextField = new JFormattedTextField(formatter);
    oldHorizontalGridPositionLabel = new JLabel("Old X Position: ");
    oldHorizontalGridPositionTextField = new JFormattedTextField(formatter);
    oldVerticalGridPositionLabel = new JLabel("Old Y Position: ");
    oldVerticalGridPositionTextField = new JFormattedTextField(formatter);

    newHorizontalGridPositionLabel = new JLabel("New X Position: ");
    newHorizontalGridPositionTextField = new JFormattedTextField(formatter);
    newVerticalGridPositionLabel = new JLabel("New Y Position: ");
    newVerticalGridPositionTextField = new JTextField();
    
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
            )
        .addComponent(gameSelectSpinner)
        );
    hGroup.addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup()
            // first half, general and paddle settings
            .addComponent(oldHorizontalGridPositionLabel)
            .addComponent(oldHorizontalGridPositionTextField)
            .addGap(0, 10, Short.MAX_VALUE) // add gap to separate   
            .addComponent(newHorizontalGridPositionLabel)
            .addComponent(newHorizontalGridPositionTextField)
            .addComponent(levelNumberLabel)
            .addComponent(levelNumberTextField)
            )
        .addGroup(layout.createParallelGroup()
            // ball settings
            .addComponent(oldVerticalGridPositionLabel)
            .addComponent(oldVerticalGridPositionTextField)
            .addComponent(newVerticalGridPositionLabel)
            .addComponent(newVerticalGridPositionTextField)
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
            )
        .addComponent(gameSelectSpinner)
        );
    vGroup.addGroup(layout.createParallelGroup()
        .addGroup(layout.createSequentialGroup()
            // first half, general and paddle settings
            .addComponent(oldHorizontalGridPositionLabel)
            .addComponent(oldHorizontalGridPositionTextField)
            .addComponent(newHorizontalGridPositionLabel)
            .addComponent(newHorizontalGridPositionTextField)
            .addGap(0, 10, Short.MAX_VALUE) // add gap to separate   
            .addComponent(levelNumberLabel)
            .addComponent(levelNumberTextField)
            )
        .addGroup(layout.createSequentialGroup()
            // ball settings
            .addComponent(oldVerticalGridPositionLabel)
            .addComponent(oldVerticalGridPositionTextField)
            .addComponent(newVerticalGridPositionLabel)
            .addComponent(newVerticalGridPositionTextField)
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
    if (levelNumberTextField.getText().isEmpty()
        || oldHorizontalGridPositionTextField.getText().isEmpty()
        || newHorizontalGridPositionTextField.getText().isEmpty()
        || oldVerticalGridPositionTextField.getText().isEmpty()
        || newVerticalGridPositionTextField.getText().isEmpty()) {
      // handle empty
      _errorMsg = "Cannot set move block with empty text fields. Try again";
      refresh();
      return;
    }
    
    // try to get values from JLabels, throws exception if any are not numbers.
    int levelNumber, oldHorizontalGridPosition, oldVerticalGridPosition, newHorizontalGridPosition, newVerticalGridPosition;
    try {
    levelNumber = Integer.parseInt(levelNumberTextField.getText());
    oldHorizontalGridPosition = Integer.parseInt(oldHorizontalGridPositionTextField.getText());
    oldVerticalGridPosition = Integer.parseInt(oldVerticalGridPositionTextField.getText());
    newHorizontalGridPosition = Integer.parseInt(newHorizontalGridPositionTextField.getText());
    newVerticalGridPosition = Integer.parseInt(newVerticalGridPositionTextField.getText());


    } catch (NumberFormatException e3) {
      _errorMsg = e3.getMessage();
      refresh();
      return;
    }
    // move block using controller
    try {
      Block223Controller.moveBlock(
    		 levelNumber,
    		 oldHorizontalGridPosition,
    		 oldVerticalGridPosition,
    		 newHorizontalGridPosition,
    		 newVerticalGridPosition);
    } catch (InvalidInputException e2) {
      _errorMsg = e2.getMessage();
      refresh();
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