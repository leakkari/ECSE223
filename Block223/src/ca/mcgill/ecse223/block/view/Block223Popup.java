package ca.mcgill.ecse223.block.view;

import javax.swing.JFrame;

/**
 * TODO: better coding practice would be to make this
 * class into a abstract popup UI class or interface and have
 * all our custom popup ui's extend/implement this. But thats also
 * a lot of work that is not really necessary.
 * 
 * All JInternalFrames that represent a popup window for a setting
 * must implement this interface. When a menu item is selected in
 * the main Block223 window, it will create a new instance of a 
 * Block223Popup (which also extend JInternalFrame).
 * 
 * 
 * might just use constructors, this might not be needed
 */
public abstract class Block223Popup extends JFrame {
  
  public static Block223Popup createPopup() {
    return null;
  }
  
  
}