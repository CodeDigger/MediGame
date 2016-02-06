package menu;

import client.ConnectWindow;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JButton;

import javax.swing.JFrame;
import mapBuilder.*;
import server.MediServer;
import client.*;
import java.awt.Graphics;
import java.awt.Image;

public class Main extends JFrame implements ComponentListener, ActionListener {

    public static final int initWidth = 1200;
    public static final int initHeight = 600;

    MediServer server;
    MenuBar menuBar;
    MapPanel mapPanel;
    ConnectWindow connectWindow;

    Dimension mapDim;
    Dimension menuDim;
    int frameBarHeight;
    boolean gameRunning = false;
    
    ArrayList<JButton> buttonList = new ArrayList();
    JButton startTestButton;
    JButton startServerButton;
    JButton joinServerButton;
    
    MediClient mediClient;

    public Main() {
        super("Medi");
        
        setContentPane(new BackgroundPane("/textures/MenuBackground.jpg"));
        
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        startTestButton = new JButton("Start Test Mode");
        startServerButton = new JButton("Start Server");
        joinServerButton = new JButton("Join Server");
        buttonList.add(startTestButton);
        buttonList.add(startServerButton);
        buttonList.add(joinServerButton);
        
        
        
        for (JButton jB : buttonList) {
            add(jB);
            jB.addActionListener(this);
        }
        
        

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
            for (JButton jB : buttonList) {
                remove(jB);
            }
            setLayout(new BorderLayout());
            add(mapPanel, BorderLayout.CENTER);
            add(menuBar, BorderLayout.SOUTH);
            pack();
            frameBarHeight = getHeight() - mapPanel.getHeight() - menuBar.getHeight();
            menuBar.initMapMenu(mapPanel);
            componentResized(null);

            remove(startTestButton);
            remove(startServerButton);
            remove(joinServerButton);
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
        if (e.getSource() == startTestButton) {
            setUpGame();
            startGame();
        } else if (e.getSource() == startServerButton) {
            server = new MediServer(4444, 2);
            System.out.println("- CREATING SERVER:");
            server.start();
        } else if (e.getSource() == joinServerButton) {
            connectWindow = new ConnectWindow(this);
        } else if (e.getSource() == connectWindow.getConnectButton() ) {
            System.out.println("- CONNECTING TO SERVER:");
            String ip = connectWindow.getIP();
            int port = connectWindow.getPort();
            mediClient = new MediClient(ip, port);
            connectWindow.dispose();
        }

    }

}
