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

public class RegisterUI extends JFrame {
      /**
	 * 
	 */
	private static final long serialVersionUID = -5754393903921792386L;

	//Constants
	  private static final String WINDOW_TITLE = "Register";
	  
	  // UI Components
	  private JLabel errorMessageLabel;
	  
	  private JButton registerButton;
	  private JButton cancelButton;
	  private JTextField userNameTextField;
      private JTextField userPassword;
      private JTextField adminPassword;
      private JLabel userNameLabelAbove;
      private JLabel userPwdAbove;
      private JLabel adminPwdAbove;
      
	  
	  private JLabel titleLabel;
	  private JLabel subTitleLabel;

	  
	  // instance variables
	  private Block223UI parent;
	  private String _errorMsg;
	  
	  
	  /**
	   * 
	   * Constructor for exit UI frame menu popup.
	   */
	  public RegisterUI (Block223UI parent) {
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
	    titleLabel = new JLabel("Please enter a username and password to register");
	    subTitleLabel = new JLabel("If you would like to register as an Admin, please enter your player's username, password and admin password, respectively.");
	    subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
	    
	      
	    registerButton = new JButton("Register");
	    registerButton.setForeground(Color.BLUE);
	    registerButton.addActionListener(al -> {
	      confirmButtonPressed();
	    });
	    
	    userNameTextField = new JTextField("Username");
	    userPassword = new JPasswordField ("UserPassword");
	    adminPassword = new JPasswordField ("AdminPassword");

	    userNameLabelAbove = new JLabel("Username");
	    userPwdAbove = new JLabel("User Password");
	    adminPwdAbove = new JLabel("Admin Password");
	      
	    
	    
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
	        .addComponent(registerButton)
	        .addComponent(cancelButton)
	        );
	 
	    hGroup.addGroup(layout.createSequentialGroup()
	    		
	    		.addComponent(userNameTextField)	
	
	    		.addComponent(userPassword)	
	    		.addComponent(adminPassword)
		
		
	    ); 
	  /*  hGroup.addGroup(layout.createSequentialGroup()
	    		
	    		.addComponent(userNameLabelAbove)	
	
	    		.addComponent(userPwdAbove)	
	    		.addComponent(adminPwdAbove)
		
		
	    ); */

	    hGroup.addGroup(layout.createSequentialGroup()
	    		.addGroup(layout.createParallelGroup()
	    				.addComponent(userNameLabelAbove)
	    	    		.addComponent(userNameTextField)	
	    		)
	    		.addGroup(layout.createParallelGroup()
	    				.addComponent(userPwdAbove)
	    	    		.addComponent(userPassword)		
	    		)
				.addGroup(layout.createParallelGroup()
						.addComponent(adminPwdAbove)
			    		.addComponent(adminPassword)
				)
				
	    ); 
	   
	 
	    // vertical group is sequential
	    vGroup.addComponent(errorMessageLabel);
	    vGroup.addComponent(errorMessageLabel);
	    vGroup.addComponent(titleLabel);
	    vGroup.addComponent(subTitleLabel);
	    
	     	
	    
	    vGroup.addGroup(layout.createParallelGroup()
	    		.addGroup(layout.createSequentialGroup()
	    				.addComponent(userNameLabelAbove)
	    	    		.addComponent(userNameTextField)	
	    		)
	    		.addGroup(layout.createSequentialGroup()
	    				.addComponent(userPwdAbove)
	    	    		.addComponent(userPassword)		
	    		)
				.addGroup(layout.createSequentialGroup()
						.addComponent(adminPwdAbove)
			    		.addComponent(adminPassword)
				)
				
	    ); 
	  
	    vGroup.addGroup(layout.createParallelGroup()
		
	    		.addComponent(userNameTextField)	
	
	    		.addComponent(userPassword)	
	    		.addComponent(adminPassword)
		
		
	    ); 

	    vGroup.addGroup(layout.createParallelGroup()
	        .addComponent(registerButton)
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
		 String passwordEntered = userPassword.getText();
		 String adminPasswordEntered = adminPassword.getText();
		  
	    try {

			Block223Controller.register(usernameEntered, passwordEntered, adminPasswordEntered);

			

			this.dispose();
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			_errorMsg=e.getMessage();
		    this.refresh();

		}
	  }
  
}