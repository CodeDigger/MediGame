
package tiles;

import java.awt.Image;

public class C1R2D extends Tile {
    
    public C1R2D() {
        super();
        type = TileHandler.C1R2D;
        initContent();
    }
    
    public C1R2D (Image img) {
        super(img);
        initContent();
    }
    
    public C1R2D(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
//                                 ^       >       v       <
        char[] c = new char[]{'O','R','O','R','O','O','O','C','O'};
        setContent(c);
    }
    
}
