/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package events;

/**
 *
 * @author David
 */
public interface MapPanelListener {
    
    void tileRequested(int stackNumber);
    void tilePlaced(int row, int col, int tileType, int alignment);
    void chatMessage(String s);
    void clientDisconnect();
    void ready();
}
