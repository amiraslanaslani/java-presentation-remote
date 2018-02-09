/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.remote.Server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mrse
 */
public class VolumeRobot {
    private final VolumeController controller;
    private boolean isVolumeControllSupported = true;
    
    public VolumeRobot() {
        if (OSValidator.isWindows()) {
            this.controller = new WindowsVolumeController();
        } else if (OSValidator.isMac()) {
            this.controller = new MacVolumeController();
        } else if (OSValidator.isUnix()) {
            this.controller = new UnixVolumeController();
        } else {
//        if (OSValidator.isSolaris()) {
//            this.controller = new SolarisVolumeController();
//        } else {
            this.controller = new FakeVolumeController();
            isVolumeControllSupported = false;
            System.out.println("Your OS is not support vollume controll!!");
        }
    }
    
    public boolean isVolumeControllSupported(){
        return this.isVolumeControllSupported;
    }
    
    public VolumeController getVolumeController(){
        return this.controller;
    }
    
    static class OSValidator{
        private static String OS = System.getProperty("os.name").toLowerCase();

        public static boolean isWindows() {
            return (OS.contains("win"));
        }

        public static boolean isMac() {
            return (OS.contains("mac"));
        }

        public static boolean isUnix() {
            return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0 );
        }

        public static boolean isSolaris() {
            return (OS.contains("sunos"));
        }
    }
    
    private class WindowsVolumeController implements VolumeController{

        @Override
        public void mute() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void unmute() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void vollumeIncrease() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void vollumeDecrease() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
    
    private class UnixVolumeController implements VolumeController{

        @Override
        public void mute() {
            try {
                Runtime.getRuntime().exec("amixer -D pulse sset Master mute");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void unmute() {
            try {
                Runtime.getRuntime().exec("amixer -D pulse sset Master unmute");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void vollumeIncrease() {
            try {
                Runtime.getRuntime().exec("amixer -D pulse sset Master 10%+");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void vollumeDecrease() {
            try {
                Runtime.getRuntime().exec("amixer -D pulse sset Master 10%-");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    private class MacVolumeController implements VolumeController{

        @Override
        public void mute() {
            try {
                Runtime.getRuntime().exec("osascript -e 'set volume output muted true'");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void unmute() {
            try {
                Runtime.getRuntime().exec("osascript -e 'set volume output muted false'");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void vollumeIncrease() {
            try {
                Runtime.getRuntime().exec("osascript -e 'set volume output volume ((output volume of (get volume settings)) + 10)'");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void vollumeDecrease() {
            try {
                Runtime.getRuntime().exec("osascript -e 'set volume output volume ((output volume of (get volume settings)) - 10)'");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    private class FakeVolumeController implements VolumeController{

        @Override
        public void mute() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void unmute() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void vollumeIncrease() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void vollumeDecrease() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
