package logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * This is second inner class of UserTank class and handel user command for move tank or fire bullet and etc.
 */
public class KeyHandler extends KeyAdapter implements Serializable {
    private boolean keyLeft, keyUp, keyDown, keyRight;
    private int keyFire, keyPrize;

    public KeyHandler() {
        keyDown = false;
        keyFire = 0;
        keyLeft = false;
        keyRight = false;
        keyUp = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
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

    @Override
    public void keyReleased(KeyEvent e) {
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

    public int getKeyPrize() {
        return keyPrize;
    }

    public void setKeyPrize(int keyPrize) {
        this.keyPrize = keyPrize;
    }


    public boolean isKeyLeft() {
        return keyLeft;
    }

    public void setKeyLeft(boolean keyLeft) {
        this.keyLeft = keyLeft;
    }

    public boolean isKeyUp() {
        return keyUp;
    }

    public void setKeyUp(boolean keyUp) {
        this.keyUp = keyUp;
    }

    public boolean isKeyDown() {
        return keyDown;
    }

    public void setKeyDown(boolean keyDown) {
        this.keyDown = keyDown;
    }

    public boolean isKeyRight() {
        return keyRight;
    }

    public void setKeyRight(boolean keyRight) {
        this.keyRight = keyRight;
    }

    public int getKeyFire() {
        return keyFire;
    }

    public void setKeyFire(int keyFire) {
        this.keyFire = keyFire;
    }
}