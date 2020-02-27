package ca.mcgill.ecse223.block.persistence;

import ca.mcgill.ecse223.block.persistence.PersistenceObjectStream;
import ca.mcgill.ecse223.block.model.Block223;

public class Block223Persistence {
	
	private static String Persistancefilename = "block-data.ser";

	public static void save(Block223 block) {
		PersistenceObjectStream.serialize(block);
	}
	
	public static Block223 load() {
		PersistenceObjectStream.setFilename(Persistancefilename);
		
		Block223 block=null;
		try{
			 block = (Block223) PersistenceObjectStream.deserialize();

		} catch (Exception e) {
			block = new Block223();
			System.out.println("ERORR - new block223 created because of serialization excption");
		}
		// model cannot be loaded - create empty block
		if (block == null) {
			block = new Block223();
		} else {
		  block.reinitialize();
		}
		// add reinitialize if hashmaps added in future
		return block;
	}
	
	public static void setFilename(String newFilename) {
		Persistancefilename = newFilename;
	}
	
	public static String getFilename() {
	  return Persistancefilename;
	}

}
