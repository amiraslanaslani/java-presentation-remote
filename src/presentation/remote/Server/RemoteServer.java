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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
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
        
        System.out.println("IP list of my system:");
        for(String ip : getIPsList()){
            if(ip.contains("%")){
                ip = ip.split("%")[0];
            }
            System.out.println("\t" + ip);
        }
        
        System.out.println("\nServer is running on port " + this.port + " . . .");
    }
    
    private ArrayList<String> getIPsList() throws SocketException{
        ArrayList<String> ips = new ArrayList<>();

        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements())
            {
                InetAddress i = (InetAddress) ee.nextElement();
                ips.add(i.getHostAddress());
            }
        }

        return ips;
    }
    
    static class Router implements HttpHandler{
        private static final KeysRobot keysRobot = new KeysRobot();
        private static final VolumeRobot volumeRobot = new VolumeRobot();

        private final String screenImageFormat;
        private final int width,height;
        
        public Router(){
            this(
                    Capture.IMAGE_FORMAT_GIF,
                    280,
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
                    case "/screen/base64": // Show screen stream
                        Capture capturebase = new Capture(this.width, this.height);
                        String base64 = capturebase.getBase64(screenImageFormat);
                        response = base64.getBytes();
                        responseLength = base64.length();
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
