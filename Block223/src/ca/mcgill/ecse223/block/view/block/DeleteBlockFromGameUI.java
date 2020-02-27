package ca.mcgill.ecse223.block.view.block;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Class that is responsible for the UI of the "Delete  Block from Game" feature
 * @author Walid Chabchoub
 *
 */
public class DeleteBlockFromGameUI extends JFrame {

	private static final String WINDOW_TITLE = "Block 223 - Delete a block";

	private JComboBox gameListComboBox;
	private JComboBox blockListComboBox;
	private JLabel gameLabel;
	private JLabel blockLabel;
	private JLabel pointsLabel;
	private JLabel pointsValueLabel;
	private JLabel rgbLabel;
	private JLabel redLabel;
	private JLabel greenLabel;
	private JLabel blueLabel;
	private JLabel errorMessageLabel;

	private JButton deleteBlockButton;
	private JButton cancelButton;
	private ArrayList<String> blockListArray = new ArrayList<String>();
	private int selectedBlockIndex = -1;
	private String _errorMsg;
	private Block223UI parent;


	public DeleteBlockFromGameUI (Block223UI parent) {
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

		List<TOGame> gameList = new ArrayList<TOGame>();
		List<TOBlock> blockList = new ArrayList<TOBlock>();

		try{
			gameList = Block223Controller.getDesignableGames();	

		} catch (InvalidInputException e) {
			_errorMsg = e.getMessage();
			refresh();
		}

		String[] gameNames = new String[gameList.size()];

		for(int index = 0; index < gameList.size(); index ++) {
			gameNames[index] = gameList.get(index).getName();
		}
		setTitle(WINDOW_TITLE);
		setPreferredSize(new Dimension(400, 350));
		// init error message label
		errorMessageLabel = new JLabel();
		errorMessageLabel.setForeground(Color.RED);
		errorMessageLabel.setText(_errorMsg);

		// init other elements
		gameLabel = new JLabel("Select a game");
		blockLabel = new JLabel("Select a block");
		rgbLabel = new JLabel("Selected Block RGB");
		pointsLabel = new JLabel("Selected Block Points");
		pointsValueLabel = new JLabel("Points : ");
		redLabel = new JLabel("Red : ");
		greenLabel = new JLabel("Green : ");
		blueLabel = new JLabel("Blue : ");
		blockListComboBox = new JComboBox<String>();
		gameListComboBox = new JComboBox<String>(gameNames);
		gameListComboBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					String gameName = (String) gameListComboBox.getSelectedItem();
					Block223Controller.selectGame(gameName);
					List<TOBlock> blockList = Block223Controller.getBlocksOfCurrentDesignableGame();
					blockListArray.clear();
					String[] blockListStringArray = new String[blockList.size()];
					for(int index = 0; index < blockList.size(); index ++) {
						blockListArray.add((String) (blockList.get(index).getPoints() +
								"PTS, RGB("+ blockList.get(index).getRed()+", "+ blockList.get(index).getGreen()+", "+ blockList.get(index).getBlue()+")"));
						// The second array is used for the ComboBox Model (can't use ArrayList)
						blockListStringArray[index] = (String) (blockList.get(index).getPoints() +
								"PTS, RGB("+ blockList.get(index).getRed()+", "+ blockList.get(index).getGreen()+", "+ blockList.get(index).getBlue()+")");
					}

					DefaultComboBoxModel<String> blockModel = new DefaultComboBoxModel<String>(blockListStringArray);
					blockListComboBox.setModel(blockModel);
				} catch (InvalidInputException e0) {
					_errorMsg = e0.getMessage();
					refresh();
				}		    
			}
		});
		blockListComboBox.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					List<TOBlock> blockList = Block223Controller.getBlocksOfCurrentDesignableGame();
					selectedBlockIndex = blockListComboBox.getSelectedIndex();
					pointsValueLabel.setText("Points : " + blockList.get(selectedBlockIndex).getPoints());
					redLabel.setText("Red : " + blockList.get(selectedBlockIndex).getRed());
					greenLabel.setText("Green : " + blockList.get(selectedBlockIndex).getGreen());
					blueLabel.setText("Blue : " + blockList.get(selectedBlockIndex).getBlue());
					
				} catch (InvalidInputException e1) {
					_errorMsg = e1.getMessage();
					refresh();
				}
				    
			}
		});
		//subTitleLabel.setFont(new Font(subTitleLabel.getFont().getName(), Font.ITALIC, 10));

		deleteBlockButton = new JButton("Delete");
		deleteBlockButton.addActionListener(al -> {
			deleteButtonPressed();
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
				.addComponent(blockLabel)
				.addComponent(blockListComboBox)
				.addComponent(deleteBlockButton)
				);
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(rgbLabel)
				.addComponent(redLabel)
				.addComponent(greenLabel)
				.addComponent(blueLabel)
				.addComponent(pointsValueLabel)
				.addComponent(cancelButton)
				);
		vGroup.addComponent(errorMessageLabel);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(gameLabel)
				.addComponent(rgbLabel)
				);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(gameListComboBox)
				.addComponent(redLabel)
				);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(blockLabel)
				.addComponent(greenLabel)
				);

		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(blockListComboBox)
				.addComponent(blueLabel)
				);
		vGroup.addComponent(pointsValueLabel);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(deleteBlockButton)
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

		errorMessageLabel.setText(_errorMsg);

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
	private void deleteButtonPressed() {
		String gameName = (String) gameListComboBox.getSelectedItem();
		if(selectedBlockIndex != -1) {
			try {
				List<TOBlock> blockList = Block223Controller.getBlocksOfCurrentDesignableGame();
				int blockId = blockList.get(selectedBlockIndex).getId();
				Block223Controller.deleteBlock(blockId);;
				parent.refresh();
				this.dispose();	
			} catch (InvalidInputException e) {
				_errorMsg = e.getMessage();
				this.refresh();
			}
		}else {
			_errorMsg = "No block has been selected";
			this.refresh();
		}
		// try and call controller method with input

	}
}
