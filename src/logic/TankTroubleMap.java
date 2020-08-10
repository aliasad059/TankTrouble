package logic;

import logic.Player.BotPlayer;
import logic.Player.UserPlayer;
import logic.Tank.Tank;
import logic.Wall.DestructibleWall;
import logic.Wall.IndestructibleWall;
import logic.Wall.Wall;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This class represent a game with of tank trouble.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class TankTroubleMap {

    private static int height;
    private static int width;
    private int[][] map;
    private static ArrayList<DestructibleWall> destructibleWalls;
    private static ArrayList<IndestructibleWall> indestructibleWalls;
    private static ArrayList<Prize> prizes;
    private ArrayList<UserPlayer> users;
    private ArrayList<BotPlayer> bots;
    private UserPlayer controller;
    private ArrayList<Bullet> bullets;
    private ArrayList<UserPlayer> audience; // user that lost his tank and want to watch game. khodkar ezafe mish baad age nakhast hazv bshe...........
    private boolean isNetwork;
    private LocalDateTime startTime;
    private boolean gameOver;
    private int winnerGroup;
    private RunGameHandler runGameHandler;
    private static ArrayList<Topography> topographies;

    /**
     * This constructor initialize some fields and fill others based on input parameters.
     *
     * @param pathOfMap      is path of text file map
     * @param isNetwork      show this game is network game or not
     * @param startTime      is starting time of game
     * @param runGameHandler is a run game handler that get game and handle its finish based on its match type
     */
    public TankTroubleMap(String pathOfMap, boolean isNetwork, LocalDateTime startTime, RunGameHandler runGameHandler) {
        topographies = new ArrayList<>();
        audience = new ArrayList<>();
        prizes = new ArrayList<>();
        destructibleWalls = new ArrayList<>();
        indestructibleWalls = new ArrayList<>();
        users = new ArrayList<>();
        bots = new ArrayList<>();
        bullets = new ArrayList<>();
        setHeightAndWidth(pathOfMap);
        map = new int[height][width];
        Constants.GAME_HEIGHT_REAL = (height - 1) * Constants.WALL_WIDTH_HORIZONTAL;
        Constants.GAME_WIDTH_REAL = (width - 1) * Constants.WALL_WIDTH_HORIZONTAL;
        readMap(pathOfMap);
        makeWalls();
        makeRocksAndTrees();
        this.isNetwork = isNetwork;
        this.startTime = startTime;
        //controller = new UserPlayer("ali", "1234", "Gold",  this, "test");
        //users.add(controller);
        gameOver = false;
        winnerGroup = -1;
        this.runGameHandler = runGameHandler;
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

    /**
     * This method calculate distance between two points in a plain.
     *
     * @param p1 is first point
     * @param p2 is second point
     * @return a double as value of distance
     */
    private static double distanceBetweenTwoPoints(@NotNull Coordinate p1, @NotNull Coordinate p2) {
        return Math.sqrt(Math.pow(p1.getXCoordinate() - p2.getXCoordinate(), 2) + Math.pow(p1.getYCoordinate() - p2.getYCoordinate(), 2));
    }

    /**
     * This method check overlap of input rectangle with indestructible walls.
     *
     * @param coordinatesToCheck is coordinate of the rectangle
     * @return answer as boolean
     */
    public static boolean checkOverlapWithAllIndestructibleWalls(ArrayList<Coordinate> coordinatesToCheck) {
        ArrayList<Wall> walls = new ArrayList<>();
        walls.addAll(indestructibleWalls);
        return checkOverlapWithWalls(coordinatesToCheck, walls);
    }

    /**
     * This method check overlap of input rectangle with destructible walls.
     *
     * @param coordinatesToCheck is coordinate of the rectangle
     * @return answer as boolean
     */
    public static boolean checkOverlapWithAllDestructibleWalls(ArrayList<Coordinate> coordinatesToCheck) {
        ArrayList<Wall> walls = new ArrayList<>();
        walls.addAll(destructibleWalls);
        return checkOverlapWithWalls(coordinatesToCheck, walls);
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

    /**
     * Getter method of destructibleWalls field
     *
     * @return array list of destructible walls in the map
     */
    public static ArrayList<DestructibleWall> getDestructibleWalls() {
        return destructibleWalls;
    }

    /**
     * Getter method of indestructibleWalls field
     *
     * @return array list of indestructible walls in the map
     */
    public static ArrayList<IndestructibleWall> getIndestructibleWalls() {
        return indestructibleWalls;
    }

    static boolean onSegment(@NotNull Coordinate p, @NotNull Coordinate q, @NotNull Coordinate r) {
        return q.getXCoordinate() <= Math.max(p.getXCoordinate(), r.getXCoordinate()) &&
                q.getXCoordinate() >= Math.min(p.getXCoordinate(), r.getXCoordinate()) &&
                q.getYCoordinate() <= Math.max(p.getYCoordinate(), r.getYCoordinate()) &&
                q.getYCoordinate() >= Math.min(p.getYCoordinate(), r.getYCoordinate());
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
     * This method allocate walls and add them to their own list.
     */
    public void makeWalls() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width - 1; column++) {
                if (map[row][column] == 1 && map[row][column + 1] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "HORIZONTAL"));
                } else if ((map[row][column] == 2 && map[row][column + 1] == 2)
                        || (map[row][column] == 2 && map[row][column + 1] == 1)
                        || (map[row][column] == 1 && map[row][column + 1] == 2)
                ) {
                    destructibleWalls.add(new DestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "HORIZONTAL", Constants.WALL_HEALTH));
                }
            }
        }
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height - 1; row++) {
                if (map[row][column] == 1 && map[row + 1][column] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "VERTICAL"));
                } else if ((map[row][column] == 2 && map[row + 1][column] == 2)
                        || (map[row][column] == 2 && map[row + 1][column] == 1)
                        || (map[row][column] == 1 && map[row + 1][column] == 2)
                ) {
                    destructibleWalls.add(new DestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "VERTICAL", Constants.WALL_HEALTH));
                }
            }
        }
    }

    private static int orientation(@NotNull Coordinate p, @NotNull Coordinate q, @NotNull Coordinate r) {
        int val = (int) Math.round((q.getYCoordinate() - p.getYCoordinate()) * (r.getXCoordinate() - q.getXCoordinate())
                - (q.getXCoordinate() - p.getXCoordinate()) * (r.getYCoordinate() - q.getYCoordinate()));
        if (val == 0) {
            return 0;
        }
        return (val > 0) ? 1 : 2;
    }

    private static boolean doIntersect(Coordinate p1, Coordinate q1, Coordinate p2, Coordinate q2) {

        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        if (o1 != o2 && o3 != o4) {
            return true;
        }

        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        if (o4 == 0 && onSegment(p2, q1, q2)) {
            return true;
        }

        return false;
    }

    private static boolean isInside(@NotNull ArrayList<Coordinate> coordinates, @NotNull Coordinate pointToCheck) {
        Coordinate extreme = new Coordinate(Constants.INF, pointToCheck.getYCoordinate());
        int count = 0, i = 0;
        do {
            int next = (i + 1) % 4;
            if (doIntersect(coordinates.get(i), coordinates.get(next), pointToCheck, extreme)) {
                if (orientation(coordinates.get(i), pointToCheck, coordinates.get(next)) == 0) {
                    return onSegment(coordinates.get(i), pointToCheck,
                            coordinates.get(next));
                }

                count++;
            }
            i = next;
        } while (i != 0);
        return (count % 2 == 1); // Same as (count%2 == 1)
    }

    /**
     * This method shows free space for rectangle with input width and height.
     *
     * @param width  is width of rectangle
     * @param height is height of rectangle
     * @return coordinate of free space in the map
     */
    public Coordinate freePlaceToPut(int width, int height) {
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

    public static ArrayList<Topography> getTopographies() {
        return topographies;
    }

    private void makeRocksAndTrees() {

        Random random = new Random();
        File dir = new File("kit/treesAndRocks");
        File[] backgrounds = dir.listFiles();
        for (int i = 0; i < 5; i++) {
            File backgroundFile = backgrounds[random.nextInt(backgrounds.length)];
            try {
                Image topographyImage = ImageIO.read(backgroundFile);
                Topography topography = new Topography(topographyImage, freePlaceToPut(Constants.TOPOGRAPHY_SIZE, Constants.TOPOGRAPHY_SIZE));
                topographies.add(topography);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * This method check overlap all tanks in the map together except input tank.
     *
     * @param tankToIgnore is tank that you don't wanna check overlap with
     * @return if have overlap return true else return false
     */
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
     * This method check overlap of input rectangle with all tanks in the map.
     *
     * @param coordinates is coordinate of the rectangle
     * @return if have overlap return true else return false
     */
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
     * Getter method of bullets field
     *
     * @return array list of bullets in the map
     */
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    /**
     * Getter method of users field
     *
     * @return array list of users in the map
     */
    public ArrayList<UserPlayer> getUsers() {
        return users;
    }

    /**
     * This is setter method for users field.
     *
     * @param users is an array list of user that you wanna set for users of this map
     */
    public void setUsers(@NotNull ArrayList<UserPlayer> users) {
        this.users = users;

        ArrayList<UserPlayer> userPlayers = new ArrayList<>();
        for (UserPlayer user : users) {
            UserPlayer userPlayer = new UserPlayer(user.getName(), user.getPassword(), user.getColor(), user.getTankTroubleMap(), user.getDataBaseFileName());
            userPlayer.getUserTank().setBulletDamage(user.getUserTank().getBulletDamage());
            userPlayer.setGroupNumber(user.getGroupNumber());
            userPlayers.add(userPlayer);
        }
        runGameHandler.setSaveSetUser(userPlayers);
    }

    /**
     * Getter method of bots field
     *
     * @return array list of bots in the map
     */
    public ArrayList<BotPlayer> getBots() {
        return bots;
    }

    /**
     * This is setter method for bots field.
     *
     * @param bots is an array list of bots that you wanna set for bots of this map
     */
    public void setBots(@NotNull ArrayList<BotPlayer> bots) {
        this.bots = bots;

        ArrayList<BotPlayer> botPlayers = new ArrayList<>();
        for (BotPlayer botPlayer : bots) {
            BotPlayer bot = new BotPlayer(botPlayer.getName(), botPlayer.getColor(), botPlayer.getTankTroubleMap(), botPlayer.getGroupNumber());
            bot.getAiTank().setBulletDamage(botPlayer.getAiTank().getBulletDamage()); //bullet damage
            bot.getAiTank().setHealth(runGameHandler.getTankHealth()); //tank health
            botPlayers.add(bot);
        }
        runGameHandler.setSaveSetBot(botPlayers);
    }

    /**
     * Getter method of audience field
     *
     * @return array list of audience in the map
     */
    public ArrayList<UserPlayer> getAudience() {
        return audience;
    }

    /**
     * Getter method of isNetwork field
     *
     * @return boolean as value of that
     */
    public boolean isNetwork() {
        return isNetwork;
    }

    /**
     * Getter method of gameOver field
     *
     * @return a boolean as value of gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * This is setter method for GameOver field.
     *
     * @param gameOver is a boolean as value of gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    /**
     * Getter method of startTime field
     *
     * @return date of time that game was started
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Getter method of winnerGroup field
     *
     * @return an integer as value of that
     */
    public int getWinnerGroup() {
        return winnerGroup;
    }

    /**
     * This is setter method for winnerGroup field.
     *
     * @param winnerGroup is an integer as value of that
     */
    public void setWinnerGroup(int winnerGroup) {
        this.winnerGroup = winnerGroup;
    }

    /**
     * Getter method of controller field
     *
     * @return an userPlayer object as controller
     */
    public UserPlayer getController() {
        return controller;
    }

    /**
     * This is setter method for controller field.
     *
     * @param controller is an userPlayer that you wanna set as controller
     */
    public void setController(UserPlayer controller) {
        this.controller = controller;
    }
}

