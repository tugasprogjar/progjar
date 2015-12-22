/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangiare;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Mangiare {
    ArrayList<Blob> blobs = new ArrayList<Blob>();
    public ArrayList<Dot> dots = new ArrayList<Dot>();
    Blob b = new Blob(10,10,10,Color.blue,10);
    int mouseX = 0;
    int mouseY = 0;
    static int score =10;
    static int xDis = 0;
    static int yDis = 0;
    int te;
    
    MyFrame mf = new MyFrame("Agario");
    
    public static void main(String[] args){
        new Mangiare().startGame();
    }
    public void startGame(){
        mf.addMouseMotionListener(new MyMouseMoveListener());
        Refresh rf = new Refresh();
        Thread t = new Thread(rf);
        t.start();
        sebar();
        while(true){
            try{
                Random r = new Random();
                Thread.sleep(r.nextInt(50));
                double dis = Math.sqrt(xDis*xDis + yDis*yDis);
                double easingAmount = 180/b.size;   //KECEPATAN
                if(dis > 1){
                    b.x += easingAmount*xDis/dis;
                    b.y += easingAmount*yDis/dis;    
                }
                if(te == 1){
                    tambahMakanan();
                    te=0;
                    System.out.println(score);
                }
                mf.repaint();
                
            }catch(Exception e){

            }
            
        }
    }
    public void sebar(){
        int i;
        Random r = new Random();
        try {
            for(i=0;i<51;i++){
                int randX = r.nextInt(900);
                int randY = r.nextInt(600);
                Dot d = new Dot(randX,randY);
                synchronized(dots){
                    dots.add(d);
                }
                mf.add(d);
                mf.repaint();
                //System.out.println(score);
            }
        } catch (Exception e) {
        }
    }
    public void tambahMakanan(){
        Random r = new Random();
        try {
            int randX = r.nextInt(700);
            int randY = r.nextInt(400);
            Dot d = new Dot(randX,randY);
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
                        Dot d = (Dot) i.next();
                        Rectangle r1 = new Rectangle(d.x,d.y,12,12);
                        if(r1.intersects(r)){
                            i.remove();
                            te=1;
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
        MyFrame(String s){
            super(s);
            setBounds(0,0,900,600);
            add(b);
            blobs.add(b);
            setVisible(true);
        }
        public void paint(Graphics g){
            for(Blob b : blobs)
                b.paint(g);
            synchronized(dots){
                Iterator i = dots.iterator();
                while(i.hasNext()){
                    Dot d = (Dot) i.next();
                   d.paint(g);
                }
            }
        }
    }
}