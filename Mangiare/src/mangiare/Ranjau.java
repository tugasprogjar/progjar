/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangiare;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Random;

/**
 *
 * @author ahmad
 */
public class Ranjau extends Component{
    public int x;
    public int y;
    public Color c;
    
    Ranjau(int x, int y){
        this.x=x;
        this.y=y;
        Random rand = new Random();
        int r = rand.nextInt(255);
        int green = rand.nextInt(255);
        int b = rand.nextInt(255);
        this.c = new Color(r, green, b);
    }
    @Override
    public void paint(Graphics g){
        g.setColor(c);
        g.fillOval(this.x, this.y, 10, 10);
        
    }
    
}
