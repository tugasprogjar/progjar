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
import java.awt.Graphics;
import java.util.Random;

public class Player extends Component{
    public int x;
    public int y;
    public int size;
    public int nilai;
    public Color color;
    //public static Player blob = new Player(800,100,40,Color.cyan);
    Player(int x, int y, int size, Color c, int nilai){
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = c;
        this.nilai = nilai;
    }

    public void paint(Graphics g){
        g.setColor(color);
        g.drawOval(x, y, size, size);
        g.drawString(String.valueOf(nilai), x, y);
            
    }
}