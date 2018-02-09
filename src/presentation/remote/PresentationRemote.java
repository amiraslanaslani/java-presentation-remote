/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.remote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import presentation.remote.Server.RemoteServer;

/**
 *
 * @author mrse
 */
public class PresentationRemote {
    private final Options options = new Options();
    private final HelpFormatter help = new HelpFormatter();
    private final CommandLineParser parser = new DefaultParser();
    
    private int port = 8888;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PresentationRemote presentationRemote = new PresentationRemote();
        presentationRemote.parse(args);
    }
    
    public PresentationRemote(){
        options.addOption("p", "port", true, "Port of server");
        options.addOption("h", "help", false, "Show help/usage");
        
        help.setDescPadding(5);
        help.setLeftPadding(2);
    }
    
    public void parse(String[] args){
        try {
            CommandLine cmd = parser.parse(options, args);
            
            if(cmd.hasOption("h")){
                this.showHelpMenu();
                return;
            }
            
            if(cmd.hasOption("p"))
                this.port = Integer.valueOf(cmd.getOptionValue("p"));
            
            RemoteServer server = new RemoteServer(port);
            
        } catch (ParseException | IOException ex) {
            Logger.getLogger(PresentationRemote.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Show help/usage in standard output stream.
     */
    private void showHelpMenu(){
        String helpText = "java -jar PresentationRemote.jar [options]\n"
                        + "\n"
                        + "Options:";
        help.printHelp(helpText, options);
    }
}
