
package testMode;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;


public class TmUserInterface extends Canvas {
    
    public Image[] tileImages;
    int currentImg;
    
    int borderSize;
    
    int tileUIwidth;
    int tileUIheight;
    int tileUIborder;
    
    
    public TmUserInterface(Dimension dim) {
        super.setSize(dim);
        borderSize = 20;
        tileUIwidth = 200;
        tileUIheight = 200;
        tileUIborder = 10;
    }
    
    public void setTileImages(Image[] imgs, int alignment) {
        tileImages = imgs;
        currentImg = alignment;
    }
    
    public void rotateTile() {
        currentImg++;
        if (currentImg >= tileImages.length) currentImg=0;
    }
    
    public void paint(Graphics g, ImageObserver imgOb) {
        Color c = new Color(40, 40, 40, 80);
        g.setColor(c);
        g.fillRoundRect(borderSize, getHeight()-borderSize-tileUIheight, tileUIwidth, tileUIheight, 20, 20);
        if (tileImages != null) {
            Image img = tileImages[currentImg];
            Image imgBig = img.getScaledInstance((int)(img.getWidth(null)*1.5), (int)(img.getHeight(null)*1.5), Image.SCALE_SMOOTH);
            g.drawImage(imgBig, borderSize+tileUIborder, getHeight()-borderSize-tileUIheight+tileUIborder, imgOb);
        }
    }


}
