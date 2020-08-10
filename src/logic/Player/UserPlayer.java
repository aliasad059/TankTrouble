package logic.Player;

import Network.NetworkData;
import logic.Constants;
import logic.KeyHandler;
import logic.Tank.UserTank;
import logic.TankTroubleMap;

import java.io.Serializable;

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
        userTank = new UserTank("kit/tanks/" + color, tankTroubleMap);
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

    /**
     * This method is for update network game and update tank state.
     *
     * @param data is an object from NetworkData that update bot tate
     */
    @Override
    public void updateFromServer(NetworkData data) {
        userTank.getTankState().updateKeys(data);
        userTank.getTankState().update();
    }

    /**
     * This method get player state for sharing to another player in network game.
     */
    @Override
    public NetworkData getPlayerState() {
        if (userTank.isBlasted()) {
            return null;
        }
        NetworkData data = new NetworkData(this, true);
        data.setKeyDown(keyHandler.isKeyDown());
        data.setKeyFire(keyHandler.getKeyFire() + 1);//????????????????
        data.setKeyLeft(keyHandler.isKeyLeft());
        data.setKeyPrize(keyHandler.getKeyPrize() + 1);//?????????????
        data.setKeyUp(keyHandler.isKeyUp());
        data.setKeyRight(keyHandler.isKeyRight());
        return data;
    }

    /**
     * This method changing XP to level if it was enough.
     */
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

    /**
     * This is setter method for wallHealth field.
     *
     * @param wallHealth is health of destroyable wall
     */
    public void setWallHealth(int wallHealth) {
        this.wallHealth = wallHealth;
    }

    /**
     * Getter method of userTank field.
     *
     * @return tank of user
     */
    public UserTank getUserTank() {
        return userTank;
    }

    /**
     * Getter method of loseInBotMatch field.
     *
     * @return number of player's lose in vs computer
     */
    public int getLoseInBotMatch() {
        return loseInBotMatch;
    }

    /**
     * This is setter method for loseInBotMatch field.
     *
     * @param loseInBotMatch is number of player's lose in vs computer
     */
    public void setLoseInBotMatch(int loseInBotMatch) {
        this.loseInBotMatch = loseInBotMatch;
    }

    /**
     * Getter method of winInBotMatch field.
     *
     * @return number of player's win in vs computer
     */
    public int getWinInBotMatch() {
        return winInBotMatch;
    }

    /**
     * This is setter method for winInBotMatch field.
     *
     * @param winInBotMatch is number of player's win in vs computer
     */
    public void setWinInBotMatch(int winInBotMatch) {
        this.winInBotMatch = winInBotMatch;
    }

    /**
     * Getter method of loseInNetworkMatch field.
     *
     * @return number of player's lose in network
     */
    public int getLoseInNetworkMatch() {
        return loseInNetworkMatch;
    }

    /**
     * This is setter method for loseInNetworkMatch field.
     *
     * @param loseInNetworkMatch is number of player's lose in network game
     */
    public void setLoseInNetworkMatch(int loseInNetworkMatch) {
        this.loseInNetworkMatch = loseInNetworkMatch;
    }

    /**
     * Getter method of winInNetworkMatch field.
     *
     * @return number of player's win in network
     */
    public int getWinInNetworkMatch() {
        return winInNetworkMatch;
    }

    /**
     * This is setter method for winInNetworkMatch field.
     *
     * @param winInNetworkMatch is number of player's win in network game
     */
    public void setWinInNetworkMatch(int winInNetworkMatch) {
        this.winInNetworkMatch = winInNetworkMatch;
    }

    /**
     * Getter method of XP field.
     *
     * @return value of xp of player
     */
    public double getXP() {
        return XP;
    }

    /**
     * This is setter method for XP field.
     *
     * @param XP is a double as XP of player
     */
    public void setXP(double XP) {
        this.XP = XP;
    }

    /**
     * This is setter method for timePlay field.
     *
     * @param timePlay is a float as timePlay of player
     */
    public void setTimePlay(float timePlay) {
        this.timePlay = timePlay;
    }

    /**
     * Getter method of KeyHandler field.
     *
     * @return KeyHandler field of player
     */
    public KeyHandler getKeyHandler() {
        if (keyHandler == null) {
            System.out.println("null");
        }
        return keyHandler;
    }

    /**
     * Getter method of KeyHandler field.
     *
     * @return KeyHandler field of player
     */
    public float getTimePlay() {
        return timePlay;
    }

    /**
     * Getter method of dataBaseFileName field.
     *
     * @return a string as name of player file
     */
    public String getDataBaseFileName() {
        return dataBaseFileName;
    }

    /**
     * Getter method of wallHealth field.
     *
     * @return an integer as health of destroyable wall
     */
    public int getWallHealth() {
        return wallHealth;
    }

    /**
     * Getter method of leaveTheMatch field.
     *
     * @return boolean that show player leave or stay and watch
     */
    public boolean didLeaveTheMatch() {
        return leaveTheMatch;
    }
}
