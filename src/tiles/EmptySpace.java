
package tiles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class EmptySpace extends Tile {

    public EmptySpace(int row, int col, Image img) {
        super(row, col, img);
        setEmpty(true);
    }
    
    @Override
    public void paint(Graphics g) {
	g.drawImage(img, x, y, null);	
        g.setColor(Color.DARK_GRAY);
        if (highlighted) {
            g.setColor(new Color(1.0f,1.0f,1.0f,0.1f));
            g.fillRect(x+2, y+4, width, height);
            g.setColor(new Color(1.0f,1.0f,1.0f,0.4f));
        }
        g.drawRect(x+2, y+4, width, height);
    }
    
}
