
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
            // Initialize the global thread-pool
            ThreadPool.init();

            // Show the game menu ...

            // After the player clicks 'PLAY' ...
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
                    ThreadPool.execute(game);
                    // and the game starts ...
                }
            });
        }
}
