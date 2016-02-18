
package tiles;

import java.awt.Image;

public class C2B extends Tile {
    
    public C2B() {
        super();
        type = TileHandler.C2B;
        initContent();
    }
    
    public C2B (Image img) {
        super(img);
        initContent();
    }
    
    public C2B(int row, int col, Image img) {
	super(row, col, img);
    }
    
    private void initContent() {
        char[] c = new char[]{'O','C','O','O','O','O','O','C','O'};
        setContent(c);
    }
    
}
