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

/**
 *
 * @author David
 */
public class ConnectWindow extends JFrame {
    
    JTextField ipTextField;
    JButton connectButton;
    
    public ConnectWindow (Main main) {
        
        setLayout(new FlowLayout(FlowLayout.CENTER));
        ipTextField = new JTextField("0.0.0.0:port", 12);
        connectButton = new JButton("Connect");
        connectButton.addActionListener(main);
        add(ipTextField);
        add(connectButton);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(main);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
}
