package Network;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent config of setting of server.
 * Such as server name and it's IP and also array list of games that there are in the server.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class ServerConfigs implements Serializable {
    private ArrayList<ServerGame> serverGames;
    private String serverName;
    private String serverIP;

    /**
     * This constructor set config part based on input parameters and save that config.
     *
     * @param serverName is string as name of server
     * @param serverIP   is string as IP of server
     */
    public ServerConfigs(String serverName, String serverIP) {
        serverGames = new ArrayList<>();
        this.serverName = serverName;
        this.serverIP = serverIP;
        saveConfigs();
    }

    /**
     * This method get a game (serverGame object) and add it to the list and save changes.
     *
     * @param newGame is game that you wanna add to list
     */
    public void addNewGame(ServerGame newGame) {
        serverGames.add(newGame);
        saveConfigs();
    }

    /**
     * This method save config of server in a file.
     */
    public void saveConfigs() {
        try {
            ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream("serverConfigs/" + serverName + "_server.config"));
            objectWriter.writeObject(this);
            objectWriter.flush();
            objectWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method of serverName field
     *
     * @return a string as name of server
     */
    public String getServerName() {
        return serverName;
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
     * Getter method of serverGames field
     *
     * @return array list of game in the server
     */
    public ArrayList<ServerGame> getServerGames() {
        return serverGames;
    }


}
