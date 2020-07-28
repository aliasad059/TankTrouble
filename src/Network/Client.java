package Network;

import logic.GameLoop;
import logic.MapFrame;
import logic.ThreadPool;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    public static void main(String[] args) {

        try (Socket client = new Socket(Constants.IP, Constants.port)) {
            System.out.println("Connected to server.");
            OutputStream out = client.getOutputStream();
            InputStream in = client.getInputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
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
                    while (!game.getState().isGameOver()){
                        try {
                            objectOutputStream.writeObject(game.getState().changeStateToNetworkData());
                        } catch (IOException e) {
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
