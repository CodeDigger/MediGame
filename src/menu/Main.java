package menu;

import multiplayerServer.Client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import multiplayerMode.ClientMapPanel;
import multiplayerServer.Server;
import testMode.TmMapPanel;
import testMode.TmMenuBar;

public class Main extends JFrame implements ComponentListener, ActionListener {
    
    //MENU
    MainMenu mainMenu;
    ConnectWindow connectWindow;

    //TEST MODE
    TmMenuBar tmMenuBar;
    TmMapPanel tmMapPanel;

    //MULTIPLAYER
    Server server;
    ClientMapPanel clientMapPanel;
    Client client;
    private boolean serverRunning = false;

    Dimension mapDim;
    Dimension menuDim;
    int frameHeight;
    int frameWidth;
    
    private boolean tmGameRunning = false;
    private boolean multiplayerGameRunning = false;

    public Main() {
        super("Medi");

        mainMenu = new MainMenu(this);
        add(mainMenu);
        pack();
        
        setLocationRelativeTo(null);

        addComponentListener(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void setUpTMGame() {
        remove(mainMenu);
        mainMenu = null;
        System.out.println(" - - -  SETTING UP GAME  - - - ");
        int menuHeight = 80;
        menuDim = new Dimension(800, menuHeight);
        mapDim = new Dimension(800, 800 - menuHeight);
        tmMapPanel = new TmMapPanel(mapDim);
        tmMenuBar = new TmMenuBar(menuDim);
        tmGameRunning = true;
        System.out.println(" - - -  ________________  - - -");
    }

    private void startTMGame() {
        if (tmGameRunning) {
            setLayout(new BorderLayout());
            add(tmMapPanel, BorderLayout.CENTER);
            add(tmMenuBar, BorderLayout.SOUTH);
            pack();
            frameHeight = getHeight() - tmMapPanel.getHeight() - tmMenuBar.getHeight();
            componentResized(null);
            tmMenuBar.initMapMenu(tmMapPanel);
        } else {
            System.out.println("- [ERROR] -: Unable to start game. Game not set up!");
        }
        pack();
    }

    private void setUpMultiplayerGame() {
        System.out.println(" - - -  PREPARING TO JOIN  - - - ");
        String ip = connectWindow.getIP();
        int port = connectWindow.getPort();
        String playerName = connectWindow.getPlayerName();
        System.out.println("CLIENT: Connecting to server: " + ip + ":" + port);
        clientMapPanel = new ClientMapPanel(this);
        
        
        try {
            client = new Client(ip, port, playerName, clientMapPanel);
            client.initServerConnection();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        multiplayerGameRunning = true;
        System.out.println(" - - -  _________________  - - -");
    }

    public void startMultiplayerGame() {
        if (multiplayerGameRunning) {
            connectWindow.dispose();
            remove(mainMenu);
            mainMenu = null;
            setLayout(new BorderLayout());
            add(clientMapPanel);
            pack();
            setLocationRelativeTo(null);
            frameHeight = getHeight() - clientMapPanel.getHeight();
            frameWidth = getWidth() - clientMapPanel.getWidth();
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
        if (tmGameRunning) {
            int newHeight = getHeight() - tmMenuBar.getHeight() - frameHeight;
            tmMapPanel.updateSize(getWidth(), newHeight);
        }
        if (multiplayerGameRunning) {
            int newHeight = getHeight() - frameHeight;
            int newWidth = getWidth() - frameWidth;
            clientMapPanel.updateSize(newWidth, newHeight);
        }
        System.out.println("Window Size Changed: W: " + getWidth() + ", H: " + getHeight());
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
        //No implementation
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
        // No implementation
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
        // No implementation
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MainMenu.START_TEST)) {
            setUpTMGame();
            startTMGame();
        } else if (e.getActionCommand().equals(MainMenu.START_SERVER)) {
            server = new Server(4444, 2); //TODO fix number of clients
            System.out.println("- CREATING SERVER:");
            server.start();
            serverRunning = true;
        } else if (e.getActionCommand().equals(MainMenu.JOIN_SERVER)) {
            connectWindow = new ConnectWindow(this);
            if (serverRunning) connectWindow.setIpString("localhost");
        } else if (e.getSource() == connectWindow.getConnectButton()) {
            setUpMultiplayerGame();
        }

    }

}
