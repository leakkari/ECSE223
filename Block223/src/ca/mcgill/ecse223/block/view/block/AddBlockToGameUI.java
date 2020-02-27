package ca.mcgill.ecse223.block.view.block;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Class that is responsible for the UI of the "Add  Block to Game" feature
 * @author Walid Chabchoub
 *
 */
public class AddBlockToGameUI extends JFrame {

	private static final String WINDOW_TITLE = "Block 223 - Add a block";

	private JComboBox gameListComboBox;
	private JLabel gameLabel;
	private JLabel blockLabel;
	private JTextField blockPoints;
	private JLabel pointsLabel;
	private JLabel rgbLabel;
	private JLabel errorMessageLabel;
	private JTextField redValue;
	private JTextField greenValue;
	private JTextField blueValue;
	private JTextField pointsValue;
	private JButton addBlockButton;
	private JButton cancelButton;

	private String _errorMsg;
	private Block223UI parent;


	public AddBlockToGameUI (Block223UI parent) {
		this.parent = parent;
		_errorMsg = "";
		initComponents();
		refreshData();
	}

	/**
	 * 
	 */
	private void initComponents() {
		// window settings

		List<TOGame> gameList = new ArrayList<TOGame>();
		try{
			gameList = Block223Controller.getDesignableGames();	
		} catch (InvalidInputException e) {
			_errorMsg = e.getMessage();
			refreshData();
		}
		String[] gameNames = new String[gameList.size()];
		
		for(int index = 0; index < gameList.size(); index ++) {
			gameNames[index] = gameList.get(index).getName();
		}
		setTitle(WINDOW_TITLE);
		setPreferredSize(new Dimension(400, 250));
		// init error message label
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.RED);
		errorMessageLabel.setText(_errorMsg);

		// init other elements
		gameLabel = new JLabel("Select a game");
		rgbLabel = new JLabel("RGB Values (0-255)");
		pointsLabel = new JLabel("Block points (1-1000)");
		pointsValue = new JTextField("");
		redValue = new JTextField("Red");
		greenValue = new JTextField("Green");
		blueValue = new JTextField("Blue");
		gameListComboBox = new JComboBox(gameNames);
		//subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));

		addBlockButton = new JButton("Add");
		addBlockButton.addActionListener(al -> {
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

		// Might add GroupLayout.Alignment.CENTER
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(errorMessageLabel)
				.addComponent(gameLabel)
				.addComponent(gameListComboBox)
				.addComponent(pointsLabel)
				.addComponent(pointsValue)
				.addComponent(addBlockButton)
				);
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(rgbLabel)
				.addComponent(redValue)
				.addComponent(greenValue)
				.addComponent(blueValue)
				.addComponent(cancelButton)
				);
		vGroup.addComponent(errorMessageLabel);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(gameLabel)
				.addComponent(rgbLabel)
				);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(gameListComboBox)
				.addComponent(redValue)
				);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(pointsLabel)
				.addComponent(greenValue)
				);

		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(pointsValue)
				.addComponent(blueValue)
				);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(addBlockButton)
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
	private void refreshData() {
		// check 
		errorMessageLabel.setText(_errorMsg);
		if (_errorMsg == null || _errorMsg.length() == 0) {
			// if no error, populate page with data


			// TODO Step iv: refresh data (update method body)
		}
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
		parent.refresh();
		this.dispose();
	}

	/**
	 * Add game button listener.
	 */
	private void addButtonPressed() {
		String gameName = (String) gameListComboBox.getSelectedItem();
		if(!(redValue.getText().isEmpty() || greenValue.getText().isEmpty() || blueValue.getText().isEmpty() 
				|| pointsValue.getText().isEmpty()) || gameName == "null") {
			int red = Integer.parseInt(redValue.getText());
			int green = Integer.parseInt(greenValue.getText());
			int blue = Integer.parseInt(blueValue.getText());
			int points = Integer.parseInt(pointsValue.getText());
			try {
				Block223Controller.selectGame(gameName);
				Block223Controller.addBlock(red, green, blue, points);
				//System.out.println(Block223Controller.getBlockOfCurrentDesignableGame(0));
				
				parent.refresh();
				this.dispose();	
			} catch (InvalidInputException e) {
				_errorMsg = e.getMessage();
				this.refreshData();
			}
		}else {
			_errorMsg = "One of the fields is empty. Try again.";
			this.refreshData();
		}
		// try and call controller method with input

	}
}
