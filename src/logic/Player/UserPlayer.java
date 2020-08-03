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
public class UserPlayer extends Player {
    private int level;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInNetworkMatch;
    private int winInNetworkMatch;
    private float timePlay;
    private KeyHandler keyHandler;
    private UserTank userTank;
    private boolean leaveTheMatch;
    private String dataBaseFileName;

    public UserPlayer(String name, String password, String color, int userID, int groupID
            , TankTroubleMap tankTroubleMap, int tankHealth, int bulletDamage, int wallHealth) {
        super(name, password, color, userID, groupID, tankTroubleMap, tankHealth, bulletDamage, wallHealth);
        //TODO: change leaveTheMatch field when the user leave
        init();
    }

    public UserPlayer(String name, String password, String color, TankTroubleMap tankTroubleMap) {
        super(name, password, color, tankTroubleMap);
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
        level = 0;
        loseInBotMatch = 0;
        winInBotMatch = 0;
        loseInNetworkMatch = 0;
        winInNetworkMatch = 0;
        timePlay = 0;
        userTank = new UserTank("kit\\tanks\\" + color, tankTroubleMap);
        keyHandler = new KeyHandler();
        userTank.getTankState().setKeyHandler(keyHandler);

    }

    public String getDataBaseFileName() {
        return dataBaseFileName;
    }

    public void setDataBaseFileName(String dataBaseFileName) {
        this.dataBaseFileName = dataBaseFileName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public UserTank getUserTank() {
        return userTank;
    }

    public void setUserTank(UserTank userTank) {
        this.userTank = userTank;
    }

    public boolean didLeaveTheMatch() {
        return leaveTheMatch;
    }

    public void setLeaveTheMatch(boolean leaveTheMatch) {
        this.leaveTheMatch = leaveTheMatch;
    }
}


