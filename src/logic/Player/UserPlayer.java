package logic.Player;

import Network.NetworkData;
import logic.Constants;
import logic.KeyHandler;
import logic.TankTroubleMap;
import logic.Tank.UserTank;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Console;
import java.io.Serializable;

/**
 * This class represent a user player with extend Player class.
 * Every user player has password, level and etc and addition fields.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class UserPlayer extends Player implements Serializable {
    private String password;
    private double XP;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInNetworkMatch;
    private int winInNetworkMatch;
    private float timePlay;
    private KeyHandler keyHandler;
    private int wallHealth;
    private boolean leaveTheMatch;
    private UserTank userTank;
    private String dataBaseFileName;

    /**
     * This constructor set initialize some of fields.
     *
     * @param name     is name of user
     * @param password is password od user
     * @param color    is color of player's tank
     */

    public UserPlayer(String name, String password, String color, TankTroubleMap tankTroubleMap, String dataBaseFileName) {
        super(name, color, tankTroubleMap);
        this.password = password;
        this.dataBaseFileName = dataBaseFileName;
        init();
    }

    @Override
    public void updateFromServer(NetworkData data) {
        userTank.getTankState().updateKeys(data);
        userTank.getTankState().update();
    }

    @Override
    public NetworkData getPlayerState() {
        if (userTank.isBlasted()) {
            return null;
        }
        NetworkData data = new NetworkData(this, true);
        data.setKeyDown(keyHandler.isKeyDown());
        data.setKeyFire(keyHandler.isKeyFire());
        data.setKeyLeft(keyHandler.isKeyLeft());
        data.setKeyPrize(keyHandler.isKeyPrize());
        data.setKeyUp(keyHandler.isKeyUp());
        data.setKeyRight(keyHandler.isKeyRight());
        return data;
    }

    private void init() {
        leaveTheMatch = false;
        this.setLevel(0);
        loseInBotMatch = 0;
        winInBotMatch = 0;
        loseInNetworkMatch = 0;
        winInNetworkMatch = 0;
        timePlay = 0;
        userTank = new UserTank("kit\\tanks\\" + this.getColor(), this.getTankTroubleMap());
        keyHandler = new KeyHandler();
        userTank.getTankState().setKeyHandler(keyHandler);
        wallHealth = Constants.WALL_HEALTH;
        this.dataBaseFileName = dataBaseFileName;

    }

    public void XPToLevel() {
        if (XP >= getLevel() + 2) {
            XP -= (getLevel() + 2);
            setLevel(getLevel() + 1);
        }
    }

    /**
     * Getter method of password field
     *
     * @return password of player
     */
    public String getPassword() {
        return password;
    }

    public UserTank getUserTank() {
        return userTank;
    }

    public double getXP() {
        return XP;
    }

    public int getLoseInBotMatch() {
        return loseInBotMatch;
    }

    public void setLoseInBotMatch(int loseInBotMatch) {
        this.loseInBotMatch = loseInBotMatch;
    }

    public int getWinInBotMatch() {
        return winInBotMatch;
    }

    public void setWinInBotMatch(int winInBotMatch) {
        this.winInBotMatch = winInBotMatch;
    }

    public int getLoseInNetworkMatch() {
        return loseInNetworkMatch;
    }

    public void setLoseInNetworkMatch(int loseInNetworkMatch) {
        this.loseInNetworkMatch = loseInNetworkMatch;
    }

    public int getWinInNetworkMatch() {
        return winInNetworkMatch;
    }

    public void setWinInNetworkMatch(int winInNetworkMatch) {
        this.winInNetworkMatch = winInNetworkMatch;
    }

    public void setXP(double XP) {
        this.XP = XP;
    }

    public float getTimePlay() {
        return timePlay;
    }

    public void setTimePlay(float timePlay) {
        this.timePlay = timePlay;
    }

    public KeyHandler getKeyHandler() {
        if (keyHandler == null) {
            System.out.println("null");
        }
        return keyHandler;
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public String getDataBaseFileName() {
        return dataBaseFileName;
    }

    public int getWallHealth() {
        return wallHealth;
    }

    public void setWallHealth(int wallHealth) {
        this.wallHealth = wallHealth;
    }

    public boolean didLeaveTheMatch() {
        return leaveTheMatch;
    }

    public void setLeaveTheMatch(boolean leaveTheMatch) {
        this.leaveTheMatch = leaveTheMatch;
    }
}
