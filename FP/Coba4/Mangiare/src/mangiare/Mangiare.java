/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangiare;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;
import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import chatservergui.ChatServerGUI;
import guiclient.GUIChatClient;

public class Mangiare extends javax.swing.JFrame{
    ArrayList<Player> blobs = new ArrayList<Player>();
    public ArrayList<Food> dots = new ArrayList<Food>();
    
    Player b = new Player(10,10,10,Color.RED,10, "client");
    int mouseX = 0;
    int mouseY = 0;
    static int score =10;
    static int xDis = 0;
    static int yDis = 0;
    private int FrameX=1366;
    private int FrameY=768;
    int cek;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    Socket socket = new Socket();
    private String username, server;
    private java.util.List<String> clients;
    JTextArea chatArea = new JTextArea(8,20);
    JTextField username1 = new JTextField(40);
    JButton connBtn = new JButton("Connect");
    JTextField inputField = new JTextField(40);
    JButton sendBtn = new JButton("Send");
    MyFrame mf = new MyFrame("Mangiare");
            
    
    public static void main(String[] args){
    
    new Mangiare().startGame();
    
    }
    public void startGame(){
     chat chat1= new chat();
     chat1.setVisible(true);
    
        mf.addMouseMotionListener(new MyMouseMoveListener());
        mf.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we)
            {
                System.exit(0);
             }
        });
        Refresh rf = new Refresh();
        Thread t = new Thread(rf);
        t.start();
        sebarMakanan();
        while(true){
            try{
                Random r = new Random();
                Thread.sleep(r.nextInt(50));
                double dis = Math.sqrt(xDis*xDis + yDis*yDis);
                 //KECEPATAN
                double easingAmount=0;
                if(b.size<180){
                    easingAmount = 180/b.size;
                }else{
                    easingAmount = 1.0;
                }
                 
                if(dis > 1){
                    double targetX= (easingAmount*xDis/dis);
                    double targetY= (easingAmount*yDis/dis);
                    if(targetX>=1 || targetX<=-1 ){
                        if(targetX>=1 && b.x<FrameX-20){
                             b.x += targetX;
                        }else if(targetX<=-1&&b.x>10){
                            b.x += targetX;
                        }
                       
                     }else{
                        if(targetX<0){
                            if(b.x>0)
                            b.x += -1;
                        }else{
                            if(b.x<FrameX)
                            b.x += 1;
                        }
                    }
                    if(targetY>=1 || targetY<=-1 ){
                        if(targetY>=1&&b.y<FrameY-50){
                             b.y += targetY;
                        }else if(targetY<=-1&&b.y>30){
                            b.y += targetY;
                        }
                     }else{
                        if(targetY<0){
                            if(b.y>0)
                            b.y += -1;
                        }else{
                            if(b.y<FrameY)
                            b.y += 1;
                        }
                    }
                      mf.repaint();
                }
                if(cek == 1){
                    tambahMakanan();
                    cek=0;
                    System.out.println(score);
                }
               
               
                
            }catch(Exception e){

            }
            
        }
    }
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
                mf.add(d);
               // mf.repaint();
                //System.out.println(score);
            }
        } catch (Exception e) {
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
            mf.add(d);
           // mf.repaint();
           // System.out.println(score);
            
        } catch (Exception e) {
        }
    }
    class Refresh implements Runnable{

        public void run() {
            while(true){
                Random ran = new Random();
               /* try{
                    Thread.sleep(ran.nextInt(20));
                }catch(Exception e){
                    System.out.println("error");
                }*/
                Rectangle r = new Rectangle(b.x,b.y,b.size,b.size);
                synchronized(dots){
                    Iterator i = dots.iterator();
                    while(i.hasNext()){
                        Food d = (Food) i.next();
                        Rectangle r1 = new Rectangle(d.x,d.y,12,12);
                        if(r1.intersects(r)){
                            i.remove();
                            cek=1;
                            b.size += 1;
                            score += 1;
                            b.nilai +=1;
                            
                        }
                    }
                }
                //mf.repaint();
            }

        }
    }
    class MyMouseMoveListener extends MouseMotionAdapter{
        public void mouseMoved(MouseEvent m){
            mouseX = m.getX();
            mouseY = m.getY();
            xDis = mouseX-b.x;
            yDis = mouseY-b.y;
        }
    }
   
    class MyFrame extends Frame{
        Panel pKirim=new Panel();
        Panel pKetik=new Panel();
        Panel pTextArea = new Panel();
        Button tombolKirim=new Button("Kirim");
        TextField areaKetik=new TextField("Meet the boss");
        TextArea viewTA = new TextArea();
        GridBagLayout gbl=new GridBagLayout();
        GridBagConstraints gbc=new GridBagConstraints();
     
        MyFrame(String s){
            super(s);
            //JTextField username1 = new JTextField(40);
            //JButton connBtn = new JButton("Connect");
            /*JPanel user = new JPanel();
            user.setLayout(new BoxLayout(user, BoxLayout.LINE_AXIS));
            user.add(username1);
            user.add(connBtn);

            JPanel Chat = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            Chat.add(new JLabel("Name:"));
            
            //JTextArea chatArea = new JTextArea(8,20);
            chatArea.setEditable(false);
            chatArea.setFocusable(false);
            JScrollPane chatScroll = new JScrollPane(chatArea);
            JPanel chatPanel = new JPanel(new BorderLayout());
            chatPanel.add(new JLabel("Chat:", SwingConstants.LEFT), BorderLayout.PAGE_START);
            chatPanel.add(chatScroll);

            //JTextField inputField = new JTextField(40);
            //JButton sendBtn = new JButton("Send");
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
            inputPanel.add(inputField);
            inputPanel.add(sendBtn);

            JPanel PostChat = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            PostChat.add(new JLabel("You:"));

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
            mainPanel.add(Chat);
            mainPanel.add(user);
            mainPanel.add(Box.createVerticalStrut(5));
            mainPanel.add(chatPanel);
            mainPanel.add(Box.createVerticalStrut(5));
            mainPanel.add(PostChat);
            mainPanel.add(inputPanel);
            //mainPanel.setBackground(Color.red);
            //mainPanel.setVisible(true);
        
            JFrame frame = new JFrame("Chat");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(mainPanel);
            frame.pack();
            frame.setAlwaysOnTop(true);
            */
            //frame.setLocationRelativeTo(null);
            //frame.setVisible(true);
        /*pTextArea.add(viewTA);
        setLayout(gbl);
        gbc.gridx=1;
        gbc.gridy=0;
        add(pTextArea,gbc);
        
        pKetik.add(areaKetik);
        setLayout(gbl);
        gbc.anchor = GridBagConstraints.PAGE_END; //bottom of space
        gbc.insets = new Insets(10,0,0,0);  //top padding
        gbc.gridx=0;
        gbc.gridy=1;
        add(pKetik, gbc);
        
        pKirim.add(tombolKirim);
        setLayout(gbl);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx=2;
        gbc.gridy=1;
         add(pKirim,gbc);*/
        /*gbc.gridx=10;
        gbc.gridy=0;
        pAps.setBackground(Color.lightGray);*/
        
        //setSize(300,300);
        setVisible(true);
    
        setBackground(Color.DARK_GRAY);
        setBounds(0,0,FrameX,FrameY);
        add(b);
        blobs.add(b);
        setVisible(true);
        }
       
        public void paint(Graphics g){
            for(Player b : blobs)
                b.paint(g);
            synchronized(dots){
                Iterator i = dots.iterator();
                while(i.hasNext()){
                    Food d = (Food) i.next();
                   d.paint(g);
                }
            }
        }
    }
}