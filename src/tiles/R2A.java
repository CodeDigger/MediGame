
package tiles;

import java.awt.Image;

public class R2A extends Tile {
    
    public R2A() {
        super();
        maxAlignments = 2;
        type = TileHandler.R2A;
        initContent();
    }
    
    public R2A (Image img) {
        super(img);
        initContent();
    }
    
    public R2A(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
        char[] c = new char[]{'O','R','O','O','O','R','O','O','O'};
        setContent(c);
    }
    
}
