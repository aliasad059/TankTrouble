package Network;

import logic.Engine.GameLoop;
import logic.Engine.MapFrame;
import logic.Engine.ThreadPool;
import logic.Player.UserPlayer;

import java.io.*;
import java.net.Socket;

/**
 * This class is used for testing if the network mode works well or not
 * Should be run several times for several clients
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class Client {

    public static void main(String[] args) {
        try (Socket client = new Socket(Constants.IP, Constants.port)) {
            System.out.println("Connected to server.");
            OutputStream out = client.getOutputStream();
            InputStream in = client.getInputStream();

            ObjectOutputStream socketObjectWriter = new ObjectOutputStream(out);
            ObjectInputStream socketObjectReader = new ObjectInputStream(in);

            ThreadPool.init();
            MapFrame frame = new MapFrame("Client", true, null);

            // Create and execute the game-loop
            GameLoop game = new GameLoop(frame, null);
            UserPlayer userPlayer = new UserPlayer("ali", "1234", "Gold", frame.getTankTroubleMap(), "");
            frame.getTankTroubleMap().setController(userPlayer);
            game.setUserController(frame.getTankTroubleMap().getController());
            frame.getTankTroubleMap().getUsers().add(frame.getTankTroubleMap().getController());
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
                        if (nullCounter == 0) {
                            NetworkData data = game.getUserController().getPlayerState();
                            socketObjectWriter.writeObject(data);
                            socketObjectWriter.flush();
                            Thread.sleep(Constants.PING);

                            if (data == null) {
                                nullCounter++;
                            }
                        }
                        NetworkData data = (NetworkData) socketObjectReader.readObject();
                        game.getState().update(data);
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
    }
}
