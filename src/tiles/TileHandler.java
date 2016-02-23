package tiles;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utilities.ImageHandler;

public class TileHandler {

    private static BufferedImage tileTextures;
    private static ArrayList<Image> imageList = new ArrayList<Image>();

    private static int tileHeight = 60;
    private static int tileWidth = 60;
    private static double tileK = ((double) tileHeight / (double) tileWidth);

    private static int spriteHeight = 60;
    private static int spriteWidth = 60;
    private static double spriteK = ((double) spriteHeight / (double) spriteWidth);

    public static final int _EMPTY = 0;
    public static final int G4 = 10;
    public static final int R2A = 14;
    public static final int C1 = 20;
    public static final int C1R2C = 24;
    public static final int C2A = 30;
    public static final int C2R2B = 32;
    public static final int C1R2A = 34;
    public static final int C2B = 40;
    public static final int C1R2D = 44;
    public static final int C2C = 50;
    public static final int R2B = 54;
    public static final int C3A = 60;
    public static final int DIFFERENT_TYPES = 12;

    public TileHandler(int tileSprites) {
        tileTextures = ImageHandler.loadImage("/resources/textures/Tiles-03.png");;
        initImages(tileSprites);
    }
    
    public static Tile initLand(int newTileType) {
        Tile toBePlaced;
        
        if (newTileType == TileHandler.G4) {
            toBePlaced = new Grass();
        } else if (newTileType == TileHandler.R2A) {
            toBePlaced = new R2A();
        } else if (newTileType == TileHandler.C1) {
            toBePlaced = new C1();
        } else if (newTileType == TileHandler.C1R2C) {
            toBePlaced = new C1R2C();
        } else if (newTileType == TileHandler.C2A) {
            toBePlaced = new C2A();
        } else if (newTileType == TileHandler.C2R2B) {
            toBePlaced = new C2R2B();
        } else if (newTileType == TileHandler.C1R2A) {
            toBePlaced = new C1R2A();
        } else if (newTileType == TileHandler.C2B) {
            toBePlaced = new C2B();
        } else if (newTileType == TileHandler.C1R2D) {
            toBePlaced = new C1R2D();
        } else if (newTileType == TileHandler.C2C) {
            toBePlaced = new C2C();
        } else if (newTileType == TileHandler.R2B) {
            toBePlaced = new R2B();
        } else if (newTileType == TileHandler.C3A) {
            toBePlaced = new C3A();
        } else {
            toBePlaced = null;
            System.out.println("- [ERROR] -: Tile initiation failed!");
        }
        int all = toBePlaced.getMaxAlignments();
        Image[] images = new Image[all];
        for (int i = 0; i < all; i++) {
            images[i] = getImage(newTileType+i);
        }
        toBePlaced.setAllImages(images);
        toBePlaced.setImage(images[0]);
        return toBePlaced;
    }

    private void initImages(int tileSprites) {

        System.out.println("Loading Tile Images:");
        System.out.println("- sH: " + spriteHeight + ", sW: " + spriteWidth + ", spriteK: " + spriteK);
        System.out.println("- tH: " + tileHeight + ", tW: " + tileWidth + ", tileK:   " + tileK);
        if (tileK != spriteK) {
            System.out.println("WARNING: Unmatching k-values!");
        }

        Image img;
        int tCol = 0; // t - texture
        int tRow = 0;
        for (int i = 0; i < tileSprites; i++) {
            img = ImageHandler.cutScaleImage(tileTextures, tCol, tRow, spriteWidth, spriteHeight, tileWidth, tileHeight);
            imageList.add(img);
            if (tCol < (tileTextures.getWidth() / spriteWidth) - 1) {
                tCol++;
            } else {
                tRow++;
                tCol = 0;
            }
        }
    }

    public static Image getImage(int i) {
        return imageList.get(i);
    }

    public static Image getBorderImage(int i) {
        Image[] imgs = new Image[2];
        imgs[0] = ImageHandler.cutScaleImageByPixels(tileTextures,
                spriteWidth * 8, spriteHeight * 2, spriteWidth, 10, spriteWidth, spriteHeight, tileWidth, tileHeight);
        imgs[1] = ImageHandler.cutScaleImageByPixels(tileTextures,
                spriteWidth * 9, spriteHeight * 1, 10, spriteHeight + 10, spriteWidth, spriteHeight, tileWidth, tileHeight);
        return imgs[i];
    }

    /*public void createBorder(Tile tile, int i) {
     Image background = tile.getImage();
     Image layer = getImage(i);
     Image[] images = {background,layer};
     Image newImage = ImageHandler.mergeImages(images);
     tile.setImage(newImage);
     }*/
    public static int getHeight() {
        return tileHeight;
    }

    public static int getWidth() {
        return tileWidth;
    }

    public static double getTileK() {
        return tileK;
    }

}
