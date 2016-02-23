/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiles;

import java.awt.Image;

/**
 *
 * @author David
 */
public class R2B extends Tile {
    
    public R2B() {
        super();
        type = TileHandler.R2B;
        initContent();
    }
    
    public R2B(Image img) {
        super(img);
        initContent();
    }
    
    public R2B(int row, int col, Image img) {
        super(row, col, img);
        initContent();
    }
    
    private void initContent() {
        
        char[] c = new char[]{'O','O','O','R','O','R','O','O','O'};
        setContent(c);
    }
    
}
