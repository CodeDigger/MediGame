
package menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;
import utilities.ImageHandler;

/**
 *
 * @author David
 */

public class BackgroundPane extends JComponent {
    
    Image bgi;
    
    public BackgroundPane(String fileName, int w, int h) {
        this(ImageHandler.loadScaleImage(fileName, w, h));
    }
    
    public BackgroundPane(String fileName) {
        this(ImageHandler.loadImage(fileName));
    }
    
    public BackgroundPane(Image bgi) {
        this.bgi = bgi;
        setPreferredSize(new Dimension(bgi.getWidth(this), bgi.getHeight(this)));
    }

    @Override
    public void paint(Graphics g){
        g.drawImage(bgi,0,0,this);
        super.paint(g);    
    }
    
    
}
