/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.remote.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
        
        private void copyNircmd() throws FileNotFoundException, IOException{
            InputStream is = getClass().getResource("/presentation/remote/Server/Executables/nircmdc.exe").openStream();
            OutputStream os = new FileOutputStream("nircmdc.exe");

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        }
        
        @Override
        public void mute() {
            try {
                copyNircmd();
                Runtime.getRuntime().exec("nircmdc.exe mutesysvolume 1");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void unmute() {
            try {
                copyNircmd();
                Runtime.getRuntime().exec("nircmdc.exe mutesysvolume 0");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void volumeIncrease() {
            try {
                copyNircmd();
                Runtime.getRuntime().exec("nircmdc.exe changesysvolume 6553");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void volumeDecrease() {
            try {
                copyNircmd();
                Runtime.getRuntime().exec("nircmdc.exe changesysvolume -6553");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        public void volumeIncrease() {
            try {
                Runtime.getRuntime().exec("amixer -D pulse sset Master 10%+");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void volumeDecrease() {
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
        public void volumeIncrease() {
            try {
                Runtime.getRuntime().exec("osascript -e 'set volume output volume ((output volume of (get volume settings)) + 10)'");
            } catch (IOException ex) {
                Logger.getLogger(VolumeRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void volumeDecrease() {
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
        public void volumeIncrease() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void volumeDecrease() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
