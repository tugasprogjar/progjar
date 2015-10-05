/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filebrowserserver;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.util.Arrays;
/**
 *
 * @author Ilham
 */
public class FileBrowserServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            // TODO code application logic here
            byte[] data=new byte[10];
            ServerSocket ss=new ServerSocket(6666);
            System.out.println("Online");
            Socket server= ss.accept();
            System.out.println("Connected");
            InputStream is=server.getInputStream();
            OutputStream os=server.getOutputStream();
            String curdir="c:/";
            File file=new File(curdir);


            File[] list=file.listFiles();
            while(true){
                is.read(data);
                String cl=new String(data,"UTF-8");
                System.out.println(cl);
                if("ls        ".equals(cl)){
                    for(File f1 : list){
                        System.out.println(f1.getName());
                        int slength=f1.getName().length();
                        data=new byte[slength];
                        data=f1.getName().getBytes();
                        os.write(data);
                        os.flush();
                    }
                }
                else
                if("mkdir     ".equals(cl)){
                    is.read(data);
                    String namafolder=new String(data,"UTF-8");
                    new File(curdir+namafolder).mkdir();
                    os.write("Direktori baru sudah dibuat".getBytes());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileBrowserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
