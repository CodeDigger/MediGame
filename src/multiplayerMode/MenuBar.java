package multiplayerMode;

import events.MenubarListener;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import utilities.ImageHandler;

public class MenuBar extends Panel implements ActionListener {

	Dimension panelDim;
        private final ArrayList<MenubarListener> menubarListeners = new ArrayList<MenubarListener>();
	Image bgi = ImageHandler.loadImage("/resources/textures/MenuBar2.png");
        Image currentDraw;
        int currentImageI;
        int currentAlignment;
	
	public MenuBar(Dimension dim) {
		panelDim = new Dimension(dim);
		setPreferredSize(panelDim);
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
		JButton Button0 = new JButton("Draw");
		JButton Button1 = new JButton("Rotate");
		add(Button0);
		add(Button1);
                Button0.addActionListener(this);
                Button1.addActionListener(this);
	}
        
        public void initMapMenu(ClientMapPanel mapPanel) {
            menubarListeners.add(mapPanel);
        }
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(bgi, 0, 0, this);
		g.drawImage(bgi, bgi.getWidth(null), 0, this);
		if (currentDraw != null) {
                    g.drawImage(currentDraw, 20, 10, this);
                }
                super.paint(g);                
	}

	@Override
	public void actionPerformed(ActionEvent aE) {
		if (aE.getActionCommand().equals("Draw")) {
                    
                    for (MenubarListener mL : menubarListeners) {
                        mL.menuInteracted(MenubarListener.ITEM_DRAW);
                    }
                    
                    //System.out.println("CurrentImageI: "+currentImageI);
                    //currentAlignment = 0;
                    //currentDraw = mapPanel.getImage(currentImageI);

                    //repaint();
                } else if (aE.getActionCommand().equals("Rotate")) {
                    /*currentImageI++;
                    currentAlignment++;
                    
                    if (currentAlignment == 4){
                        currentImageI -= 4;
                        currentAlignment = 0;
                    }*/
                    //currentDraw = mapPanel.getImage(currentImageI);
                    //mapPanel.getToBePlaced().rotate(currentDraw);
                    for (MenubarListener mL : menubarListeners) {
                        mL.menuInteracted(MenubarListener.ITEM_ROTATE);
                    }
                    //repaint();
                } else {
                    System.out.println("ERROR: Button not found!");
		}
	}

}
