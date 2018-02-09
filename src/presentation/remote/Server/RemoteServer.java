/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation.remote.Server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.sun.net.httpserver.HttpHandler;
import java.awt.AWTException;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import screenshot.capture.Capture;

/**
 *
 * @author mrse
 */
public class RemoteServer {
    private final int port;
    
    public RemoteServer(int port) throws IOException{
        this.port = port;
        this.run();
    }
    
    private void run() throws IOException{
        HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        
        server.createContext("/", new Router());
        
        server.start();
        System.out.println("Server is running...");
    }
    
    static class Router implements HttpHandler{
        private static final KeysRobot keysRobot = new KeysRobot();
        private static final VolumeRobot volumeRobot = new VolumeRobot();

        private final String screenImageFormat;
        private final int width,height;
        
        public Router(){
            this(
                    Capture.IMAGE_FORMAT_GIF,
                    700,
                    -1
            );
        }
        
        public Router(String format,int width, int height){
            this.screenImageFormat = format;
            this.width = width;
            this.height = height;
        }

        @Override
        public void handle(HttpExchange ex) throws IOException {
            try {
                byte[] response = ("").getBytes();
                String contentType = "text/html";
                int responseStatus = HttpURLConnection.HTTP_OK,
                    responseLength = 0;
                String file;
                
                String loadedPath = ex.getRequestURI().getRawPath();
                switch(loadedPath){
                    case "/": // Show Main Page
                        file = getFileStringInUIDirectory("Main.html");
                        responseLength = file.length();
                        response = file.getBytes();
                        break;
                    case "/screen": // Show screen stream
                        Capture capture = new Capture(this.width, this.height);
                        response = capture.getBytes(screenImageFormat).toByteArray();
                        responseLength = response.length;
                        contentType = "image/" + screenImageFormat;
                        break;
                    case "/prlib.js": // Show PRLib.js
                        file = getFileStringInUIDirectory("PRLib.js");
                        responseLength = file.length();
                        response = file.getBytes();
                        contentType = "application/javascript";
                        break;
                    case "/mute":
                        volumeRobot.getVolumeController().mute();
                        break;
                    case "/unmute":
                        volumeRobot.getVolumeController().unmute();
                        break;
                    case "/volup":
                        volumeRobot.getVolumeController().volumeIncrease();
                        break;
                    case "/voldown":
                        volumeRobot.getVolumeController().volumeDecrease();
                        break;
                    default:
                        if(! keysRobot.checkKey(loadedPath)){ // Check to key send
                            responseStatus = HttpURLConnection.HTTP_NOT_FOUND;
                        }
                }
                
                ex.getResponseHeaders().put("Content-Type", Arrays.asList(contentType));
                ex.sendResponseHeaders(responseStatus, responseLength);
                OutputStream os = ex.getResponseBody();
                os.write(response);
                os.close();
            } catch (URISyntaxException | AWTException ex1) {
                Logger.getLogger(RemoteServer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        public String getFileStringInUIDirectory(String fileName) throws URISyntaxException, IOException{
            String UIPath = "/presentation/remote/Server/UI/";
            String content = FileUtils.readFileToString(
                    new File(getClass().getResource(UIPath + fileName).toURI()),
                    Charsets.UTF_8
            );
            return content;
        }
    }
}
