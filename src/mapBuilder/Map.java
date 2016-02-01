/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapBuilder;

import events.MapListener;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import tiles.*;
import utilities.ImageHandler;

public class Map {
    
    private final ArrayList<MapListener> mapListeners = new ArrayList<MapListener>();

    private static int xCount = 26;
    private static int yCount = 16;

    private static int mapWidth = (int) (xCount * TileHandler.getWidth());
    private static int mapHeight = (int) (yCount * TileHandler.getHeight());
    
    Image bgi;
    private TileHandler tH;
    
    ArrayList<TileStack> stackList = new ArrayList();

    private Tile[][] tileArray;
    private Tile[] highlightedTiles = null;
    private ArrayList<Tile> drawList = new ArrayList();

    //private int newTileType = TileHandler.G4;
    
    private int screenX = 0;
    private int screenY = 0;
    
    public Map() {
        bgi = ImageHandler.loadImage("/textures/wood_big.jpg");
        tH = new TileHandler(100);
        
        int w = TileHandler.getWidth();
        int h = TileHandler.getHeight();
        Image i0 = ImageHandler.cutScaleImageByPixels(ImageHandler.loadImage("/textures/Tiles-01.png"), 8*w, 1*h, 
                w+8, h+10, w, h, w, h);
        Image i1 = ImageHandler.cutScaleImageByPixels(ImageHandler.loadImage("/textures/Tiles-01.png"), 8*w, 1*h, 
                w+3, h+5, w, h, w, h);
        
        stackList.add(new TileStack(i0,i1,14,300,400));
        stackList.add(new TileStack(i0,i1,14,214,490));
        stackList.add(new TileStack(i0,i1,14,324,520));
        stackList.add(new TileStack(i0,i1,14,286,618));
        

        generateMap();
    }
    
        public void generateMap() {
        System.out.println("Generating Map:");
        System.out.println("- yCount: " + yCount + ", xCount: " + xCount);
        System.out.println("- Active tile types: " + TileHandler.DIFFERENT_TYPES);

        tileArray = new Tile[yCount][xCount];

        int centerRow = (int) (yCount / 2);
        int centerCol = (int) (xCount / 2);

        Tile t = initLand(TileHandler.G4);
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
            stackList.remove(tS);
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
        int all = toBePlaced.getAlignments();
        Image[] images = new Image[all];
        for (int i = 0; i < all; i++) {
            images[i] = tH.getImage(newTileType+i);
        }
        toBePlaced.setAllImages(images);
        return toBePlaced;
    }
    
    boolean playerPlaceLand(Player p, int row, int col) {
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
            tileArray[row][col] = t;
            
            // Create adjacent tiles:
            if (col - 1 >= 0) {
                createSpace(row, col - 1);
            }
            if (row - 1 >= 0) {
                createSpace(row - 1, col);
            }
            if (row + 1 <= xCount) {
                createSpace(row + 1, col);
            }
            if (col + 1 <= yCount) {
                createSpace(row, col + 1);
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

    private void createSpace(int row, int col) {
        if (row >= 0 && col >= 0 && row < yCount && col < xCount) {
            if (tileArray[row][col] == null) {
                tileArray[row][col] = new EmptySpace(row, col, tH.getImage(TileHandler._EMPTY));
            }
        }
    }

    private boolean matchSurroundings(Tile toBePlaced, int row, int col) {
        Tile t = tileArray[row][col];
        if (t != null) {
            if (!t.isEmpty()) {
                System.out.println("(WARNING): Occupied!");
                return false;
            }
        }
        
        t = null;
        //UNDER
        if (row - 1 >= 0) {
            t = tileArray[row - 1][col];
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

        //TO THE LEFT
        if (col + 1 < xCount) {
            t = tileArray[row][col + 1];
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

        //OVER
        if (row + 1 < yCount) {
            t = tileArray[row + 1][col];
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

        //TO THE RIGHT
        if (col - 1 >= 0) {
            t = tileArray[row][col - 1];
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
        
        for (int x = 0; x < mapWidth; x += bgi.getWidth(null)) {
            for (int y = 0; y < mapHeight; y += bgi.getHeight(null)) {
                g.drawImage(bgi, x, y, null);
            }
        }
        
        for (TileStack tS : stackList) {
            tS.paintShadow(g);
            tS.paint(g);
        }

        for (int row = 0; row < yCount; row++) {
            for (int col = 0; col < xCount; col++) {
                if (tileArray[row][col] != null) {
                    if (tileArray[row][col].isEmpty()) {
                        tileArray[row][col].paint(g);
                    } else {
                        drawList.add(tileArray[row][col]);
                    }
                }
            }
        }
        
        for (Tile t : drawList) {
            t.paint(g);
        }
        drawList.clear();

        if (highlightedTiles != null) {
            for (int i = 0; i < highlightedTiles.length; i++) {
                highlightedTiles[i].paint(g);
            }
        }

    }

    
    void setHighlightedTiles(Tile mouseTile) {
        if (highlightedTiles != null) {
            for (int i = 0; i < highlightedTiles.length; i++) {
                highlightedTiles[i].setHighLighted(false);
            }
        }
        highlightedTiles = null;

        if (mouseTile != null) {
            /*int[] s = structSpace;
             if (s[0]*s[1] == 1) {*/
            highlightedTiles = new Tile[1];
            highlightedTiles[0] = mouseTile;
            /*} else if (s[0]*s[1] > 1) {
             int t = (s[0]*s[1]);
             highlightedTiles = new Tile[t];
             int y = mouseTile.getRow();
             int x = mouseTile.getCol();
             int c = 0;
             for (int a = 0; a < s[1]; a++) {
             int tempY = y;
             int tempX = x;
             for (int b = 0; b < s[0]; b++) {
             highlightedTiles[c] = getTile(tempY, tempX);
             tempY--;
             if (tempY % 2 == 1) tempX--;
             c++;
             }
             y--;
             if (y %2 == 0) x++;
             }
             }*/
            for (int i = 0; i < highlightedTiles.length; i++) {
                highlightedTiles[i].setHighLighted(true);
            }
        }
    }

    void updateHighlight(MouseEvent mME) {
        Tile newTile = getMouseTile(mME.getY(), mME.getX());
        if (newTile != null) {
            if (highlightedTiles == null || newTile != highlightedTiles[0]) {
                setHighlightedTiles(newTile);
            }
        } else {
            if (highlightedTiles != null) {
                for (int i = 0; i < highlightedTiles.length; i++) {
                    highlightedTiles[i].setHighLighted(false);
                }
            }
            highlightedTiles = null;
        }
    }

    public Tile getTile(int row, int col) {
        return tileArray[row][col];
    }
    
    public TileStack highlightStacks(int mX, int mY) {
        for (TileStack tS : stackList) {
            if (tS.checkHighlight(mX, mY)) {
                return tS;
            }
        }
        
        return null;
    }
    
    public int getWidth() {
        return mapWidth;
    }
    
    public int getHeight() {
        return mapHeight;
    }
    
    public void setMapX(int i) {
        screenX = i;
    }
    public void setMapY(int i) {
        screenY = i;
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
        if (mouseRow < yCount && mouseCol < xCount) {
            try {
                tile = tileArray[mouseRow][mouseCol];
            } catch (ArrayIndexOutOfBoundsException aE) {
                // Do Nothing
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return tile;
    }

    
}
