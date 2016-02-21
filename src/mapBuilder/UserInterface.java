
package mapBuilder;

import events.ServerMessageListener;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Calendar;


public class UserInterface extends Canvas implements ServerMessageListener {

    public Image[] tileImages;
    int currentImg;
    final static int activeMessages = 3;
    Calendar cal = Calendar.getInstance();
    String[] messages;
    int fontSize = 12;
    int fontSpace = 4;
    Font messageFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);

    int borderSize;

    int tileUIwidth;
    int tileUIheight;
    int tileUIborder;
    
    int logUIheight;
    int logUIwidth;
    int logUIborder;

    int round = 20;

    public UserInterface(Dimension dim) {
        super.setSize(dim);
        borderSize = 20;
        tileUIwidth = 200;
        tileUIheight = 200;
        tileUIborder = 10;
        messages = new String[activeMessages];
        logUIborder = 10;
        logUIheight = fontSize*messages.length + fontSpace*(messages.length-1) + logUIborder*2;
        logUIwidth = dim.width-borderSize*3-tileUIwidth;
    }

    public void setTileImages(Image[] imgs, int alignment) {
        tileImages = imgs;
        currentImg = alignment;
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        logUIwidth = width-borderSize*3-tileUIwidth;
    }
    
    @Override
    public void setSize(Dimension dim) {
        super.setSize(dim);
        logUIwidth = dim.width-borderSize*3-tileUIwidth;
    }

    public void rotateTile() {
        if (tileImages != null) {
            currentImg++;
            if (currentImg >= tileImages.length)
                currentImg = 0;
        }
    }
    
    public void newMessage(String s) {
        for (int i = messages.length-1; i > 0; i--) {
            messages[i] = messages[i-1];
        }
        messages[0] = s;
    }

    public void paint(Graphics g, ImageObserver imgOb) {
        Color c = new Color(40, 40, 40, 80);
        g.setColor(c);
        g.fillRoundRect(borderSize, getHeight()-borderSize-tileUIheight, tileUIwidth, tileUIheight, round, round);
        if (tileImages != null) {
            Image img = tileImages[currentImg];
            Image imgBig = img.getScaledInstance((int)(img.getWidth(null)*1.5), (int)(img.getHeight(null)*1.5), Image.SCALE_SMOOTH);
            g.drawImage(imgBig, borderSize+tileUIborder, getHeight()-borderSize-tileUIheight+tileUIborder, imgOb);
        }
        g.fillRoundRect(borderSize+tileUIwidth+borderSize, getHeight()-borderSize-logUIheight, logUIwidth, logUIheight, round, round);
        g.setColor(Color.WHITE);
        for (int i = 0; i < messages.length; i++) {
            g.drawString(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.SECOND) +" - "+ messages[i], borderSize+tileUIwidth+borderSize+logUIborder, getHeight()-borderSize-logUIborder-fontSpace*(i)-fontSize*i);
        }
    }

    @Override
    public void serverMessage(String s) {
        newMessage(s);
    }


}
