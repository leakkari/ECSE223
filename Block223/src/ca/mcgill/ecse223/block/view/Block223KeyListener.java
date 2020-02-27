package ca.mcgill.ecse223.block.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Block223KeyListener implements KeyListener {

  /** Temp storage for user input inbetween game loops. */
  private volatile String _keyString;
  
  //****************************
  // Key Listeners
  //****************************

  @Override
  public synchronized void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    // if its 'l', 'r' or 'space'
    if (code == KeyEvent.VK_LEFT) {
       _keyString += "l";
    } else if (code == KeyEvent.VK_RIGHT) {
        _keyString += "r";
    } else if (code == KeyEvent.VK_SPACE) {
        _keyString += " ";
    } else {
        // if any key is pressed, clear error message
        //_errorMsg = "";
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub
  }
  
  
  /**
   * Takes key inputs and clears the input string. marked as synchronized to make
   * the key inputs as thread-safe
   */
  public synchronized String takeInputs() {
      String passString = _keyString;
      _keyString = "";
      return passString;
  }
  
  
  
}
