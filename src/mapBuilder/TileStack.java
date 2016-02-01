
package mapBuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import tiles.TileHandler;

class StackPosition {
    int x = 0;
    int y = 0;
    double r = 0;
}

public class TileStack {
    
    Image bottomImg;
    Image stackImg;
    
    int w;
    int h;
    
    int initStackCount;
    int stackCount;
    
    int stackX= 0;
    int stackY= 0;
    double scaleZ=1.02;
    ArrayList<StackPosition> stackPositions;
    boolean highlight = false;
    
    public TileStack(Image bottomImg, Image stackImg, int i, int initX, int initY) {
        this.bottomImg = bottomImg;
        this.stackImg = stackImg;
        w = TileHandler.getWidth();
        h = TileHandler.getHeight();
        
        initStackCount = i;
        
        stackCount = initStackCount;
        stackPositions = new ArrayList();
        
        for (int j = 0; j < stackCount; j++) {
            stackPositions.add(new StackPosition());
        }
        stackX = initX;
        stackY = initY;
        touchStack();
    }
    
    
    public void moveStack(int newX, int newY) {
        stackX = newX;
        stackY = newY;
        touchStack();
    }
    
    public void drawStack() {
        stackCount--;
        stackPositions.remove(stackCount);
        touchStack();
        System.out.println("StackCount: "+stackCount);
    }
    
    public void touchStack() {
        int i = 0;
        for (StackPosition sP : stackPositions) {
            sP.x = (int)(stackX-1*i-3*Math.random());
            sP.y = (int)(stackY-2*i-2*Math.random());
            sP.r = (-Math.random()/4+Math.random()/4)*Math.random();
            i++;
        }
    }
    
    public int getStackCount() {
        return stackCount;
    }
    
    double testX;
    double testY;
    
    public boolean checkHighlight(int mX, int mY) {
        int cornerX = stackX-4*stackCount;
        int cornerY = stackY-5*stackCount;
        if (mX > cornerX && mX < cornerX+w*Math.pow(scaleZ,stackCount) && mY > cornerY && mY < cornerY+h*Math.pow(scaleZ,stackCount)) {
            highlight = true;
        } else {
            highlight = false;
        }
        return highlight;
    }
    
    public void paintShadow(Graphics g) {
        g.setColor(new Color(40,40,40,70));
        g.fillRoundRect((int)stackX-8-stackCount*3, stackY-5-stackCount*3, 68+stackCount*3, 68+stackCount*3, 20+stackCount*5, 20+stackCount*4);
    }
    
    public void paint(Graphics g) {
        g.drawImage(bottomImg, stackX, stackY, w+6, h+6,  null);
        
        
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform aT = new AffineTransform();
        aT.translate(stackX, stackY);
        
        for (StackPosition sP : stackPositions) {
            aT.translate(-3, -4);
            aT.translate(w/2, h/2);
            aT.scale(scaleZ,scaleZ);
            aT.rotate(sP.r);
            aT.translate(-w/2, -h/2);
            g2d.drawImage(stackImg, aT, null);
            aT.rotate(-sP.r*0.8);
        }
        
        if (highlight) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(stackX-4*stackCount, stackY-5*stackCount, (int)(w*Math.pow(scaleZ,stackCount)), (int)(h*Math.pow(scaleZ,stackCount)) );
        }
        
        
    }

}
