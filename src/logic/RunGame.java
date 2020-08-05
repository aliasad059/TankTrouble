package logic;

import Network.Constants;
import Network.NetworkData;
import Network.ServerGame;
import logic.Engine.GameLoop;
import logic.Engine.MapFrame;
import logic.Engine.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.*;

public class RunGame {
    private MapFrame mapFrame;
    private RunGameHandler runGameHandler;
    private GameLoop game;

    public RunGame(MapFrame mapFrame, RunGameHandler runGameHandler) {
        this.mapFrame = mapFrame;
        this.runGameHandler = runGameHandler;
    }

    public void run() {
        // Initialize the global thread-pool
        ThreadPool.init();
        // Show the game menu ...
//        Interface in = new Interface();
//        in.runAndShow();
        // After the player clicks 'PLAY' ...
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
    public void run(String IP, int port) {
        ThreadPool.init();
        EventQueue.invokeLater(() -> {
            game = new GameLoop(mapFrame, runGameHandler);
            game.init();
            try (Socket client = new Socket(IP, port)) {
                System.out.println("Connected to server.");
                InputStream in = client.getInputStream();

                int groupNumber = Integer.parseInt(new String(in.readAllBytes()));
                game.getTankTroubleMap().getController().setGroupNumber(groupNumber);

                OutputStream out = client.getOutputStream();
                ObjectOutputStream socketObjectWriter = new ObjectOutputStream(out);
                ObjectInputStream socketObjectReader = new ObjectInputStream(in);


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
                                Thread.sleep(Constants.PING);

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
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print("All messages sent.\nClosing ... ");
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public MapFrame getMapFrame() {
        return mapFrame;
    }

    public void setMapFrame(MapFrame mapFrame) {
        this.mapFrame = mapFrame;
    }

    public GameLoop getGame() {
        return game;
    }

    public void setGame(GameLoop game) {
        this.game = game;
    }


}
