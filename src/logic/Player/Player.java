package logic.Player;

import Network.NetworkData;
import logic.Tank.Tank;
import logic.TankTroubleMap;

import java.io.Serializable;

/**
 * This class represent a player for game.
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
     * This constructor just fill fields with input parameters.
     *
     * @param name           is name of player
     * @param color          is color of player's tank
     * @param tankTroubleMap is tankTrouble map that tank of bot is in
     */
    public Player(String name, String color, TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
        this.name = name;
        this.color = color;
        groupNumber = -1;
    }

    /**
     * This abstract method override in subclass (bot and user player) and update state of player
     *
     * @param data is an object from "NetworkData" class and update state of player
     */
    abstract public void updateFromServer(NetworkData data);

    /**
     * This abstract method override in subclass (bot and user player) and geet state of player to sharing
     * to others player in network game
     *
     * @return a object from NetworkData that have state of this player
     */
    abstract public NetworkData getPlayerState();

    /**
     * Getter method of name field
     *
     * @return name of player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method of groupNumber field.
     *
     * @return an integer as group of player
     */
    public int getGroupNumber() {
        return groupNumber;
    }

    /**
     * Getter method of level field.
     *
     * @return an integer as level of player
     */
    public int getLevel() {
        return level;
    }

    /**
     * This is setter method for level field.
     *
     * @param level is level of player
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Getter method of level field.
     *
     * @return an integer as level of player
     */
    public String getColor() {
        return color;
    }


    /**
     * This is setter method for groupNumber field.
     *
     * @param groupNumber is an integer as group of player
     */
    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    /**
     * This is setter method for color field.
     *
     * @param color is a string that show color of player' tank
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Getter method of tankTroubleMap field.
     *
     * @return tankTroubleMap of game
     */
    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    /**
     * This is setter method for tankTroubleMap field.
     *
     * @param tankTroubleMap is tankTrouble map as map of game
     */
    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }
}
