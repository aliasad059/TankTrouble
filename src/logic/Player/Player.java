package logic.Player;

import Network.NetworkData;
import logic.TankTroubleMap;

import java.io.Serializable;

/**
 * This class represent a player.
 * This player can be bot or user player.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public abstract class Player implements Serializable {
    String name;
    String color;
    String password;
    int userID;
    int groupID;
    TankTroubleMap tankTroubleMap;
    int tankHealth;
    int bulletDamage;
    int wallHealth;

    /**
     * This constructor just fill fields with input parameters
     *
     * @param name  is name of player
     * @param color is color of player's tank
     */
    public Player(String name, String password, String color, int userID, int groupID
            , TankTroubleMap tankTroubleMap,int tankHealth, int bulletDamage, int wallHealth) {
        this.name = name;
        this.color = color;
        this.password = password;
        this.userID = userID;
        this.groupID = groupID;
        this.tankTroubleMap = tankTroubleMap;
        this.wallHealth = wallHealth;
        this.bulletDamage = bulletDamage;
        this.tankHealth = tankHealth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public void setTankHealth(int tankHealth) {
        this.tankHealth = tankHealth;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public int getWallHealth() {
        return wallHealth;
    }

    public void setWallHealth(int wallHealth) {
        this.wallHealth = wallHealth;
    }

    abstract public void updateFromServer(NetworkData data);
    abstract public NetworkData getPlayerState();
}
