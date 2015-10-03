/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filebrowser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ilham
 */
public class FileBrowser {
    private JFrame frame;
    private JTextField txtfield;
  
public static void main(String[] args) {
        // TODO code application logic here
        EventQueue.invokeLater (new Runnable(){
        public void run(){        
        FileBrowser window=new FileBrowser();
        window.frame.setVisible(true);
}
}
);
    
    
}

public FileBrowser(){
    initialize();
}

private void initialize(){
    frame=new JFrame("Pencarian");
    frame.setBounds(200,100,500,400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().setLayout(null);
    
    txtfield= new JTextField();
    txtfield.setBounds(10,100,450,20);
    frame.getContentPane().add(txtfield);
    txtfield.setColumns(10);
    
    JButton tombol=new JButton("Tampilkan Direktori");
    tombol.setBounds(150,125,200, 40);
    frame.getContentPane().add(tombol);
    
    tombol.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
            JFileChooser pencari=new JFileChooser();
            //pencari.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);    //untuk direktori saja
            //pencari.setFileSelectionMode(JFileChooser.FILES_ONLY);          //untuk file saja
            pencari.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            pencari.setAcceptAllFileFilterUsed(false);
            
            int app=pencari.showOpenDialog(null);
            if(app==JFileChooser.APPROVE_OPTION){
             txtfield.setText(pencari.getSelectedFiles().toString());
             
            }
        }
        
    });
}
}