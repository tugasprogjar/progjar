/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utsserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author baskoro
 */
public class ChatServer {

    private ArrayList<ThreadClient> alClients;
    private File logFile;
    private BufferedWriter bwLogFile;
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            server.startServer();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ChatServer() throws FileNotFoundException {
        this.logFile = new File("utsserver.log");
        this.bwLogFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.logFile)));
    }
    
    public synchronized void writeLog(String client, String message) throws IOException {
        String log = "[" + (client == null ? "LOG":client) + "] " + message + "\n";
        System.out.print(log);
        this.bwLogFile.write(log);
        this.bwLogFile.flush();
    }
    
    public void startServer() {
        try {
            this.alClients = new ArrayList<>();
            ServerSocket servsock = new ServerSocket(6666);
            this.writeLog(null, "Server mau berjalan ....");
            //servsock.setSoTimeout(10000);
            while(true) {
                Socket socket;
                socket = servsock.accept();
                ThreadClient tc = new ThreadClient(socket, this);
                synchronized(this.alClients) {      //mutex, synchronize untuk memproteksi, bisa juga di set dalam getter dan setter variable yang dibuat
                    this.alClients.add(tc);
                }
                Thread t = new Thread(tc);
                t.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void removeClient(ThreadClient tc) {     //
        synchronized(this.alClients) {
            this.alClients.remove(tc);
        }
    }
    
    public void sendToAll(Object message) throws IOException {
        for (ThreadClient tc : this.alClients) {
            tc.send(message);
        }
    }
}
