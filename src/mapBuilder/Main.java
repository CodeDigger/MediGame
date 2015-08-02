package mapBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

public class Main extends JFrame implements ComponentListener {

    public static final int initWidth = 1200;
    public static final int initHeight = 600;

    MenuBar menuBar;
    MapPanel mapPanel;

    Dimension mapDim;
    Dimension menuDim;
    int frameBarHeight;

    public Main() {
        super("IsometricTiles");
        setLayout(new BorderLayout());
        mapDim = new Dimension(initWidth, initHeight);
        menuDim = new Dimension(mapDim.width, 80);
        mapPanel = new MapPanel(mapDim);
        menuBar = new MenuBar(menuDim);
        add(mapPanel, BorderLayout.CENTER);
        add(menuBar, BorderLayout.SOUTH);
        pack();
        frameBarHeight = getHeight() - mapPanel.getHeight() - menuBar.getHeight();

        menuBar.initMapMenu(mapPanel);
        addComponentListener(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
        int newHeight = getHeight() - menuBar.getHeight() - frameBarHeight;
        mapPanel.updateSize(getWidth(), newHeight);
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

}
