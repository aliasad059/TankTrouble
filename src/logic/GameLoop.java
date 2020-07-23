package logic;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods
 * in the while loop (update() and render()) should be
 * long running! Both must execute very quickly, without
 * any waiting and blocking!
 *
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 *    http://gameprogrammingpatterns.com/game-loop.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameLoop implements Runnable {

    /**
     * Frame Per Second.
     * Higher is better, but any value above 24 is fine.
     */
    public static final int FPS = 40;

    private MapFrame canvas;
    private GameState state;
    private Time time;

    public GameLoop(MapFrame frame) {
        canvas = frame;
        time=new Time();
    }

    /**
     * This must be called before the game loop starts.
     */
    public void init() {
        state = new GameState();
        for (int i = 0; i < TankTroubleMap.getUserTanks().size(); i++) {
            canvas.addKeyListener(TankTroubleMap.getUserTanks().get(i).getTankState().getKeyListener());
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
                long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
                if (delay > 0)
                    Thread.sleep(delay);
            } catch (InterruptedException ignored) {
            }
        }
        canvas.render(state);
    }
}
