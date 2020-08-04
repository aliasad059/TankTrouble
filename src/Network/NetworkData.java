package Network;

import logic.Player.Player;
import logic.Player.UserPlayer;
import logic.Tank.Tank;

import java.io.Serializable;
import java.util.ArrayList;

public class NetworkData implements Serializable {
    private Player senderPlayer;
    private boolean isUser;
    private boolean keyUp, keyDown, keyRight, keyLeft, keyFire, keyPrize;

    public NetworkData(Player player, boolean isUser) {
        this.senderPlayer = player;
        this.isUser = isUser;
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

    public Player getSenderPlayer() {
        return senderPlayer;
    }

    public void setSenderPlayer(Player senderPlayer) {
        this.senderPlayer = senderPlayer;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }

    public boolean isUser() {
        return isUser;
    }
}
