package logic;

import logic.Player.BotPlayer;
import logic.Player.Player;
import logic.Player.UserPlayer;
import logic.Tank.AITank;
import logic.Tank.Tank;
import logic.Tank.UserTank;
import logic.Wall.DestructibleWall;
import logic.Wall.IndestructibleWall;
import logic.Wall.Wall;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represent a map for the game.
 * This type of wall has health parameter.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class TankTroubleMap {

    private static int height;
    private static int width;
    private int[][] map;
    private static ArrayList<Wall> destructibleWalls, indestructibleWalls;
    private static ArrayList<Prize> prizes;
    private ArrayList<UserPlayer> users;
    private ArrayList<BotPlayer> bots;
    private UserPlayer controller;
    private ArrayList<Bullet> bullets;
    private ArrayList<UserPlayer> audience; // user that lost his tank and want to watch game. khodkar ezafe mish baad age nakhast hazv bshe...........
    private boolean isNetwork;
    private LocalDateTime startTime;

    /**
     * This constructor initialize some fields.
     *
     * @param pathOfMap is path of text file map
     */
    public TankTroubleMap(String pathOfMap, boolean isNetwork, LocalDateTime startTime) {
        audience = new ArrayList<>();
        prizes = new ArrayList<>();
        destructibleWalls = new ArrayList<>();
        indestructibleWalls = new ArrayList<>();
        users = new ArrayList<>();
        bots = new ArrayList<>();
        bullets = new ArrayList<>();
        setHeightAndWidth(pathOfMap);
        map = new int[height][width];
        Constants.GAME_HEIGHT_REAL = height * Constants.WALL_WIDTH_HORIZONTAL;
        Constants.GAME_WIDTH_REAL = width * Constants.WALL_WIDTH_HORIZONTAL;
        readMap(pathOfMap);
        makeWalls();
        this.isNetwork = isNetwork;
        this.startTime = startTime;
        controller = new UserPlayer("ali", "1234", "Blue", 0, -1, this, 100, 50, 50);
        users.add(controller);
    }

    /**
     * This method set size of map (x axis and y axis size) based on it's text file.
     *
     * @param pathOfMap is path of text file map
     */
    private void setHeightAndWidth(String pathOfMap) {
        try (Scanner scanner = new Scanner(new File(pathOfMap))) {
            String s = "";
            int column = 0;
            int row = 0;
            while (scanner.hasNext()) {
                s = scanner.next();
                row++;
                column = s.getBytes().length;
            }
            width = column;
            height = row;
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    /**
     * This method find text file in folder "notes".
     *
     * @return these file as array
     */
    private File[] textFileFinder(String dir) {
        File file = new File(dir);
        return file.listFiles((dir1, filename) -> filename.endsWith(".txt"));
    }

    /**
     * This method read a text file as map of game.
     *
     * @param pathOfMap is address of the text file
     */
    private void readMap(String pathOfMap) {
        try (Scanner scanner = new Scanner(new File(pathOfMap))) {
            String s = "";
            int column = 0;
            int row = 0;
            while (scanner.hasNext()) {
                s = scanner.next();
                for (int i = 0; i < s.toCharArray().length; i++) {
                    map[row][i] = Integer.parseInt("" + s.toCharArray()[i]);
                }
                row++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    /**
     * This method set a random prize in a random valid coordinate of map.
     */
    public void prizeSetter() {
        if (prizes.size() < 5) {
            SecureRandom random = new SecureRandom();
            int prizeType = random.nextInt(5) + 1;
            Coordinate coordinate = freePlaceToPut(Constants.PRIZE_SIZE, Constants.PRIZE_SIZE);
            prizes.add(new Prize(prizeType, coordinate));
        }
    }

    /**
     * This method allocate walls and add them to their own list.
     */
    public void makeWalls() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width - 1; column++) {
                if (map[row][column] == 1 && map[row][column + 1] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "HORIZONTAL"));
                } else if (map[row][column] == 2 && map[row][column + 1] == 2) {
                    destructibleWalls.add(new DestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "HORIZONTAL", Constants.WALL_HEALTH));
                }
            }
        }
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height - 1; row++) {
                if (map[row][column] == 1 && map[row + 1][column] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "VERTICAL"));
                } else if (map[row][column] == 2 && map[row + 1][column] == 2) {
                    destructibleWalls.add(new DestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "VERTICAL", Constants.WALL_HEALTH));
                }
            }
        }
    }

    public static boolean checkOverLap(@NotNull ArrayList<Coordinate> p_1, ArrayList<Coordinate> p_2){
        for (Coordinate coordinate : p_1) {
            if (isInside(p_2,coordinate)) return true;
        }
        for (Coordinate coordinate : p_2) {
            if (isInside(p_1, coordinate)) return true;
        }
        return false;
    }

    /**
     * This method check overlap of input rectangle with a type of walls.
     *
     * @param coordinatesToCheck is coordinate of the rectangle
     * @param walls              is array list of this type of walls
     * @return answer as boolean
     */
    private static boolean checkOverlapWithWalls(ArrayList<Coordinate> coordinatesToCheck, @NotNull ArrayList<Wall> walls) {
        for (Wall wall : walls) {
            if (distanceBetweenTwoPoints(wall.getStartingPoint(), coordinatesToCheck.get(0)) < 2 * Constants.WALL_HEIGHT_VERTICAL) {
                if (checkOverLap(wall.getPointsArray(), coordinatesToCheck)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static double distanceBetweenTwoPoints(Coordinate p1, Coordinate p2) {
        return Math.sqrt(Math.pow(p1.getXCoordinate() - p2.getXCoordinate(), 2) + Math.pow(p1.getYCoordinate() - p2.getYCoordinate(), 2));
    }

    /**
     * This method check overlap of input rectangle with indestructible walls.
     *
     * @param coordinatesToCheck is coordinate of the rectangle
     * @return answer as boolean
     */
    public static boolean checkOverlapWithAllIndestructibleWalls(ArrayList<Coordinate> coordinatesToCheck) {
        return checkOverlapWithWalls(coordinatesToCheck, indestructibleWalls);
    }

    /**
     * This method check overlap of input rectangle with destructible walls.
     *
     * @param coordinatesToCheck is coordinate of the rectangle
     * @return answer as boolean
     */
    public static boolean checkOverlapWithAllDestructibleWalls(ArrayList<Coordinate> coordinatesToCheck) {
        return checkOverlapWithWalls(coordinatesToCheck, destructibleWalls);
    }

    /**
     * This method check overlap of input rectangle with all walls.
     *
     * @param coordinates is coordinate of the rectangle
     * @return answer as boolean
     */
    public static boolean checkOverlapWithAllWalls(ArrayList<Coordinate> coordinates) {
        return checkOverlapWithAllDestructibleWalls(coordinates) || checkOverlapWithAllIndestructibleWalls(coordinates);
    }

    /**
     * This method check overlap of input rectangle with all prizes.
     *
     * @param coordinates is coordinate of the rectangle
     * @return answer as boolean
     */
    public static boolean checkOverlapWithAllPrizes(ArrayList<Coordinate> coordinates) {
        for (Prize prize : prizes) {
            if (distanceBetweenTwoPoints(prize.getCenterCoordinate(), coordinates.get(0)) < 3 * Constants.TANK_SIZE) {
                if (checkOverLap(prize.getCoordinates(), coordinates))
                    return true;
            }
        }
        return false;
    }

    public static void setHeight(int height) {
        TankTroubleMap.height = height;
    }

    public boolean checkOverlapWithAllTanks(Tank tankToIgnore) {
        ArrayList<Tank> tanks = new ArrayList<>();
        for (UserPlayer userPlayer : users) {
            tanks.add(userPlayer.getUserTank());
        }
        for (BotPlayer bot : bots) {
            tanks.add(bot.getAiTank());
        }
        tanks.remove(tankToIgnore);
        for (Tank tank : tanks) {
            if (checkOverLap(tank.getTankCoordinates(), tankToIgnore.getTankCoordinates())) return true;
        }
        return false;
    }

    /**
     * This method shows free space for rectangle with input width and height.
     *
     * @param width  is width of rectangle
     * @param height is height of rectangle
     * @return coordinate of free space in the map
     */
    public  Coordinate freePlaceToPut(int width, int height) {
        SecureRandom random = new SecureRandom();
        boolean coordinateIsGood = false;
        Coordinate goodCoordinate = new Coordinate();
        while (!coordinateIsGood) {
            goodCoordinate.setXCoordinate(random.nextInt(Constants.GAME_WIDTH_REAL));
            goodCoordinate.setYCoordinate(random.nextInt(Constants.GAME_HEIGHT_REAL));
            ArrayList<Coordinate> goodCoordinates = new ArrayList<>();
            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() - (double) width / 2
                    , goodCoordinate.getYCoordinate() - (double) height / 2));

            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() + (double) width / 2
                    , goodCoordinate.getYCoordinate() - (double) height / 2));

            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() + (double) width / 2
                    , goodCoordinate.getYCoordinate() + (double) height / 2));

            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() - (double) width / 2
                    , goodCoordinate.getYCoordinate() + (double) height / 2));
            coordinateIsGood = !checkOverlapWithAllWalls(goodCoordinates)
                    && !checkOverlapWithAllPrizes(goodCoordinates)
                    && !checkOverlapWithAllTanks(goodCoordinates);
        }
        return goodCoordinate;
    }

    public boolean checkOverlapWithAllTanks(ArrayList<Coordinate> coordinates) {
        ArrayList<Tank> tanks = new ArrayList<>();
        for (UserPlayer userPlayer : users) {
            tanks.add(userPlayer.getUserTank());
        }
        for (BotPlayer bot : bots) {
            tanks.add(bot.getAiTank());
        }
        for (Tank tank : tanks) {
            if (distanceBetweenTwoPoints(tank.getCenterPointOfTank(), coordinates.get(0)) < 3 * Constants.TANK_SIZE) {
                if (!coordinates.equals(tank.getTankCoordinates())) {
                    if (checkOverLap(tank.getTankCoordinates(), coordinates)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Getter method of prizes field
     *
     * @return array list of prizes in the map
     */
    public static ArrayList<Prize> getPrizes() {
        return prizes;
    }

    /**
     * Getter method of destructibleWalls field
     *
     * @return array list of destructible walls in the map
     */
    public static ArrayList<Wall> getDestructibleWalls() {
        return destructibleWalls;
    }

    /**
     * Getter method of indestructibleWalls field
     *
     * @return array list of indestructible walls in the map
     */
    public static ArrayList<Wall> getIndestructibleWalls() {
        return indestructibleWalls;
    }

    static boolean onSegment(Coordinate p, Coordinate q, Coordinate r)
    {
        if (q.getXCoordinate() <= Math.max(p.getXCoordinate(), r.getXCoordinate()) &&
                q.getXCoordinate() >= Math.min(p.getXCoordinate(), r.getXCoordinate()) &&
                q.getYCoordinate() <= Math.max(p.getYCoordinate(), r.getYCoordinate()) &&
                q.getYCoordinate() >= Math.min(p.getYCoordinate(), r.getYCoordinate()))
        {
            return true;
        }
        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private static int orientation(Coordinate p, Coordinate q, Coordinate r)
    {
        int val = (int) Math.round((q.getYCoordinate() - p.getYCoordinate()) * (r.getXCoordinate() - q.getXCoordinate())
                - (q.getXCoordinate() - p.getXCoordinate()) * (r.getYCoordinate() - q.getYCoordinate()));

        if (val == 0)
        {
            return 0; // colinear
        }
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    // The function that returns true if
    // line segment 'p1q1' and 'p2q2' intersect.
    private static boolean doIntersect(Coordinate p1, Coordinate q1,
                               Coordinate p2, Coordinate q2)
    {
        // Find the four orientations needed for
        // general and special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
        {
            return true;
        }

        // Special Cases
        // p1, q1 and p2 are colinear and
        // p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1))
        {
            return true;
        }

        // p1, q1 and p2 are colinear and
        // q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1))
        {
            return true;
        }

        // p2, q2 and p1 are colinear and
        // p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2))
        {
            return true;
        }

        // p2, q2 and q1 are colinear and
        // q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2))
        {
            return true;
        }

        // Doesn't fall in any of the above cases
        return false;
    }

    // Returns true if the point p lies
    // inside the polygon[] with n vertices
    private static boolean isInside(ArrayList<Coordinate> coordinates, Coordinate pointToCheck)
    {

        // Create a point for line segment from p to infinite
        Coordinate extreme = new Coordinate(Constants.INF, pointToCheck.getYCoordinate());

        // Count intersections of the above line
        // with sides of polygon
        int count = 0, i = 0;
        do
        {
            int next = (i + 1) % 4;

            // Check if the line segment from 'p' to
            // 'extreme' intersects with the line
            // segment from 'polygon[i]' to 'polygon[next]'
            if (doIntersect(coordinates.get(i), coordinates.get(next), pointToCheck, extreme))
            {
                // If the point 'p' is colinear with line
                // segment 'i-next', then check if it lies
                // on segment. If it lies, return true, otherwise false
                if (orientation(coordinates.get(i), pointToCheck, coordinates.get(next)) == 0)
                {
                    return onSegment(coordinates.get(i), pointToCheck,
                            coordinates.get(next));
                }

                count++;
            }
            i = next;
        } while (i != 0);

        // Return true if count is odd, false otherwise
        return (count % 2 == 1); // Same as (count%2 == 1)
    }

    class SetPrizeTime implements Runnable, Serializable {
        private LocalDateTime dataOfLastPrize;
        private int prizeTime; // time that prize have to set in the map. (is a random number between 0 and 20)

        protected SetPrizeTime() {
            dataOfLastPrize = LocalDateTime.now();
            Random random = new Random();
            prizeTime = random.nextInt(2);
        }

        @Override
        public void run() {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(dataOfLastPrize, now);
            if (duration.getSeconds() >= 40 + prizeTime) {
                Random random = new Random();
                prizeTime = random.nextInt(2);
                prizeSetter();
                dataOfLastPrize = LocalDateTime.now();
            }
        }
    }

    /**
     * Getter method of bullets field
     *
     * @return array list of bullets in the map
     */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public ArrayList<UserPlayer> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserPlayer> users) {
        this.users = users;
    }

    public ArrayList<BotPlayer> getBots() {
        return bots;
    }

    public void setBots(ArrayList<BotPlayer> bots) {
        this.bots = bots;
    }

    public ArrayList<UserPlayer> getAudience() {
        return audience;
    }

    public boolean isNetwork() {
        return isNetwork;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public UserPlayer getController() {
        return controller;
    }

    public void setController(UserPlayer controller) {
        this.controller = controller;
    }
}

