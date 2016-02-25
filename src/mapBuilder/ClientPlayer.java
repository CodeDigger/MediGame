
package mapBuilder;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import tiles.Tile;


public class ClientPlayer {
    
    private Tile toBePlaced = null;
    private int placeAlignment = 0;
    String playerName = "NoName";
    UserInterface uI;
    private boolean hasLeftGame;

    public ClientPlayer(String name, Dimension dim) {
        playerName = name;
        hasLeftGame = false;
        uI = new UserInterface(dim);
        
    }
    
    public ClientPlayer(String name) {
        playerName = name;
    }
    
    public void messagePlayer(String s) {
        uI.newMessage(s);
    }
    
    public void giveTile(Tile t) {
        toBePlaced = t;
        uI.setTileImages(t.getAllImages(), t.getAlignment());
    }
    
    public Tile checkTile() {
        return toBePlaced;
    }
    
    public UserInterface getUI() {
        return uI;
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
        uI.setSize(panelWidth, panelHeight);
    }
    
    public void paintUI(Graphics g, ImageObserver imgOb) {
        uI.paint(g, imgOb);
    }

    public void leaveGame() {
        hasLeftGame = true;
    }

    public boolean hasLeftGame(){
        return hasLeftGame;
    }
    
    public String getName() {
        return playerName;
    }
}
