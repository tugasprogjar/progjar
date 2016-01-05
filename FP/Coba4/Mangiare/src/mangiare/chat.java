/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mangiare;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author cancer
 */
public class chat extends javax.swing.JFrame {

    /**
     * Creates new form GUIChatClient
     */
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket socket;
    private String server, username;
    private List<String> clients;
    public chat() {
        initComponents();
        clients = new ArrayList<>();
    }

    
    public boolean start() {
        try {
            socket = new Socket("localhost", 6666);
            
        } catch (Exception ec) {
            System.out.println("Error connectiong to server:" + ec);
            return false;
        }
 
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        System.out.println(msg);
 
        try {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException eIO) {
            System.out.println("Exception IO Streams: " + eIO);
            return false;
        }
 
        new chat.ListenFromServer().start();
 
        try {
            output.writeObject("login~" + username + "~" + username + " sedang login...~server~\n");
            output.writeObject("list~" + username + "~" + username + " sedang login...~server~\n");
 
        } catch (IOException eIO) {
            System.out.println("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
 
        return true;
    }
 
    private void disconnect() {
        try {
            // TODO add your handling code here:
            output.writeObject("logout~" + username + "\n");
        } catch (IOException ex) {
            //Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        try {
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
        }
        try {
            if (output != null) {
                output.close();
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
    
    
     class ListenFromServer extends Thread {
 
        @Override
        public void run() {
            while (true) {
                try {
                    String msg = (String) input.readObject();
                    String res;
                    String type = msg.split("~")[0];
                    String pengirim = msg.split("~")[1];
                    String text = msg.split("~")[2];
                    String kepada = msg.split("~")[3];
                    switch (type) {
                        case "recieveText":
                            if(pengirim.equals(username)){
                                res = "You" + ": " + text;
                            }else{
                                res = pengirim + ": " + text;
                            }
                            
                            viewTextArea.setText(viewTextArea.getText() + res + "\n");
                            break;
                       
                        case "login":
                            viewTextArea.setText(viewTextArea.getText() +"\n" + pengirim + " sudah login..." + "\n");
                            clients.add(pengirim);
                            break;
                        case "logout":
                            viewTextArea.setText(viewTextArea.getText() + pengirim + " telah logout..." + "\n");
                            clients.remove(pengirim);
                            break;
                        case "list":
                            setTable(text);
                            break;
                    }
                } catch (IOException e) {
                    System.out.println("Server has close the connection: " + e);
                    break;
                } catch (ClassNotFoundException e2) {
                }
            }
        }
 
        private void setTable(String text) {
            int rows = text.split(":").length - 1;
            Object[][] data = new Object[rows][1];
            for (int i = 0; i < rows; i++) {
                String t = text.split(":")[i];
                data[i][0] = t;
            }
 
            String[] header = {"Clients"};
 
           // clientTable.setModel(new DefaultTableModel(data, header));
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        username1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        viewTextArea = new javax.swing.JTextArea();
        TmblConnect = new javax.swing.JButton();
        postTextField = new javax.swing.JTextField();
        TmblKirim = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        username1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                username1ActionPerformed(evt);
            }
        });

        viewTextArea.setEditable(false);
        viewTextArea.setColumns(20);
        viewTextArea.setRows(5);
        jScrollPane1.setViewportView(viewTextArea);

        TmblConnect.setText("Connect");
        TmblConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TmblConnectActionPerformed(evt);
            }
        });

        postTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                postTextFieldActionPerformed(evt);
            }
        });

        TmblKirim.setText("Send");
        TmblKirim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TmblKirimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(username1)
                        .addGap(18, 18, 18)
                        .addComponent(TmblConnect))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(postTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(TmblKirim)
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addGap(56, 56, 56))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TmblConnect))
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(postTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TmblKirim, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TmblConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TmblConnectActionPerformed
        // TODO add your handling code here:
        this.username = username1.getText();
        start();
        viewTextArea.setText("Selamat datang ");
    }//GEN-LAST:event_TmblConnectActionPerformed

    private void TmblKirimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TmblKirimActionPerformed
        try {
            // TODO add your handling code here:
            String message = "postText~" + username + "~" + postTextField.getText() + "~all~\n";
            output.writeObject(message);
            postTextField.setText("");
        } catch (IOException ex) {
            Logger.getLogger(chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_TmblKirimActionPerformed

    private void username1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_username1ActionPerformed
        // TODO add your handling code here:
        TmblConnectActionPerformed(evt);
    }//GEN-LAST:event_username1ActionPerformed

    private void postTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_postTextFieldActionPerformed
        // TODO add your handling code here:
        TmblKirimActionPerformed(evt);
    }//GEN-LAST:event_postTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton TmblConnect;
    private javax.swing.JButton TmblKirim;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField postTextField;
    private javax.swing.JTextField username1;
    private javax.swing.JTextArea viewTextArea;
    // End of variables declaration//GEN-END:variables
}
