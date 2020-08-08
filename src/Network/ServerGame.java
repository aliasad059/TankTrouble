package Network;

import logic.Player.UserPlayer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent game for the server.
 * And contain some information about game as fields such as gameName, gameType etc.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class ServerGame implements Serializable {
    private String serverIP;
    private int port;
    private String gameName;
    private String gameType; // solo or multi
    private String endingMode; // death match o
    private int playersNumber;
    private int tankHealth;
    private int bulletDamage;
    private int DWallHealth;
    private UserPlayer creator;

    /**
     * This constructor just fill fields with input parameters.
     *
     * @param gameName      is string as name of game
     * @param serverIP      is string as IP of game
     * @param port          is an integer as port of this game in the server
     * @param gameType      is string that show type of game (solo/team)
     * @param endingMode    is a string that show match type (death match/lig match)
     * @param playersNumber is number of player that need for this game
     * @param tankHealth    is health of tanks of player in this game
     * @param bulletDamage  is dame of bullet of player's tanks in this game
     * @param DWallHealth   is health of destroyable walls in this game
     */
    public ServerGame(String gameName, String serverIP, int port, String gameType, String endingMode
            , int playersNumber, int tankHealth, int bulletDamage, int DWallHealth) {
        this.serverIP = serverIP;
        this.port = port;
        this.gameName = gameName;
        this.gameType = gameType;
        this.endingMode = endingMode;
        this.playersNumber = playersNumber;
        this.tankHealth = tankHealth;
        this.bulletDamage = bulletDamage;
        this.DWallHealth = DWallHealth;
    }

    /**
     * This is second constructor of this game and just allocate our object.
     */
    public ServerGame() {

    }

    /**
     * Getter method of serverIP field
     *
     * @return a string as IP of server
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Getter method of port field
     *
     * @return a string as port of this game in the server
     */
    public int getPort() {
        return port;
    }


    /**
     * This is setter method for port field.
     *
     * @param port is a string that you wanna set as port of game
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Getter method of gameName field
     *
     * @return a string as name of this game in the server
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * This is setter method for gameName field.
     *
     * @param gameName is a string that you wanna set as name of game
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * Getter method of gameType field
     *
     * @return a string as type of this game
     */
    public String getGameType() {
        return gameType;
    }

    /**
     * This is setter method for gameType field.
     *
     * @param gameType is a string that you wanna set as type of game
     */
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    /**
     * Getter method of endingMode field
     *
     * @return a string as ending mode (match type) of game
     */
    public String getEndingMode() {
        return endingMode;
    }

    /**
     * This is setter method for endingMode field.
     *
     * @param endingMode is a string that you wanna set as ending mode (match type) of game
     */
    public void setEndingMode(String endingMode) {
        this.endingMode = endingMode;
    }

    /**
     * Getter method of playersNumber field
     *
     * @return an integer as number of player
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * This is setter method for playersNumber field.
     *
     * @param playersNumber is an integer that you wanna set as number of player in this game
     */
    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    /**
     * Getter method of bulletDamage field
     *
     * @return an integer as damage of bullets of players' tank
     */
    public int getBulletDamage() {
        return bulletDamage;
    }

    /**
     * This is setter method for bulletDamage field.
     *
     * @param bulletDamage is an integer that you wanna set as damage of bullets of players' tank
     */
    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    /**
     * Getter method of creator field
     *
     * @return a userPlayer object as creator of this game
     */
    public UserPlayer getCreator() {
        return creator;
    }

    /**
     * This is setter method for creator field.
     *
     * @param creator is an userPlayer that you wanna creator of this game
     */
    public void setCreator(UserPlayer creator) {
        this.creator = creator;
    }

    /**
     * This is getter method for Destructible walls health
     *
     * @return Destructible walls health of game
     */
    public int getDWallHealth() {
        return DWallHealth;
    }

    /**
     * This is setter method for DWallHealth field.
     *
     * @param DWallHealth is an integer that you wanna set as health of destroyable walls in this game
     */
    public void setDWallHealth(int DWallHealth) {
        this.DWallHealth = DWallHealth;
    }

    /**
     * This is getter method for tanks health
     *
     * @return tanks health of game
     */
    public int getTankHealth() {
        return tankHealth;
    }

    /**
     * This is setter method for tankHealth field.
     *
     * @param tankHealth is an integer that you wanna set as health of tank in this game
     */
    public void setTankHealth(int tankHealth) {
        this.tankHealth = tankHealth;
    }
}
