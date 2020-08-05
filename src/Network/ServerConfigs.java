package Network;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ServerConfigs implements Serializable {
    ArrayList<ServerGame> serverGames;
    private String serverName;
    private String serverIP;

    public ServerConfigs(String serverName, String serverIP) {
        serverGames = new ArrayList<>();
        this.serverName = serverName;
        this.serverIP = serverIP;
        saveConfigs();
    }

    public void addNewGame(ServerGame newGame) {
        serverGames.add(newGame);
        saveConfigs();
    }

    public void saveConfigs() {
        try {
            ObjectOutputStream objectWriter = new ObjectOutputStream(new FileOutputStream("serverConfigs\\" + serverName + "_server.config"));
            objectWriter.writeObject(this);
            objectWriter.flush();
            objectWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerName() {
        return serverName;
    }

    public String getServerIP() {
        return serverIP;
    }

    public ArrayList<ServerGame> getServerGames() {
        return serverGames;
    }

}
