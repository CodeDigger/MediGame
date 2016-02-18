/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapBuilder;

import tiles.TileStack;
import java.util.ArrayList;
import tiles.Tile;

/**
 *
 * @author David
 */
public class MultiplayerMap {
    
    private static int xCount;
    private static int yCount;

    private static int mapWidth;
    private static int mapHeight;
    
    private Tile[][] tileArray;
    ArrayList<TileStack> stackList;
    
    public MultiplayerMap(int xC, int yC, int width, int height) {
        xCount = xC;
        yCount = yC;
        mapWidth = width;
        mapHeight = height;
        tileArray = new Tile[yCount][xCount];
        stackList = new ArrayList();
    }
    
    public void addTileStack(TileStack tS) {
        stackList.add(tS);
    }
    
    public int getXCount() {
        return xCount;
    }
    
    public int getYCount() {
        return yCount;
    }
    
    public int getHeight() {
        return mapHeight;
    }
    
    public int getWidth() {
        return mapWidth;
    }
    
    public Tile getTile(int row, int col) {
        return tileArray[row][col];
    }
    
    public TileStack getStack(int i) {
        return stackList.get(i);
    }
    
    public ArrayList<TileStack> getStackList() {
        return stackList;
    }
    
    public void deleteStack(TileStack tS) {
        stackList.remove(tS);
    }
    
    public void placeTile(Tile t, int row, int col) {
        tileArray[row][col] = t;
    }
    
}
