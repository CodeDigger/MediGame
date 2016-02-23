
package tiles;

import java.awt.Image;

public class C1R2C extends Tile {
    
    public C1R2C() {
        super();
        type = TileHandler.C1R2C;
        initContent();
    }
    
    public C1R2C (Image img) {
        super(img);
        initContent();
    }
    
    public C1R2C(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
//                                 ^       >       v       <
        char[] c = new char[]{'O','O','O','R','O','R','O','C','O'};
        setContent(c);
    }
    
}
