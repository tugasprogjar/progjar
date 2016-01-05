/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservergui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/**
 *
 * @author cancer
 */
public class ChatServerGUI {

   
    private ArrayList<ChatServerGUI.ClientThread> clients;
    //private List<String> clients;
    private int port;
    private boolean keepGoing;
    
    public ChatServerGUI() {
      
        clients = new ArrayList<>();
    }
    
    public void start() {
        keepGoing = true;
        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            while (keepGoing) {
                System.out.println("ChatServer waiting for Clients on port " + port + ".");
                Socket socket = serverSocket.accept();
                if (!keepGoing) {
                    break;
                }
                ChatServerGUI.ClientThread t = new ChatServerGUI.ClientThread(socket);
                clients.add(t);
                t.start();
                send("login~" + t.username + "~" + t.username + " sedang login...~Server~\n");
            }
            try {
                serverSocket.close();
                for (int i = 0; i < clients.size(); ++i) {
                    ChatServerGUI.ClientThread tc = clients.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception closing the server and clients: " + e);
            }
        } catch (IOException e) {
            String msg = "Exception on new ServerSocket: " + e + "\n";
            System.out.println(msg);
        }
    }
    
    private synchronized void send(String message) {
        System.out.println(message);
        for (int i = clients.size(); --i >= 0;) {
            ChatServerGUI.ClientThread ct = clients.get(i);
            if (!ct.writeMsg(message)) {
                clients.remove(i);
                System.out.println("Disconnected Client " + ct.username + " removed from list.");
            }
        }
    }

    private String getClients() {
        String s = "";
        for (ClientThread clientThread : clients) {
            s += clientThread.username + ":";
        }
        s += "---";
        System.out.println(s);
        return s;
    }

    private synchronized void remove(int id) {
        for (int i = 0; i < clients.size(); ++i) {
            ChatServerGUI.ClientThread ct = clients.get(i);
            if (ct.id == id) {
                clients.remove(i);
                return;
            }
        }
    }

    public static void main(String[] args) {
        ChatServerGUI server = new ChatServerGUI();
        server.start();
    }
    
      private class ClientThread extends Thread {

        private Socket socket;
        private ObjectInputStream sInput;
        private ObjectOutputStream sOutput;
        private int id;
        private String username;

        public ClientThread(Socket socket) {
            
            this.socket = socket;
            System.out.println("Menciptakan IO Streams");
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                String message = (String) sInput.readObject();
                username = message.split("~")[1];
                System.out.println(username + " masuk.");
            } catch (IOException e) {
                System.out.println("Exception IO Streams: " + e);
            } catch (ClassNotFoundException e) {
            }
        }

        @Override
        public void run() {
            boolean keepGoing = true;
            while (keepGoing) {

                String message;
                try {
                    message = sInput.readObject().toString();
                } catch (IOException e) {
                    System.out.println(username + " Exception reading Streams: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                    break;
                }

                String type = message.split("~")[0];
                String pengirim = message.split("~")[1];
                String text = message.split("~")[2];
                String kepada = message.split("~")[3];
                String response;

                switch (type) {
                    case "postText":
                        response = "recieveText~" + pengirim + "~" + text + "~" + kepada + "~\n";
                        send(response);
                        break;
               
                    case "login":
                        response = "login~" + pengirim + "~" + text + "~" + kepada + "~\n";
                        send(response);
                        break;
                    case "logout":
                        response = "logout~" + pengirim + "~" + text + "~" + kepada + "~\n";
                        send(response);
                        break;
                    case "list":
                        response = "list~server~" + getClients() + "~ ~ ~ ~ ~\n";
                        send(response);
                        break;
                }
            }

            remove(id);
            close();
        }

        private void close() {
            try {
                if (sOutput != null) {
                    sOutput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (sInput != null) {
                    sInput.close();
                }
            } catch (Exception e) {
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {
            }
        }

        private boolean writeMsg(String msg) {
            if (!socket.isConnected()) {
                close();
                return false;
            }
            try {
                sOutput.writeObject(msg);
            } catch (IOException e) {
                System.out.println("Error sending message to " + username);
                System.out.println(e.toString());
            }
            return true;
        }
    }
    
   /* 
    /**
     * @param args the command line arguments
     
    public static void main(String[] args) {
        // TODO code application logic here
    }
    */
}
