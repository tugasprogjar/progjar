/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import perseon.Person;

/**
 *
 * @author sai
 */
public class PersonServer {
    private static Object socket;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        try { // TODO code application logic here
            ServerSocket ss = new ServerSocket(5666);
            Socket s = ss.accept(); 
            ObjectOutputStream ous = new ObjectOutputStream(s.getOutputStream());
            Person person = new Person();
            person.setNama("Joni");
            person.setUmur(22);
            
            ous.writeObject(person);
            ous.flush();
            
            ous.close();
        }
         catch(IOException ex)    {
             Logger.getLogger(PersonServer.class.getName()).log(Level.SEVERE, null, ex);
    }
  }  
}