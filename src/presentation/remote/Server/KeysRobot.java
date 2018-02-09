/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.remote.Server;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrse
 */
public class KeysRobot{
    public final int[] AVAILABLE_KEYS = {
        KeyEvent.VK_UP,
        KeyEvent.VK_DOWN,
        KeyEvent.VK_LEFT,
        KeyEvent.VK_RIGHT,
        
        KeyEvent.VK_PAGE_UP,
        KeyEvent.VK_PAGE_DOWN,
        
        KeyEvent.VK_ENTER,
        
        KeyEvent.VK_HOME,
        KeyEvent.VK_END
    };
    
    public final HashMap<String, Integer> AVAILABLE_KEYS_MAP = new HashMap<>();
    
    private static Robot robot;

    public KeysRobot() {
        try {
            robot = new Robot();
            
            AVAILABLE_KEYS_MAP.put("/up",    KeyEvent.VK_UP);
            AVAILABLE_KEYS_MAP.put("/down",  KeyEvent.VK_DOWN);
            AVAILABLE_KEYS_MAP.put("/left",  KeyEvent.VK_LEFT);
            AVAILABLE_KEYS_MAP.put("/right", KeyEvent.VK_RIGHT);
            AVAILABLE_KEYS_MAP.put("/pup",   KeyEvent.VK_PAGE_UP);
            AVAILABLE_KEYS_MAP.put("/pdown", KeyEvent.VK_PAGE_DOWN);
            AVAILABLE_KEYS_MAP.put("/enter", KeyEvent.VK_ENTER);
            AVAILABLE_KEYS_MAP.put("/home",  KeyEvent.VK_HOME);
            AVAILABLE_KEYS_MAP.put("/end",   KeyEvent.VK_END);
        } catch (AWTException ex) {
            Logger.getLogger(KeysRobot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean checkKey(int key){
        if( Arrays.asList(AVAILABLE_KEYS).contains(key) ){
            sendKey(key);
            return true;
        }
        return false;
    }
    
    public boolean checkKey(String key){
        if( AVAILABLE_KEYS_MAP.containsKey(key) ){
            sendKey( AVAILABLE_KEYS_MAP.get(key) );
            return true;
        }
        return false;
    }
    
    private void sendKey(int key){
        robot.keyPress(key);
        robot.keyRelease(key);
    }
}
