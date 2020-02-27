package ca.mcgill.ecse223.block.view.game;

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

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.view.Block223UI;

/**
 * Class that is reponsible for the UI of the "delete  game" feature
 * @author leaakkari
 *
 */
public class DeleteGameUI extends JFrame {
  
 
   //Define UI Elements
	
	private JLabel errorMsg;
	private String error;
	
	private JPanel contentPage;
	private JButton deleteButton;
	private JButton cancelButton;
	private JTable gameTable;
	
	public DeleteGameUI(Block223UI parent) {
		initComponents();
	}
	
	//initialize UI elements
	public void initComponents() {
		//get Object
		Block223 block223 = Block223Application.getBlock223();
		
		//Set Error 
		error = "";
		//errorMsg.setText(error);
		errorMsg = new JLabel(error);
		errorMsg.setForeground(Color.RED);
		errorMsg.setText(error);
		errorMsg.setBounds(10, 370, 350, 29);
		

		
		setTitle("Delete Game");
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
				new String[] { "Game No.", "Select Game" }) {
			boolean[] columnEditables = new boolean[] { false, false, false, false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		DefaultTableModel model = (DefaultTableModel) gameTable.getModel();
		scrollPane.setViewportView(gameTable);

		refresh(model, block223);
		
		JButton deleteGameBtn = new JButton("Delete");
		if(block223.getGames().isEmpty()) {
			error = "Emtpy Game List";
			errorMsg.setText(error);
		}
		deleteGameBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//error = null;
				// Get selected row
				int selectedRow = gameTable.getSelectedRow();
				// Make sure a row was selected
				if (selectedRow != -1) {
					// Get selected table object
					Game gameToDel = block223.getGames().get(selectedRow);
					String nameToDel =gameToDel.getName();
					// Delete table
					try {
						Block223Controller.deleteGame(nameToDel);
					} catch (Exception e1) {
						error = e1.getMessage();
						errorMsg.setText(error);
					}
					refresh(model, block223);
				}
			}
		});
		
		deleteGameBtn.setBounds(258, 398, 119, 35);
		contentPage.add(deleteGameBtn);
	}
	
	/**
	 * Update the display table
	 */
	private void refresh(DefaultTableModel model, Block223 block223)  {
		// Clear table data
		errorMsg.setText(error);
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		int i=0;
		
		Object[] newRow = { 0, 0};
		try {
			for (Game currGame : block223.getGames()) {
				newRow[0] = i++;
				newRow[1] = currGame.getName();
	
				model.addRow(newRow);
			}
		}
		catch(Exception e) {
			
			errorMsg.setText("No Games");
		}
	}

  
}
