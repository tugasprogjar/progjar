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
//import java.util.Arrays;
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
            byte[] data;//new byte[10];
            //byte[] data1=new byte[10];
            ServerSocket ss=new ServerSocket(6666);
            System.out.println("Online");
            Socket server= ss.accept();
            System.out.println("Connected");
            InputStream is=server.getInputStream();
            OutputStream os=server.getOutputStream();
            String curdir="e:/";
            
            File file=new File(curdir);

            String cl = null;
          //  File[] list=file.listFiles();
            while(!"exit".equals(cl)){
               // File[] list=file.listFiles();
                int panjang=is.read();
                data=new byte[panjang];
                is.read(data);
                cl=new String(data,"UTF-8");
                //data=null; 
                //data=new byte[10];
                System.out.println(cl);
                if("ls".equals(cl)){
                    File[] list=file.listFiles();
                    for(File f1 : list){
                        System.out.println(f1.getName());
                        int slength=f1.getName().length();
                        os.write(slength);
                        data=new byte[slength];
                        data=f1.getName().getBytes();
                        os.write(data);
                        os.flush();
                        
                    }
                    
                    int ln=-1;
                   os.write(ln);
                   
  
                }
                else
                if("mkdir".equals(cl)){
                   // data=null;
                    //panjang=is.read();
                    //System.out.
                    panjang=is.read();
                    data=new byte[panjang];
                    is.read(data);
                    String namafolder=new String(data,"UTF-8");
                    System.out.println(namafolder);
                    File mk=new File(curdir+namafolder);
                    mk.mkdir();
                    String s="Direktori baru sudah dibuat";
                    os.write(s.length());
                    os.write(s.getBytes());
                    os.write(255);
                    
                }
                 else
                if("cd".equals(cl)){
                    //data=new byte[100];
                    panjang=is.read();
                    data=new byte[panjang];
                    is.read(data);
                    curdir = new String(data,"UTF-8");
                    System.out.println(curdir);
                    //curdir = namaFldr;
                    file=new File(curdir);
                    int i;
                    i = "Sudah pindah directory".length();
                    os.write(i);
                    os.write("Sudah pindah directory".getBytes());
                    os.write(255);
                }
                
            }
            os.close();
            is.close();
            ss.close();
        } catch (IOException ex) {
            Logger.getLogger(FileBrowserServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
