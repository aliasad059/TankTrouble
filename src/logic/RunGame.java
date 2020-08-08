package logic;

import Network.ClientConfigs;
import Network.Constants;
import Network.NetworkData;
import logic.Engine.GameLoop;
import logic.Engine.MapFrame;
import logic.Engine.ThreadPool;
import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * This class get a map frame and run game based on map frame.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class RunGame {
    private MapFrame mapFrame;
    private RunGameHandler runGameHandler;
    private GameLoop game;

    /**
     * This constructor just fill field based on input parameters.
     *
     * @param mapFrame       is map of game that you wanna run
     * @param runGameHandler is a run game handler that get game and handle its finish based on its match type
     */
    public RunGame(MapFrame mapFrame, RunGameHandler runGameHandler) {
        this.mapFrame = mapFrame;
        this.runGameHandler = runGameHandler;
    }

    /**
     * This method run vs computer game and add it to thread pool.
     */
    public void run() {
        // Initialize the global thread-pool
        ThreadPool.init();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and execute the game-loop
                game = new GameLoop(mapFrame, runGameHandler);
                //GameLoop serverGame = new GameLoop(frame1,frame2);
                game.init();
                ThreadPool.execute(game);
                // and the game starts ...
            }
        });
    }


    /**
     * This method run network game and add it to thread pool after all needed client was connected to the server.
     *
     * @param IP   is IP of server
     * @param port is port of server (that show a game in the server)
     */
    public void run(String IP, int port) {
        ThreadPool.init();
        EventQueue.invokeLater(() -> {

            game = new GameLoop(mapFrame, runGameHandler);
            game.setUserController(mapFrame.getTankTroubleMap().getController());
            game.init();
            try (Socket client = new Socket(IP, port)) {
                System.out.println("Connected to server.");
                OutputStream out = client.getOutputStream();
                InputStream in = client.getInputStream();

                ObjectOutputStream socketObjectWriter = new ObjectOutputStream(out);
                ObjectInputStream socketObjectReader = new ObjectInputStream(in);

                ClientConfigs clientConfigs = (ClientConfigs) socketObjectReader.readObject();

                game.getTankTroubleMap().getController().setGroupNumber(clientConfigs.getGroupNumber());
                game.getTankTroubleMap().getController().getUserTank().setHealth(clientConfigs.getTankHealth());
                game.getTankTroubleMap().getController().getUserTank().setBulletDamage(clientConfigs.getBulletDamage());
                game.getTankTroubleMap().getController().setWallHealth(clientConfigs.getDWallHealth());

                ThreadPool.execute(game);

                //if the client tank is alive send network data.
                // when the tank blasted, sends null and finishes sending network data
                // just receives the data of the other players from server and updates
                // sends another null to finish receiving data from server
                int nullCounter = 0;

                while (!(game.getTankTroubleMap().isGameOver())) {
                    try {
                        if (!game.getUserController().didLeaveTheMatch()) {
                            if (nullCounter == 0) {
                                NetworkData data = game.getUserController().getPlayerState();
                                socketObjectWriter.writeObject(data);
                                //Thread.sleep(Constants.PING);
                                socketObjectWriter.flush();

                                if (data == null) {
                                    nullCounter++;
                                }
                            }
                            game.getState().update((NetworkData) socketObjectReader.readObject());
                        } else {
                            for (int i = 0; i < 2 - nullCounter; i++) {
                                socketObjectWriter.writeObject(null);
                            }
                            break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("All messages sent.\nClosing ... ");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Getter method of mapFrame field
     *
     * @return map frame of game
     */
    public MapFrame getMapFrame() {
        return mapFrame;
    }

    /**
     * Getter method of game field
     *
     * @return game loop of game
     */
    public GameLoop getGame() {
        return game;
    }

    /**
     * This is setter method for game field.
     *
     * @param game is game loop that you wanna set for field
     */
    public void setGame(GameLoop game) {
        this.game = game;
    }


}
