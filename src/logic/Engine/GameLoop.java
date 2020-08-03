package logic.Engine;

import logic.*;
import logic.Player.UserPlayer;

import java.net.Socket;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods
 * in the while loop (update() and render()) should be
 * long running! Both must execute very quickly, without
 * any waiting and blocking!
 * <p>
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 * http://gameprogrammingpatterns.com/game-loop.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameLoop implements Runnable {

    private MapFrame canvas;
    private GameState state;
    private Socket networkSocket;
    private TankTroubleMap tankTroubleMap;
    private UserPlayer userController;
    private RunGameHandeler runGameHandeler;
    private SetPrizeTime prizeTime;

    /**
     * Constructor of this class set canvas frame and initialize time field.
     *
     * @param frame is frame of map
     */
    public GameLoop(MapFrame frame, RunGameHandeler runGameHandeler) {
        canvas = frame;
        this.tankTroubleMap = canvas.getTankTroubleMap();
        this.userController = tankTroubleMap.getController();
        canvas.addKeyListener(userController.getKeyHandler());
        canvas.addMouseListener(userController.getKeyHandler());

        this.runGameHandeler = runGameHandeler;
        prizeTime = new SetPrizeTime(tankTroubleMap);
    }

    /**
     * This must be called before the game loop starts.
     */
    public void init() {
        state = new GameState(tankTroubleMap);
    }

    @Override
    public void run() {
        boolean gameOver = false;
        while (!gameOver) {
            try {
                long start = System.currentTimeMillis();
                //
                state.update();
                canvas.render(state);
                gameOver = tankTroubleMap.isGameOver();
                prizeTime.run();
                long delay = (1000 / Constants.FPS) - (System.currentTimeMillis() - start);
                if (delay > 0)
                    Thread.sleep(delay);
            } catch (InterruptedException ignored) {
            }
        }
        SoundsOfGame gameOverMusic = new SoundsOfGame("gameOver", false);
        gameOverMusic.playSound();
        runGameHandeler.checkAllGame();
        canvas.render(state);
    }

    public GameState getState() {
        return state;
    }


    public MapFrame getCanvas() {
        return canvas;
    }

    public void setCanvas(MapFrame canvas) {
        this.canvas = canvas;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Socket getNetworkSocket() {
        return networkSocket;
    }

    public void setNetworkSocket(Socket networkSocket) {
        this.networkSocket = networkSocket;
    }

    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }

    public UserPlayer getUserController() {
        return userController;
    }

    public void setUserController(UserPlayer user) {
        this.userController = user;
    }
}