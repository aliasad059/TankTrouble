package logic;

import javax.swing.*;
import java.awt.event.*;
import java.io.Serializable;

/**
 * This is second inner class of UserTank class and handel user command for move tank or fire bullet and etc.
 */
public class KeyHandler extends MouseAdapter implements Serializable, KeyListener {
    private boolean keyPrize, keyLeft, keyUp, keyDown, keyRight, keyFire;

    public KeyHandler() {
        keyDown = false;
        keyFire = false;
        keyLeft = false;
        keyRight = false;
        keyUp = false;
        keyPrize = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            keyFire = true;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            keyPrize = true;
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            keyFire = false;
        }
        if (SwingUtilities.isRightMouseButton(e)) {
            keyPrize = false;
        }
    }

    public boolean isKeyPrize() {
        return keyPrize;
    }

    public void setKeyPrize(boolean keyPrize) {
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

    public boolean isKeyFire() {
        return keyFire;
    }

    public void setKeyFire(boolean keyFire) {
        this.keyFire = keyFire;
    }
}