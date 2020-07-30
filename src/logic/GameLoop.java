package logic;

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
    private SetPrizeTime time;
    private Socket networkSocket;

    /**
     * Constructor of this class set canvas frame and initialize time field.
     *
     * @param frame is frame of map
     */
    public GameLoop(MapFrame frame) {
        canvas = frame;
        time = new SetPrizeTime();
    }

    /**
     * Constructor of this class set canvas frame, time filed and a network socket
     * @param frame is frame of map
     * @param socketToTransferDate a socket to transfer data from and to server
     */
    public GameLoop(MapFrame frame , Socket socketToTransferDate){
        canvas = frame;
        time = new SetPrizeTime();
        networkSocket = socketToTransferDate;
    }

    /**
     * This must be called before the game loop starts.
     */
    public void init() {
        state = new GameState();
        for (int i = 0; i < TankTroubleMap.getUserTanks().size(); i++) {
            canvas.addKeyListener(TankTroubleMap.getUserTanks().get(i).getTankState().getKeyHandler()); // key handle is equal to key listener
        }
        //TODO: add key listener of the main tank
    }

    @Override
    public void run() {
        boolean gameOver = false;
        while (!gameOver) {
            try {
                long start = System.currentTimeMillis();
                //
                time.run();
                state.update();
                canvas.render(state);
                gameOver = state.isGameOver();
                //
                long delay = (1000 / Constants.FPS) - (System.currentTimeMillis() - start);
                if (delay > 0)
                    Thread.sleep(delay);
            } catch (InterruptedException ignored) {
            }
        }
        canvas.render(state);
    }

    public GameState getState() {
        return state;
    }
}
