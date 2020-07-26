package logic;

/**
 * This class represent a user player with extend Player class.
 * Every user player has password, level and etc and addition fields.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class UserPlayer extends Player {
    private String password;
    private int level;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInServerMatch;
    private int winInServerMatch;
    private float timePlay;
    private UserTank userTank;

    /**
     * This constructor set initialize some of fields.
     *
     * @param name  is name of user
     * @param color is color of player's tank
     */
    public UserPlayer(String name, String color) {
        super(name, color);
        loseInBotMatch = 0;
        winInBotMatch = 0;
        loseInServerMatch = 0;
        winInServerMatch = 0;
    }

    /**
     * Getter method of password field
     *
     * @return password of player
     */
    public String getPassword() {
        return password;
    }

}
