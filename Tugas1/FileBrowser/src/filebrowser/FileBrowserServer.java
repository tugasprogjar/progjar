/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filebrowser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Ilham
 */
public class FileBrowserServer {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        byte[] data=new byte[10];
        ServerSocket ss=new ServerSocket(1001);
        Socket server= ss.accept();
        InputStream is=server.getInputStream();
        OutputStream os=server.getOutputStream();
        
        
    }
    
}
