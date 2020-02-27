package ca.mcgill.ecse223.block.view.game;

import java.awt.Color;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Popup windows for settings should extend JInternalFrame.
 * Add whatever UI elements to this Frame. Then in the main UI
 * there will be a List of JInternalFrames.
 * 
 * These should be singletons
 * 
 * @author Christos Cunning
 */
public class AddGameUI extends JFrame {

  /**
   * 
   */
  private static final long serialVersionUID = 8610892373958029056L;
  
  // Constants
  private static final String WINDOW_TITLE = "Block 223 - Add a New Game";
  
  // UI Components
  private JLabel errorMessageLabel;
  private JLabel titleLabel;
  private JLabel subTitleLabel;
  private JTextField gameNameTextField;
  private JButton addGameButton;
  private JButton cancelButton;
  
  // instance variables
  private String _errorMsg;
  private Block223UI parent;
  
  /**
   * Constructor for add game internal frame menu popup.
   */
  public AddGameUI (Block223UI parent) {
    this.parent = parent;
    _errorMsg = "";
    
    initComponents();
    refresh();
  }
  
  /**
   * 
   */
  private void initComponents() {
    // window settings
    setTitle(WINDOW_TITLE);
    
    // init error message label
    errorMessageLabel = new JLabel();
    errorMessageLabel.setForeground(Color.RED);
    errorMessageLabel.setText(_errorMsg);
    
    // init other elements
    titleLabel = new JLabel("Enter a name for the new game:");
    subTitleLabel = new JLabel("Note: game settings must be specfied before game can be played.");
    subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
    
    gameNameTextField = new JTextField("New game");
    
    addGameButton = new JButton("Add");
    addGameButton.addActionListener(al -> {
      addButtonPressed();
    });
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(al -> {
      cancelButtonPressed();
    });
    
    // layout
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    
    GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
    
    hGroup.addComponent(errorMessageLabel);
    hGroup.addComponent(titleLabel);
    hGroup.addComponent(subTitleLabel);
    hGroup.addComponent(gameNameTextField);
    hGroup.addGroup(layout.createSequentialGroup()
        .addComponent(addGameButton)
        .addComponent(cancelButton)
        );
    
    vGroup.addComponent(errorMessageLabel);
    vGroup.addComponent(titleLabel);
    vGroup.addComponent(subTitleLabel);
    vGroup.addComponent(gameNameTextField);
    vGroup.addGroup(layout.createParallelGroup()
        .addComponent(addGameButton)
        .addComponent(cancelButton)
        );
    
    // pack JInternalFrame, final step
    layout.setHorizontalGroup(hGroup);
    layout.setVerticalGroup(vGroup);
    pack(); 
  }
  
  /**
   * 
   */
  private void refresh() {
 // check 
    errorMessageLabel.setText(_errorMsg);
    if (_errorMsg == null || _errorMsg.length() == 0) {
      // if no error, populate page with data
      
      
      // TODO Step iv: refresh data (update method body)
    }
    
    // refresh more stuff here idk
    
    // this is needed because the size of the window changes depending on whether an error message is shown or not
    pack();
  }
  
  
  //****************************
  // Button Listeners
  // ***************************
  
  /**
   * Cancel button listener.
   */
  private void cancelButtonPressed() {
    refresh();
    parent.refresh();
    this.dispose();
  }
  
  /**
   * Add game button listener.
   */
  private void addButtonPressed() {
    String newgamename = gameNameTextField.getText();
    // try and call controller method with input
    try {
      Block223Controller.createGame(newgamename);
      // refresh and return to parent
      parent.refresh();
      this.dispose();
    } catch (InvalidInputException e) {
      _errorMsg = e.getMessage();
    }
    refresh();
  }
  
}
