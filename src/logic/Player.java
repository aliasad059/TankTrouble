package logic;

import java.io.Serializable;

/**
 * This class represent a player.
 * This player can be bot or user player.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Player implements Serializable {
    private String name;
    private String color;

    /**
     * This constructor just fill fields with input parameters
     *
     * @param name  is name of player
     * @param color is color of player's tank
     */
    public Player(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Getter method of name field
     *
     * @return name of player
     */
    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
