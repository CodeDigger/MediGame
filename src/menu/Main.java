package menu;

import multiplayer.MediClient;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import mapBuilder.ClientMapPanel;
import multiplayer.MediServer;
import testMode.TmMapPanel;
import testMode.TmMenuBar;

public class Main extends JFrame implements ComponentListener, ActionListener {

    public static final int initWidth = 1200;
    public static final int initHeight = 800;

    //MENU
    MainMenu mainMenu;
    ConnectWindow connectWindow;

    //TEST MODE
    TmMenuBar tmMenuBar;
    TmMapPanel tmMapPanel;

    //MULTIPLAYER
    MediServer server;
    ClientMapPanel clientMapPanel;

    Dimension mapDim;
    Dimension menuDim;
    int frameBarHeight;
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
        menuDim = new Dimension(initWidth, menuHeight);
        mapDim = new Dimension(initWidth, initHeight - menuHeight);
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
            frameBarHeight = getHeight() - tmMapPanel.getHeight() - tmMenuBar.getHeight();
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
        clientMapPanel = new ClientMapPanel(new Dimension(initWidth, initHeight));
        new MediClient(ip, port, clientMapPanel).start();
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
            frameBarHeight = getHeight() - clientMapPanel.getHeight();
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
            int newHeight = getHeight() - tmMenuBar.getHeight() - frameBarHeight;
            tmMapPanel.updateSize(getWidth(), newHeight);
        }
        if (multiplayerGameRunning) {
            int newHeight = getHeight() - frameBarHeight;
            clientMapPanel.updateSize(getWidth(), newHeight);
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
            server = new MediServer(4444, 2);
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
