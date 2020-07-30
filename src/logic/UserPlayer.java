package logic;

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

    /**
     * This constructor set initialize some of fields.
     *
     * @param name      is name of user
     * @param password  is password od user
     * @param color     is color of player's tank
     */
    public UserPlayer(String name, String password, String color) {
        super(name, color);
        this.password=password;
        level=0;
        loseInBotMatch = 0;
        winInBotMatch = 0;
        loseInServerMatch = 0;
        winInServerMatch = 0;
        timePlay=0;
        tankHealth=Constants.TANK_HEALTH;
        bulletDamage=Constants.BULLET_DAMAGE;
        wallHealth=Constants.WALL_HEALTH;
        //userTank=new UserTank(tankHealth,bulletDamage,"kit\\tanks\\"+color);
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


}
