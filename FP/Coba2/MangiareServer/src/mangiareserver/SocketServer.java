package mangiareserver;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerThread extends Thread { 
	
    public SocketServer server = null;
    public Socket socket = null;
    public int ID = -1;
    public String username = "";
    public ObjectInputStream streamIn  =  null;
    public ObjectOutputStream streamOut = null;
    public ServerFrame ui;

    public ServerThread(SocketServer _server, Socket _socket){  
    	super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
        ui = _server.ui;
    }
    
    public void send(Message msg){
        try {
            streamOut.writeObject(msg);
            streamOut.flush();
        } 
        catch (IOException ex) {
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
    
    public int getID(){  
	    return ID;
    }
   
    @SuppressWarnings("deprecation")
	public void run(){  
    	ui.jTextArea1.append("\nServer Thread " + ID + " running.");
        while (true){  
    	    try{  
                Message msg = (Message) streamIn.readObject();
    	    	server.handle(ID, msg);
            }
            catch(Exception ioe){  
            	System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }
    
    public void open() throws IOException {  
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }
    
    public void close() throws IOException {  
    	if (socket != null)    socket.close();
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}

public class SocketServer implements Runnable {
    
    public ServerThread clients[];
    public ServerSocket server = null;
    public Thread       thread = null;
    public int clientCount = 0, port = 13000;
    public ServerFrame ui;
   // public Database db;

    public SocketServer(ServerFrame frame) throws SQLException, ClassNotFoundException{
       
        clients = new ServerThread[50];
        ui = frame;
     //   db = new Database(ui.dbcon);
        
	try{  
	    server = new ServerSocket(port);
            port = server.getLocalPort();
	    ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
	    start(); 
        }
	catch(IOException ioe){  
            ui.jTextArea1.append("Can not bind to port : " + port + "\nRetrying"); 
            ui.RetryStart(0);
	}
    }
    
    public SocketServer(ServerFrame frame, int Port) throws ClassNotFoundException{
       
        clients = new ServerThread[50];
        ui = frame;
        port = Port;
       // db = new Database(ui.dbcon);
        
	try{  
	    server = new ServerSocket(port);
            port = server.getLocalPort();
	    ui.jTextArea1.append("Server startet. IP : " + InetAddress.getLocalHost() + ", Port : " + server.getLocalPort());
	    start(); 
        }
	catch(IOException ioe){  
            ui.jTextArea1.append("\nCan not bind to port " + port + ": " + ioe.getMessage()); 
	}
    }
	
    public void run(){  
	while (thread != null){  
            try{  
		ui.jTextArea1.append("\nWaiting for a client ..."); 
	        addThread(server.accept()); 
	    }
	    catch(Exception ioe){ 
                ui.jTextArea1.append("\nServer accept error: \n");
                try {
                    ui.RetryStart(0);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
                }
	    }
        }
    }
	
    public void start(){  
    	if (thread == null){  
            thread = new Thread(this); 
	    thread.start();
	}
    }
    
    @SuppressWarnings("deprecation")
    public void stop(){  
        if (thread != null){  
            thread.stop(); 
	    thread = null;
	}
    }
    
    private int findClient(int ID){  
    	for (int i = 0; i < clientCount; i++){
        	if (clients[i].getID() == ID){
                    return i;
                }
	}
	return -1;
    }
    
	
    public synchronized void handle(int ID, Message msg){  
        Random r=new Random();
        int range=101;
	if (msg.content.equals(".bye")){
            AnnounceMessage("signout", "SERVER", msg.sender);
            remove(ID); 
	}
	else{
            if(msg.type.equals("baru")){
              if(clientCount==0){
                  sebarMakanan();
                  Player b = new Player(msg.content,r.nextInt(range),r.nextInt(range),10,Color.blue,10);
                  blobs.add(b);
                  Announce("update", msg.sender, blobs,dots);
              }
              else{
                  Player b = new Player(msg.content,r.nextInt(range),r.nextInt(range),10,Color.blue,10);
                  blobs.add(b);
                  Announce("update", msg.sender, blobs,dots);
              }
            }
            else if(msg.type.equals("message")){
              
                    AnnounceMessage("message", msg.sender, msg.content);
                
            }
            else if(msg.type.equals("update")){
                    this.blobs=msg.blob;
                    this.dots=msg.food;
                    Announce("update", msg.sender, blobs,dots);
                
            }
            else if(msg.type.equals("updatemakan")){
                    this.blobs=msg.blob;
                    this.dots=msg.food;
                    tambahMakanan();
                    Announce("update", msg.sender, blobs,dots);
                
            }
            else if(msg.type.equals("test")){
                clients[findClient(ID)].send(new Message("test", "SERVERs", "OK", msg.sender,null,null));
            }
            
          
          
	}
    }
    
    public void Announce(String type, String sender,ArrayList<Player> blobs,ArrayList<Food> food ){
        Message msg = new Message(type, sender, null, "All",blobs,food);
        for(int i = 0; i < clientCount; i++){
            clients[i].send(msg);
        }
    }
    
    public void AnnounceMessage(String type, String sender, String content){
        Message msg = new Message(type, sender, content, "All",null,null);
        for(int i = 0; i < clientCount; i++){
            clients[i].send(msg);
        }
    }
    
    public void SendUserList(String toWhom){
        for(int i = 0; i < clientCount; i++){
            findUserThread(toWhom).send(new Message("newuser", "SERVER", clients[i].username, toWhom,null,null));
        }
    }
    
    public ServerThread findUserThread(String usr){
        for(int i = 0; i < clientCount; i++){
            if(clients[i].username.equals(usr)){
                return clients[i];
            }
        }
        return null;
    }
	
    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID){  
    int pos = findClient(ID);
        if (pos >= 0){  
            ServerThread toTerminate = clients[pos];
            ui.jTextArea1.append("\nRemoving client thread " + ID + " at " + pos);
	    if (pos < clientCount-1){
                for (int i = pos+1; i < clientCount; i++){
                    clients[i-1] = clients[i];
	        }
	    }
	    clientCount--;
	    try{  
	      	toTerminate.close(); 
	    }
	    catch(IOException ioe){  
	      	ui.jTextArea1.append("\nError closing thread: " + ioe); 
	    }
	    toTerminate.stop(); 
	}
    }
    
    private void addThread(Socket socket){  
	if (clientCount < clients.length){  
            ui.jTextArea1.append("\nClient accepted: " + socket);
	    clients[clientCount] = new ServerThread(this, socket);
	    try{  
	      	clients[clientCount].open(); 
	        clients[clientCount].start();  
	        clientCount++; 
	    }
	    catch(IOException ioe){  
	      	ui.jTextArea1.append("\nError opening thread: " + ioe); 
	    } 
	}
	else{
            ui.jTextArea1.append("\nClient refused: maximum " + clients.length + " reached.");
	}
    }
    //game
    ArrayList<Player> blobs = new ArrayList<Player>();
    public ArrayList<Food> dots = new ArrayList<Food>();
    //Player b = new Player(10,10,10,Color.blue,10);
    int mouseX = 0;
    int mouseY = 0;
    static int score =10;
    static int xDis = 0;
    static int yDis = 0;
    private int FrameX=1366;
    private int FrameY=768;
    int cek;
    
    public void sebarMakanan(){
        int i;
        Random r = new Random();
        try {
            for(i=0;i<51;i++){
                int randX = r.nextInt(FrameX-10);
                int randY = r.nextInt(FrameY-10);
                Food d = new Food(randX,randY);
                synchronized(dots){
                    dots.add(d);
                }
                
            }
           
        } catch (Exception e) {
            System.out.println("gagal sebar makanan");
        }
    }
    public void tambahMakanan(){
        Random r = new Random();
        try {
            int randX = r.nextInt(FrameX);
            int randY = r.nextInt(FrameY);
            Food d = new Food(randX,randY);
            synchronized(dots){
                dots.add(d);
            }
            //mf.add(d);
           // mf.repaint();
           // System.out.println(score);
            
        } catch (Exception e) {
             System.out.println("gagal tambah makanan");
        }
    }
   
}
    
