/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ServerSocket ss=new ServerSocket(1234);
            System.out.println("Server is on");
            int i=1;
            while(true){
                Socket socket=ss.accept();
                ThreadClient c=new ThreadClient(socket,i);
                Thread t=new Thread(c);
                t.start();
                i++;
                
                
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
