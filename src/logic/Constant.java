package logic;

/**
 * This class just store our constant in the game such as tank, wall and bullets size and etc.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
class Constants {
    public static final int TANK_SIZE = 30;
    public static final int BULLET_SIZE = 10;
    public static final int TANK_HEALTH = 100;
    public static final int TANK_SPEED = 2,
            TANK_ROTATION_SPEED = 2; //means 5 degree
    public static final int BULLET_POWER = 20, BULLET_SPEED = (2 * TANK_SPEED);
    public static final int WALL_HEIGHT_HORIZONTAL = 8, WALL_WIDTH_HORIZONTAL = 25, WALL_HEIGHT_VERTICAL = 25, WALL_WIDTH_VERTICAL = 8;
    public static final int GAME_HEIGHT = 720;                  // 720p game resolution
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio
    public static final int TOP_MARGIN = 10, LEFT_MARGIN = 10;
    public static final int PRIZE_SIZE = 50;
    public static final int WALL_HEALTH = 50;
    public static int GAME_HEIGHT_REAL, GAME_WIDTH_REAL;
    public static final int LOOLE_TANK_SIZE = 20;
    public static final int INF = 10000;
    public static final int FPS = 100;
    public static final int STATUS_ICON_SIZE = 25;
    public static final int STATUS_MARGIN = 20;

}
