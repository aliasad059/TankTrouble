package logic;

/**
 * This class just store our constant in the game such as tank, wall and bullets size and etc.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class Constants {
    // Tank
    public static final int TANK_HEALTH = 100;           // default
    public static final int TANK_SIZE = 30;
    public static final int TANK_SPEED = 2;
    public static final int TANK_ROTATION_SPEED = 2;
    public static final int LOOLE_TANK_SIZE=30;

    // Bullet
    public static final int BULLET_SIZE = 10;
    public static final int BULLET_DAMAGE = 20;           // default
    public static final int BULLET_SPEED = 3;

    // Wall
    public static final int WALL_HEIGHT_HORIZONTAL = 8;
    public static final int WALL_WIDTH_HORIZONTAL = 25;
    public static final int WALL_HEIGHT_VERTICAL = 25;
    public static final int WALL_WIDTH_VERTICAL = 8;
    public static final int WALL_HEALTH = 50;             // default

    // Prize
    public static final int PRIZE_SIZE = 25;

    // Prize
    public static final int TOPOGRAPHY_SIZE = 30;

    // Game
    public static final int GAME_HEIGHT = 720;                  // 720p game resolution
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio
    public static int GAME_HEIGHT_REAL, GAME_WIDTH_REAL;
    public static final int TOP_MARGIN = 10, LEFT_MARGIN = 10; //????????????????????????? don't use
    public static final int INF = 10000;
    public static final int FPS = 200;
    public static final int STATUS_ICON_SIZE = 25;
    public static final int STATUS_MARGIN = 20;

    //lig
    public static final int LIG_MATCH_NUMBER = 3;

}
