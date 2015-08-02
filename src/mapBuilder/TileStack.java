
package mapBuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import tiles.TileHandler;


public class TileStack {
    
    Image bottomImg;
    Image stackImg;
    
    int w;
    int h;
    
    int initStackCount;
    static final int paintDivider = 4;
    int paintCount;
    
    int stackX= -100;
    int stackY= -100;
    
    int[] stack;
    
    int[] xS;
    int[] yS;
    
    public TileStack(Image bottomImg, Image stackImg, int i) {
        this.bottomImg = bottomImg;
        this.stackImg = stackImg;
        w = TileHandler.getWidth();
        h = TileHandler.getHeight();
        
        initStackCount = i;
        stack = new int[initStackCount];
        
        paintCount = (int)initStackCount/paintDivider;
        xS = new int[paintCount];
        yS = new int[paintCount];
        touchStack();
    }
    
    public void moveStack(int newX, int newY) {
        stackX = newX;
        stackY = newY;
    }
    
    public void touchStack() {
        xS[0] = stackX;
        yS[0] = stackY;
        for (int i = 1; i < paintCount; i++) {
            xS[i]=(int)(xS[i-1]-1-3*Math.random());
            yS[i]=(int)(yS[i-1]-2-2*Math.random());
        }
    }
    
    public void paintShadow(Graphics g) {
        g.setColor(new Color(40,40,40,70));
        g.fillRoundRect((int)stackX-28, stackY-42, 90, 106, 80, 80);
    }
    
    public void paint(Graphics g) {
        g.drawImage(bottomImg, stackX, stackY, w+6, h+6,  null);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform aT = new AffineTransform();
        aT.translate(stackX, stackY);
        double rotation;
        
        for (int i = 1; i < paintCount; i++) {
            aT.translate(-3, -4);
            aT.translate(w/2, h/2);
            rotation = (-Math.random()/4+Math.random()/4)*Math.random();
            aT.rotate(rotation);
            aT.scale(1.02,1.02);
            aT.translate(-w/2, -h/2);
            g2d.drawImage(stackImg, aT, null);
            aT.rotate(-rotation*0.8);
            //g2d.drawImage(stackImg, xS[i], yS[i], (int)(TileHandler.getWidth()+(i*1.14)), (int)(TileHandler.getHeight()+(i*1.14)), null);
        }
        
    }
    
}
