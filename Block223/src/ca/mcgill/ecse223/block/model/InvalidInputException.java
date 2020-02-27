/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.block.model;
import java.lang.Exception;

// line 278 "../../../../../Block223 v2.ump"
public class InvalidInputException extends Exception
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //InvalidInputException Attributes
  private String errorMsg;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public InvalidInputException(String aErrorMsg)
  {
    super();
    errorMsg = aErrorMsg;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setErrorMsg(String aErrorMsg)
  {
    boolean wasSet = false;
    errorMsg = aErrorMsg;
    wasSet = true;
    return wasSet;
  }

  public String getErrorMsg()
  {
    return errorMsg;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "errorMsg" + ":" + getErrorMsg()+ "]";
  }
}