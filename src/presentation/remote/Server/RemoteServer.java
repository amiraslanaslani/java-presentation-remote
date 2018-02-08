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
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author mrse
 */
public class RemoteServer {
    private final int port;
    public RemoteServer(int port) throws IOException{
        this.port = port;
        HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
        server.setExecutor(Executors.newCachedThreadPool());
        
        server.createContext("/", new Router());
        
        server.start();
        System.out.println("Server is running...");
    }
    
    static class Router implements HttpHandler{

        @Override
        public void handle(HttpExchange ex) throws IOException {
            try {
                
                String response = "",
                        contentType = "text/html";
                int responseStatus = HttpURLConnection.HTTP_OK;
                System.out.println(ex.getRequestURI().getRawPath());
                
                
                switch(ex.getRequestURI().getRawPath()){
                    case "/": // Show Main Page
                        response = getFileStringInUIDirectory("Main.html");
                        break;
                    case "/screen": // Show screen stream

                        break;
                    case "/prlib.js": // Show PRLib.js
                        response = getFileStringInUIDirectory("PRLib.js");
                        contentType = "application/javascript";
                        break;
                    default:

                }
                
                ex.getResponseHeaders().put("Content-Type", Arrays.asList(contentType));
                ex.sendResponseHeaders(200, response.length());
                OutputStream os = ex.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (URISyntaxException ex1) {
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
