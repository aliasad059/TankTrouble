package logic;

import Network.NetworkData;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private int level;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInServerMatch;
    private int winInServerMatch;
    private float timePlay;
    private int tankHealth;
    private int bulletDamage;
    private int wallHealth;
    private transient KeyHandler keyHandler;
    private int groupID;
    private transient UserTank userTank;
    private transient TankTroubleMap userTankTroubleMap;

    /**
     * This constructor set initialize some of fields.
     *
     * @param name      is name of user
     * @param password  is password od user
     * @param color     is color of player's tank
     */
    public UserPlayer(String name, String password, String color,int groupID,TankTroubleMap tankTroubleMap) {
        super(name, color);
        this.password = password;
        level = 0;
        loseInBotMatch = 0;
        winInBotMatch = 0;
        loseInServerMatch = 0;
        winInServerMatch = 0;
        timePlay = 0;
        tankHealth = Constants.TANK_HEALTH;
        bulletDamage = Constants.BULLET_DAMAGE;
        wallHealth = Constants.WALL_HEALTH;
        this.userTankTroubleMap = tankTroubleMap;
        userTank = new UserTank(tankHealth,bulletDamage,"kit\\tanks\\+"+color+"\\normal.png",groupID,userTankTroubleMap);
        keyHandler = new KeyHandler();
    }

    /**
     * This is second inner class of UserTank class and handel user command for move tank or fire bullet and etc.
     */
    public class KeyHandler extends KeyAdapter {
        boolean keyPrize,keyLEFT,keyUP,keyDOWN,keyRIGHT,keyFire;

        public KeyHandler() {
            keyDOWN = false;
            keyFire = false;
            keyLEFT = false;
            keyRIGHT = false;
            keyUP = false;
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
                    keyUP = true;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = true;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = true;
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
                    keyUP = false;
                    break;
                case KeyEvent.VK_DOWN:
                    keyDOWN = false;
                    break;
                case KeyEvent.VK_LEFT:
                    keyLEFT = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    keyRIGHT = false;
                    break;
            }
        }
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    /**
     * Getter method of password field
     *
     * @return password of player
     */
    public String getPassword() {
        return password;
    }


    public void setTankHealth(int tankHealth) {
        this.tankHealth = tankHealth;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public void setWallHealth(int wallHealth) {
        this.wallHealth = wallHealth;
    }


    public int getLevel() {
        return level;
    }

    public int getLoseInBotMatch() {
        return loseInBotMatch;
    }

    public int getWinInBotMatch() {
        return winInBotMatch;
    }

    public int getLoseInServerMatch() {
        return loseInServerMatch;
    }

    public int getWinInServerMatch() {
        return winInServerMatch;
    }

    public float getTimePlay() {
        return timePlay;
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public int getWallHealth() {
        return wallHealth;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLoseInBotMatch(int loseInBotMatch) {
        this.loseInBotMatch = loseInBotMatch;
    }

    public void setWinInBotMatch(int winInBotMatch) {
        this.winInBotMatch = winInBotMatch;
    }

    public void setLoseInServerMatch(int loseInServerMatch) {
        this.loseInServerMatch = loseInServerMatch;
    }

    public void setWinInServerMatch(int winInServerMatch) {
        this.winInServerMatch = winInServerMatch;
    }

    public void setTimePlay(float timePlay) {
        this.timePlay = timePlay;
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public UserTank getUserTank() {
        return userTank;
    }

    public void setUserTank(UserTank userTank) {
        this.userTank = userTank;
    }

    public TankTroubleMap getUserTankTroubleMap() {
        return userTankTroubleMap;
    }

    public void setUserTankTroubleMap(TankTroubleMap userTankTroubleMap) {
        this.userTankTroubleMap = userTankTroubleMap;
    }
    public NetworkData getUseNetworkData(){
        return null;
    }
}
