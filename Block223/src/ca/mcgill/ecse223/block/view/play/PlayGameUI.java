package ca.mcgill.ecse223.block.view.play;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOPlayableGame;

import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Class that is reponsible for the UI of the "Play Game" feature
 * @author leaakkari
 *
 */
public class PlayGameUI extends JFrame {
	
	public static final String USER_NAME = "Player1";
	public static final String USER_NAME_2 = "Bob";
	public static final String USER_PASS = "userpass";
	public static final String ADMIN_PASS = "pass";
	public static final String TEST_GAME_NAME_1 = "Test game name 1";
	public static final String TEST_GAME_NAME_2 = "Test game name 2";
	public static final String EMPTY_NAME = "";
	public static final String MISSING_EXPECTED_EXCEPTION = "Missing expected exception with error message: ";

	public static final int LEVELS = 10;
	public static final int BLOCKS_PER_LEVEL = 20;
	public static final int MIN_BALL_SPEED_X = 2;
	public static final int MIN_BALL_SPEED_Y = 2;
	public static final Double BALL_SPEED_INCREASE_FACTOR = 1.1;
	public static final int MAX_PADDLE_LENGTH = 6;
	public static final int MIN_PADDLE_LENGTH = 2;

	public static final int RED = 1;
	public static final int GREEN = 1;
	public static final int BLUE = 1;
	public static final int POINTS = 1;

	public static final int RED_2 = 2;
	public static final int GREEN_2 = 2;
	public static final int BLUE_2 = 2;
	public static final int POINTS_2 = 2;

	public static final int HORIZONTAL_POS = 2;
	public static final int VERTICAL_POS = 2;
	public static final int BLOCK_LEVEL = 1;
	
  
 
   //Define UI Elements
	
	private JLabel errorMsg;
	private String error;
	
	private JPanel contentPage;
	private JButton deleteButton;
	private JButton cancelButton;
	private JTable gameTable;
	private Block223UI parent;
	
	public static String gameName;
	
	public PlayGameUI(Block223UI parent) {
		this.parent = parent;
		initComponents();
	}
	
	//initialize UI elements
	public void initComponents() {
		
		//Set Error 
		error = "";
		//errorMsg.setText(error);
		errorMsg = new JLabel(error);
		errorMsg.setForeground(Color.RED);
		errorMsg.setText(error);
		errorMsg.setBounds(10, 370, 350, 29);
		

		
		setTitle("Play Game");
		setBounds(100,100,649,483);
		contentPage = new JPanel();
		contentPage.setBorder(new EmptyBorder(5,5,5,5));
		contentPage.add(errorMsg);
		setContentPane(contentPage);
		contentPage.setLayout(null);
		
		// Set scroll pane for table
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(63, 35, 489, 299);
		contentPage.add(scrollPane);
		
		gameTable = new JTable();
		
		
		gameTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"ID", "Select Game" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		DefaultTableModel model = (DefaultTableModel) gameTable.getModel();
		scrollPane.setViewportView(gameTable);

		refresh(model);
		
		JButton okBtn = new JButton("Play");
		
		
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				confirmButtonPressed();
			}
		});
		
		okBtn.setBounds(258, 398, 119, 35);
		contentPage.add(okBtn);
	}
	
	/**
	 * Update the display table
	 */
	private void refresh(DefaultTableModel model)  {
		// Clear table data
		errorMsg.setText(error);
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		
		Object[] newRow = {0, 0};
		try {
			for (TOPlayableGame game : Block223Controller.getPlayableGames()) {
  					newRow[1] = game.getName();
  					newRow[0] = game.getNumber();
  					model.addRow(newRow);
			}
		}
		catch(Exception e) {
			errorMsg.setText("No Games");
		}
	}
	
	
	private void confirmButtonPressed() {
      //error = null;
      // Get selected row
      int selectedRow = gameTable.getSelectedRow();
      // Make sure a row was selected
      if (selectedRow != -1) {
          try {
              TOPlayableGame g2p = Block223Controller.getPlayableGames().get(selectedRow);
              gameName = g2p.getName();
              
              parent.startGame(this, g2p);
             
              
          } catch (InvalidInputException e1) {
              errorMsg.setText(e1.getMessage());
          }
      }
	}
	
	
  
}
