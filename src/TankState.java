import java.awt.event.*;

/**
 * This class holds the state of game and all of its elements.
 * This class also handles user inputs, which affect the game state.
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class TankState {

    public int locX, locY, diam;
    public boolean tankBlasted;

    private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT;
    private KeyHandler keyHandler;
    public TankState(int locX, int locY) {
        this.locX = locX;
        this.locY = locY;
        this.diam = diam;
        tankBlasted = false;
        diam = Constants.TANK_SIZE;
        //
        keyUP = false;
        keyDOWN = false;
        keyRIGHT = false;
        keyLEFT = false;
        //
        keyHandler = new KeyHandler();
    }

    /**
     * The method which updates the game state.
     */
    public void update() {
        if (keyUP)
            locY -= 8;
        if (keyDOWN)
            locY += 8;
        if (keyLEFT)
            locX -= 8;
        if (keyRIGHT)
            locX += 8;

        locX = Math.max(locX, 0);
        locX = Math.min(locX, Interface.getTankTroubleMap().getWidth()- diam);
        locY = Math.max(locY, 0);
        locY = Math.min(locY, Interface.getTankTroubleMap().getHeight() - diam);
    }


    public KeyListener getKeyListener() {
        return keyHandler;
    }

    /**
     * The keyboard handler.
     */
    class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    keyUP = true;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = true;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = true;
                    break;
                case KeyEvent.VK_ESCAPE:
                    tankBlasted = true;
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_UP:
                    keyUP = false;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = false;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = false;
                    break;
            }
        }
    }
}

