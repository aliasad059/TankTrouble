package logic.Player;

import Network.NetworkData;
import logic.*;
import logic.Engine.GameLoop;
import logic.Engine.MapFrame;
import logic.Engine.ThreadPool;
import logic.Tank.UserTank;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

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
    private double XP;
    private int loseInBotMatch;
    private int winInBotMatch;
    private int loseInNetworkMatch;
    private int winInNetworkMatch;
    private float timePlay;
    private KeyHandler keyHandler;
    private int wallHealth;
    private boolean leaveTheMatch;
    private UserTank userTank;
    private String dataBaseFileName;

    /**
     * This constructor set initialize some of fields.
     *
     * @param name     is name of user
     * @param password is password od user
     * @param color    is color of player's tank
     */
    public UserPlayer(String name, String password, String color, TankTroubleMap tankTroubleMap, String dataBaseFileName) {
        super(name, color, tankTroubleMap);
        this.password = password;
        userTank = new UserTank("kit\\tanks\\" + color, tankTroubleMap);
        setLevel(0);
        XP = 0;
        loseInBotMatch = 0;
        winInBotMatch = 0;
        loseInNetworkMatch = 0;
        winInNetworkMatch = 0;
        timePlay = 0;
        wallHealth = Constants.WALL_HEALTH;
        this.dataBaseFileName = dataBaseFileName;
        keyHandler = userTank.getTankState().getKeyHandler();
    }

    @Override
    public void updateFromServer(NetworkData networkData) {
        userTank.getTankState().updateKeys(networkData);
        userTank.getTankState().update();
    }

    @Override
    public NetworkData getPlayerState() {
        if (userTank.isBlasted()) {
            return null;
        }
        NetworkData data = new NetworkData(this, true);
        data.setKeyDown(keyHandler.isKeyDown());
//        data.setKeyFire(keyHandler.isKeyFire());
        data.setKeyLeft(keyHandler.isKeyLeft());
        data.setKeyPrize(keyHandler.isKeyPrize());
        data.setKeyUp(keyHandler.isKeyUp());
        data.setKeyRight(keyHandler.isKeyRight());
        return data;
    }

    public void XPToLevel() {
        if (XP >= getLevel() + 2) {
            XP -= (getLevel() + 2);
            setLevel(getLevel() + 1);
        }
    }

    /*
    public void client() {
        try (Socket client = new Socket(Network.Constants.IP, Network.Constants.port)) {
            System.out.println("Connected to server.");
            OutputStream out = client.getOutputStream();
            InputStream in = client.getInputStream();
            ObjectOutputStream socketObjectWriter = new ObjectOutputStream(out);
            ObjectInputStream socketObjectReader = new ObjectInputStream(in);

            ThreadPool.init();
            MapFrame frame = new MapFrame("Client", true);
            frame.setLocationRelativeTo(null); // put frame at center of screen
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.initBufferStrategy();
            // Create and execute the game-loop
            GameLoop game = new GameLoop(frame, null);
            game.init();
            // and the game starts ...
            ThreadPool.execute(game);


            //if the client tank is alive send network data.
            // when the tank blasted, sends null and finishes sending network data
            // just receives the data of the other players from server and updates
            // sends another null to finish receiving data from server
            int nullCounter = 0;
            while (!(game.getTankTroubleMap().isGameOver())) {
                try {
                    if (!game.getUserController().didLeaveTheMatch()) {
//                        System.out.println(1);
                        if (nullCounter == 0) {
                            NetworkData data = game.getUserController().getPlayerState();
                            socketObjectWriter.writeObject(data);
                            Thread.sleep(Network.Constants.PING);

                            if (data == null) {
                                nullCounter++;
                            }
                        }
//                        System.out.println(2);
                        game.getState().update((NetworkData) socketObjectReader.readObject());
//                        System.out.println(3);
                    } else {
                        for (int i = 0; i < 2 - nullCounter; i++) {
                            socketObjectWriter.writeObject(null);
                        }
                        break;
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.print("All messages sent.\nClosing ... ");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("done.");
    }

     */

    /**
     * Getter method of password field
     *
     * @return password of player
     */
    public String getPassword() {
        return password;
    }

    public void setWallHealth(int wallHealth) {
        this.wallHealth = wallHealth;
    }

    public UserTank getUserTank() {
        return userTank;
    }

    public int getLoseInBotMatch() {
        return loseInBotMatch;
    }

    public void setLoseInBotMatch(int loseInBotMatch) {
        this.loseInBotMatch = loseInBotMatch;
    }

    public int getWinInBotMatch() {
        return winInBotMatch;
    }

    public void setWinInBotMatch(int winInBotMatch) {
        this.winInBotMatch = winInBotMatch;
    }

    public int getLoseInNetworkMatch() {
        return loseInNetworkMatch;
    }

    public void setLoseInNetworkMatch(int loseInNetworkMatch) {
        this.loseInNetworkMatch = loseInNetworkMatch;
    }

    public int getWinInNetworkMatch() {
        return winInNetworkMatch;
    }

    public void setWinInNetworkMatch(int winInNetworkMatch) {
        this.winInNetworkMatch = winInNetworkMatch;
    }

    public double getXP() {
        return XP;
    }

    public void setXP(double XP) {
        this.XP = XP;
    }

    public void setTimePlay(float timePlay) {
        this.timePlay = timePlay;
    }

    public KeyHandler getKeyHandler() {
        if (keyHandler == null) {
            System.out.println("null");
        }
        return keyHandler;
    }

    public void setKeyHandler(KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public float getTimePlay() {
        return timePlay;
    }

    public String getDataBaseFileName() {
        return dataBaseFileName;
    }

    public int getWallHealth() {
        return wallHealth;
    }

    public boolean didLeaveTheMatch() {
        return leaveTheMatch;
    }

    public void setLeaveTheMatch(boolean leaveTheMatch) {
        this.leaveTheMatch = leaveTheMatch;
    }


}
