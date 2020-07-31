package Network;

import logic.Engine.GameLoop;
import logic.MapFrame;
import logic.Engine.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try (Socket client = new Socket(Constants.IP, Constants.port)) {
            System.out.println("Connected to server.");
            OutputStream out = client.getOutputStream();
            InputStream in = client.getInputStream();
            ObjectOutputStream socketObjectWriter = new ObjectOutputStream(out);
            ObjectInputStream socketObjectReader = new ObjectInputStream(in);
            ThreadPool.init();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MapFrame frame = new MapFrame("walls!");
                    frame.setLocationRelativeTo(null); // put frame at center of screen
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                    frame.initBufferStrategy();
                    // Create and execute the game-loop
                    GameLoop game = new GameLoop(frame);
                    game.init();
                    // and the game starts ...
                    ThreadPool.execute(game);


                    //if the client tank is alive send network data.
                    // when the tank blasted, sends null and finishes sending network data
                    // just receives the data of the other players from server and updates
                    // sends another null to finish receiving data from server
                    int nullCounter = 0;

                    while (!game.getState().isGameOver()) {
                        try {
                            if (!game.getUser().isLeaveTheMatch()) {
                                if (nullCounter == 0) {
                                    NetworkData data = game.getUser().getPlayerState();
                                    socketObjectWriter.writeObject(data);
                                    if (data == null) {
                                        nullCounter++;
                                    }
                                }
                                game.getState().update((NetworkData) socketObjectReader.readObject());
                                Thread.sleep(Constants.PING);
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
                }
            });
            System.out.print("All messages sent.\nClosing ... ");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("done.");
    }


}
