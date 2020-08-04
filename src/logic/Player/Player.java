package logic.Player;

import Network.NetworkData;
import logic.Tank.Tank;
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
    private String name;
    private String color;
    private int level;
    private int groupNumber;
    private transient TankTroubleMap tankTroubleMap;

    /**
     * This constructor just fill fields with input parameters
     *
     * @param name  is name of player
     * @param color is color of player's tank
     */
    public Player(String name, String color, TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
        this.name = name;
        this.color = color;
        groupNumber = -1;
    }


    /**
     * Getter method of name field
     *
     * @return name of player
     */
    public String getName() {
        return name;
    }


    public int getGroupNumber() {
        return groupNumber;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getColor() {
        return color;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public void setColor(String color) {
        this.color = color;
    }

    abstract public void updateFromServer(NetworkData data);

    abstract public NetworkData getPlayerState();

    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }
}
