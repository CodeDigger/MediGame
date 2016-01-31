package mapBuilder;

import events.MenubarListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.VolatileImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import tiles.Tile;

public class MapPanel extends Panel implements MouseListener, MouseMotionListener, KeyListener, MenubarListener {

    private Dimension panelDim;

    private Map map = new Map();

    int mapX = 0;
    int mapY = 0;

    Player player1;

    public MapPanel(Dimension dim) {
        panelDim = new Dimension(dim);
        setPreferredSize(panelDim);
        setBackground(Color.BLACK);
        
        panelDim = dim;
        
        map.generateMap();
        player1 = new Player(dim);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
    }

    VolatileImage mapImage = null;
    VolatileImage updateImage = null;
    private long updateTime;
    private long t1;
    private int fps;
    
    public void paintMap(Graphics g) {
        if (mapImage == null) {
            mapImage = createVolatileImage(map.getWidth(), map.getHeight());
            requestFocus();
        }
        map.paint(mapImage.getGraphics());
        g.drawImage(mapImage, mapX, mapY, this);
        
    }
    
    @Override
    public void paint(Graphics g) {
        long p0 = System.currentTimeMillis();
        
        paintMap(g);
        
        if (player1 != null) {
           player1.paintUI(g, this);
        }
        
        g.setColor(Color.WHITE);
        g.drawString("    - System Info -", 10, 16);
        g.drawString(" Render Time: " + (long) (System.currentTimeMillis() - p0) + " ms", 10, 32);
        g.drawString(" Update Time: " + updateTime + " ms", 10, 48);
        g.drawString(" FPS: " + fps, 10, 64);
    }

    @Override
    public void update(Graphics g) {
        if (updateImage == null) {
            updateImage = createVolatileImage(panelDim.width, panelDim.height);
            requestFocus();
        }
        
        updateImage.getGraphics().clearRect(0, 0, panelDim.width, panelDim.height);
        paint(updateImage.getGraphics());
        g.drawImage(updateImage, 0, 0, null);

        long dt = System.currentTimeMillis() - t1;
        updateTime = dt;
        if (dt != 0) {
            fps = (int) (1000 / (dt));
        }
        if (fps > 60) {
            try {
                Thread.sleep(1000 / 60 - dt);
            } catch (InterruptedException ex) {
                Logger.getLogger(MapPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            fps = 60;
        }
        t1 = System.currentTimeMillis();
    }

    void updateSize(int width, int height) {
        panelDim.setSize(width, height);
        player1.resizeUI(width, height);
        updateImage = null;
    }

    void moveMapLocation(int dX, int dY, int maxWidth, int maxHeight) {
        mapX += dX;
        mapY += dY;
        if (mapX > 0) {
            mapX = 0;
        } else if (mapX < (-map.getWidth() + maxWidth)) {
            mapX = -map.getWidth() + maxWidth;
        }
        if (mapY > 0) {
            mapY = 0;
        } else if (mapY < (-map.getHeight() + maxHeight)) {
            mapY = -map.getHeight() + maxHeight;
        }
        map.setMapX(mapX);
        map.setMapY(mapY);
    }

    @Override
    public void mouseReleased(MouseEvent mE) {
        if (mE.getButton() == MouseEvent.BUTTON1) {
            Tile tile = map.getMouseTile(mE.getY(), mE.getX());

            if (tile != null && player1.checkTile() != null) {
                int currentRow = tile.getRow();
                int currentCol = tile.getCol();

                map.playerPlaceLand(player1, currentRow, currentCol);
                //if (matchSurroundings(currentRow, currentCol, newLand)) createLand(currentRow, currentCol, newLand);
                //createLand(new City(currentRow, currentCol, tH.getImage(TileHandler.CITY_E)));
            } else {
                System.out.println("WARNING: No Tile to place!");
            }

        } else if (mE.getButton() == MouseEvent.BUTTON3) {
            Tile tile = map.getMouseTile(mE.getY(), mE.getX());
        }
        map.updateHighlight(mE);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mME) {
        map.updateHighlight(mME);
        //map.getHi(mME);
        int pointX = mME.getX()-mapX;
        int pointY = mME.getY()-mapY;
        map.highlightStacks(pointX,pointY);
        
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {

        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    private int lastMouseX;
    private int lastMouseY;

    @Override
    public void mouseDragged(MouseEvent mME) {
        if (mME.getButton() == MouseEvent.BUTTON3) {
            int dX = (int) (mME.getX() - lastMouseX);
            int dY = (int) (mME.getY() - lastMouseY);

            moveMapLocation(dX, dY, panelDim.width, panelDim.height);
            
            lastMouseX = mME.getX();
            lastMouseY = mME.getY();
        }
        map.updateHighlight(mME);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ESCAPE:
                System.out.println("_Key: Esc");
            case KeyEvent.VK_E:
                System.out.println("_Key: E");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do Nothing
    }

    @Override
    public void menuInteracted(int menuItem) {
        switch (menuItem) {
            case MenubarListener.ITEM_DRAW:
                Tile t = map.drawLand();
                player1.giveTile(t);
                repaint();
                break;
            case MenubarListener.ITEM_ROTATE:
                player1.rotateTile();
                repaint();
                /*placeAlignment++;
                 if (placeAlignment >= toBePlaced.getAlignments()) {
                 placeAlignment = 0;
                 }
                 toBePlaced.rotate(tH.getImage(newTileType+placeAlignment));*/
                break;
        }
        repaint();
    }

}
