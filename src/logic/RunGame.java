package logic;

import logic.Engine.GameLoop;
import logic.Engine.MapFrame;
import logic.Engine.ThreadPool;

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
