
package mapBuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import tiles.Tile;


public class Player {
    
    private Tile toBePlaced = null;
    private int placeAlignment = 0;
    UserInterface uI;
    
    public Player(Dimension dim) {
        uI = new UserInterface(dim);
        
    }
    
    public void giveTile(Tile t) {
        toBePlaced = t;
        uI.setTileImages(t.getAllImages(), t.getAlignment());
    }
    
    public Tile checkTile() {
        return toBePlaced;
    }
    
    public void rotateTile() {
        if (toBePlaced != null) {
            toBePlaced.rotate();
            uI.rotateTile();
        }
    }
    
    public Tile takeTile() {
        Tile t = toBePlaced;
        toBePlaced = null;
        uI.tileImages = null;
        return t;
    }
    
    public static final int TILE_BOT = 0;
    
    public void resizeUI(int panelWidth, int panelHeight) {
        uI.resize(panelWidth, panelHeight);
    }
    
    public void paintUI(Graphics g, ImageObserver imgOb) {
        uI.paint(g, imgOb);
    }
    
}
