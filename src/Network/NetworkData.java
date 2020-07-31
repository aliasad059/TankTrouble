package Network;

import logic.Tank.Tank;

import java.io.Serializable;

public class NetworkData implements Serializable {
    private Tank tank;
    private boolean keyUp,keyDown, keyRight, keyLeft, keyFire, keyPrize;
    private int senderID;

    public NetworkData(int userID ,Tank tank) {
        this.tank = tank;
        this.senderID = userID;
    }

    public Tank getTank() {
        return tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
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

    public boolean isKeyLeft() {
        return keyLeft;
    }

    public void setKeyLeft(boolean keyLeft) {
        this.keyLeft = keyLeft;
    }

    public boolean isKeyFire() {
        return keyFire;
    }

    public void setKeyFire(boolean keyFire) {
        this.keyFire = keyFire;
    }

    public boolean isKeyPrize() {
        return keyPrize;
    }

    public void setKeyPrize(boolean keyPrize) {
        this.keyPrize = keyPrize;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }
}
