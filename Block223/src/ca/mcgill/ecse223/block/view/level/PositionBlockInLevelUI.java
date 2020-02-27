package ca.mcgill.ecse223.block.view.level;

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
import ca.mcgill.ecse223.block.view.block.UpdateBlockFromGameUI;

public class PositionBlockInLevelUI extends JFrame{
	
	private JLabel errorMessage;

	private JComboBox <String> gameToggleList;
	private JLabel gameToggleLabel;
	private JComboBox <String> blockToggleList;
	private JLabel blockToggleLabel;
	private JButton cancelButton;
	
	private JLabel levelLabel;
	private JTextField levelSelect;
	private JLabel horizPosLabel;
	private JTextField horizPosSelect;
	private JLabel vertPosLabel;
	private JTextField vertPosSelect;
	private JButton positionButton;
	private int WIN_WIDTH = 400;
	private int WIN_HEIGHT = 300;
	private int TXT_WIDTH = 20;
	private int TXT_HEIGHT = 10;
	
	
	private List<TOGame> gameList;
	private List<TOBlock> blockList;
	private List<TOBlock> blockLister;
	private List<String> gameNames;
	private List<String> blockNames;
	private List <TOBlock> posBlockList;
	private String _errorMsg;
	private Block223UI parent;
	
	
	public PositionBlockInLevelUI(Block223UI parent){
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

		// Elements for common
		gameToggleList = new JComboBox<String>(gameNamesArray);
		gameToggleLabel = new JLabel();
		gameToggleLabel.setText("Select Game:");
		blockToggleList = new JComboBox<String>(blockNamesArray);
		blockToggleLabel = new JLabel();
		blockToggleLabel.setText("Select Block:");
		cancelButton = new JButton();
		cancelButton.setText("Cancel");
		
		levelLabel = new JLabel();
		levelLabel.setText("Level:");
		levelSelect = new JTextField();
		levelSelect.setPreferredSize(new Dimension(TXT_WIDTH, TXT_HEIGHT));
		horizPosLabel = new JLabel();
		horizPosLabel.setText("Horizontal Position:");
		horizPosSelect = new JTextField();
		horizPosSelect.setPreferredSize(new Dimension(TXT_WIDTH, TXT_HEIGHT));
		vertPosLabel = new JLabel();
		vertPosLabel.setText("Vertical Position:");
		vertPosSelect = new JTextField();
		vertPosSelect.setPreferredSize(new Dimension(TXT_WIDTH, TXT_HEIGHT));
		positionButton = new JButton();
		positionButton.setText("Position Block");
		
		//global settings
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Block223 - Position a Block in a Level");
		
		positionButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed (java.awt.event.ActionEvent evt) {
				positionButtonActionPerformed(evt);
				
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
		
		GroupLayout.ParallelGroup hGroup = layout.createParallelGroup();
	    GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
	    
	    hGroup.addComponent(errorMessage);
	    hGroup.addGroup(layout.createSequentialGroup()
	        .addGroup(layout.createParallelGroup()
	            .addComponent(gameToggleLabel))
	        .addComponent(gameToggleList));
	    hGroup.addGroup(layout.createSequentialGroup()
	        .addGroup(layout.createParallelGroup() 
	            .addComponent(levelLabel)
	            .addComponent(levelSelect)
	            .addComponent(horizPosLabel)
	            .addComponent(horizPosSelect)
	            .addGap(0, 10, Short.MAX_VALUE)   
	            )
	        .addGroup(layout.createParallelGroup()
	            .addComponent(blockToggleLabel)
	            .addComponent(blockToggleList)
	            .addComponent(vertPosLabel)
	            .addComponent(vertPosSelect)
	            .addGap(0,50,Short.MAX_VALUE)));
	    hGroup.addGroup(layout.createSequentialGroup()
	        .addComponent(positionButton)
	        .addComponent(cancelButton));
	    
	    vGroup.addComponent(errorMessage);
	    vGroup.addGroup(layout.createParallelGroup()
	        .addGroup(layout.createSequentialGroup()
	            .addComponent(gameToggleLabel))
	        .addComponent(gameToggleList));
	    vGroup.addGroup(layout.createParallelGroup()
	        .addGroup(layout.createSequentialGroup()
	            // first half, general and paddle settings
	            .addComponent(levelLabel)
	            .addComponent(levelSelect)
	            .addComponent(horizPosLabel)
	            .addComponent(horizPosSelect)
	            .addGap(0, 10, Short.MAX_VALUE) // add gap to separate 
	            )
	        .addGroup(layout.createSequentialGroup()
	            // ball settings
	            .addComponent(blockToggleLabel)
	            .addComponent(blockToggleList)
	            .addComponent(vertPosLabel)
	            .addComponent(vertPosSelect)
	            .addGap(0,50,Short.MAX_VALUE)));
	    vGroup.addGroup(layout.createParallelGroup()
	        .addComponent(positionButton)
	        .addComponent(cancelButton));
		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
		pack(); 
	}
	private void refresh() {
		errorMessage.setText(_errorMsg);

		if (_errorMsg == null || _errorMsg.length() == 0) {
			horizPosSelect.setText("");
			vertPosSelect.setText("");
			levelSelect.setText("");

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
	private void positionButtonActionPerformed(java.awt.event.ActionEvent evt) {
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
			_errorMsg= "Block needs to be selected";
			refresh();
			return;
		}
		int level = 0;
		try {
			level = Integer.parseInt(levelSelect.getText());
		}
		catch (NumberFormatException e) {
			_errorMsg= _errorMsg + "Level must be a numerical value";
			refresh();
			return;
		}
		int horizPosi = 0;
		try {
			horizPosi = Integer.parseInt(horizPosSelect.getText());
		}
		catch (NumberFormatException e ) {
			_errorMsg= _errorMsg + "Horizontal position must be a numerical value";
			refresh();
			return;
		}
		
		int vertPosi = 0;
		try {
			vertPosi = Integer.parseInt(vertPosSelect.getText());
		}
		catch (NumberFormatException e ) {
			_errorMsg= _errorMsg + "Vertical position must be a numerical value";
			refresh();
			return;
		}
		
		try {
			List <TOBlock> posBlockList = Block223Controller.getBlocksOfCurrentDesignableGame();
			int blockID = posBlockList.get(selectedBlock).getId();
			Block223Controller.positionBlock(blockID, level, horizPosi, vertPosi);
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

