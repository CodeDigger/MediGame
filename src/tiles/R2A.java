
package tiles;

import java.awt.Image;

public class R2A extends Tile {
    
    public R2A() {
        super();
        maxAlignments = 2;
        initContent();
    }
    
    public R2A (Image img) {
        super(img);
        initContent();
    }
    
    public R2A(int row, int col, Image img) {
	super(row, col, img);
    }
    
    private void initContent() {
        char[] c = new char[]{'O','R','O','O','O','R','O','O','O'};
        setContent(c);
    }
    
}
