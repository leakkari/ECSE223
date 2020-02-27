package ca.mcgill.ecse223.block.view;

import ca.mcgill.ecse223.block.controller.TOHallOfFameEntry;

public interface Block223PlayModeInterface {
	
	/**
	 * returns  all  inputs  received  from  the  user  since  the  last  call  to  takeInputs() 
	 * and removes all of these inputs from the queue
	 * possible input are: l for left and r for right and spacebar
	 * @return
	 */
	public String takeInputs();
	
	/**
	 * redisplays the game in progress
	 */
	public void refresh();

	/**
	 * Ends the current game and updates the HoF
	 * 
	 * @param nrOfLives
	 * @param hof
	 */
    public void endGame(int nrOfLives, TOHallOfFameEntry hof);

}
