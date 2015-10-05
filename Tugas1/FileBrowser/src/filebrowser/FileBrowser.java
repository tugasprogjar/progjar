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
            byte[] data=new byte[10];
            String out;
            int len;
            Scanner rd=new Scanner(System.in);
            Socket client=new Socket("localhost",6666);
            input = client.getInputStream();
            OutputStream output=client.getOutputStream();
            while(true){
                out=rd.nextLine();
                if("exit".equals(out)){break;}
                else
                if("mkdir     ".equals(out)){
                  output.write(out.getBytes());
                  output.flush();
                  out=rd.nextLine();
                  output.write(out.getBytes());
                  output.flush();
                }
                else
                output.write(out.getBytes());
                output.flush();
                
                while(true){
                    data=new byte[1024];
                    len=input.read(data);
                    
                    if(len==-1){
                        break;
                    }
                    System.out.println(new String(data)+"/n");
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

