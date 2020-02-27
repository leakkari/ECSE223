package ca.mcgill.ecse223.block.view.play;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.view.Block223UI;

public class GameOverUI extends JFrame {
	
	private Block223UI parent;
	private String gamemsg;
	private JLabel gameover;
	private JButton okbutton;
	private JPanel contentPage;
	
	public GameOverUI (Block223UI parent) {
		this.parent = parent;
		initComponents();
	}

	public void initComponents() {
		gamemsg ="";
		gameover = new JLabel();
		gameover.setForeground(Color.RED);		
		//get score
		int score = Block223Controller.getScore();
		
		gamemsg = "GameOver! You got " + score + " points";
		gameover.setText(gamemsg);
		
		setTitle("Game Over");
		setBounds(100,100,300,170);
		gameover.setBounds(70, 50, 200, 29);
		
		contentPage = new JPanel();
		contentPage.setBorder(new EmptyBorder(5,5,5,5));
		contentPage.add(gameover, CENTER_ALIGNMENT);
		setContentPane(contentPage);
		contentPage.setLayout(null);
		okbutton = new JButton("OK");
		
		contentPage.add(okbutton, BOTTOM_ALIGNMENT);
		okbutton.setBounds(95, 90, 100, 29);
		
		okbutton.setVisible(true);

		okbutton.addActionListener(al-> {
			okbuttonpressed();
		});
	}
	
	private void okbuttonpressed() {
		parent.refresh();
		this.dispose();
	}
	
	public static void main(String [] args) {
		Block223UI frame = new Block223UI();
		GameOverUI frame2 = new GameOverUI(frame);
		frame2.setVisible(true);
	}
}
