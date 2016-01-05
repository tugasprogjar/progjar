/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangiare;

/**
 *
 * @author sai
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;

public class Player extends Component{
    public int x;
    public int y;
    public int size;
    public int nilai;
    public Color color;
    public String name;
    //public static Player blob = new Player(800,100,40,Color.cyan);
    Player(int x, int y, int size, Color c, int nilai, String name){
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = c;
        this.nilai = nilai;
        this.name = name;
    }

    public void paint(Graphics g){
        g.setColor(color);
        g.drawOval(x-size/2, y-size/2, size, size);
        //g.drawOval(x, y, size, size);
        FontMetrics fm = g.getFontMetrics();
        double textW = fm.getStringBounds(name, g).getWidth();
        int textH = fm.getMaxAscent();
        g.setColor(Color.white);
        g.drawString(name, (int)(x-textW/2), (int)(y+textH/4));
        g.setColor(Color.YELLOW);
        g.drawString(String.valueOf(nilai), (int)(x-textW), (int)(y+textH));
    }
}