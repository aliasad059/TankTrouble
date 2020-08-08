package logic;

import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * This class handle keys of game that player use them and based on update state of users' tank.
 * This class do that with extend "KeyAdapter" class.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class KeyHandler extends KeyAdapter implements Serializable {
    private boolean keyLeft, keyUp, keyDown, keyRight;
    private int keyFire, keyPrize;

    /**
     * this is only constructor of this class and set false for boolean fields and set 0 for integer fields.
     */
    public KeyHandler() {
        keyDown = false;
        keyFire = 0;
        keyPrize = 0;
        keyLeft = false;
        keyRight = false;
        keyUp = false;
    }

    /**
     * When a key pressed this method get event and update key field based on.
     *
     * @param e is event that happened
     */
    @Override
    public void keyPressed(@NotNull KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                keyFire++;
                break;
            case KeyEvent.VK_ENTER:
                keyPrize++;
                break;
            case KeyEvent.VK_UP:
                this.keyUp = true;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = true;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = true;
                break;
        }
    }

    /**
     * When a key released this method get event and update key field based on.
     *
     * @param e is event that happened
     */
    @Override
    public void keyReleased(@NotNull KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                keyUp = false;
                break;
            case KeyEvent.VK_DOWN:
                keyDown = false;
                break;
            case KeyEvent.VK_LEFT:
                keyLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                keyRight = false;
                break;
        }
    }

    /**
     * Getter method of keyPrize field
     *
     * @return an integer as value of keyPrize
     */
    public int getKeyPrize() {
        return keyPrize;
    }

    /**
     * Getter method of keyLeft field
     *
     * @return an boolean as value of keyLeft
     */
    public boolean isKeyLeft() {
        return keyLeft;
    }

    /**
     * Getter method of keyUp field
     *
     * @return an boolean as value of keyUp
     */
    public boolean isKeyUp() {
        return keyUp;
    }

    /**
     * Getter method of keyDown field
     *
     * @return an boolean as value of keyDown
     */
    public boolean isKeyDown() {
        return keyDown;
    }

    /**
     * Getter method of keyRight field
     *
     * @return an boolean as value of keyRight
     */
    public boolean isKeyRight() {
        return keyRight;
    }


    /**
     * Getter method of keyFire field
     *
     * @return an integer as value of keyFire
     */
    public int getKeyFire() {
        return keyFire;
    }

}