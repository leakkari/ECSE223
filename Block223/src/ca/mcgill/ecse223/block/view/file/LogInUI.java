package ca.mcgill.ecse223.block.view.file;

import java.awt.Color;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.view.Block223UI;

public class LogInUI extends JFrame {
      /**
	 * 
	 */
	private static final long serialVersionUID = -8982393903921792386L;

	//Constants
	  private static final String WINDOW_TITLE = "Log In";
	  
	  // UI Components
	  private JLabel errorMessageLabel;
	  
	  private JButton logInButton;
	  private JButton cancelButton;
	  private JTextField userNameTextField;
      private JTextField password;
      
	  
	  private JLabel titleLabel;
	  private JLabel subTitleLabel;

	  
	  // instance variables
	  private Block223UI parent;
	  private String _errorMsg;
	  
	  
	  /**
	   * 
	   * Constructor for exit UI frame menu popup.
	   */
	  public LogInUI (Block223UI parent) {
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
	    titleLabel = new JLabel("Please enter your username and password to Log In");
	    subTitleLabel = new JLabel("While in the game, any unsaved progress will be lost.");
	    subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
	    
	      
	    logInButton = new JButton("Log In");
	    logInButton.setForeground(Color.BLUE);
	    logInButton.addActionListener(al -> {
	      confirmButtonPressed();
	    });
	    
	    userNameTextField = new JTextField("Username");
	    password = new JPasswordField ("Password");
	    
	    
	    
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
	        .addComponent(logInButton)
	        .addComponent(cancelButton)
	        );
	    hGroup.addGroup(layout.createSequentialGroup()
	    		.addComponent(userNameTextField)
	    		.addComponent(password)
	    		); 
	    
	    // vertical group is sequential
	    vGroup.addComponent(errorMessageLabel);
	    vGroup.addComponent(errorMessageLabel);
	    vGroup.addComponent(titleLabel);
	    vGroup.addComponent(subTitleLabel);
	    
	    
	    vGroup.addGroup(layout.createParallelGroup()
	    		.addComponent(userNameTextField)
	    		.addComponent(password));
	  
	    vGroup.addGroup(layout.createParallelGroup()
	        .addComponent(logInButton)
	        .addComponent(cancelButton)
	        );
	    
	    // pack JInternalFrame, final step
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
		  
		 String usernameEntered = userNameTextField.getText();
		 String passwordEntered = password.getText();
		  
	    try {

			Block223Controller.login(usernameEntered, passwordEntered);

			//Block223Controller.saveGame();

			this.dispose();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			_errorMsg=e.getMessage();
		    this.refresh();

		}
	  }
  
}