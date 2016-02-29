/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;


public class LobbyFrame extends JFrame {
    
    JTextArea lobbyTextArea;
    JButton readyButton;
    
    public LobbyFrame (String head, JFrame p) {
        super(head);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        lobbyTextArea = new JTextArea(10, 24);
        readyButton = new JButton("READY");
        lobbyTextArea.setEditable(false);
        lobbyTextArea.append(" - Joined Lobby -\n");
        add(lobbyTextArea);
        add(readyButton);
        setAlwaysOnTop(true);
        pack();
        setLocationRelativeTo(p);
        setVisible(true);
        
    }
    
    public JButton getReadyButton() {
        return readyButton;
    }
    
    public void writeMessage(String s) {
        lobbyTextArea.append(s+"\n");
    }
    
}
