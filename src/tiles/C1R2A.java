
package tiles;

import java.awt.Image;

public class C1R2A extends Tile {
    
    public C1R2A() {
        super();
        type = TileHandler.C1R2A;
        initContent();
    }
    
    public C1R2A (Image img) {
        super(img);
        initContent();
    }
    
    public C1R2A(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
//                                 ^       >       v       <        
        char[] c = new char[]{'O','R','O','O','O','R','O','C','O'};
        setContent(c);
    }
    
}
