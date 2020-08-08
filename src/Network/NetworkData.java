package Network;

import logic.Player.UserPlayer;
import java.io.Serializable;

/**
 * This class store key event of user that connected to the server include movement, fire etc.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class NetworkData implements Serializable {
    private UserPlayer senderPlayer;
    private boolean isUser;
    private boolean keyUp, keyDown, keyRight, keyLeft;
    private int keyFire, keyPrize;

    /**
     * this constructor just fill fields based on input parameters.
     *
     * @param player is a user player that send his state
     * @param isUser show player is user or not
     */
    public NetworkData(UserPlayer player, boolean isUser) {
        this.senderPlayer = player;
        this.isUser = isUser;
        keyFire = 0;
        keyPrize = 0;
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
     * This is setter method for keyUp field.
     *
     * @param keyUp is boolean that show state of this key
     */
    public void setKeyUp(boolean keyUp) {
        this.keyUp = keyUp;
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
     * This is setter method for keyDown field.
     *
     * @param keyDown is boolean that show state of this key
     */
    public void setKeyDown(boolean keyDown) {
        this.keyDown = keyDown;
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
     * This is setter method for keyRight field.
     *
     * @param keyRight is boolean that show state of this key
     */
    public void setKeyRight(boolean keyRight) {
        this.keyRight = keyRight;
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
     * This is setter method for keyLeft field.
     *
     * @param keyLeft is boolean that show state of this key
     */
    public void setKeyLeft(boolean keyLeft) {
        this.keyLeft = keyLeft;
    }

    /**
     * Getter method of senderPlayer field
     *
     * @return user player as sender player
     */
    public UserPlayer getSenderPlayer() {
        return senderPlayer;
    }

    /**
     * Getter method of isUser field
     *
     * @return an boolean as value this field
     */
    public boolean isUser() {
        return isUser;
    }

    public int getKeyFire() {
        return keyFire;
    }

    public void setKeyFire(int keyFire) {
        this.keyFire = keyFire;
    }

    public int getKeyPrize() {
        return keyPrize;
    }

    public void setKeyPrize(int keyPrize) {
        this.keyPrize = keyPrize;
    }
}
