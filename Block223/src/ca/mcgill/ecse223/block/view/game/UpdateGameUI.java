package ca.mcgill.ecse223.block.view.game;

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
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * This class implements updating game settings functionality.
 * @author leaakkari
 *
 */
public class UpdateGameUI extends JFrame {

//Constants
 private static final String WINDOW_TITLE = "Block223 - Update Game Settings";
 
 // UI Components
 private JLabel errorMessageLabel;
 private JLabel titleLabel;
 private JButton confirmButton;
 private JButton cancelButton;
 // game settings
 private JLabel name;
 private JTextField nameTextField;
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
 private List<TOGame> gameList = new ArrayList<TOGame>();
 private List<String> gameNameList = new ArrayList<String>();
 
 // instance variables
 private Block223UI parent;
 private String _errorMsg;
 
 
 public UpdateGameUI (Block223UI parent) {
	this.parent = parent;
	//set error
	this._errorMsg = "";
	
	//add first element to Spinner
	gameNameList.clear();
	gameList.clear();
	gameNameList.add("Select Game");
	   try {
		   gameList = Block223Controller.getDesignableGames();
		   if (gameList.size() == 0) {
			   _errorMsg = "No current Games";
		   }
		   for(TOGame currGame : gameList) {
			   gameNameList.add(currGame.getName());
		   }
	   }
	   catch(InvalidInputException e) {
		    _errorMsg = e.getMessage();
	   }
	   
    initComponents();
    refresh();
	   
  }
 
 /**
  * 
  */
 private void initComponents() {
   // window settings
   setTitle(WINDOW_TITLE);
   
   // init error msg label
   errorMessageLabel = new JLabel();
   errorMessageLabel.setForeground(Color.RED);
   errorMessageLabel.setText(_errorMsg);
   
   // init other elements
   titleLabel = new JLabel("Select a game to change settings");
   //subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));
   
   // init buttons
   confirmButton = new JButton("Apply");
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
   name = new JLabel("Name: ");
   nameTextField = new JTextField("Game name");
   numLevelsLabel = new JLabel("Number of levels:");
   numLevelsTextField = new JFormattedTextField(formatter);
   numBlocksLabel = new JLabel("Number of blocks per level:");
   numBlocksTextField = new JFormattedTextField(formatter);
   minXLabel = new JLabel("Minimum X speed of ball:");
   minXTextField = new JFormattedTextField(formatter);
   minYLabel = new JLabel("Minimum Y speed of ball:");
   minYTextField = new JFormattedTextField(formatter);
   speedFactorLabel = new JLabel("Ball speed increase factor:");
   speedFactorTextField = new JTextField("Speed Factor Increase");
   minPaddleLabel = new JLabel("Minimum paddle length:");
   minPaddleTextField = new JFormattedTextField(formatter);
   maxPaddleLabel = new JLabel("Maximum paddle length:");
   maxPaddleTextField = new JFormattedTextField(formatter);
   
   // init game select spinner
   
   gameNameList.add("Select Game");
   spinnerListModel = new SpinnerListModel(gameNameList);
   gameSelectSpinner = new JSpinner(spinnerListModel);
   gameSelectSpinner.setEditor(new JSpinner.DefaultEditor(gameSelectSpinner));
  
  
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
    	   .addComponent(name)
    	   .addComponent(nameTextField)
           .addComponent(numLevelsLabel)
           .addComponent(numLevelsTextField)
           .addComponent(numBlocksLabel)
           .addComponent(numBlocksTextField)
           .addGap(0, 10, Short.MAX_VALUE) // add gap to separate   
           .addComponent(minPaddleLabel)
           .addComponent(minPaddleTextField)
           
           
           )
       .addGroup(layout.createParallelGroup()
           // ball settings
    		   .addComponent(maxPaddleLabel)
    	   .addComponent(maxPaddleTextField)
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
           //.addComponent(subTitleLabel)
           )
       .addComponent(gameSelectSpinner)
       );
   vGroup.addGroup(layout.createParallelGroup()
       .addGroup(layout.createSequentialGroup()
           // first half, general and paddle settings
    		   .addComponent(name)
        	   .addComponent(nameTextField)
           .addComponent(numLevelsLabel)
           .addComponent(numLevelsTextField)
           .addComponent(numBlocksLabel)
           .addComponent(numBlocksTextField)
           .addGap(0, 10, Short.MAX_VALUE) // add gap to separate   
           .addComponent(minPaddleLabel)
           .addComponent(minPaddleTextField)
         
           )
       .addGroup(layout.createSequentialGroup()
           // ball settings
    		   .addComponent(maxPaddleLabel)
               .addComponent(maxPaddleTextField)
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
   } else {
     _errorMsg = "";
   }
   
 
   
   // this is needed because the size of the window changes depending on whether an error message is shown or not
   pack();
 }
 
 //****************************
 // Button Listeners
 // ***************************
 
 /**
  * 
  */
 private void confirmButtonPressed() {
	 parent.refresh();
	 Block223 block223 = Block223Application.getBlock223();
	 //select game from spinner 
   String selectedGameName = gameSelectSpinner.getValue().toString();
     //User Cannot select "Select Game"
   if(selectedGameName.equals(gameNameList.get(0))) {
	   _errorMsg = "Invalid Game Selection" ;  
	   refresh();
	   return;
   }
     //calling the selectGame method on the selected game name from spinner
   try {
	      Block223Controller.selectGame(selectedGameName);
	    } catch (InvalidInputException e) {
	      _errorMsg = e.getMessage();
	      refresh();
	      return;
	    }
	//find selected game    }
   Game selectedGame = block223.findGame(selectedGameName);
   Block223Application.setCurrentGame(selectedGame);
   
   // check all text items have at least something in it
   if (numLevelsTextField.getText().isEmpty()
	
	   || nameTextField.getText().isEmpty()   
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
   
   String gameName = nameTextField.getText();
   int nrLevels = Integer.parseInt(numLevelsTextField.getText());
   int nrBlocksPerLevel = Integer.parseInt(numBlocksTextField.getText());
   int minBallSpeedX = Integer.parseInt(minXTextField.getText());
   int minBallSpeedY = Integer.parseInt(minYTextField.getText());
   double ballSpeedIncreaseFactor = 0.0;
   try {
	    ballSpeedIncreaseFactor = Double.parseDouble(speedFactorTextField.getText());
	} catch (NumberFormatException e1) {
	    _errorMsg = e1.getMessage();
	    refresh();
	    return;
	    
	}
   
   int maxPaddleLength = Integer.parseInt(maxPaddleTextField.getText());
   int minPaddleLength = Integer.parseInt(minPaddleTextField.getText());
   

   
 
   try {
     Block223Controller.updateGame(
         gameName,
         nrLevels,
         nrBlocksPerLevel,
         minBallSpeedX,
         minBallSpeedY,
         ballSpeedIncreaseFactor,
         maxPaddleLength,
         minPaddleLength);
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
