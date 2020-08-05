package Network;

import logic.Player.UserPlayer;

import java.io.Serializable;
import java.util.ArrayList;

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

    public ServerGame() {

    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getEndingMode() {
        return endingMode;
    }

    public void setEndingMode(String endingMode) {
        this.endingMode = endingMode;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
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

    public int getDWallHealth() {
        return DWallHealth;
    }

    public void setDWallHealth(int DWallHealth) {
        this.DWallHealth = DWallHealth;
    }

    public UserPlayer getCreator() {
        return creator;
    }

    public void setCreator(UserPlayer creator) {
        this.creator = creator;
    }
}
