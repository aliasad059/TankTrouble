package logic.Engine;

import logic.*;
import logic.Player.UserPlayer;
import org.jetbrains.annotations.NotNull;

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

    /**
     * Frame Per Second.
     * Higher is better, but any value above 24 is fine.
     */
    private MapFrame canvas;
    private GameState state;
    private Socket networkSocket;
    private TankTroubleMap tankTroubleMap;
    private UserPlayer userController;
    private RunGameHandler runGameHandler;
    private SetPrizeTime prizeTime;

    /**
     * Constructor of this class set some field as canvas frame, run game handler etc and initialize time field.
     *
     * @param frame          is frame that show map of game
     * @param runGameHandler is a object of "RunGameHandler" class that handle game and its finish and also update
     *                       user after game
     */
    public GameLoop(@NotNull MapFrame frame, RunGameHandler runGameHandler) {
        canvas = frame;
        this.tankTroubleMap = frame.getTankTroubleMap();
        this.userController = tankTroubleMap.getController();
        this.runGameHandler = runGameHandler;
        prizeTime = new SetPrizeTime(tankTroubleMap);
    }

    /**
     * This must be called before the game loop starts.
     */
    public void init() {
        state = new GameState(tankTroubleMap);
        canvas.addKeyListener(userController.getUserTank().getTankState().getKeyHandler());
    }

    /**
     * Because of this class implement "Runnable" class we have to implement this method.
     * This method do different work as set prize in map and update state etc.
     */
    @Override
    public void run() {
        boolean gameOver = false;
        while (!gameOver) {
            try {
                long start = System.currentTimeMillis();
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
        //System.out.println("game loop....................");
        SoundsOfGame gameOverMusic = new SoundsOfGame("gameOver", false);
        gameOverMusic.playSound();
        runGameHandler.checkAllGame();
        canvas.render(state);
    }

    /**
     * Getter method of state field
     *
     * @return state of game as GameState object
     */
    public GameState getState() {
        return state;
    }

    /**
     * This is setter method for state field.
     *
     * @param state is state of our game
     */
    public void setState(GameState state) {
        this.state = state;
    }

    /**
     * Getter method of tankTroubleMap field.
     *
     * @return tankTroubleMap of game
     */
    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    /**
     * This is setter method for tankTroubleMap field.
     *
     * @param tankTroubleMap is tankTrouble map as map of game
     */
    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }

    /**
     * Getter method of userController field.
     *
     * @return UserPlayer as controller of map of game
     */
    public UserPlayer getUserController() {
        return userController;
    }

    public void setUserController(UserPlayer userController) {
        this.userController = userController;
    }
    public MapFrame getCanvas() {
        return canvas;
    }
}
