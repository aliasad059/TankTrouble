package logic;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
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
    private static ArrayList<AITank> AITanks;
    private static ArrayList<UserTank> userTanks;
    private static ArrayList<Bullets> bullets;


    /**
     * This constructor initialize some fields.
     *
     * @param pathOfMap is path of text file map
     */
    public TankTroubleMap(String pathOfMap) {
        prizes = new ArrayList<>();
        destructibleWalls = new ArrayList<>();
        indestructibleWalls = new ArrayList<>();
        AITanks = new ArrayList<>();
        userTanks = new ArrayList<>();
        bullets = new ArrayList<>();
        setHeightAndWidth(pathOfMap);
        map = new int[height][width];
        Constants.GAME_HEIGHT_REAL = height * Constants.WALL_HEIGHT_HORIZONTAL;
        Constants.GAME_WIDTH_REAL = width * Constants.WALL_WIDTH_HORIZONTAL;
        readMap(pathOfMap);
        makeWalls();
        userTanks.add(new UserTank(100, freePlaceToPut(Constants.TANK_SIZE, Constants.TANK_SIZE), ".\\kit++\\kit\\tanks\\Blue\\normal.png"));

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
    public static void prizeSetter() {
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
                    destructibleWalls.add(new DestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "HORIZONTAL"));
                }
            }
        }
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height - 1; row++) {
                if (map[row][column] == 1 && map[row + 1][column] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "VERTICAL"));
                } else if (map[row][column] == 2 && map[row + 1][column] == 2) {
                    destructibleWalls.add(new DestructibleWall(new Coordinate(column * Constants.WALL_WIDTH_HORIZONTAL, row * Constants.WALL_HEIGHT_VERTICAL), "VERTICAL"));
                }//else {
                //   destructibleWalls.add(new Wall(column,row,false,"NW_VERTICAL"));
                //}
            }
        }
    }

    /**
     * This method calculate area of a triangle based on it's point.
     *
     * @param p1 is coordinate of first point
     * @param p2 is coordinate of second point
     * @param p3 is coordinate of third point
     * @return area as double
     */
    private static double areaOfTriangle(@NotNull Coordinate p1, @NotNull Coordinate p2, @NotNull Coordinate p3) {
        return 0.5 * Math.abs(p1.getYCoordinate() * (p2.getXCoordinate() - p3.getXCoordinate()) + p2.getYCoordinate() * (p3.getXCoordinate() - p1.getXCoordinate()) + p3.getYCoordinate() * (p1.getXCoordinate() - p2.getXCoordinate()));
    }

    /**
     * This method calculate distance between two points.
     *
     * @param p1 is coordinate of first point
     * @param p2 is coordinate of second point
     * @return answer as double
     */
    private static double distanceBetweenTwpPoints(@NotNull Coordinate p1, @NotNull Coordinate p2) {
        return Math.sqrt(Math.pow(p1.getXCoordinate() - p2.getXCoordinate(), 2) + Math.pow(p1.getYCoordinate() - p2.getYCoordinate(), 2));
    }

    /**
     * This method shows a point there is in or on the rectangle or not.
     *
     * @param p           is coordinate of the point
     * @param coordinates are coordinates of points of rectangle
     * @return answer as boolean
     */
    private static boolean isPointInOrOnRectangle(Coordinate p, @NotNull ArrayList<Coordinate> coordinates) {
        double sumOfAreaOfTriangles = areaOfTriangle(p, coordinates.get(0), coordinates.get(1)) + areaOfTriangle(p, coordinates.get(1), coordinates.get(2)) + areaOfTriangle(p, coordinates.get(2), coordinates.get(3)) + areaOfTriangle(p, coordinates.get(3), coordinates.get(0));
        double areaOfRectangle = distanceBetweenTwpPoints(coordinates.get(0), coordinates.get(1)) * distanceBetweenTwpPoints(coordinates.get(1), coordinates.get(2));
        return sumOfAreaOfTriangles == areaOfRectangle;
    }

    /**
     * This method check overlap of two rectangles
     *
     * @param p_1 are coordinates of points of first rectangle
     * @param p_2 are coordinates of points of second rectangle
     * @return answer as boolean
     */
    static boolean checkOverLap(@NotNull ArrayList<Coordinate> p_1, ArrayList<Coordinate> p_2) {
        for (Coordinate coordinate : p_1) {
            if (isPointInOrOnRectangle(coordinate, p_2)) return true;
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
//            System.out.println("size: "+wall.getPointsArray().size());
//            System.out.println("point : ("+ wall.getPointsArray().get(0).getXCoordinate()+","+wall.getPointsArray().get(0).getYCoordinate()+")");
//            System.out.println("point : ("+ wall.getPointsArray().get(1).getXCoordinate()+","+wall.getPointsArray().get(1).getYCoordinate()+")");
//            System.out.println("point : ("+ wall.getPointsArray().get(2).getXCoordinate()+","+wall.getPointsArray().get(2).getYCoordinate()+")");
//            System.out.println("point : ("+ wall.getPointsArray().get(3).getXCoordinate()+","+wall.getPointsArray().get(3).getYCoordinate()+")");
//            if(TankTroubleMap.getUserTanks().size()!=0) {
//                System.out.println(TankTroubleMap.getUserTanks().get(0).getTankCoordinates().size());
//                System.out.println("point tank: (" + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(0).getXCoordinate() + "," + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(0).getYCoordinate() + ")");
//                System.out.println("point tank: (" + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(1).getXCoordinate() + "," + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(1).getYCoordinate() + ")");
//                System.out.println("point tank: (" + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(2).getXCoordinate() + "," + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(2).getYCoordinate() + ")");
//                System.out.println("point tank: (" + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(3).getXCoordinate() + "," + TankTroubleMap.getUserTanks().get(0).getTankCoordinates().get(3).getYCoordinate() + ")");
//            }
            if (checkOverLap(wall.getPointsArray(), coordinatesToCheck)) {
                return true;
            }
        }
        return false;
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
            if (checkOverLap(prize.getCoordinates(), coordinates))
                return true;
        }
        return false;
    }
//
//    public static boolean checkOverlapWithAllTanks(Tank tankToIgnore) {
//        ArrayList<Tank> tanks = new ArrayList<>();
//        tanks.addAll(userTanks);
//        tanks.addAll(AITanks);
//        tanks.remove(tankToIgnore);
//        for (Tank tank : tanks) {
//            if (checkOverLap(tank.getTankCoordinates(), tankToIgnore.tankCoordinates)) return true;
//        }
//        return false;
//    }

    public static boolean checkOverlapWithAllTanks(ArrayList<Coordinate> coordinates) {
        ArrayList<Tank> tanks = new ArrayList<>();
        tanks.addAll(userTanks);
        tanks.addAll(AITanks);
        for (Tank tank : tanks) {
            if (coordinates != tank.getTankCoordinates()) {
                if (checkOverLap(tank.getTankCoordinates(), coordinates)) return true;
            }
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
    private static Coordinate freePlaceToPut(int width, int height) {
        SecureRandom random = new SecureRandom();
        boolean coordinateIsGood = false;
        Coordinate goodCoordinate = new Coordinate();
        while (!coordinateIsGood) {
            goodCoordinate.setXCoordinate(random.nextInt(Constants.GAME_WIDTH_REAL));
            goodCoordinate.setYCoordinate(random.nextInt(Constants.GAME_HEIGHT_REAL));
            ArrayList<Coordinate> goodCoordinates = new ArrayList<>();
            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() + (double) width / 2
                    , goodCoordinate.getYCoordinate() + (double) height / 2));

            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() - (double) width / 2
                    , goodCoordinate.getYCoordinate() + (double) height / 2));

            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() - (double) width / 2
                    , goodCoordinate.getYCoordinate() - (double) height / 2));

            goodCoordinates.add(new Coordinate(goodCoordinate.getXCoordinate() + (double) width / 2
                    , goodCoordinate.getYCoordinate() - (double) height / 2));
            coordinateIsGood = !checkOverlapWithAllWalls(goodCoordinates)
                    && !checkOverlapWithAllPrizes(goodCoordinates)
                    && !checkOverlapWithAllTanks(goodCoordinates);
        }
        return goodCoordinate;
    }

    /**
     * Getter method of bullets field
     *
     * @return array list of bullets in the map
     */
    public static ArrayList<Bullets> getBullets() {
        return bullets;
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
     * Getter method of AITanks field
     *
     * @return array list of AI tanks (bot tanks) in the map
     */
    public static ArrayList<AITank> getAITanks() {
        return AITanks;
    }

    /**
     * Getter method of user tanks field
     *
     * @return array list of userTanks in the map
     */
    public static ArrayList<UserTank> getUserTanks() {
        return userTanks;
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
}
