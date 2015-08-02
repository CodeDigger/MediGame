
package tiles;

import java.awt.Image;

public class C2C extends Tile {
    
    public C2C() {
        super();
        initContent();
    }
    
    public C2C (Image img) {
        super(img);
        initContent();
    }
    
    public C2C(int row, int col, Image img) {
	super(row, col, img);
    }
    
    private void initContent() {
        char[] c = new char[]{'O','C','O','O','O','O','O','C','O'};
        setContent(c);
    }
    
}
