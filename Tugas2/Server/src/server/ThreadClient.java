/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadClient implements Runnable{
    private Socket socket;
    int i;
    public ThreadClient(Socket socket,int i){
    this.socket=socket;
    this.i=i;
    }
    public void run(){
        System.out.println("Client "+i+" is here");
        try {
            // TODO code application logic here
            byte[] data;//new byte[10];
            //byte[] data1=new byte[10];
          
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            String curdir="c:/";
            
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
                        //System.out.println(f1.getName());
                        int slength=f1.getName().length();
                        os.write(slength);
                        data=new byte[slength];
                        data=f1.getName().getBytes();
                        os.write(data);
                        os.flush();
                        
                    }
                    os.write(255);
                   // int ln=-1;
                   //os.write(ln);
                   //System.out.println("selesai");
                   
  
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
                    int i;
                    i = "Sudah pindah ke directory:".length();
                    os.write(i);
                    os.write("Sudah pindah ke directory:".getBytes());
                    panjang=is.read();
                    data=new byte[panjang];
                    is.read(data);
                    curdir = new String(data,"UTF-8");
                    os.write(curdir.length());
                    os.write(curdir.getBytes());
                    //System.out.println(curdir);
                    //curdir = namaFldr;
                    file=new File(curdir);
                    
                    os.write(255);
                }else
                    if("upload".equals(cl)){
                        panjang=is.read();
                    data=new byte[panjang];
                    is.read(data);
                    String fl = new String(data,"UTF-8");
                    File source=new File(fl);
                    
                    panjang=is.read();
                    data=new byte[panjang];
                    is.read(data);
                    String ds = new String(data,"UTF-8");
                    File destination=new File(ds);
                        if (destination.isDirectory()){
                        destination = new File(destination, source.getName());
                        FileInputStream input = new FileInputStream(source);
                        copyFile(input, destination);
                        }
                        
                    }
                
                
            }
            os.close();
            is.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Thread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void copyFile(InputStream input, File destination) throws FileNotFoundException, IOException {
    OutputStream output = null;

    output = new FileOutputStream(destination);

    byte[] buffer = new byte[1024];

    int bytesRead = input.read(buffer);

    while (bytesRead >= 0) {
      output.write(buffer, 0, bytesRead);
      bytesRead = input.read(buffer);
    }

    input.close();

    output.close();
  }
    }
