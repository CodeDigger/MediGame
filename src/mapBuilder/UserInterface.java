
package mapBuilder;

import events.MessageListener;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.Calendar;
import tiles.TileHandler;


public class UserInterface extends Canvas implements MessageListener {

    public Image[] tileImages;
    Image imgBig;
    int currentImg;
    final static int activeMessages = 3;
    Calendar cal = Calendar.getInstance();
    String[] messages;
    
    // Layout variables
    int spaceBorder;
    int insideBorder;
    int round = 20;
    
    // Tile UI variables
    double uiTileScale;
    int tileUIwidth;
    int tileUIheight;
    
    // Turn UI variables
    int turnUIheight;
    boolean playersTurn;
    
    // Log UI variables
    int logUIheight;
    int logUIwidth;
    int fontSize = 12;
    int fontSpace = 4;
    Font messageFont = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize);

    public UserInterface(Dimension dim) {
        super.setSize(dim);
        spaceBorder = 20;
        uiTileScale = 1.8;
        insideBorder = 10;
        tileUIwidth = (int)(TileHandler.getWidth()*uiTileScale)+insideBorder*2;
        tileUIheight = (int)(TileHandler.getHeight()*uiTileScale)+insideBorder*2;
        turnUIheight = 16;
        messages = new String[activeMessages];
        logUIheight = fontSize*messages.length + fontSpace*(messages.length-1) + insideBorder*2;
        logUIwidth = dim.width-spaceBorder*3-tileUIwidth;
    }

    public void setTileImages(Image[] imgs, int alignment) {
        tileImages = imgs;
        currentImg = alignment;
        if (imgs != null) {
            Image img = tileImages[currentImg];
            imgBig = img.getScaledInstance((int)(img.getWidth(null)*uiTileScale), (int)(img.getHeight(null)*uiTileScale), Image.SCALE_SMOOTH);
        } else {
            imgBig = null;
        }
        
    }
    
    public void setTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }
    
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        logUIwidth = width-spaceBorder*3-tileUIwidth;
    }
    
    @Override
    public void setSize(Dimension dim) {
        super.setSize(dim);
        logUIwidth = dim.width-spaceBorder*3-tileUIwidth;
    }

    public void rotateTile() {
        if (tileImages != null) {
            currentImg++;
            if (currentImg >= tileImages.length)
                currentImg = 0;
        }
        Image img = tileImages[currentImg];
        imgBig = img.getScaledInstance((int)(img.getWidth(null)*uiTileScale), (int)(img.getHeight(null)*uiTileScale), Image.SCALE_SMOOTH);
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
        g.fillRoundRect(spaceBorder, getHeight()-spaceBorder-tileUIheight-insideBorder-turnUIheight, tileUIwidth, tileUIheight+insideBorder+turnUIheight, round, round);
        if (imgBig != null) {
            g.drawImage(imgBig, spaceBorder+insideBorder, getHeight()-spaceBorder-tileUIheight+insideBorder, imgOb);
        }
        if (playersTurn) {
            g.setColor(Color.GREEN);
            g.drawString("It's your turn!", spaceBorder+insideBorder,getHeight()-spaceBorder-tileUIheight-turnUIheight/2);
        } else {
            g.setColor(Color.YELLOW);
            g.drawString("XXXXXXXXX's turn", spaceBorder+insideBorder,getHeight()-spaceBorder-tileUIheight-turnUIheight/2);
        }
        g.setColor(c);
        g.fillRoundRect(spaceBorder+tileUIwidth+spaceBorder, getHeight()-spaceBorder-logUIheight, logUIwidth, logUIheight, round, round);
        g.setColor(Color.WHITE);
        for (int i = 0; i < messages.length; i++) {
            g.setFont(messageFont);
            g.drawString(cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.SECOND) +" - "+ messages[i], spaceBorder+tileUIwidth+spaceBorder+insideBorder, getHeight()-spaceBorder-insideBorder-fontSpace*(i)-fontSize*i);
        }
    }

    @Override
    public void receivedMessage(String s) {
        newMessage(s);
    }


}
