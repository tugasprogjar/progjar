/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangiareclient;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author baskoro
 */
public class ThreadRead implements Runnable {
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream ous;
    private boolean done;
    
    public ThreadRead(Socket socket) throws IOException {
        this.socket = socket;
        this.ois = new ObjectInputStream(this.socket.getInputStream());
        this.ous = new ObjectOutputStream(this.socket.getOutputStream());
        this.done = false;
    }
    
    public void send(Message message) throws IOException {
        this.ous.writeObject(message);
        this.ous.flush();
        this.ous.reset();
    }

    public void stop() {
        this.done = true;
    }
    
    @Override
    public void run() {
        try {
            while(!this.done) {
                try {
                    Object incoming = this.ois.readObject();
                    if(incoming instanceof Message) {
                        Message msg = (Message) incoming;
                        System.out.print(msg.getSender() + " : " + msg.getContent());
                    }
                    
                } catch (IOException|ClassNotFoundException ex) {
                    this.done = true;
                    Logger.getLogger(ThreadRead.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            System.out.println("Bye");
            this.ois.close();
            this.ous.close();
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ThreadRead.class.getName()).log(Level.SEVERE, null, ex); 
        }
    }
}
