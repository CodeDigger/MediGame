package tiles;

import java.awt.Graphics;
import java.awt.Image;

public class Tile {

    protected int x;
    protected int y;
    protected int row;
    protected int col;
    protected int width;
    protected int height;
    protected int alignment = 0;
    protected int maxAlignments = 4;
    private char[] content;
    //private int[] polyX = new int[4];
    //private int[] polyY = new int[4];
    protected Image img;
    protected Image[] allImgs;
    protected Image borderBot;
    protected Image borderRight;
    protected boolean highlighted = false;
    private boolean empty;

    public Tile() {
        width = TileHandler.getWidth();
        height = TileHandler.getHeight();
        borderBot = TileHandler.getBorderImage(0);
        borderRight = TileHandler.getBorderImage(1);
    }

    public Tile(Image img) {
        this.img = img;
        width = TileHandler.getWidth();
        height = TileHandler.getHeight();
        borderBot = TileHandler.getBorderImage(0);
        borderRight = TileHandler.getBorderImage(1);
    }

    public Tile(int row, int col, Image img) {
        this.row = row;
        this.col = col;
        this.img = img;

        x = col * TileHandler.getWidth();
        y = row * TileHandler.getHeight();
        width = TileHandler.getWidth();
        height = TileHandler.getHeight();
        /*if (row % 2 == 0) {
         y -= TileHandler.getH   eight() * row / 2;
         } else if (row % 2 == 1) {
         y -= TileHandler.getHeight() / 2;
         y -= TileHandler.getHeight() * ((row - 1) / 2);
         x += TileHandler.getWidth() / 2;
         } else {
         System.out.println("ERROR: Correcting TILE cordinates failed");
         }*/

        /*polyY[0] = y;
         polyY[1] = y+TileHandler.getHeight()/2;
         polyY[2] = y+TileHandler.getHeight();
         polyY[3] = y+TileHandler.getHeight()/2;
		
         polyX[0] = x+TileHandler.getWidth()/2;
         polyX[1] = x;
         polyX[2] = x+TileHandler.getWidth()/2;
         polyX[3] = x+TileHandler.getWidth();*/
    }

    /*
     public int getType() {
     return type;
     }
    
     public void changeType(Image img, int type) {
     this.type = type;
     setImage(img);
     }*/
    public void place(int row, int col) {
        this.row = row;
        this.col = col;
        x = col * TileHandler.getWidth();
        y = row * TileHandler.getHeight();
    }

    public void setImage(Image img) {
        this.img = img;
    }
    
    public void setAllImages(Image[] images) {
        this.allImgs = images;
        this.img = images[0];
    }
    
    public Image[] getAllImages() {
        return allImgs;
    }

    public void setContent(char[] c) {
        content = c;
    }
    
    public int getAlignment() {
        return alignment;
    }
    
    public int getMaxAlignments() {
        return maxAlignments;
    }

    public Image getImage() {
        return img;
    }

    public void setHighLighted(boolean h) {
        highlighted = h;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean b) {
        empty = b;
    }
    
    public void removeExcessImages() {
        allImgs = null;
    }

    public void rotate() {
            // c        content
        // 0 1 2 -> 6 7 0
        // 7 8 3    5 8 1
        // 6 5 4    4 3 2
        char[] c = content.clone();
        for (int i = 2; i < content.length - 1; i++) {
            content[i] = c[i - 2];
        }
        content[0] = c[6];
        content[1] = c[7];
        alignment++;
        if (alignment >= maxAlignments) alignment = 0;
        setImage(allImgs[alignment]);
    }

    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;

    public char[] collectContent(int side) {

        char[] c = new char[3];
        for (int i = 0; i < c.length; i++) {
            int a = i + 2 * side;
            if (a >= 8) {
                a = 0;
            }
            c[i] = content[a];
        }

        return c;
    }

    public void paint(Graphics g) {
        if (highlighted) {
            g.drawImage(borderRight, x + width - 3, y - 4, null);
            g.drawImage(borderBot, x - 3, y + height - 4, null);
            g.drawImage(img, x - 3, y - 4, null);
 //            g.setColor(new Color(1.0f,1.0f,1.0f,0.5f) );
 //            g.drawRect(x-2, y-4, width, height);
        } else {
            g.drawImage(borderRight, x + width, y, null);
            g.drawImage(borderBot, x, y + height, null);
            g.drawImage(img, x, y, null);
        }

    }

}
