package menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import mapBuilder.*;
import server.MediServer;
import client.*;

public class Main extends JFrame implements ComponentListener, ActionListener {

    public static final int initWidth = 1200;
    public static final int initHeight = 600;

    
    MainMenu mainMenu;
    MenuBar menuBar;
    MapPanel mapPanel;
    MediServer server;
    ConnectWindow connectWindow;
    MediClient mediClient;

    Dimension mapDim;
    Dimension menuDim;
    int frameBarHeight;
    boolean gameRunning = false;

    public Main() {
        super("Medi");
        
        mainMenu = new MainMenu(this);
        setContentPane(mainMenu);
        pack();

        addComponentListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    
    private void setUpGame() {
        System.out.println(" - - -  SETTING UP GAME  - - - ");
        mapDim = new Dimension(initWidth, initHeight);
        menuDim = new Dimension(mapDim.width, 80);
        mapPanel = new MapPanel(mapDim);
        menuBar = new MenuBar(menuDim);
        gameRunning = true;
        System.out.println(" - - -  ________________  - - -");
    }

    private void startGame() {
        if (gameRunning) {
            setLayout(new BorderLayout());
            add(mapPanel, BorderLayout.CENTER);
            add(menuBar, BorderLayout.SOUTH);
            pack();
            frameBarHeight = getHeight() - mapPanel.getHeight() - menuBar.getHeight();
            menuBar.initMapMenu(mapPanel);
            componentResized(null);
        } else {
            System.out.println("- [ERROR] -: Unable to start game. Game not set up!");
        }
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
        if (gameRunning) {
            int newHeight = getHeight() - menuBar.getHeight() - frameBarHeight;
            mapPanel.updateSize(getWidth(), newHeight);
        }
        System.out.println("Window Size Changed: W: " + getWidth() + ", H: " + getHeight());
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MainMenu.START_TEST)) {
            setUpGame();
            startGame();
        } else if (e.getActionCommand().equals(MainMenu.START_SERVER)) {
            server = new MediServer(4444, 2);
            System.out.println("- CREATING SERVER:");
            server.start();
        } else if (e.getActionCommand().equals(MainMenu.JOIN_SERVER)) {
            connectWindow = new ConnectWindow(this);
        } else if (e.getSource() == connectWindow.getConnectButton()) {
            System.out.println("- CONNECTING TO SERVER:");
            String ip = connectWindow.getIP();
            int port = connectWindow.getPort();
            mediClient = new MediClient(ip, port);
            connectWindow.dispose();
        }

    }

}
