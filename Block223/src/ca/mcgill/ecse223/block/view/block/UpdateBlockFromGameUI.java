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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.view.Block223UI;

public class UpdateBlockFromGameUI extends JFrame {
	
	//UI elements 
	private JLabel errorMessage;
	
	//Update Block
	private JLabel gameLabel;
	private JComboBox <String> gameToggleList;
	private JLabel gameToggleLabel;
	private JLabel chooseBlock;
	private JComboBox <String> blockToggleList;
	private JLabel blockToggleLabel;
	private JButton cancelButton;
	private JLabel updateLabel;
	private JLabel pointsLabel;
	private JTextField pointSelect;
	private JLabel redLabel;
	private JTextField redSelect;
	private JLabel blueLabel;
	private JTextField blueSelect;
	private JLabel greenLabel;
	private JTextField greenSelect;
	private JButton updateButton;

	
	private int WIN_WIDTH = 700;
	private int WIN_HEIGHT = 200;
	
	
	
	private List<TOGame> gameList;
	private List<TOBlock> blockList;
	private List <TOBlock> updBlockList;
	private List<TOBlock> blockLister;
	private List<String> gameNames;
	private List<String> blockNames;
	private String _errorMsg;
	private Block223UI parent;
	
	

	public UpdateBlockFromGameUI(Block223UI parent){
		this.parent = parent;
		this._errorMsg = "";

		gameList = new ArrayList<TOGame>();
		try {
			gameList = Block223Controller.getDesignableGames();
			if (gameList.size() == 0) {
				// add empty game so spinner can still be initialized.
				_errorMsg = "No current games.";
			}
		} catch (InvalidInputException e) {
			_errorMsg = e.getMessage();
		}

		gameNames = new ArrayList<String>();
		for (int i = 0; i < gameList.size(); i++) {
			gameNames.add(gameList.get(i).getName());
		}
		if (gameNames.size() == 0) {
			gameNames.add("No games.");
		}
		
		blockList = new ArrayList<TOBlock>();
		try {
			blockList = Block223Controller.getBlocksOfCurrentDesignableGame();
			if (blockList.size() == 0) {
				// add empty game so spinner can still be initialized.
				_errorMsg = "No available blocks.";
			}
		}
		catch (InvalidInputException e){
			_errorMsg = e.getMessage();
		}
		blockNames = new ArrayList<String>();
		
		for (int i = 0; i < blockList.size(); i++) {
			blockNames.add(i, (String) (blockList.get(i).getPoints()
					+ "pts, RGB("+ blockList.get(i).getRed()+", "+ blockList.get(i).getGreen()+", "+ blockList.get(i).getBlue()+")"));
		}
		

		initComponents();
		refresh();
	}

	
	
	private void initComponents() {
		
		String[] gameNamesArray = new String[gameList.size()];
		String[] blockNamesArray = new String [blockList.size()];

		
		for(int index = 0; index < gameList.size(); index ++) {
			gameNamesArray[index] = gameList.get(index).getName(); 
		}
		for(int index = 0; index < blockNames.size(); index ++) {
			blockNamesArray[index] = blockNames.get(index);
		}
		
		
		
		

		
		errorMessage = new JLabel();
		errorMessage.setForeground(Color.RED);
		errorMessage.setText(_errorMsg);
		
		// Elements for common
		gameToggleList = new JComboBox<String>(gameNamesArray);
		gameToggleLabel = new JLabel();
		gameToggleLabel.setText("Select Game:");
		
		chooseBlock = new JLabel();
		chooseBlock.setText("Current Game:");
		blockToggleList = new JComboBox<String>();
		blockToggleLabel = new JLabel();
		blockToggleLabel.setText("Select Block:");
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		
		// Elements for update
		updateLabel = new JLabel();
		updateLabel.setText("Update:");
		pointsLabel  = new JLabel();
		pointsLabel.setText("Points:");
		pointSelect = new JTextField();
		
		redLabel = new JLabel();
		
		redLabel.setText("Red:");
		redSelect = new JTextField();		
		greenLabel = new JLabel();
		greenLabel.setText("Green:");
		greenSelect = new JTextField();
		blueLabel = new JLabel();
		blueLabel.setText("Blue:");
		blueSelect = new JTextField();
		updateButton = new JButton();
		updateButton.setText("Update");
		
		//global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Block223 - Update a Block");
		
		// listeners for update
		updateButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				updateButtonActionPerformed(evt);

			}
		});
		
		cancelButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});
		
		
		gameToggleList.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					String gameName = (String) gameToggleList.getSelectedItem();
					Block223Controller.selectGame(gameName);
					blockLister = Block223Controller.getBlocksOfCurrentDesignableGame();
					String[] blockArray = new String[blockList.size()];
					for(int i = 0; i < blockList.size(); i++) {
						blockArray[i] = (String) ((blockLister.get(i).getPoints()
								+ "pts, RGB("+ blockLister.get(i).getRed()+", "+ 
								blockLister.get(i).getGreen()+", "+ blockLister.get(i).getBlue()+")"));
					}

					DefaultComboBoxModel<String> blockModel = new DefaultComboBoxModel<String>(blockArray);
					blockToggleList.setModel(blockModel);
				} catch (InvalidInputException e1) {
					_errorMsg = e1.getMessage();
					refresh();
				}		    
			}
		});
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		setPreferredSize(new Dimension (WIN_WIDTH, WIN_HEIGHT));
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		

	
		
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(errorMessage)
				.addComponent(gameToggleLabel)
				.addComponent(updateLabel)
				.addComponent(pointsLabel)
				.addComponent(blueLabel));
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(gameToggleList)
				.addComponent(pointSelect)
				.addComponent(updateButton)
				.addComponent(blueSelect));
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(blockToggleLabel)
				.addComponent(redLabel)
				.addComponent(greenLabel)
				.addComponent(cancelButton));
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(blockToggleList)
				.addComponent(redSelect)
				.addComponent(greenSelect));
		hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(blockToggleLabel));
				

		
		
		vGroup.addComponent(errorMessage);
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(gameToggleLabel)
				.addComponent(gameToggleList)
				.addComponent(blockToggleLabel)
				.addComponent(blockToggleList));
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(updateLabel));
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(pointsLabel)
				.addComponent(pointSelect)
				.addComponent(redLabel)
				.addComponent(redSelect));
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(blueLabel)
				.addComponent(blueSelect)
				.addComponent(greenLabel)
				.addComponent(greenSelect));
		vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(updateButton)
				.addComponent(cancelButton));

		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
		pack(); 
	}

	private void refresh() {
		errorMessage.setText(_errorMsg);

		if (_errorMsg == null || _errorMsg.length() == 0) {
			redSelect.setText("");
			blueSelect.setText("");
			greenSelect.setText("");
			pointSelect.setText("");

			try {
				gameList = Block223Controller.getDesignableGames();
			} catch (InvalidInputException e) {
				_errorMsg = e.getMessage();
			}
		} else {
			//
			_errorMsg = "";
		}
		
		try {
			blockList = Block223Controller.getBlocksOfCurrentDesignableGame();
		}catch(InvalidInputException e){
			_errorMsg = e.getMessage();
		}
		try {
			gameList = Block223Controller.getDesignableGames();
		} catch (InvalidInputException e) {
			_errorMsg = e.getMessage();
		}
		// always update gameNames from gameList
		// we only sometimes update gameList
		gameNames.clear();
		if (gameList.size() != 0) {
			for (TOGame to : gameList) {
				gameNames.add(to.getName());
			}
		} else {
			gameNames.add("No current games.");
		}
		
		blockNames.clear();
		if(gameList.size() !=0) {
			for (TOBlock to: blockList) {
				blockNames.add((String) (to.getPoints()
						+ "pts, RGB("+ to.getRed()+", "+ to.getGreen()+", "+ to.getBlue()+")"));
			}
		}
		else {
			blockNames.add("No available blocks");
		}
		
		pack();


	}
	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		refresh();
		parent.refresh();
		this.dispose();
	}
	private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String selectedGame = (String) gameToggleList.getSelectedItem();
		try {
		      Block223Controller.selectGame(selectedGame);
		    } catch (InvalidInputException e1) {
		      _errorMsg = e1.getMessage();
		      refresh();
		      return;
		    }
		
		int selectedBlock = blockToggleList.getSelectedIndex();
		if (selectedBlock < 0) {
			_errorMsg = "Block needs to be selected";
		    refresh();
		    return;
			
		}
		int points = 0;
		try {
			points = Integer.parseInt(pointSelect.getText());
		}
		catch (NumberFormatException e ) {
			_errorMsg = "Points must be filled in with an integer between 0-1000";
		    refresh();
		    return;
		}
		int red = 0;
		try {
			red = Integer.parseInt(redSelect.getText());
		}
		catch (NumberFormatException e) {
			_errorMsg = _errorMsg + "Red must be filled in with an integer between 0-260";
		    refresh();
		    return;
		}
		int green = 0;
		try {
			green = Integer.parseInt(greenSelect.getText());
		}
		catch (NumberFormatException e ) {
			_errorMsg = _errorMsg + "Green must be filled in with an integer between 0-260";
		    refresh();
		    return;
		}
		int blue = 0;
		try {
			blue = Integer.parseInt(blueSelect.getText());
		}
		catch (NumberFormatException e) {
			_errorMsg = _errorMsg + "Blue must be filled in with an integer between 0-260";
		     refresh();
		     return;
		}
		
		try {
			List <TOBlock> updBlockList = Block223Controller.getBlocksOfCurrentDesignableGame();
			int blockID = updBlockList.get(selectedBlock).getId();
			Block223Controller.updateBlock(blockID, red, green, blue, points);
		}
		catch(InvalidInputException e){
			_errorMsg = e.getMessage();
			refresh();
			return;
		}
		
		parent.refresh();
		this.dispose();

		
		
	}
}

