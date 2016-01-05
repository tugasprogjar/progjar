package mangiareclient;


//import com.ui.*;
import java.io.*;
import java.net.*;

public class SocketClient implements Runnable{
    
    public int port;
    public String serverAddr;
    public Socket socket;
    public ObjectInputStream In;
    public ObjectOutputStream Out;
    //public History hist;
    
    public SocketClient(String Addr, int port) throws IOException{
        this.serverAddr = Addr; 
        this.port = port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
            
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());
        
    }

    @Override
    public void run() {
        boolean keepRunning = true;
        while(keepRunning){
            try {
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                
                if(msg.getType().equals("test")){
                    
                }
                else if(msg.getType().equals("login")){
                   
                }
                else if(msg.getType().equals("signup")){
                   
                }
              
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void send(Message msg){
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : "+msg.toString());
            

        } 
        catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }
    
    public void closeThread(Thread t){
        t = null;
    }
}
