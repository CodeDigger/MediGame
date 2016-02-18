
package tiles;

import java.awt.Image;

public class Grass extends Tile {
    
    public Grass() {
        super();
        maxAlignments = 1;
        type = TileHandler.G4;
        initContent();
    }
    
    public Grass(Image img) {
        super(img);
        initContent();
    }
    
    public Grass(int row, int col, Image img) {
	super(row, col, img);
    }
    
    private void initContent() {
        char[] c = new char[]{'O','O','O','O','O','O','O','O','O'};
        setContent(c);
    }

}