
package events;


public interface MenubarListener {
    
    public static final int ITEM_DRAW = 0;
    public static final int ITEM_ROTATE = 1;
    
    void menuInteracted(int menuItem);
}
