/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package personclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import perseon.Person;

/**
 *
 * @author sai
 */
public class PersonClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try{
            Socket s = new Socket("localhost", 5666);
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            Person p = (Person) ois.readObject();
            
            System.out.println("Nama : " + p.getNama() + "\nUmur : " + p.getUmur());
        }
        catch (IOException | ClassNotFoundException ex){
            Logger.getLogger(PersonClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}