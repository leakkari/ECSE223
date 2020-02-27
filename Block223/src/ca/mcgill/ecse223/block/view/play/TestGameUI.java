package ca.mcgill.ecse223.block.view.play;

import java.awt.Color;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOPlayableGame;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Test game UI.
 * 
 * @author Christos Cunning
 */

public class TestGameUI extends JFrame {

 
  // Constants
  private static final String WINDOW_TITLE = "Test game";
  
  // UI Components
  private JLabel errorMessageLabel;
  private JLabel titleLabel;
  private JLabel subTitleLabel;
  private JButton confirmButton;
  private JButton cancelButton;
  
  // instance variables
  private Block223UI parent;
  private String _errorMsg;
  
  /**
   * Constructor for exit UI frame menu popup.
   */
  public TestGameUI (Block223UI parent) {
    this.parent = parent;
    this._errorMsg = "";
    
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
    titleLabel = new JLabel("Are you sure you want to test the game?");
    subTitleLabel = new JLabel("Please note you must be logged in as the Admin of the game to contiue.");
    subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
    
    confirmButton = new JButton("Test game");
    confirmButton.setForeground(Color.RED);
    confirmButton.addActionListener(al -> {
      confirmButtonPressed();
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
    
    // horizontal group is parallel
    hGroup.addComponent(errorMessageLabel);
    hGroup.addComponent(titleLabel);
    hGroup.addComponent(subTitleLabel);
    hGroup.addGroup(layout.createSequentialGroup()
        .addComponent(confirmButton)
        .addComponent(cancelButton)
        );
    
    // vertical group is sequential
    vGroup.addComponent(errorMessageLabel);
    vGroup.addComponent(errorMessageLabel);
    vGroup.addComponent(titleLabel);
    vGroup.addComponent(subTitleLabel);
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
      
      
      // TODO Step iv: refresh data (update method body)
    }
    
    // refresh more stuff here idk
    
    // this is needed because the size of the window changes depending on whether an error message is shown or not
    pack();
  }
  
  //****************************
  // Button Listeners
  //
  //   ALL LISTENERS MUST block223 .REFRESH DATA AT THE END
  //   > parent.refresh();
  //
  // except this one because it just closes the application??
  //
  // ***************************
  
  /**
   * Cancel button listener.
   * Example listener.
   * Most UI's will have one of these.
   */
  private void cancelButtonPressed() {
    parent.refresh();
    this.dispose();
  }
 
  /**
   * Confirm button listener.
   * Exits the entire Block223 Application.
   */
  private void confirmButtonPressed() {
    // test game
    try {
        parent.refresh();
    	this.dispose();
        Block223Controller.testGame(this.parent, this.parent);
        //parent.startGame(this, new TOPlayableGame(Block223Controller.getCurrentDesignableGame().getName(),-1, 1));

    } catch (InvalidInputException e) {
      // set error message and refresh data
        _errorMsg=e.getMessage();
        this.refresh();
    }
  }
}
