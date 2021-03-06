
package tiles;

import java.awt.Image;

public class C3A extends Tile {
    
    public C3A() {
        super();
        type = TileHandler.C3A;
        initContent();
    }
    
    public C3A (Image img) {
        super(img);
        initContent();
    }
    
    public C3A(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
        char[] c = new char[]{'O','C','O','C','O','O','O','C','O'};
        setContent(c);
    }

    
}
