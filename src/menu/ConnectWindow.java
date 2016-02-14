/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import menu.Main;

/**
 *
 * @author David
 */
public class ConnectWindow extends JFrame {
    
    JTextField ipTextField;
    JTextField portTextField;
    JButton connectButton;
    
    public ConnectWindow (Main main) {
        
        setLayout(new FlowLayout(FlowLayout.CENTER));
        ipTextField = new JTextField("192.168.1.92",10);
        portTextField = new JTextField("4444", 4);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(main);
        add(ipTextField);
        add(portTextField);
        add(connectButton);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(main);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    public JButton getConnectButton() {
        return connectButton;
    }
    
    public String getIP() {
        return ipTextField.getText();
    }
    
    public int getPort() {
        int p = Integer.parseInt(portTextField.getText());
        return p;
    }
    
}
