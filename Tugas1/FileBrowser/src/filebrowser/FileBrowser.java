/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filebrowser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class FileBrowser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InputStream input=null;
        try {
            // TODO code application logic here
            byte[] data;//=new byte[10];
            String out;
            int len=0;
            int panjang;
            Scanner rd=new Scanner(System.in);
            Socket client=new Socket("localhost",6666);
            input = client.getInputStream();
            OutputStream output=client.getOutputStream();
            OUTER:
            while (true) {
                out=rd.nextLine();
                panjang=out.length();
                output.write(panjang);
                switch (out) {
                    case "exit":
                        break OUTER;
                    case "mkdir":
                        output.write(out.getBytes());
                        output.flush();
                        out=rd.nextLine();
                        panjang=out.length();
                        output.write(panjang);
                        output.write(out.getBytes());
                        output.flush();
                        break;
                    case "cd":
                        output.write(out.getBytes());
                        output.flush();
                        out=rd.nextLine();
                        //panjang=out.length();
                        //output.write(panjang);
                        output.write(out.length());
                        output.write(out.getBytes());
                        output.flush();
                        break;
                //panjang=input.read();
                //data=new byte[panjang];
                    case "ls":                
                        output.write(out.getBytes());
                        output.flush();
                        break;
                    default:
                        break OUTER;
                }
                while(true){
                    panjang=input.read();
                    //System.out.println(panjang);
                    if(panjang==255){
                        break;
                    }
                    data=new byte[panjang];
                    len=input.read(data); 
                    
                    System.out.println(new String(data));
                    
                  
                    
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
           }
    
}

