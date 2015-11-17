/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utsserver;

import chatmessage.AnotherMessage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author baskoro
 */
public class ThreadClient implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream ous;
    private ChatServer server;
    private boolean done;
    private SocketAddress sa;
    private String username;
    private Random random;
    
    public ThreadClient(Socket socket, ChatServer server) throws IOException {
        this.socket = socket;
        this.sa = this.socket.getRemoteSocketAddress();
        this.ous = new ObjectOutputStream(this.socket.getOutputStream());
        this.ois = new ObjectInputStream(this.socket.getInputStream());
        this.server = server;
        this.done = false;
    }
    
    public void send(Object message) throws IOException {
        if(this.ous != null) {
            this.ous.writeObject(message);
            this.ous.flush();
            this.ous.reset();
        }
    }
    
    public void stop() {
        try {
            this.done = true;
            this.server.removeClient(this);
            this.server.writeLog(this.sa.toString(), "Client disconnected....");
        } catch (IOException ex) {
            Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void parse(Object incoming) throws IOException {
        this.server.sendToAll(incoming);    
    }
    
    public void run() {
        try {
            this.server.writeLog(this.socket.getRemoteSocketAddress().toString(), "New client....");
            Object incoming;
            
            AnotherMessage am = new AnotherMessage();
            am.setPesan("Selamat datang...\n");
            this.send(am);
            
            while(!this.done) {
                incoming = this.ois.readObject();       //membaca pesan
                this.parse(incoming);                   //panggil fungsi parse
            }
            
            this.ois.close();
            this.ous.close();
            this.socket.close();
        } catch (IOException|ClassNotFoundException ex) {
            try {
                this.server.writeLog(this.socket.getRemoteSocketAddress().toString(), ex.getMessage());
            } catch (IOException ex1) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
