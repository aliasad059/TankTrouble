package logic;

//import graphic.Interface;

import logic.Engine.GameLoop;
import logic.Engine.MapFrame;
import logic.Engine.ThreadPool;

import javax.swing.*;
import java.awt.*;

// just for now.....
// we have to delete this class nad create tun class
public class Main {
    public static void main(String[] args) {
        // Initialize the global thread-pool
        ThreadPool.init();
        // Show the game menu ...
//        Interface in = new Interface();
//        in.runAndShow();
        // After the player clicks 'PLAY' ...
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MapFrame frame = new MapFrame("walls!", false);
                frame.setLocationRelativeTo(null); // put frame at center of screen
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.initBufferStrategy();
                // Create and execute the game-loop
                GameLoop game = new GameLoop(frame, new RunGameHandeler());
                //GameLoop serverGame = new GameLoop(frame1,frame2);
                game.init();
                ThreadPool.execute(game);
                // and the game starts ...
            }
        });
    }
}
