package ca.mcgill.ecse223.block.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;
import ca.mcgill.ecse223.block.model.Player;
import ca.mcgill.ecse223.block.model.User;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.Block223KeyListener;
import ca.mcgill.ecse223.block.view.Block223PlayModeInterface;
import ca.mcgill.ecse223.block.view.Block223UI;
import ca.mcgill.ecse223.block.view.play.PlayGameUI;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.model.Admin;
import ca.mcgill.ecse223.block.model.Ball;
import ca.mcgill.ecse223.block.model.Block;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.BlockAssignment;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.HallOfFameEntry;
import ca.mcgill.ecse223.block.model.Level;
import ca.mcgill.ecse223.block.model.Paddle;
import ca.mcgill.ecse223.block.model.PlayedBlockAssignment;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.PlayedGame.PlayStatus;

/**
 * Block223Controller class, methods are called by User Interface View model to
 * manipulate and interact with Block223 domain model.
 * 
 * {@link block223_controller_TODO_list.txt}
 * 
 * TODO: add check if game is published when attempting to update or
 *       set game settings (a published game cannot be modified).
 *       
 */

public class Block223Controller {

    // **************************************************************************************** //
    // Iteration 4 Modifier   Methods                                                           //
    // **************************************************************************************** //
  
    /**
     * Sets the currently Playable game in block223 application class.
     * 
     * @author leaakkari
     * @param name
     * @param id
     * @throws InvalidInputException
     */
    public static void selectPlayableGame(String name, int id) throws InvalidInputException  {
      Block223 block223 = Block223Application.getBlock223();
      Player player;
  	  UserRole currentUserRole = Block223Application.getCurrentUserRole();
  	  
      if(!(currentUserRole instanceof Player)) {
  		throw new InvalidInputException("Player privileges are required to play a game.");
  	  }
      if(block223.findGame(name)==null && block223.findPlayableGame(id)==null) {
    	  throw new InvalidInputException("The game does not exist.");
      }
      if(block223.findGame(name)==null && !currentUserRole.equals(block223.findPlayableGame(id).getPlayer())) {
    	  throw new InvalidInputException("Only the player that started a game can continue the game.");
      }
      
      Game game = block223.findGame(name);
      if(game!=null && id == -1) {
    	  player = (Player) Block223Application.getCurrentUserRole();
    	  
    	  // we have a Player object and we want to get its user name
    	  //String username = User.findUsername(player);   --- Replaced with chuck of code below for persistence reasons (talk to christos)
    	  /*
    	  String username = "";
    	  for (User user: Block223Application.getBlock223().getUsers()) {
    	    // get all users of current application
    	    for (UserRole role : user.getRoles()) {
    	      // get all roles of user
    	      if (role.equals(player)) {
    	        // if we find a match, get username
    	        username = user.getUsername();
    	      }
    	    }
    	  }
    	  */
    	  System.out.println("ff"+player.getPassword());
    	  String username = User.findUsername(player);
    	  System.out.println("asdf"+username);
    	  PlayedGame pgame = new PlayedGame(username, game, block223);
    	  pgame.setPlayer(player);  
    	  //System.out.println("set??"+ pgame.hasPlayer());
    	  Block223Application.setCurrentPlayableGame(pgame);
      } else {
    	 PlayedGame pgame =  block223.findPlayableGame(id);
    	 Block223Application.setCurrentPlayableGame(pgame);
    	  
      }
      
    }
  
    /**
     * Starts the currently selected game. 
     * 
     * <p>If the game is not published, it can only be played by the Admin
     * that created it, and there is no Hall Of Fame Entry recorded. If a game
     * is published, it can only be played by a User of UserRole player.</p>   
     * 
     * @author christos
     * @throws InvalidInputException
     */
    public static void startGame(Block223PlayModeInterface ui) throws InvalidInputException {

      // Validation Checks
      // if current user role is not set throw
      if (Block223Application.getCurrentUserRole() == null) {
        throw new InvalidInputException("Player privileges are required to play a game.");
      }
      // if currentPlayablegame is not set throw
      if (Block223Application.getCurrentPlayableGame() == null) {
        throw new InvalidInputException("A game must be selected to play it.");
      }
      // user role is admin and game is not test mode
      if (Block223Application.getCurrentUserRole() instanceof Admin && Block223Application.getCurrentPlayableGame().getPlayer() != null) {
        throw new InvalidInputException("Player privileges are required to play a game.");
      }
      // if admin is not admin of current game
      if (Block223Application.getCurrentUserRole() instanceof Admin && !Block223Application.getCurrentPlayableGame().getGame().getAdmin().equals((Admin) Block223Application.getCurrentUserRole())) {
        throw new InvalidInputException("Only the admin of a game can test the game.");
      }
      // if current user is a player and currentPlayableGame has no user
      if (Block223Application.getCurrentUserRole() instanceof Player && Block223Application.getCurrentPlayableGame().getPlayer() == null) {
        throw new InvalidInputException("Admin privileges are required to test a game.");
      }
      
      /*
      // Create new game thread
      Thread game_thread = new Thread() {
        @Override
        public void run() {
          // TODO Auto-generated method stub
          //could put code in here to try and get separate
          PlayedGame game = Block223Application.getCurrentPlayableGame();
          
          game.play();
          
          String userInputs = ui.takeInputs();
          
          while (game.getPlayStatus() == PlayStatus.Moving || game.getPlayStatus() == PlayStatus.Paused) {
             // get user input
             userInputs = ui.takeInputs();
             
             // if game is paused then we just check if userInput has a space
             // else, do all game stuff
             if (game.getPlayStatus() == PlayStatus.Paused && userInputs.contains(" ")) {
               game.play();
               userInputs.replace(" ", ""); // remove spaces from userInput string
             } else if (game.getPlayStatus() == PlayStatus.Moving) {
               // move paddle depending on user input
               updatePaddlePosition(userInputs); // TODO: not sure where this method is suppose to be
               
               // move everything on the game
               //System.out.println(System.currentTimeMillis() - starttime ); 
               game.move();
               
               // if user pressed space, pause
               if (userInputs.contains(" ")) {
                 game.pause();
               }
             }
               
             // wait for game.getWaitTime() milliseconds
             try {
               //Thread.sleep((long) game.getWaitTime());
               //System.out.println("Game wait time: " + game.getWaitTime());
               
               TimeUnit.MILLISECONDS.sleep((long) game.getWaitTime()); // TODO why divide by 30
             } catch (InterruptedException e) {
               System.out.println("Does this ever happen");
               //Thread.currentThread().interrupt();
             }
             
             
             // refresh ui
             ui.refresh();
          }
          
          if (game.getPlayStatus() == PlayStatus.GameOver) {
            //set current played game to null
            Block223Application.setCurrentPlayableGame(null);
            //ui.endGame(game.getLives(), new TOHallOfFameEntry(null, null, null, null));
            
          } else if (game.getPlayer() != null) {
              game.setBounce(null);
            // save game if player is not null (ie: game is NOT being played in test mode by an admin)
            Block223 block223 = Block223Application.getBlock223();
            Block223Persistence.save(block223);
          }
          
        }
      };
      
      // start game thread
      game_thread.start();
      */
      
      // old controller method - works with tests - but not really with application
      
      PlayedGame game = Block223Application.getCurrentPlayableGame();
      
      game.play();
      
      String userInputs = ui.takeInputs();
      
     // while (game.getPlayStatus() != PlayStatus.GameOver) {
    	  
    	  while (game.getPlayStatus() == PlayStatus.Moving) {
    		  // get user input
    		  userInputs = ui.takeInputs();
    		  // move paddle depending on user input
    		  updatePaddlePosition(userInputs); // TODO: not sure where this method is suppose to be
    		  // move everything on the game
    		  game.move();
    		  // if user pressed space, pause
    		  if (userInputs.contains(" ")) {
    			  game.pause();
    		  }
    		  // wait for game.getWaitTime() milliseconds
    		  try {
    			  Thread.sleep((long) game.getWaitTime());
    		  } catch (InterruptedException e) {
    			  Thread.currentThread().interrupt();
    		  }
    		  // refresh ui
    		  ui.refresh();
    	  }
    	  System.out.println(game.getPlayStatus());
    	  
//    	  while (game.getPlayStatus() == PlayStatus.Paused) {
//    		  userInputs = "";
//    		  userInputs = ui.takeInputs();
//    		  if(userInputs.contains(" ")) {
//    			  System.out.println("Hello");
//        		  userInputs = "";
//    			  game.play();
//    		  }
//    		  try {
//    			  Thread.sleep((long)10000);
//    		  } catch (InterruptedException e) {
//    			  Thread.currentThread().interrupt();
//    		  }
//    		  game.play();
//
//    	  }
      
//      }
      
      if (game.getPlayStatus() == PlayStatus.GameOver) {
        
        // create new HoF entry with current data
        if (game.getPlayer() != null) {
          //HallOfFameEntry hofentry = new HallOfFameEntry(game.getScore(), game.getPlayername(), game.getPlayer(), game.getGame(), Block223Application.getBlock223());
        }
        //HallOfFameEntry hofentry = new HallOfFameEntry(game.getScore(), game.getPlayername(), game.getPlayer(), game.getGame(), Block223Application.getBlock223());
        
       // System.out.println(Block223Controller.getHallOfFameWithMostRecentEntry(10).getEntry(0));
        // add entry to game
        //game.getGame().addHallOfFameEntry(hofentry);
        ui.endGame(0,null);

        //set current played game to null
        Block223Application.setCurrentPlayableGame(null);
      } else if (game.getPlayer() != null) {
        game.setBounce(null);
        // save game if player is not null (ie: game is NOT being played in test mode by an admin)
        Block223 block223 = Block223Application.getBlock223();
        Block223Persistence.save(block223);
      }
      
    }
    
    /**
     * Not sure how I was supposed to implement this method so I just put it 
     * 
     * Moves the paddle one PlayedPaddle.PADDLE_MOVE_LEFT
     * to the left for each l in the userInputs string before a space and one
     * PlayedPaddle.PADDLE_MOVE_RIGHT to the right for each r in the userInputs before a space.
     * 
     * @param aUserInput String userInput obtained from UI.
     */
    private static void updatePaddlePosition(final String aUserInput) {
      PlayedGame pgame = Block223Application.getCurrentPlayableGame();
      
      if (aUserInput.isEmpty()) {
        return;
      }
      
      char[] userInputArray = aUserInput.toCharArray();
      //System.out.println("string: " + aUserInput + " char " + userInputArray);
      for (char c : userInputArray) {
        double move_amt = 0; // default to 0 move if no 'l' or 'r' character
        if (c == 'l') {
          // move paddle one unit left
          move_amt += PlayedGame.PADDLE_MOVE_LEFT;
        } else if (c == 'r') {
          // move paddle one unit right
          move_amt += PlayedGame.PADDLE_MOVE_RIGHT;
        } else if (c == ' ') {
          // if we get a space, pause game
          // we are done moving paddle if game is paused
          break;
        } else {
          // if any other character, we don't care
        }
        // move paddle based on move_amt (either -1,0 or 1);
        pgame.setCurrentPaddleX(pgame.getCurrentPaddleX() + move_amt);
      }
    }
    
    public static void testGame(Block223PlayModeInterface ui) throws InvalidInputException {

  	  // valid input checks
	  	  if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
	  	    throw new InvalidInputException("Admin privileges are required to test a game.");
	  	  }
    
	  	  if (Block223Application.getCurrentGame()==null) {
	  	    throw new InvalidInputException("A game must be selected to test it.");
	  	  }
  	
  	
	  	  Game game=Block223Application.getCurrentGame();
  	
	  	  Admin admin=(Admin) Block223Application.getCurrentUserRole();
  	
	  	  if (!admin.equals(game.getAdmin())) {
	  		  throw new InvalidInputException("Only the admin who created the game can test it.");
	  	  }
	  	  
  	
	  	  String username=User.findUsername(admin);
  	
	  	  Block223 block223=Block223Application.getBlock223();
  	
	  	  PlayedGame pgame=new PlayedGame(username,game,block223);
  	
	  	  pgame.setPlayer(null);
  	
	  	  Block223Application.setCurrentPlayableGame(pgame);
	      
	  	  startGame(ui);
    }
    
    /**
     * Feature:9 
     * 
     * Mahdis Asaadi
     * @throws InvalidInputException
     */
    public static void testGame(Block223PlayModeInterface ui, Block223UI parent) throws InvalidInputException {
    	
    	  // valid input checks
	  	  if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
	  	    throw new InvalidInputException("Admin privileges are required to test a game.");
	  	  }
      
    	if (Block223Application.getCurrentGame()==null) {
	  	    throw new InvalidInputException("A game must be selected to test it.");
	  	  }
    	
    	
    	Game game=Block223Application.getCurrentGame();
    	
    	Admin admin=(Admin) Block223Application.getCurrentUserRole();
    	
    	if (!admin.equals(game.getAdmin())) {
	  	    throw new InvalidInputException("Only the admin who created the game can test it.");
	  	  }
	  	  
    	
    	String username=User.findUsername(admin);
    	
    	Block223 block223=Block223Application.getBlock223();
    	
    	PlayedGame pgame=new PlayedGame(username,game,block223);
    	
    	pgame.setPlayer(null);
    	
    	Block223Application.setCurrentPlayableGame(pgame);
    	
    	Block223KeyListener blockKeyListener = new Block223KeyListener();
    	    
	    //Block223Controller.startGame(this);
	    
	    Runnable r1 = new Runnable() {
	      @Override
	      public void run() {
	          // in the actual game, add keyListener to the game window
	        parent.addKeyListener(blockKeyListener);
	      }
	    };
	    Thread t1 = new Thread(r1);
	    t1.start();
	    // to be on the safe side use join to start executing thread t1 before executing
	    // the next thread
	    try {
	        t1.join();
	    } catch (InterruptedException e1) {
	    }
	  
	    // initiating a thread to start the game loop
	    Runnable r2 = new Runnable() {
	        @Override
	        public void run() {
	            try {
	                Block223Controller.startGame(ui);
	            } catch (InvalidInputException e) {
	            }
	        }
	    };
	    Thread t2 = new Thread(r2);
	    t2.start();
    }
    
    /**
     * 
     * implemented by Mahdis Asaadi
     * @throws InvalidInputException
     */
    public static void publishGame() throws InvalidInputException {
    	
  	  // valid input checks
    	//Admin admin=(Admin) Block223Application.getCurrentUserRole();
	  	  if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
	  	    throw new InvalidInputException("Admin privileges are required to publish a game.");
	  	  }

    	if (Block223Application.getCurrentGame()==null) {
	  	    throw new InvalidInputException("A game must be selected to publish it.");
	  	  }
    	
    	 Game game=Block223Application.getCurrentGame();
    	 
	  	Admin admin=(Admin) Block223Application.getCurrentUserRole();
    	if (!admin.equals(game.getAdmin())) {
	  	    throw new InvalidInputException("Only the admin who created the game can publish it.");
	  	  }
    		
    
     
      
      if(game.getBlocks().size()<1) {
    	  throw new InvalidInputException("At least one block must be defined for a game to be published.");
      }
      
      game.setPublished(true);
      System.out.println("published game " + game.getName() +" published: "+game.isPublished());
    }
    
    // All done
    // **************************************************************************************** //
    // Iteration 4 Query methods        (PLAY MODE METHODS)                                     //
    // **************************************************************************************** //
    
    /**
     * Returns an ArrayList<TOPlayableGames of the current published games and all in progress games
     * for the current user. UserRole must be set to Player or this method will throw an
     * InvalidInputException.
     * 
     * @author Christos
     * @return List<TOPlayableGames> Of all Currently playable games.
     * @throws InvalidInputException If UserRole is not set to player.
     */
    public static List<TOPlayableGame> getPlayableGames() throws InvalidInputException {
      // get application
      Block223 block223 = Block223Application.getBlock223();
      // get current user
      UserRole user = Block223Application.getCurrentUserRole();
      Player player;
      // check if userrole is of type player
      if (!(user instanceof Player)) {
        throw new InvalidInputException("Player privileges are required to play a game.");
      } else {
        player = (Player) user;
      }
      
      // create result list
      List<TOPlayableGame> result = new ArrayList<TOPlayableGame>();
      
      // get all games of block223 application
      List<Game> games1 = block223.getGames();
      
      // add all published games to result list
      for (Game game : games1) {
        boolean published = game.isPublished();
        if(published) {
          TOPlayableGame to = new TOPlayableGame(game.getName(),-1,0);
          result.add(to);
        }
      }
      
      // add all games played by played to list
      List<PlayedGame> games2 = player.getPlayedGames();
      for (PlayedGame game : games2) {
        TOPlayableGame to = new TOPlayableGame(game.getGame().getName(),game.getId(), game.getCurrentLevel());
        result.add(to);
      }
      
      return result;
    }
    
    
    /**
     * Returns the currently playable game as a TOCurrentlyPlayedGame transfer object.
     * 
     * TODO : validation checks for this method may be wrong because it looks like a type in the Sample solution document
     *        for iteration 4. At the bottom it has the same notes as after the table for 

() exceptions, so these
     *        exceptions may just be for startGame(), and not this method too.
     * 
     * @author christos
     * @return A transfer object representing the currently playable game.
     * @throws InvalidInputException If any of the validation checks fail.
     */
    public static TOCurrentlyPlayedGame getCurrentPlayableGame() throws InvalidInputException {
      
      // Validation Checks
      // if current user role is not set throw
      if (Block223Application.getCurrentUserRole() == null) {
        throw new InvalidInputException("Player privileges are required to play a game.");
      }
      // if currentPlayablegame is not set throw
      if (Block223Application.getCurrentPlayableGame() == null) {
        throw new InvalidInputException("A game must be selected to play it.");
      }
      // user role is admin and game is not test mode
      if (Block223Application.getCurrentUserRole() instanceof Admin && Block223Application.getCurrentPlayableGame().getPlayer() != null) {
        throw new InvalidInputException("Player privileges are required to play a game.");
      }
      // if admin is not admin of current game
      UserRole curRole = Block223Application.getCurrentUserRole();
      if (curRole instanceof Admin && !Block223Application.getCurrentPlayableGame().getGame().getAdmin().equals((Admin)curRole)) {
        throw new InvalidInputException("Only the admin of a game can test the game.");
      }
      // if current user is a player and currentPlayableGame has no user
      if (Block223Application.getCurrentUserRole() instanceof Player && Block223Application.getCurrentPlayableGame().getPlayer() == null) {
        throw new InvalidInputException("Admin privileges are required to test a game.");
      }
      
      PlayedGame pgame = Block223Application.getCurrentPlayableGame();
      
      boolean paused = pgame.getPlayStatus() == PlayStatus.Ready || pgame.getPlayStatus() == PlayStatus.Paused;
      
      TOCurrentlyPlayedGame result = new TOCurrentlyPlayedGame(
          pgame.getGame().getName(),
          paused,
          pgame.getScore(),
          pgame.getLives(),
          pgame.getCurrentLevel(),
          pgame.getPlayername(),
          pgame.getCurrentBallX(),
          pgame.getCurrentBallY(),
          pgame.getCurrentPaddleLength(),
          pgame.getCurrentPaddleX()
          );
      
      List<PlayedBlockAssignment> blocks = pgame.getBlocks();
      
      for (PlayedBlockAssignment pblock : blocks) {
        // add transfer object for each block in current played game to transfer object of full currently played game
        TOCurrentBlock to = new TOCurrentBlock(
            pblock.getBlock().getRed(),
            pblock.getBlock().getGreen(),
            pblock.getBlock().getBlue(),
            pblock.getBlock().getPoints(),
            pblock.getX(), pblock.getY(), result
            );
      }
      
      return result;
    }
    /**
     * 
     * @param name
     * @return
     */
    public static TOHallOfFame getHallOfFameofGame(String name, int numberOfEntries) {
    	Block223 block223 = Block223Application.getBlock223();
   	  	UserRole currentUserRole = Block223Application.getCurrentUserRole();
   	  	Player player;
   	    Game game = block223.findGame(name);
   	  	
   	  	// ******* Input Validation **********************
    	if(!(currentUserRole instanceof Player)) {
    		try {
				throw new InvalidInputException("Player privileges are required to access a game's hall of fame.");
			} catch (InvalidInputException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	// check if current game exists
		
		if (game == null) {
		  try {
			throw new InvalidInputException("A game must be selected to view its hall of fame.");
		} catch (InvalidInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
    	//************************************************
		
    	
    	
    	TOHallOfFame result = new TOHallOfFame(game.getName());
    	
    	//get most recent entry
		HallOfFameEntry mostRecent = game.getMostRecentEntry();
		int index = game.indexOfHallOfFameEntry(mostRecent);
		
	    
	    
		int start = index + (numberOfEntries/2);

		if(start>game.numberOfHallOfFameEntries()-1) {
			start=game.numberOfHallOfFameEntries()-1;
			
		}
		
		int end = start-numberOfEntries +1;
		
		if(end<0) {
			end=0;
		}
		
		start = start-1;
		end = end-1;
		
		while(start>=end) {
			//String userName = pgame.getPlayername();
			TOHallOfFameEntry to = new TOHallOfFameEntry(index+1, game.getHallOfFameEntry(index).getPlayername(), game.getHallOfFameEntry(index).getScore(), result);
			start--;
			index--;
		}
		
    	return result;
    }

    
    /**
     * @author Lea Akkari
     * @param start
     * @param end
     * @return
     * @throws InvalidInputException
     */
    public static TOHallOfFame getHallOfFame(int start, int end) throws InvalidInputException {
    	
    	Block223 block223 = Block223Application.getBlock223();
   	  	UserRole currentUserRole = Block223Application.getCurrentUserRole();
   	  	Player player;
   	    PlayedGame pgame = Block223Application.getCurrentPlayableGame();
   	  	
   	  	// ******* Input Validation **********************
    	if(!(currentUserRole instanceof Player)) {
    		throw new InvalidInputException("Player privileges are required to access a game's hall of fame.");
    	}
    	
    	// check if current game exists
		
		if (pgame == null) {
		  throw new InvalidInputException("A game must be selected to view its hall of fame.");
		}
    	//************************************************
		
    	Game game = pgame.getGame();
    	
    	TOHallOfFame result = new TOHallOfFame(game.getName());
    	
    	if(start<1) {
    		start = 1;
    	}
    	
    	if(end>game.numberOfHallOfFameEntries()) {
    		end = game.numberOfHallOfFameEntries();
    	}
    	start = game.numberOfHallOfFameEntries() - start;
    	end = game.numberOfHallOfFameEntries() - end;
  
    	for(int i= start; i>=end; i--) {
   
    		//String username = pgame.getPlayername();
    		TOHallOfFameEntry to = new TOHallOfFameEntry(i+1,game.getHallOfFameEntry(i).getPlayername(),game.getHallOfFameEntry(i).getScore(),result);
    		
    	}
    	return result;
    }

    /**
     * @author Lea Akkari
     * @param numberOfEntries
     * @return
     * @throws InvalidInputException
     */
    public static TOHallOfFame getHallOfFameWithMostRecentEntry(int numberOfEntries) throws InvalidInputException {
    	Block223 block223 = Block223Application.getBlock223();
   	  	UserRole currentUserRole = Block223Application.getCurrentUserRole();
   	  	Player player;
   	    PlayedGame pgame = Block223Application.getCurrentPlayableGame();
   	 System.out.println("pgame?"+pgame);
 	  	// ******* Input Validation **********************
    	if(!(currentUserRole instanceof Player)) {
    		throw new InvalidInputException("Player privileges are required to access a game's hall of fame.");
    	}
    	
    	// check if current game exists
		if (pgame == null) {
		  throw new InvalidInputException("A game must be selected to view its hall of fame.");
		}
    	//************************************************
     
		Game game = pgame.getGame();
		
		
		TOHallOfFame result = new TOHallOfFame(game.getName());

		//get most recent entry
		HallOfFameEntry mostRecent = game.getMostRecentEntry();
		int index = game.indexOfHallOfFameEntry(mostRecent);
		
	    
	    
		int start = index + (numberOfEntries/2);

		if(start>game.numberOfHallOfFameEntries()-1) {
			start=game.numberOfHallOfFameEntries()-1;
			
		}
		
		int end = start-numberOfEntries +1;
		
		if(end<0) {
			end=0;
		}
		
		start = start-1;
		end = end-1;
		
		while(start>=end) {
			//String userName = pgame.getPlayername();
			TOHallOfFameEntry to = new TOHallOfFameEntry(index+1, game.getHallOfFameEntry(index).getPlayername(), game.getHallOfFameEntry(index).getScore(), result);
			start--;
			index--;
		}
		
    	return result;
    }
    
    /*
     * `This method is for gameover popup
     */
    public static int getScore() {
   	Block223 block223 = Block223Application.getBlock223();
    	String gameName = PlayGameUI.gameName;
    	Game game = block223.findGame(gameName);
    	HallOfFameEntry HOF = game.getMostRecentEntry();
    	int score = HOF.getScore();
    	return score;
    }
    
    
  
    // **************************************************************************************** //
    // Modifier Methods   (iterations 2 and 3)                                                  //
    // **************************************************************************************** //
    
    /**
     * Feature 1: Add Game.
     * @author Christos
     * 
     * Adds a new game to the current block223 application with default settings.
     * Default settings:
     * 
     * @param name The name for the new game to be created.
     * @throws InvalidInputException Exception with message containing error to be displayed to user.
     */
	public static void createGame(String name) throws InvalidInputException {
	  Block223 block223 = Block223Application.getBlock223();
	  UserRole currentUserRole = Block223Application.getCurrentUserRole();
	  Admin admin;
      Game newGame;
	  // check if currentUserRole isA Admin
	  if (!(currentUserRole instanceof Admin)) {
	    throw new InvalidInputException("Admin privileges are required to create a game.");
	  } else {
	    // if current 
	    admin = (Admin) currentUserRole;
	  }
	  
	  Game testGame = block223.findGame(name);
	  // check for unique game name here because of umple bug
	  if (!(testGame == null)) {
		  //System.out.println("The name of a game must be unique.");
	    throw new InvalidInputException("The name of a game must be unique.");
	  }
	  // try to create new game with default settings
	  try {
	    // added: init with published = false
	    newGame = new Game(name,1,admin,1,1,1.0,10,10,block223);
	  } catch (RuntimeException e) {
	    throw new InvalidInputException(e.getMessage());
	  }
	  block223.addGame(newGame);
	}

	/**
	 * Feature 2: Define Game Settings
	 * @author Christos
	 * 
	 * First, create  a  game  with  createGame(...)  (see  "Add a  game") 
	 * or select a game with selectGame(...) (see "Update a game"). 
	 * 
	 * @param nrLevels Number of levels of game.
	 * @param nrBlocksPerLevel The number of blocks per level of the game.
	 * @param minBallSpeedX The minimum ball speed (x component).
	 * @param minBallSpeedY The minimum ball speed (y component).
	 * @param ballSpeedIncreaseFactor The speed increase factor of the Ball.
	 * @param maxPaddleLength The max length of the Paddle.
	 * @param minPaddleLength The max length of the Paddle.
	 * @throws InvalidInputException If any of the input are invalid.
	 */
	public static void setGameDetails(int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {
	  // throw exception if currentUserRole is not an admin
	  Admin admin;
	  Game currentGame = Block223Application.getCurrentGame();
	  int size = 0;
	  if (Block223Application.getCurrentUserRole() instanceof Admin) {
	    admin = (Admin) Block223Application.getCurrentUserRole();
	  } else {
	    throw new InvalidInputException("Admin privileges are required to define game settings.");
	  }
	  // check if currentGame is set
	  if (currentGame == null) {
	    throw new InvalidInputException("A game must be selected to define game settings.");
	  }
	  if(!admin.equals(currentGame.getAdmin())) {
	    throw new InvalidInputException("Only the admin who created the game can define its game settings.");
	  }
	  // nrLevels valid input check
	  if(nrLevels < 1 || nrLevels > 99) {
	    throw new InvalidInputException("The number of levels must be between 1 and 99.");
	  }
	  // if ball speed is zero
	  if (minBallSpeedX == 0 && minBallSpeedY == 0) {
	    throw new InvalidInputException("The minimum speed of the ball must be greater than zero.");   
	  }
	  // check rest of valid input checks in umple code
	  try {
	    currentGame.setNrBlocksPerLevel(nrBlocksPerLevel);
	    currentGame.getBall().setMinBallSpeedX(minBallSpeedX);
	    currentGame.getBall().setMinBallSpeedY(minBallSpeedY);
	    currentGame.getBall().setBallSpeedIncreaseFactor(ballSpeedIncreaseFactor);
	    currentGame.getPaddle().setMaxPaddleLength(maxPaddleLength);
	    currentGame.getPaddle().setMinPaddleLength(minPaddleLength);
	  } catch (RuntimeException e) {
	    throw new InvalidInputException(e.getMessage());
	  }
	  // set levels
	  size = currentGame.getLevels().size();
	  if (nrLevels > size) {
	    // add more levels to game
	    for (int i = size; i < nrLevels; i++) {
	      currentGame.addLevel();
	    }
	  } else {
	    // remove more levels
	    for (int i = size; i > nrLevels; i--) {

	      Level le = currentGame.getLevel(i-1);
	      le.delete();
          
	      //boolean s = currentGame.removeLevel(le);
	    }
	  }
	  // done setting game settings
	}

	/**
	 * Feature 3: Delete Game
	 * @param Lea Akkari
	 * @throws InvalidInputException
	 */
	public static void deleteGame(String name) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
		Admin admin;
		// must be admin to delte a game
		if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
		  throw new InvalidInputException("Admin privileges are required to delete a game.");
		} else {
		  admin = (Admin) Block223Application.getCurrentUserRole();
		}
		
		//find game with this name
		Game gameToDel = block223.findGame(name);
		if (gameToDel != null) {
			//check if game is published
			if(gameToDel.isPublished()) {
				throw new InvalidInputException("A published game cannot be deleted.");
			}
			else if (! (gameToDel.getAdmin().equals(admin))) {
    		  throw new InvalidInputException("Only the admin who created the game can delete the game.");
    		}
    		//else delete game
    		block223 = gameToDel.getBlock223();
    		gameToDel.delete();
            Block223Persistence.save(block223);
    	} else {
    	  // no game with that name, no need to delete, no need to throw error
    	}
	}

	/**
	 * Extra method for features update and define game settings.
	 * 
	 * @param name
	 * @throws InvalidInputException
	 */
	public static void selectGame(String name) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
	  Admin admin;
      Game game = block223.findGame(name);
      // must be ad Admin to select a game
	  if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
	    throw new InvalidInputException("Admin privileges are required to select a game.");
	  } else {
	    admin = (Admin) Block223Application.getCurrentUserRole();
	  }
	  // game must not be null
      if (game == null) {
        throw new InvalidInputException("A game with name " + name + " does not exist.");
      }
	  // must be Admin of specific game to select it
	  if (!game.getAdmin().equals(admin)) {
	    throw new InvalidInputException("Only the admin who created the game can select the game.");
	  }
	  if(game.isPublished()) {
		  throw new InvalidInputException("A published game cannot be changed.");
	  }
	  // all checks passed, set current game
	  Block223Application.setCurrentGame(game);
	}
	
    /**
     * Feature 4: Update Game Settings
     * @param Lea Akkari
     * @param nrLevels
     * @param nrBlocksPerLevel
     * @param minBallSpeedX
     * @param minBallSpeedY
     * @param ballSpeedIncreaseFactor
     * @param maxPaddleLength
     * @param minPaddleLength
     * @throws InvalidInputException
     */
	public static void updateGame(String name, int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
		Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {
		
		Block223 block223 = Block223Application.getBlock223();
		// exception checking
		Admin admin;
		// must be ad Admin to select a game
	      if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
	        throw new InvalidInputException("Admin privileges are required to define game settings.");
	      } else {
	        admin = (Admin) Block223Application.getCurrentUserRole();
	      }
		// check if current game exists
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
		  throw new InvalidInputException("A game must be selected to define game settings.");
		}
        // must be Admin of specific game to update it
        if (!game.getAdmin().equals(admin)) {
          throw new InvalidInputException("Only the admin who created the game can define its game settings.");
        }
        
        List<Game> games = block223.getGames();
        int flag = 0;
        for (Game g : games) {
          if (g.getName().equals(name) ) {
            // if game with name already exists
        	  flag ++; 
          }
        }
        
        if(game.getName().equals(name) && flag >1) {
        	throw new InvalidInputException("The name of a game must be unique.");
        }
        else if( !(game.getName().equals(name)) && flag >0) {
        	throw new InvalidInputException("The name of a game must be unique.");
        }
         flag = 0;
//        if(block223.findGame(name) != null) {
//        	throw new InvalidInputException("The name of a game must be unique.");
//        }
        // set game name and check if successful (returns false if game with that name already exists)
        try {
          game.setName(name); 
        } catch (RuntimeException e) {
          throw new InvalidInputException(e.getMessage());
        }
        
		setGameDetails(nrLevels, nrBlocksPerLevel,minBallSpeedX,minBallSpeedY,ballSpeedIncreaseFactor,maxPaddleLength, minPaddleLength);
		
		//save to persistence
		//Block223Persistence.save(block223);
	}

	 /**
     * Feature 5: Add Block.
     * @author Walid Chabchoub
     * 
     * Adds a new block to currently selected game on the Block223 Application
     * Default settings:
     * 
     * @param red Red RGB value for the block.
     * @param green Green RGB value for the block.
     * @param blue Blue RGB value for the block.
     * @param points Points value for the block.
     * @throws InvalidInputException Exception with message containing error to be displayed to user.
     */
	public static void addBlock(int red, int green, int blue, int points) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
		Game game = Block223Application.getCurrentGame();
		Admin admin;
		
		// Checking for Admin priviledges
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
		    throw new InvalidInputException("Admin privileges are required to add a block.");
		} else {
			admin = (Admin) Block223Application.getCurrentUserRole();
		}
	    // game must not be null
	    if (game == null) {
	    	throw new InvalidInputException("A game must be selected to add a block.");
	    }
		// must be Admin of specific game to select it
		if (!game.getAdmin().equals(admin)) {
			throw new InvalidInputException("Only the admin who created the game can add a block.");
		}
		
		// checking that block with same color doesn't exist already
		List<Block> blockList = game.getBlocks();
		for(Block block : blockList) {
			if(block.getRed() == red && block.getGreen() == green && block.getBlue() == blue) {
				throw new InvalidInputException("A block with the same color already exists for the game.");
			}
		}
		
		// then try to add the block and catch any runtime exceptions
		try {
			game.addBlock(red, green, blue, points);
		}
		catch(RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
			
	}
	/**
     * Feature 6: Delete Block.
     * @author Walid Chabchoub
     * 
     * Deletes a block from the currently selected game on the Block223 Application
     * Default settings:
     * 
     * @param id ID of the block to be deleted
     * @throws InvalidInputException Exception with message containing error to be displayed to user.
     */
	public static void deleteBlock(int id) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();
		Game game = Block223Application.getCurrentGame();
		Admin admin;
		
		// Checking for Admin priviledges
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
		    throw new InvalidInputException("Admin privileges are required to delete a block.");
		} else {
			admin = (Admin) Block223Application.getCurrentUserRole();
		}
	    // game must not be null
	    if (game == null) {
	    	throw new InvalidInputException("A game must be selected to delete a block.");
	    }
		// must be Admin of specific game to select it
		if (!game.getAdmin().equals(admin)) {
			throw new InvalidInputException("Only the admin who created the game can delete a block.");
		}
		// Fetching the block to delete by its id
		Block block = game.findBlock(id);
		// Checking that it exists
		if(block != null) {
			block.delete();
		}
	}


	/*
	 * Feature 7: Update a Block
	 * Trevor Tabah
	 * 
	 * Updates a block's info (points and RGB code
	 */
	public static void updateBlock(int id, int red, int green, int blue, int points) throws InvalidInputException {
		String error = "";
		// 1st check
		if ((Block223Application.getCurrentUserRole() instanceof Admin) == false) {
			throw new InvalidInputException("Admin privileges are required to update a block.");
		}
		Game game = Block223Application.getCurrentGame();
		// 2nd check
		if (game == null) {
			throw new InvalidInputException("A game must be selected to update a block.");
		}
		if (game.isPublished()) {
			throw new InvalidInputException("A published game cannot be changed.");
		}
		// 3rd checks
		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can update a block.");
		}
		// 4th check
		List<Block> blocksInCurrentGame = game.getBlocks();
		for (int i = 0; i < blocksInCurrentGame.size(); i++) {
			Block b = blocksInCurrentGame.get(i);
			if (b.getRed() == red && b.getGreen() == green && b.getBlue() == blue) {
				if(game.findBlock(id).equals(b)) { 
					// if block that has same color is the block that we are updating, than it is ok
				} else {
					throw new InvalidInputException("A block with the same color already exists for the game.");
				}
			}
		}
		// 5th check
		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}
		//6
		int oldRed = block.getRed();
		try {
			block.setRed(red);
		}
		catch(RuntimeException e){
			throw new InvalidInputException(e.getMessage());
		}
		//7
		int oldGreen = block.getGreen();
		try {
			block.setGreen(green);
		}
		catch(RuntimeException e){
			block.setRed(oldRed);
			throw new InvalidInputException(e.getMessage());
		}
		//8
		int oldBlue = block.getBlue();
		try {
			block.setBlue(blue);
		}
		catch(RuntimeException e){
			block.setRed(oldRed);
			block.setGreen(oldGreen);
			throw new InvalidInputException(e.getMessage());
		}
		//9
		try {
			block.setPoints(points);
		}

		catch(RuntimeException e){
			block.setRed(oldRed);
			block.setGreen(oldGreen);
			block.setBlue(oldBlue);
			throw new InvalidInputException(e.getMessage());
		}

	}

   /*
    * Trevor Tabah
    */
	public static void positionBlock(int id, int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() instanceof Admin) == false) {
			throw new InvalidInputException("Admin privileges are required to position a block.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
			throw new InvalidInputException("A game must be selected to position a block.");
		}
		if (game.isPublished()) {
			throw new InvalidInputException("A published game cannot be changed.");
		}
		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can position a block.");
		}

		Level level1;
		try {
			level1 = game.getLevel(level-1);
		}catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}
		
		int nrBlocksPerLevel = game.getNrBlocksPerLevel();
		if (level1.numberOfBlockAssignments() == nrBlocksPerLevel) {
			throw new InvalidInputException("The number of blocks has reached the maximum number ("+nrBlocksPerLevel+") allowed for this game.");
		}
		for (BlockAssignment blockA : level1.getBlockAssignments()) {
			if (blockA.getGridHorizontalPosition() == gridHorizontalPosition && blockA.getGridVerticalPosition() == gridVerticalPosition) {
				throw new InvalidInputException("A block already exists at location " + gridHorizontalPosition + "/" + gridVerticalPosition+ ".");
			}
		}
		Block block = game.findBlock(id);
		if (block == null) {
			throw new InvalidInputException("The block does not exist.");
		}
		BlockAssignment blockAssignment;
		try {
			blockAssignment = new BlockAssignment(gridHorizontalPosition,gridVerticalPosition, level1, block, game);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		level1.addBlockAssignment(blockAssignment);
	}

       /*
        * Feature 9 - Jeremy Chow
        */
	public static void moveBlock(int level, int oldGridHorizontalPosition, int oldGridVerticalPosition,
			int newGridHorizontalPosition, int newGridVerticalPosition) throws InvalidInputException {
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to move a block.");
		}

		Game game = Block223Application.getCurrentGame();
		if (game == null) {
		  throw new InvalidInputException("A game must be selected to move a block.");
		}
		if (game.isPublished()) {
			throw new InvalidInputException("A published game cannot be changed.");
		}
		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
		  throw new InvalidInputException("Only the admin who created the game can move a block.");
		}

		Level levelToEdit = null;
		// Check if level of block we are trying to move exists
		try {
		  levelToEdit = game.getLevel(level - 1);
		} catch(IndexOutOfBoundsException e){
		  throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}
		
		// try to get block assignment we are trying to move
		// and check validity of old and new move coordinates
		BlockAssignment assignment = levelToEdit.findBlockAssignment(oldGridHorizontalPosition, oldGridVerticalPosition);
		if (assignment == null) {
			throw new InvalidInputException("A block does not exist at location " + oldGridHorizontalPosition + "/"
                + oldGridVerticalPosition + ".");
		}
		// check if block already exists at new location
		if (levelToEdit.findBlockAssignment(newGridHorizontalPosition, newGridVerticalPosition) != null) {
		  throw new InvalidInputException("A block already exists at location " + newGridHorizontalPosition + "/"
              + newGridVerticalPosition + ".");
		}
		// try to set horz pos to new location
		try {
			assignment.setGridHorizontalPosition(newGridHorizontalPosition);
		} catch(RuntimeException e){
			throw new InvalidInputException(e.getMessage());
		}
		// try to set vert pos to new location
		try {
			assignment.setGridVerticalPosition(newGridVerticalPosition);
		} catch(RuntimeException e){
          throw new InvalidInputException(e.getMessage());
		}
	}
	

	public static void removeBlock(int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() instanceof Admin) == false) {
			throw new InvalidInputException("Admin privileges are required to remove a block.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
			throw new InvalidInputException("A game must be selected to remove a block.");
		}
		if (game.isPublished()) {
			throw new InvalidInputException("A published game cannot be changed.");
		}
		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can remove a block.");
		}
		Level levelToEdit = game.getLevel(level - 1);
		BlockAssignment assignment = levelToEdit.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition);
		if (assignment != null) {
			assignment.delete();
		}
		
		
		
	}

	/**
     * Feature 6: Save Game.
     * @author Mahdis Asaadi
     * 
     * Saves a game to Persistance.
     * 
     * @throws InvalidInputException Exception with message containing error to be displayed to user.
     */
	public static void saveGame() throws InvalidInputException {
	  Admin admin;
	  Game game;
	  // valid input checks
	  if (! (Block223Application.getCurrentUserRole() instanceof Admin)) {
	    throw new InvalidInputException("Admin privileges are required to save a game.");
	  } else {
	    admin = (Admin) Block223Application.getCurrentUserRole();
	  }
	  game = Block223Application.getCurrentGame();
	  if (game == null) {
	    throw new InvalidInputException("A game must be selected to save it.");
	  }
	  if (!game.getAdmin().equals(admin)) {
	    throw new InvalidInputException("Only the admin who created the game can save it.");
	  }
	  // save application, catch runtime exception and rethrow
		try {
			Block223Persistence.save(Block223Application.getBlock223());
		}
		catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	/**
     * Feature 7: Register.
     * @author Mahdis Asaadi
     * 
     * @param username Player's username
     * @param playerPassword Player's password
     * @param adminPassword Admin's password. Once user enters two passwords (player's and admin's) he becomes an Admin.
     * 
     * @throws InvalidInputException Exception with message containing error to be displayed to user.
     */
	public static void register(String username, String playerPassword, String adminPassword)
			throws InvalidInputException {
		
		Block223 block223=Block223Application.getBlock223();
	  // stupid little piece of code to set var to null if it is empty
//	  if (adminPassword != null) {
//	  if (adminPassword.isEmpty()) {
//	    adminPassword = null;
//	  }
//	  }
	  
	  
	  // load application first?
          //Block223Persistence.load();
      
				if (Block223Application.getCurrentUserRole() != null){
					throw new InvalidInputException("Cannot register a new user while a user is logged in.");
	
				}
				// only check if they are equal if they are both not null 
  				if (playerPassword != null && adminPassword != null) {
    				if (playerPassword.equals(adminPassword)) {
    					throw new InvalidInputException("The passwords have to be different.");
    				}
  				}
  				if(playerPassword==null || playerPassword=="") {
  					throw new InvalidInputException("The player password needs to be specified.");
  				}
  				
  				if(username==null || username=="") {
  					throw new InvalidInputException("The username must be specified.");
  				}
  				
  			
  				
  				if(adminPassword==null || adminPassword=="") {
  					adminPassword ="";
					Player player=new Player(playerPassword,block223);
					try {
					User user=new User(username,block223,player);
					}
					catch(RuntimeException e) {
//						if(User.getWithUsername(username)!=null) {
							player.delete();
							throw new InvalidInputException("The username has already been taken.");
						//}
						
					}
  				}
  		
				
				
				
				
				
					if (adminPassword != null && adminPassword  !="") {
						Admin admin = new Admin(adminPassword,block223);
						Player player=new Player(playerPassword,block223);
						try {
						User user=new User(username,block223,player);
						user.addRole(admin);
						}catch(RuntimeException e) {
							player.delete();
							admin.delete();
							e.getStackTrace();
						  //throw new InvalidInputException("The username has already been taken.");
							
						}
					
					}
				
				
				
				Block223Persistence.save(block223);
	} 
	

	/**
     * Feature 8: LogIn
     * @author Mahdis Asaadi
     * 
     * @param username User's username
     * @param password Player's password
     * 
     * @throws InvalidInputException Exception with message containing error to be displayed to user.
     */
	
	public static void login(String username, String password) throws InvalidInputException {
		if (Block223Application.getCurrentUserRole()!=null) {
			throw new InvalidInputException("Cannot login a user while a user is already logged in.");
		}
		
		
		Block223Application.resetBlock223();
	    Block223 block223 = Block223Application.getBlock223();
	    if(block223 != null) {
	    	block223.delete();
	    }
	    
	    Block223Application.setCurrentGame(null);
	    block223 = Block223Persistence.load();
	    
		
		
//		if (user==null) {
//			throw new InvalidInputException("The username and password do not match.");
//		}
	    User user;
		
		if(User.getWithUsername(username)==null) {
			throw new InvalidInputException("The username and password do not match.");
		}
		else {
			user = User.getWithUsername(username);
		}
		
		List<UserRole> roles = user.getRoles();
		
		
		for (UserRole role : roles) {
			String rolePassword=role.getPassword();
			
			if (rolePassword.equals(password)) {
				Block223Application.setCurrentUserRole(role);
			}
		}
		
		if (Block223Application.getCurrentUserRole()==null) {
			throw new InvalidInputException("The username and password do not match.");
		}
		
		
	}
	
	
//	public static TOUserMode getUserMode() {
//		UserRole role = Block223Application.getCurrentUserRole();
//		if(role == null) {
//			TOUserMode to = new TOUserMode(Mode.None);
//			return to;
//		}
//		if(role instanceof Player) {
//			TOUserMode to = new TOUserMode(Mode.Play);
//			return to;
//		}
//		
//		if(role instanceof Admin) {
//			TOUserMode to = new TOUserMode(Mode.Design);
//			return to;
//		}
//		return null;
//	}
	
	/**
     * Feature 9: LogOut
     * @author Mahdis Asaadi
     * 
     *
     */
	public static void logout() {
		Block223Application.setCurrentUserRole(null);		
	}
	
	
	// **************************************************************************************** //
	// Query methods   (pre iteration 4)                                                        //
	// **************************************************************************************** //
	
	
	/**
	 * What constitutes a 'designable' game?
	 */
	public static List<TOGame> getDesignableGames() throws InvalidInputException {
	  // if current user is not an admin, throw exception
	  if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
	    throw new InvalidInputException("Admin privileges are required to access game information.");
	  }
	  List<TOGame> currentGameTOList = new ArrayList<TOGame>();
	  List<Game> currentGameList = Block223Application.getBlock223().getGames();
	  for (int i = 0; i < currentGameList.size(); i++) {
	    // create new Game Transfer Object with data from current game list
		  if( (!currentGameList.get(i).isPublished()) && (currentGameList.get(i).getAdmin().equals(Block223Application.getCurrentUserRole()))
				    ) {
		    TOGame to = new TOGame(
		        currentGameList.get(i).getName(),
		        currentGameList.get(i).getLevels().size(),
		        currentGameList.get(i).getNrBlocksPerLevel(),
		        currentGameList.get(i).getBall().getMinBallSpeedX(),
		        currentGameList.get(i).getBall().getMinBallSpeedY(),
		        currentGameList.get(i).getBall().getBallSpeedIncreaseFactor(),
		        currentGameList.get(i).getPaddle().getMaxPaddleLength(),
		        currentGameList.get(i).getPaddle().getMinPaddleLength()
		        );
		    // add game to currentGameList of TO
		    currentGameTOList.add(to);
		  }
	  }
	  
	  return currentGameTOList;
	}
	
	/**
	 * 
	 */
	public static TOGame getCurrentDesignableGame() throws InvalidInputException {
		Admin admin;
		Game game;
		TOGame to;
		// if current user not instance of admin
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
		  throw new InvalidInputException("Admin privileges are required to access game information.");
		} else {
		  admin = (Admin) Block223Application.getCurrentUserRole();
		}
		admin = (Admin) Block223Application.getCurrentUserRole();
		// if current game is null, throw exception
		if(Block223Application.getCurrentGame() == null) {
		  throw new InvalidInputException("A game must be selected to access its information.");
		}
		game = Block223Application.getCurrentGame();
		// if current admin is not admin of selected game, throw exception
		if (!(game.getAdmin().equals(admin))) {
		  throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		// all checks passed, create new Transfer object and return it
	    to = new TOGame(
	        game.getName(),
	        game.getLevels().size(),
	        game.getNrBlocksPerLevel(),
	        game.getBall().getMinBallSpeedX(),
	        game.getBall().getMinBallSpeedY(),
	        game.getBall().getBallSpeedIncreaseFactor(),
	        game.getPaddle().getMaxPaddleLength(),
	        game.getPaddle().getMinPaddleLength()
	        );
	    return to;
	}

	public static List<TOBlock> getBlocksOfCurrentDesignableGame() throws InvalidInputException {
		Admin admin;
		Game game;
		List<TOBlock> result = new ArrayList<TOBlock>();
		
		// if current user not instance of admin
		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
		  throw new InvalidInputException("Admin privileges are required to access game information.");
		} else {
		  admin = (Admin) Block223Application.getCurrentUserRole();
		}
		// if current game is null, throw exception
		if(Block223Application.getCurrentGame() == null) {
		  throw new InvalidInputException("A game must be selected to access its information.");
		}
		game = Block223Application.getCurrentGame();
		// if current admin is not admin of selected game, throw exception
		if (!(game.getAdmin().equals(admin))) {
		  throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		
		List<Block> blockList = game.getBlocks();
		
		for(Block block : blockList) {
			TOBlock to = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints());
			result.add(to);
		}
		return result;
	  
	}

	
	public static TOBlock getBlockOfCurrentDesignableGame(int id) throws InvalidInputException {
	  Admin admin;
      Game game;
      Block block;
      TOBlock to;
      
      // if current user not instance of admin
      if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
        throw new InvalidInputException("Admin privileges are required to access game information.");
      } else {
        admin = (Admin) Block223Application.getCurrentUserRole();
      }
      // if current game is null, throw exception
      if(Block223Application.getCurrentGame() == null) {
        throw new InvalidInputException("A game must be selected to access its information.");
      }
      game = Block223Application.getCurrentGame();
      // if current admin is not admin of selected game, throw exception
      if (!(game.getAdmin().equals(admin))) {
        throw new InvalidInputException("Only the admin who created the game can access its information.");
      }
	  block = game.findBlock(id);
	  if (block == null) {
	    throw new InvalidInputException("The block does not exist.");
	  }
      to = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints());
      return to;
	}
		
		
	
	public static List<TOGridCell> getBlocksAtLevelOfCurrentDesignableGame(int level) throws InvalidInputException {
       
		if ((Block223Application.getCurrentUserRole() instanceof Admin) == false) {
		  throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		Game game = Block223Application.getCurrentGame();
		if (game == null) {
		  throw new InvalidInputException("A game must be selected to access its information.");
		}
		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
		  throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		
		List<TOGridCell> result = new ArrayList<TOGridCell>();
		Level level1 = null;
		try {
		  level1 = game.getLevel(level-1);
		}
		catch (RuntimeException e) {
			throw new InvalidInputException ("Level "+ level + " does not exist for the game.");
		}
		
		List<BlockAssignment> assignments = level1.getBlockAssignments();
		int index = 0;
		for(BlockAssignment assignment : assignments) {
			TOGridCell to = new TOGridCell(assignment.getGridHorizontalPosition(),assignment.getGridVerticalPosition(), assignment.getBlock().getId(), assignment.getBlock().getRed(), assignment.getBlock().getGreen(), assignment.getBlock().getBlue(), assignment.getBlock().getPoints());
			result.add(index, to);
		}
		return result;
	  
	}

	public static TOUserMode getUserMode() {
		UserRole userRole = Block223Application.getCurrentUserRole();
		
		if (userRole==null) {
			return  new TOUserMode(Mode.None);	
		}
		else if (userRole instanceof Player) {
			return new TOUserMode(Mode.Play);
		}
		else {
			return new TOUserMode(Mode.Design);
		}
		
	}

}