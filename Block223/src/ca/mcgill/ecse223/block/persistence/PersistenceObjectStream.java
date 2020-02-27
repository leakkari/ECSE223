package ca.mcgill.ecse223.block.persistence;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import ca.mcgill.ecse223.block.model.Block223;

public class PersistenceObjectStream {
	
	private static String filename;

	public static void serialize(Object object) {
	  // check if file exists, if not create it
	  File file = new File(filename);
	  if(file.exists()){
	     // then we are good, do nothing
	  } else {
	    // if it doesn't exist, try to create
	     try {
	       file.createNewFile();
	     } catch (IOException e) {
	       e.printStackTrace();
	     }
	  }
	  // try to load from file
		try (
	          // try-with-resource statement
		    FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
		) {
			out.writeObject(object);
		} catch (Exception e) {
			throw new RuntimeException("Could not save data to file '" + filename + "'.");
		}

	}

	/**
	 * Deserializes the object located in file filename. Returns null if file is empty.
	 * @return An object that has been deserialized from a file. must case to what you want it to be.
	 */
	public static Object deserialize() {
	// check if file exists, if not create it
      File file = new File(filename);
      if(file.exists()){
         // if file exists, we are ok, do nothing
      } else {
        // if file does not exist, try to create new file
         try {
           file.createNewFile();
         } catch (IOException e) {
           //e.printStackTrace();
         }
      }  
        // try to load from file
		Object o = null;
		try (
		    // try-with-resource statement
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
		) {
		 	try {
        o = in.readObject();
      } catch (IOException e) {
        e.printStackTrace();
      }
		 
		  
		} catch (EOFException e1) {
		  //e1.printStackTrace();
		  // if we get EOFException, that means that file is empty, so we write null object to file
		  // and return null
		} catch (ClassNotFoundException e2) {
		  //e2.printStackTrace(); // print stack trace for debugging
		  throw new RuntimeException("Could not load data from file '" + filename + "'.");
        } catch (IOException e3) {
      e3.printStackTrace();
    } 
		
		if (o == null) {
		  //System.out.println("Object is null at end of deserialize method, nothing loaded");
		}
		// return block223 object
		return o;
	}
	
	/**
	 * Setter for static filename variable of this class.
	 * 
	 * @param newFilename
	 */
	public static void setFilename(String newFilename) {
		filename = newFilename;
	}
	
}
