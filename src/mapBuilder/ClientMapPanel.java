package mapBuilder;

import events.MapPanelListener;
import tiles.TileStack;
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
import utilities.AudioHandler;

public class ClientMapPanel extends Panel implements MouseListener, MouseMotionListener, KeyListener, MenubarListener {

    private final static int initWidth = 1200;
    private final static int initHeight = 700;
    private Dimension panelDim;

    private ClientMapHandler mapHandler;

    int mapX = 0;
    int mapY = 0;

    private int lastMouseX;
    private int lastMouseY;

    ClientPlayer player;

    AudioHandler audioHandler;
    MapPanelListener mPL;

    UserInterface uI;
    boolean waitingForStart = true;
    private boolean tileRequestMode = true;
    private boolean playing = false;
    private int requestedTileStackNumber;

    public ClientMapPanel() {
        panelDim = new Dimension(initWidth, initHeight);
        setPreferredSize(panelDim);
        setBackground(Color.BLACK);
        mapHandler = new ClientMapHandler();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        audioHandler = new AudioHandler();
        audioHandler.play("/resources/music/BGM-InGame-SkySpisn.wav");
    }

    public void setPlayer(ClientPlayer p) {
        player = p;
    }

    public void setUI(UserInterface uI) {
        this.uI = uI;
    }


    VolatileImage mapImage = null;
    VolatileImage updateImage = null;
    private long updateTime;
    private long t1;
    private int fps;

    public void paintMap(Graphics g) {
        if (mapImage == null) {
            mapImage = createVolatileImage(mapHandler.getMapWidth(), mapHandler.getMapHeight());
            requestFocus();
        }
        mapHandler.paint(mapImage.getGraphics());
        g.drawImage(mapImage, mapX, mapY, this);
    }

    public void waitForStart() {
        while(waitingForStart) {
            //Do nothing
        }
    }

    public void runGame() {
        waitingForStart = false;
    }

    @Override
    public void paint(Graphics g) {
        long p0 = System.currentTimeMillis();

        paintMap(g);

        if (uI != null) {
            uI.paint(g, this);
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
                Logger.getLogger(ClientMapPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            fps = 60;
        }
        t1 = System.currentTimeMillis();
    }

    public void updateSize(int width, int height) {
        panelDim.setSize(width, height);
        uI.setSize(width, height);
        updateImage = null;
    }

    public void moveMapLocation(int dX, int dY, int maxWidth, int maxHeight) {
        mapX += dX;
        mapY += dY;
        if (mapX > 0) {
            mapX = 0;
        } else if (mapX < (-mapHandler.getMapWidth() + maxWidth)) {
            mapX = -mapHandler.getMapWidth() + maxWidth;
        }
        if (mapY > 0) {
            mapY = 0;
        } else if (mapY < (-mapHandler.getMapHeight() + maxHeight)) {
            mapY = -mapHandler.getMapHeight() + maxHeight;
        }
        mapHandler.setMapX(mapX);
        mapHandler.setMapY(mapY);
    }

    @Override
    public void mouseReleased(MouseEvent mE) {
        if (mE.getButton() == MouseEvent.BUTTON1) {
            pointX = mE.getX() - mapX;
            pointY = mE.getY() - mapY;
            TileStack tS = mapHandler.highlightStacks(pointX, pointY);
            
            if (tS != null && tileRequestMode) { //Tile request
                tS.drawStack();
                mPL.tileDrawn(mapHandler.getTileStackNumber(tS));
                tileRequestMode = false;
                repaint();
            } else { // Tile placement
                Tile mapCell = mapHandler.getMouseTile(mE.getY(), mE.getX());
                Tile playerTile = player.checkTile();
                if (mapCell != null && playing && playerTile != null) {
                    int currentRow = mapCell.getRow();
                    int currentCol = mapCell.getCol();
                    
                    if(mapHandler.playerPlaceLand(playerTile, currentRow, currentCol)) {
                        player.takeTile();
                        uI.setTileImages(null, 0);
                        mPL.tilePlaced(playerTile.getRow(), playerTile.getCol(), playerTile.getType(), playerTile.getAlignment());
                        playing = false;
                        tileRequestMode = true;
                    }
                }
            }

        }
        mapHandler.updateHighlight(mE);
        repaint();
    }

    int pointX = 0;
    int pointY = 0;

    @Override
    public void mouseMoved(MouseEvent mME) {
        mapHandler.updateHighlight(mME);
        //map.getHi(mME);
        pointX = mME.getX()-mapX;
        pointY = mME.getY()-mapY;
        mapHandler.highlightStacks(pointX,pointY);

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    @Override
    public void mouseDragged(MouseEvent mME) {
        if (mME.getButton() == MouseEvent.BUTTON1 || mME.getButton() == MouseEvent.NOBUTTON) {
            int dX = mME.getX() - lastMouseX;
            int dY = mME.getY() - lastMouseY;

            moveMapLocation(dX, dY, panelDim.width, panelDim.height);

            lastMouseX = mME.getX();
            lastMouseY = mME.getY();
        }
        mapHandler.updateHighlight(mME);
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
                break;
            case KeyEvent.VK_SPACE:
                player.rotateTile();
                break;
            case KeyEvent.VK_Q:
                mPL.clientDisconnect();
                break;
            case KeyEvent.VK_D:
                mPL.chatMessage("MESSAGE: DAVID Hej oooo AUAUUA!");
                System.out.println("MESSAGE SENT");
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
                //Tile t = map.drawLand();
                //player1.giveTile(t);
                repaint();
                break;
            case MenubarListener.ITEM_ROTATE:
                player.rotateTile();
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

    public void permissionToPlay() {
        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean getTileRequestMode() {
        return tileRequestMode;
    }

    public ClientMapHandler getMapHandler() {
        return mapHandler;
    }

    public int getRequestedTileStackNumber() {
        return requestedTileStackNumber;
    }

    public void addMapPanelListener(MapPanelListener mPL) {
        this.mPL = mPL;
    }

}
