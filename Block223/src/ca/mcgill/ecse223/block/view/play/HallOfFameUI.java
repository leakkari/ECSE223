package ca.mcgill.ecse223.block.view.play;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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
import ca.mcgill.ecse223.block.controller.TOHallOfFameEntry;
import ca.mcgill.ecse223.block.view.Block223UI;

class backImage extends JComponent {
	 
Image i;
 
//Creating Constructer
public backImage(Image i) {
this.i = i;
 
}
 
//Overriding the paintComponent method
@Override
public void paintComponent(Graphics g) {
 
	

    Image img1 = Toolkit.getDefaultToolkit().getImage("⁨Block223⁩/src⁩/⁨ca⁩/mcgill⁩/⁨ecse223⁩/⁨block⁩/⁨resource⁩/hof.png");
    g.drawImage(img1, 40, 40, this);
    g.finalize();
 
}
}


/**
 * Class that is reponsible for the UI of the "View Hall Of Fame" feature
 * @author leaakkari
 */

public class HallOfFameUI extends JFrame{
	
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
		private JButton ok_Button;
		//private JButton cancelButton;
		private JTable hofTable;
		
		  // instance variables
		  private Block223UI parent;
		  private String _errorMsg;
		  private BufferedImage image;
		
		public HallOfFameUI(Block223UI parent) {
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
			

			
			setTitle("Hall Of Fame");
			setBounds(100,100,649,483);
			contentPage = new JPanel();
			contentPage.setBorder(new EmptyBorder(5,5,5,5));
			contentPage.add(errorMsg);
			setContentPane(contentPage);
			contentPage.setLayout(null);
			
//			
//			try {
//				image = ImageIO.read(new File("hof.jpg"));
//				//image = ImageIO.read(getClass().getResource("file:///Users/leaakkari/Documents/git/ecse223-group-project-p-16/Block223/src/ca/mcgill/ecse223/block/resource/hof.jpg"));
//				//image = ImageIO.read(new File("Folder/hof.jpg"));
//				 JLabel label = new JLabel(new ImageIcon(image));
//				 label.setBounds(70, 70, 500, 500);
//				 contentPage.add(label);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			   
		
			
			 
			// Set scroll pane for table
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(63, 35, 489, 299);
			contentPage.add(scrollPane);
			
			hofTable = new JTable();
			
			
			hofTable.setModel(new DefaultTableModel(new Object[][] {},
					new String[] { "Player", "Score" }) {
				boolean[] columnEditables = new boolean[] { false, false, false, false };

				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
			DefaultTableModel model = (DefaultTableModel) hofTable.getModel();
			scrollPane.setViewportView(hofTable);

			refresh(model);
			
			
			
			JButton okBtn = new JButton("Ok");
			
		   okBtn.addActionListener(al -> {
		      cancelButtonPressed();
		    });
			okBtn.setBounds(258, 398, 119, 35);
			contentPage.add(okBtn);
		}
		
		/**
		 * Update the display table
		 */
		private void refresh(DefaultTableModel model)  {
			String gameName = PlayGameUI.gameName;
			System.out.println("GameName: " + gameName);
			// Clear table data
			errorMsg.setText(error);
			for (int i = model.getRowCount() - 1; i >= 0; i--) {
				model.removeRow(i);
			}
		
			Object[] newRow = { 0, 0};
			try {

				
				for (TOHallOfFameEntry currHof : Block223Controller.getHallOfFameofGame(gameName,10).getEntries()) {
					newRow[0] = currHof.getPlayername();
					newRow[1] = currHof.getScore();
		
					model.addRow(newRow);
				}
			}
			catch(Exception e) {
				String err = e.getMessage();
				errorMsg.setText(err);;
			}
		}
		
		 /**
		   * Cancel button listener.
		   * Example listener.
		   * Most UI's will have one of these.
		   */
		  private void cancelButtonPressed() {
		    parent.refresh();
		    this.dispose();
		  }

}
