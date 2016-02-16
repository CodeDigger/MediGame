/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testMode;

import mapBuilder.*;
import tiles.TileStack;
import events.MapListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import tiles.*;
import utilities.ImageHandler;

public class TmMapHandler {
    
    TmMap map;
    Image bgi;
    private final ArrayList<MapListener> mapListeners = new ArrayList<MapListener>();
    
    private TileHandler tH;
    
    private Tile highlightedTile;
    private ArrayList<Tile> drawList = new ArrayList();
    
    private int screenX = 0;
    private int screenY = 0;
    
    public TmMapHandler() {
        bgi = ImageHandler.loadImage("/resources/textures/wood_big.jpg");
        tH = new TileHandler(100);
        
        generateMap();
    }

    public void generateMap() {
        int xCount = 26;
        int yCount = 16;
        int mapWidth = (int) (xCount * TileHandler.getWidth());
        int mapHeight = (int) (yCount * TileHandler.getHeight());
        
        map = new TmMap(xCount,yCount,mapWidth,mapHeight);
        
        System.out.println("Generating Map:");
        System.out.println("- yCount: " + yCount + ", xCount: " + xCount);
        System.out.println("- Active tile types: " + TileHandler.DIFFERENT_TYPES);


        int centerRow = (int) (yCount / 2);
        int centerCol = (int) (xCount / 2);
        
        int w = TileHandler.getWidth();
        int h = TileHandler.getHeight();
        Image stackImg = ImageHandler.cutScaleImageByPixels(ImageHandler.loadImage("/resources/textures/Tiles-03.png"), 8*w, 1*h,
                w+8, h+10, w, h, w, h);
        
        map.addTileStack(new TileStack(stackImg,14,300,400));
        map.addTileStack(new TileStack(stackImg,14,214,490));
        map.addTileStack(new TileStack(stackImg,14,324,520));
        map.addTileStack(new TileStack(stackImg,14,286,618));

        Tile t = initLand(TileHandler.C1R2A);
        t.rotate();
        placeLand(t, centerRow, centerCol);
    }
        
    public void addTileListener(MapListener tL) {
        mapListeners.add(tL);
    }

    public Tile drawLand(TileStack tS) {
        int newTileType = 0;
        double d = Math.random();
        double div = (1.0 / TileHandler.DIFFERENT_TYPES);
        int i = 1;
        if (d <= div * i) {
            newTileType = TileHandler.G4;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.R2A;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C1;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C1R2B;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C2A;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C1R2A;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C2B;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C2C;
        } else if (d > (div * i++) && d <= (div * i)) {
            newTileType = TileHandler.C3A;
        }
        System.out.println("Note: Draw: " + newTileType + " (" + div + "|" + d + ")");
        Tile t = initLand(newTileType);
        tS.drawStack();
        if (tS.getStackCount() == 0) {
            map.deleteStack(tS);
        }
        return t;
    }

    Tile initLand(int newTileType) {
        Tile toBePlaced;
        
        if (newTileType == TileHandler.G4) {
            toBePlaced = new Grass();
        } else if (newTileType == TileHandler.R2A) {
            toBePlaced = new R2A();
        } else if (newTileType == TileHandler.C1) {
            toBePlaced = new C1();
        } else if (newTileType == TileHandler.C1R2B) {
            toBePlaced = new C1R2B();
        } else if (newTileType == TileHandler.C2A) {
            toBePlaced = new C2A();
        } else if (newTileType == TileHandler.C1R2A) {
            toBePlaced = new C1R2A();
        } else if (newTileType == TileHandler.C2B) {
            toBePlaced = new C2B();
        } else if (newTileType == TileHandler.C2C) {
            toBePlaced = new C2C();
        } else if (newTileType == TileHandler.C3A) {
            toBePlaced = new C3A();
        } else {
            toBePlaced = null;
            System.out.println("- [ERROR] -: Tile initiation failed!");
        }
        int all = toBePlaced.getMaxAlignments();
        Image[] images = new Image[all];
        for (int i = 0; i < all; i++) {
            images[i] = tH.getImage(newTileType+i);
        }
        toBePlaced.setAllImages(images);
        return toBePlaced;
    }
    
    boolean playerPlaceLand(TmPlayer p, int row, int col) {
        Tile t = p.takeTile();
        if (placeLand(t, row, col)) {
            return true;
        } else {
            p.giveTile(t);
            System.out.println("NOTE: Tile returned to player");
            return false;
        }
    }

    boolean placeLand(Tile t, int row, int col) {
        
        if (matchSurroundings(t, row, col)) {
            t.place(row, col);
            map.placeTile(t, row, col);
            
            // Create adjacent tiles:
            if (col - 1 >= 0) {
                checkAdjecentSpace(row, col - 1);
            }
            if (row - 1 >= 0) {
                checkAdjecentSpace(row - 1, col);
            }
            if (row + 1 <= map.getXCount()) {
                checkAdjecentSpace(row + 1, col);
            }
            if (col + 1 <= map.getYCount()) {
                checkAdjecentSpace(row, col + 1);
            }
            
            for (MapListener tL : mapListeners) {
                tL.tilePlaced();
            }
            t.removeExcessImages();
            return true;
        } else {
            System.out.println("(WARNING): Placement failed");
            return false;
        }
    }

    private void checkAdjecentSpace(int row, int col) {
        if (row >= 0 && col >= 0 && row < map.getYCount() && col < map.getXCount()) {
            if (map.getTile(row, col) == null) {
                Tile t = new EmptySpace(row, col, tH.getImage(TileHandler._EMPTY));
                map.placeTile(t, row, col);
            }
        }
    }

    private boolean matchSurroundings(Tile toBePlaced, int row, int col) {
        Tile t = map.getTile(row, col);
        if (t != null) {
            if (!t.isEmpty()) {
                System.out.println("(WARNING): Occupied!");
                return false;
            }
        }
        
        t = null;
        //OVER
        if (row - 1 >= 0) {
            t = map.getTile(row - 1,col);
        }
        if (t == null) {
            //Do nothing
        } else if (!t.isEmpty()) {
            char[] cTile = toBePlaced.collectContent(Tile.TOP);
            char[] c = t.collectContent(Tile.BOTTOM);
            for (int i = 0; i < c.length; i++) {
                if (cTile[i] != c[i]) {
                    System.out.println("(WARNING): Not matching surrounding tiles:");
                    System.out.println("    - Placing: " + cTile[0] + cTile[1] + cTile[2]);
                    System.out.println("    - Placed:  " + c[0] + c[1] + c[2]);
                    return false;
                }
            }
        }

        //TO THE RIGHT
        if (col + 1 < map.getXCount()) {
            t = map.getTile(row, col+1);
        }
        if (t == null) {
            //Do nothing
        } else if (!t.isEmpty()) {
            char[] cTile = toBePlaced.collectContent(Tile.RIGHT);
            char[] c = t.collectContent(Tile.LEFT);
            for (int i = 0; i < c.length; i++) {
                if (cTile[i] != c[i]) {
                    System.out.println("(WARNING): Not matching surrounding tiles:");
                    System.out.println("    - Placing: " + cTile[0] + cTile[1] + cTile[2]);
                    System.out.println("    - Placed:  " + c[0] + c[1] + c[2]);
                    return false;
                }
            }
        }

        //UNDER
        if (row + 1 < map.getYCount()) {
            t = map.getTile(row+1, col);
        }
        if (t == null) {
            //Do nothing
        } else if (!t.isEmpty()) {
            char[] cTile = toBePlaced.collectContent(Tile.BOTTOM);
            char[] c = t.collectContent(Tile.TOP);
            for (int i = 0; i < c.length; i++) {
                if (cTile[i] != c[i]) {
                    System.out.println("(WARNING): Not matching surrounding tiles:");
                    System.out.println("    - Placing: " + cTile[0] + cTile[1] + cTile[2]);
                    System.out.println("    - Placed:  " + c[0] + c[1] + c[2]);
                    return false;
                }
            }
        }

        //TO THE LEFT
        if (col - 1 >= 0) {
            t = map.getTile(row, col);
        }
        if (t == null) {
            //Do nothing
        } else if (!t.isEmpty()) {
            char[] cTile = toBePlaced.collectContent(Tile.LEFT);
            char[] c = t.collectContent(Tile.RIGHT);
            for (int i = 0; i < c.length; i++) {
                if (cTile[i] != c[i]) {
                    System.out.println("(WARNING): Not matching surrounding tiles:");
                    System.out.println("    - Placing: " + cTile[0] + cTile[1] + cTile[2]);
                    System.out.println("    - Placed:  " + c[0] + c[1] + c[2]);
                    return false;
                }
            }
        }
        return true;
    }

    void paint(Graphics g) {
        
        for (int x = 0; x < map.getWidth(); x += bgi.getWidth(null)) {
            for (int y = 0; y < map.getHeight(); y += bgi.getHeight(null)) {
                g.drawImage(bgi, x, y, null);
            }
        }
        
        for (int row = 0; row < map.getYCount(); row++) {
            for (int col = 0; col < map.getXCount(); col++) {
                Tile t = map.getTile(row, col);
                if (t != null) {
                    if (t.isEmpty()) {
                        t.paint(g);
                    } else {
                        drawList.add(t);
                    }
                }
            }
        }
        
        if (highlightedTile != null) {
            if (highlightedTile.isEmpty()) {
            highlightedTile.paint(g);
            }
        }

        for (TileStack tS : map.getStackList()) {
            tS.paintShadow(g);
            tS.paint(g);
        }

        
        
        for (Tile t : drawList) {
            t.paint(g);
        }
        drawList.clear();

        if (highlightedTile != null) {
            if (!highlightedTile.isEmpty()) {
                highlightedTile.paint(g);
            }
        }

    }

    void updateHighlight(MouseEvent mME) {
        Tile newTile = getMouseTile(mME.getY(), mME.getX());
        
        if (newTile != null && highlightedTile != null) {
            if (!highlightedTile.equals(newTile)) {
                highlightedTile.setHighLighted(false);
                highlightedTile = newTile;
                highlightedTile.setHighLighted(true);
            }
        } else if (newTile != null) {
            highlightedTile = newTile;
            highlightedTile.setHighLighted(true);
        } else if (highlightedTile != null) {
            highlightedTile.setHighLighted(false);
            highlightedTile = null;
        }
    }
    
    public TileStack highlightStacks(int mX, int mY) {
        for (TileStack tS : map.getStackList()) {
            if (tS.checkHighlight(mX, mY)) {
                return tS;
            }
        }
        
        return null;
    }
    
    public void setMapX(int i) {
        screenX = i;
    }
    public void setMapY(int i) {
        screenY = i;
    }
    
    public int getMapWidth() {
        return map.getWidth();
    }
    public int getMapHeight() {
        return map.getHeight();
    }

    /*public Tile getToBePlaced() {
        return toBePlaced;
    }*/

    public Image getImage(int type) {
        return tH.getImage(type);
    }
    
    /*public TileStack getMoustStack(MouseEvent mME) {
        tileStack1.checkHighlight();
        
        
        return null;
    }*/

    public Tile getMouseTile(int mY, int mX) {
        mY -= screenY;
        mX -= screenX;
        int mouseRow = mY / TileHandler.getHeight();
        int mouseCol = mX / TileHandler.getWidth();
        /*double leftX = mouseCol * TileHandler.getWidth();
         double centerY = mouseRow * TileHandler.getHeight() / 2 + TileHandler.getHeight() / 2;
         double localMX = mX - leftX;
         double TileHandlerK = TileHandler.getTileK(); 
		
         if (mY < centerY - TileHandlerK * localMX) {
         // North West
         mouseRow--;
         mouseCol--;
         } else if (mY < centerY - TileHandler.getHeight() + TileHandlerK * localMX) {
         // North East
         mouseRow--;
         } else if (mY > centerY + TileHandler.getHeight() - TileHandlerK * localMX) {
         // South East
         mouseRow++;
         } else if (mY > centerY + TileHandlerK * localMX) {
         // South West
         mouseRow++;
         mouseCol--;
         }*/

        Tile tile = null;
        if (mouseRow < map.getYCount() && mouseCol < map.getXCount()) {
            try {
                tile = map.getTile(mouseRow, mouseCol);
            } catch (ArrayIndexOutOfBoundsException aE) {
                // Do Nothing
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tile;
    }

    
}
