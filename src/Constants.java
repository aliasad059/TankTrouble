import java.security.PublicKey;
import java.util.DoubleSummaryStatistics;

public class Constants {
    public static final int TANK_SIZE = 4;
    public static final int BULLET_SIZE = 1;
    public static final int TANK_HEALTH = 100;
    public static final int TANK_SPEED = 10,TANK_ROTATION_SPEED = 10;
    public static final int BULLET_POWER = 20 ,BULLET_SPEED =(2*TANK_SPEED) ;
    public static  int WALL_HEIGHT_HORIZONTAL = 7, WALL_WIDTH_HORIZONTAL = 25
            ,WALL_HEIGHT_VERTICAL = 25, WALL_WIDTH_VERTICAL = 7;
    public static final int GAME_HEIGHT = 720;                  // 720p game resolution
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio

}
