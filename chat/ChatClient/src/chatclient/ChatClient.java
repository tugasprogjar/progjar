/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import chatmessage.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author baskoro
 */
public class ChatClient {

    /**
     * @param name
     */
    public static void main(String name) {
        try {
            Socket socket = new Socket("localhost", 6666);
            ThreadRead tr = new ThreadRead(socket);
            Thread t = new Thread(tr);
            t.start();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Username : " + name + "\n");
                        
            while(true) {
                String msg = br.readLine();
                if(msg.equalsIgnoreCase("end")) {
                    break;
                }
                
                Message objMsg = new Message();
                objMsg.setMessage(msg);
                //objMsg.setSender(username);
                tr.send(objMsg);
            }
            
            tr.stop();
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}