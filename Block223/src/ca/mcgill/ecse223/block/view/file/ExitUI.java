package ca.mcgill.ecse223.block.view.file;

import java.awt.Color;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Exit is probably the simplest UI, so it  is a good example.
 * 
 * Every UI should at least look like this, although may need 
 * much more functionality. 
 * 
 * TODO add save a quit option
 * 
 * @author Christos Cunning
 */
public class ExitUI extends JFrame {
  
  // Constants
  private static final String WINDOW_TITLE = "Exit";
  
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
  public ExitUI (Block223UI parent) {
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
    titleLabel = new JLabel("Are you sure you want to exit the application?");
    subTitleLabel = new JLabel("Any unsaved progress will be lost.");
    subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
    
    confirmButton = new JButton("Exit");
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
    // don't need to refresh because closing program.
    parent.dispose();
    this.dispose();
    System.exit(0);
  }
}
