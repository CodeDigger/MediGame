package menu;

import multiplayer.Client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import mapBuilder.ClientMapPanel;
import multiplayer.Server;
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

    Dimension mapDim;
    Dimension menuDim;
    int frameHeight;
    int frameWidth;
    
    boolean tmGameRunning = false;
    private boolean multiplayerGameRunning = false;

    public Main() {
        super("Medi");

        mainMenu = new MainMenu(this);
        add(mainMenu);
        pack();
        
        setLocationRelativeTo(null);

        addComponentListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        System.out.println("CLIENT: Connecting to server: " + ip + ":" + port);
        clientMapPanel = new ClientMapPanel();
        
        
        try {
            client = new Client(ip, port, clientMapPanel);
            client.initServerConnection();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        multiplayerGameRunning = true;
        System.out.println(" - - -  _________________  - - -");
    }

    private void startMultiplayerGame() {
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
            clientMapPanel.waitForStart();
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
            server = new Server(4444, 2);
            System.out.println("- CREATING SERVER:");
            server.start();
        } else if (e.getActionCommand().equals(MainMenu.JOIN_SERVER)) {
            connectWindow = new ConnectWindow(this);
        } else if (e.getSource() == connectWindow.getConnectButton()) {
            setUpMultiplayerGame();
            startMultiplayerGame();
        }

    }

}
