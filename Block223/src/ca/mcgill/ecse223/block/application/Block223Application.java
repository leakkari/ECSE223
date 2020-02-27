package ca.mcgill.ecse223.block.application;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223UI;


public class Block223Application {
  
  /**
   * This main method is used only for testing the UI. All it does is create a instance of a Block223UI
   * and display it. Application will initialize using a different method later.
   * 
   * @param args Not used.
   */
  public static void main(String[] args) {
    
    
    //Block223UI block223ui = new Block223UI();
    //block223ui.setVisible(true);
    
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
          new Block223UI();
      }
    });
    
    
    //un comment code block to run code to create a new game and play it
    
    
    // try to login, create game, login to player, and register
//    try {
//     
//     //block223.delete();
//     resetBlock223();
//     
//     Block223Controller.register("chris", "1", "2");
//     
//     Block223Controller.login("chris", "2");
//     
//     Block223Controller.createGame("playtestgame");
//     
//     Block223Controller.selectGame("playtestgame");
//     
//     Block223Controller.setGameDetails(5, 5, 2, 2, 1.0, 40, 10);
//     
//     Block223Controller.addBlock(5, 100, 5, 5);
//     
//     Block223Controller.positionBlock(Block223Controller.getBlocksOfCurrentDesignableGame().get(0).getId(), 1, 1, 1);
//     Block223Controller.positionBlock(Block223Controller.getBlocksOfCurrentDesignableGame().get(0).getId(), 1, 8, 12);
//     Block223Controller.positionBlock(Block223Controller.getBlocksOfCurrentDesignableGame().get(0).getId(), 1, 12, 8);
//     
//     
//     
//     Block223Controller.publishGame();
//     
//     
//     Block223Controller.logout();
//     
//     Block223Controller.login("chris", "1"); // TODO error with loading from serialized file
//      
//      /*
//      System.out.println(Block223Application.getCurrentUserRole() instanceof ca.mcgill.ecse223.block.model.Player);
//      Block223Controller.selectPlayableGame("playtestgame", -1);
//      
//      Block223Controller.startGame(block223ui);
//      
//      */
//
//    } catch (Exception e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    
    
    
    
  }
  
  
	private static UserRole currentRole = null;
	private static Game currentGame = null;
	private static Block223 block223 = null;
	private static PlayedGame pg = null;

	public static Block223 getBlock223() {
		if (block223 == null) {
			// load model
			block223 = Block223Persistence.load();
		}
		return block223;
	}

	public static void setCurrentGame(Game game) {
	  currentGame = game;
	}
	
	public static Game getCurrentGame () {
	  return currentGame;
	}

	public static void setCurrentUserRole(UserRole role) {
		currentRole=role;
	}

	public static UserRole getCurrentUserRole() {
		return currentRole;
	}
	
	//play
	
	public static void setCurrentPlayableGame(PlayedGame pgame) {
		pg = pgame;
	}
	
	public static PlayedGame getCurrentPlayableGame() {
		return pg;
	}
	
	/**
	 * Resets the current block223 application by deleteing all related Users, UserRoles and games and reloading
	 * the block223 from persistence. 
	 */
	public static void resetBlock223() {
		if (block223 != null) {
		  block223.delete();
		  block223 = null;
		}
		setCurrentGame(null);
		setCurrentPlayableGame(null);
		block223 = Block223Persistence.load();
		//return block223;
		
		try {

	      System.out.println(block223.getGames().get(0).getName());
		} catch (IndexOutOfBoundsException e) {
		  
		}
	}
}
