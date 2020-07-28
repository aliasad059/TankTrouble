package Network;

import java.io.Serializable;

public class NetworkData implements Serializable {
    private final int senderID,receiverID;
    private boolean keyUp,keyDown, keyRIGHT, keyLEFT,keyFIRE,keyUSE_PRIZE;

    public NetworkData(int senderID, int receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public int getSenderID() {
        return senderID;
    }

    public int getReceiverID() {
        return receiverID;
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

    public boolean isKeyRIGHT() {
        return keyRIGHT;
    }

    public void setKeyRIGHT(boolean keyRIGHT) {
        this.keyRIGHT = keyRIGHT;
    }

    public boolean isKeyLEFT() {
        return keyLEFT;
    }

    public void setKeyLEFT(boolean keyLEFT) {
        this.keyLEFT = keyLEFT;
    }

    public boolean isKeyFIRE() {
        return keyFIRE;
    }

    public void setKeyFIRE(boolean keyFIRE) {
        this.keyFIRE = keyFIRE;
    }

    public boolean isKeyUSE_PRIZE() {
        return keyUSE_PRIZE;
    }

    public void setKeyUSE_PRIZE(boolean keyUSE_PRIZE) {
        this.keyUSE_PRIZE = keyUSE_PRIZE;
    }
}
