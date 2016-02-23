
package tiles;

import java.awt.Image;

public class C2R2B extends Tile {
    
    public C2R2B() {
        super();
        maxAlignments = 2;
        type = TileHandler.C2R2B;
        initContent();
    }
    
    public C2R2B (Image img) {
        super(img);
        initContent();
    }
    
    public C2R2B(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
//                                 ^       >       v       <
        char[] c = new char[]{'O','R','O','C','O','R','O','C','O'};
        setContent(c);
    }
    
}
