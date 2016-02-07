
package menu;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import utilities.ImageHandler;

/**
 *
 * @author David
 */
public class MainMenu extends JPanel  {
    
    public static String START_TEST = "Start Test Mode";
    public static String START_SERVER = "Start Server";
    public static String JOIN_SERVER = "Join Server";
    
    JButton startTestButton;
    JButton startServerButton;
    JButton joinServerButton;
    
    Image bgi;
    
    public MainMenu(Main main) {
        super();
        
        bgi = ImageHandler.loadImage("/textures/MenuBackground.jpg");
        setPreferredSize(new Dimension(bgi.getWidth(this), bgi.getHeight(this)));
        
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        startTestButton = new JButton(START_TEST);
        startServerButton = new JButton(START_SERVER);
        joinServerButton = new JButton(JOIN_SERVER);
        add(startTestButton);
        add(startServerButton);
        add(joinServerButton);
        
        startTestButton.addActionListener(main);
        startServerButton.addActionListener(main);
        joinServerButton.addActionListener(main);
        
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(bgi, 0, 0, this);
        paintComponents(g);
    }
    
    
}
