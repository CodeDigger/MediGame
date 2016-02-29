/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testMode;

import tiles.TileStack;
import java.util.ArrayList;
import tiles.Tile;
import tiles.TileHandler;

/**
 *
 * @author David
 */
public class TmMap {
    
    private static int xCount;
    private static int yCount;

    private static int mapWidth;
    private static int mapHeight;
    
    private Tile[][] tileArray;
    ArrayList<TileStack> stackList;
    
    public TmMap(int xC, int yC, int width, int height) {
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
