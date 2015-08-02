
package tiles;

import java.awt.Image;

public class C1R2A extends Tile {
    
    public C1R2A() {
        super();
        maxAlignments = 2;
        initContent();
    }
    
    public C1R2A (Image img) {
        super(img);
        initContent();
    }
    
    public C1R2A(int row, int col, Image img) {
	super(row, col, img);
    }
    
    private void initContent() {
        char[] c = new char[]{'O','R','O','O','O','R','O','C','O'};
        setContent(c);
    }
    
}
