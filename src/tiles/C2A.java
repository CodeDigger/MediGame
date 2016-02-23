
package tiles;

import java.awt.Image;

public class C2A extends Tile {
    
    public C2A() {
        super();
        maxAlignments = 2;
        type = TileHandler.C2A;
        initContent();
    }
    
    public C2A (Image img) {
        super(img);                 
        initContent();
    }
    
    public C2A(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
        char[] c = new char[]{'O','O','O','C','O','O','O','C','O'};
        setContent(c);
    }
    
}
