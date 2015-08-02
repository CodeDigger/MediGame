
package tiles;

import java.awt.Image;

public class C1R2B extends Tile {
    
    public C1R2B() {
        super();
        initContent();
    }
    
    public C1R2B (Image img) {
        super(img);
        initContent();
    }
    
    public C1R2B(int row, int col, Image img) {
	super(row, col, img);
    }
    
    private void initContent() {
        char[] c = new char[]{'O','O','O','R','O','R','O','C','O'};
        setContent(c);
    }
    
}
