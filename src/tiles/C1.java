
package tiles;


import java.awt.Image;

public class C1 extends Tile {
    
    public C1() {
        super();
        type = TileHandler.C1;
        initContent();
    }
    
    public C1 (Image img) {
        super(img);
        initContent();
    }
    
    public C1(int row, int col, Image img) {
	super(row, col, img);
        initContent();
    }
    
    private void initContent() {
        char[] c = new char[]{'O','O','O','O','O','O','O','C','O'};
        setContent(c);
    }
    
}
