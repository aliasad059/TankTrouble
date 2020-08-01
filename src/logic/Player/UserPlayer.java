package logic.Player;

import Network.NetworkData;
import logic.Constants;
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
    private int level;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInNetworkMatch;
    private int winInNetworkMatch;
    private float timePlay;
    private transient KeyHandler keyHandler;
    private transient UserTank userTank;
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
        userTank.getTankState().update(data);
    }

    @Override
    public NetworkData getPlayerState() {
        NetworkData data = new NetworkData(userID, userTank);
        data.setKeyDown(keyHandler.keyDown);
        data.setKeyFire(keyHandler.keyFire);
        data.setKeyLeft(keyHandler.keyLeft);
        data.setKeyPrize(keyHandler.keyPrize);
        data.setKeyUp(keyHandler.keyUp);
        data.setKeyRight(keyHandler.keyRight);
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

    public boolean isLeaveTheMatch() {
        return leaveTheMatch;
    }

    public void setLeaveTheMatch(boolean leaveTheMatch) {
        this.leaveTheMatch = leaveTheMatch;
    }


    /**
     * This is second inner class of UserTank class and handel user command for move tank or fire bullet and etc.
     */
    public class KeyHandler extends KeyAdapter {
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
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    keyFire = false;
                    break;
                case KeyEvent.VK_ENTER:
                    keyPrize = true;
                    break;
                case KeyEvent.VK_UP:
                    keyUp = true;
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
                case KeyEvent.VK_ENTER:
                    keyPrize = false;
                    break;
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

}
