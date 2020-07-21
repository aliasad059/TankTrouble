import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TankTroubleMap {
    private static int height;
    private static int width;
    private int[][] map;
    private static ArrayList<Wall> walls;
    private static ArrayList<Tank> tanks;
    private static ArrayList<Bullets> bullets;

    /**
     *
     */
    public TankTroubleMap(String address) {
        walls = new ArrayList<>();
        tanks = new ArrayList<>();
        tanks.add(new Tank(100, freePlaceToPut(Constants.TANK_SIZE, Constants.TANK_SIZE),".\\kit++\\kit\\tanks\\Blue\\normal.png"));
        bullets = new ArrayList<>();
        setHeightAndWidth(address);
        map = new int[height][width];
        readMap(address);
        makeWalls();
    }

    /**
     * This method set size of map (x axis and y axis size) based on it's text file.
     *
     * @param address is address of the text file
     */
    private void setHeightAndWidth(String address) {
        try (Scanner scanner = new Scanner(new File(address))) {
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
     * This method show list of map in file of game maps.
     */
    private void showListOfMaps() { //this method need graphic graphic

    }

    /**
     * This method read a text file as map of game.
     *
     * @param address is address of the text file
     */
    private void readMap(String address) {
        try (Scanner scanner = new Scanner(new File(address))) {
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
     * This method set a random prize in a random coordinate of map.
     */
    private void PrizeSetter() {
        Random random = new Random();
        int prize = random.nextInt(5) + 4;
        do {
            Coordinate coordinate = new Coordinate();
            coordinate.setXCoordinate(random.nextInt(height));
            coordinate.setYCoordinate(random.nextInt(width));
        } while (map[height][width] != 0);
        map[height][width] = prize;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public int[][] getArrayMap() {
        return map;
    }

    public void makeWalls() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width - 1; column++) {
                if (map[row][column] == 1 && map[row][column + 1] == 1) {
                    walls.add(new Wall(column, row, false, "HORIZONTAL"));
                } else if (map[row][column] == 2 && map[row][column + 1] == 2) {
                    walls.add(new Wall(column, row, true, "HORIZONTAL"));
                }
            }
        }
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height - 1; row++) {
                if (map[row][column] == 1 && map[row + 1][column] == 1) {
                    walls.add(new Wall(column, row, false, "VERTICAL"));
                } else if (map[row][column] == 2 && map[row + 1][column] == 2) {
                    walls.add(new Wall(column, row, true, "VERTICAL"));
                }
            }
        }
    }

    public static boolean checkOverLap(int x1, int y1, int width1, int height1, int x2, int y2, int width2, int height2) {
        return x1 < x2 + width2 && x1 + width1 > x2 && y1 < y2 + height2 && y1 + height1 > y2;
    }

    public static ArrayList<Wall> getWalls() {
        return walls;
    }

    public Coordinate freePlaceToPut(int width, int height) {
        int x = -1, y = -1;
        Random random = new Random();
        boolean coordinateIsGood = false;
        while (!coordinateIsGood) {
            x = random.nextInt(Constants.GAME_WIDTH);
            y = random.nextInt(Constants.GAME_HEIGHT);
            coordinateIsGood = !overlapWithAllWalls(x,y,width,height);
        }
        Coordinate goodCoordinate = new Coordinate();
        goodCoordinate.setXCoordinate(x);
        goodCoordinate.setYCoordinate(y);
        return goodCoordinate;
    }

    public static boolean overlapWithAllWalls(int x, int y, int width, int height) {
        boolean haveOverlap = false;
        for (int i = 0; i < walls.size(); i++) {
            Wall wallToCheck = walls.get(i);
            if (wallToCheck.getDirection().equals("HORIZONTAL")) {
                haveOverlap = haveOverlap || checkOverLap(x, y, width, height
                        , wallToCheck.getStartingPoint().getXCoordinate(), wallToCheck.getStartingPoint().getYCoordinate()
                        , Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL);
            } else if (wallToCheck.getDirection().equals("VERTICAL")) {
                haveOverlap = haveOverlap || checkOverLap(x, y, width, height
                        , wallToCheck.getStartingPoint().getXCoordinate(), wallToCheck.getStartingPoint().getYCoordinate()
                        , Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL);
            }
        }
        return haveOverlap;
    }

    public static ArrayList<Tank> getTanks() {
        return tanks;
    }

    public static ArrayList<Bullets> getBullets() {
        return bullets;
    }
}
