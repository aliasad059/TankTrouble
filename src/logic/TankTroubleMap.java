package logic;

import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
     *
     */
    public TankTroubleMap(String address) {
        prizes = new ArrayList<>();
        destructibleWalls = new ArrayList<>();
        indestructibleWalls = new ArrayList<>();
        AITanks = new ArrayList<>();
        userTanks = new ArrayList<>();
        bullets = new ArrayList<>();
        setHeightAndWidth(address);
        map = new int[height][width];
        Constants.GAME_HEIGHT_REAL = height*Constants.WALL_HEIGHT_HORIZONTAL;
        Constants.GAME_WIDTH_REAL = width*Constants.WALL_WIDTH_HORIZONTAL;
        readMap(address);
        makeWalls();
        userTanks.add(new UserTank(100, freePlaceToPut(Constants.TANK_SIZE, Constants.TANK_SIZE, 0), ".\\kit++\\kit\\tanks\\Blue\\normal.png"));

    }

    public static ArrayList<AITank> getAITanks() {
        return AITanks;
    }

    public static ArrayList<UserTank> getUserTanks() {
        return userTanks;
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
    public static void prizeSetter() {
        if (prizes.size() <5) {
            SecureRandom random = new SecureRandom();
        int prizeType = random.nextInt(5) + 1;
        Coordinate coordinate = freePlaceToPut(Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, 0);
            prizes.add(new Prize(prizeType, coordinate));
        }
    }

    public void makeWalls() {
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width - 1; column++) {
                if (map[row][column] == 1 && map[row][column + 1] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(column*Constants.WALL_WIDTH_HORIZONTAL, row*Constants.WALL_HEIGHT_VERTICAL, "HORIZONTAL"));
                } else if (map[row][column] == 2 && map[row][column + 1] == 2) {
                    destructibleWalls.add(new DestructibleWall(column*Constants.WALL_WIDTH_HORIZONTAL, row*Constants.WALL_HEIGHT_VERTICAL, "HORIZONTAL"));
                }
            }
        }
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height - 1; row++) {
                if (map[row][column] == 1 && map[row + 1][column] == 1) {
                    indestructibleWalls.add(new IndestructibleWall(column*Constants.WALL_WIDTH_HORIZONTAL, row*Constants.WALL_HEIGHT_VERTICAL, "VERTICAL"));
                } else if (map[row][column] == 2 && map[row + 1][column] == 2) {
                    destructibleWalls.add(new DestructibleWall(column*Constants.WALL_WIDTH_HORIZONTAL, row*Constants.WALL_HEIGHT_VERTICAL, "VERTICAL"));
                }//else {
                //   destructibleWalls.add(new Wall(column,row,false,"NW_VERTICAL"));
                //}
            }
        }
    }

    private static double areaOfTriangle(Coordinate p1, Coordinate p2, Coordinate p3){
        return 0.5*Math.abs(p1.getYCoordinate()*(p2.getXCoordinate()-p3.getXCoordinate())+p2.getYCoordinate()*(p3.getXCoordinate()-p1.getXCoordinate())+p3.getYCoordinate()*(p1.getXCoordinate()-p2.getXCoordinate()));
    }

    private static double distanceBetweenTwpPoints(Coordinate p1, Coordinate p2){
        return Math.sqrt(Math.pow(p1.getXCoordinate()-p2.getXCoordinate(),2)+Math.pow(p1.getYCoordinate()-p2.getYCoordinate(),2));
    }

    private static boolean isPointInOrOnRectangle(Coordinate p, ArrayList<Coordinate> coordinates){
        double sumOfAreaOfTriangles=areaOfTriangle(p,coordinates.get(0),coordinates.get(1))+areaOfTriangle(p,coordinates.get(1),coordinates.get(2))+areaOfTriangle(p,coordinates.get(2),coordinates.get(3))+areaOfTriangle(p,coordinates.get(3),coordinates.get(0));
        double areaOfRectangle=distanceBetweenTwpPoints(coordinates.get(0),coordinates.get(1))*distanceBetweenTwpPoints(coordinates.get(1),coordinates.get(2));
        return sumOfAreaOfTriangles==areaOfRectangle;
    }

     static boolean checkOverLap(ArrayList<Coordinate> p_1, ArrayList<Coordinate> p_2) {
        for (Coordinate coordinate : p_1) {
            if (isPointInOrOnRectangle(coordinate, p_2)) return true;
        }
        return false;
    }

    public static ArrayList<Wall> getDestructibleWalls() {
        return destructibleWalls;
    }

    public static ArrayList<Wall> getIndestructibleWalls() {
        return indestructibleWalls;
    }

     static ArrayList<Coordinate> findRectangleFromStartingPointAndAngle(int width, int high, Coordinate p, double angle){
        ArrayList<Coordinate> coordinates=new ArrayList<>();
        Coordinate p2=new Coordinate();
        Coordinate p3=new Coordinate();
        Coordinate p4=new Coordinate();
        ArrayList<Wall> walls = new ArrayList<>();
        walls.addAll(indestructibleWalls);
        walls.addAll(destructibleWalls);
        // angle positive
        if(angle>=0) {
            while (angle>=360){
                angle-=360;
            }
            if (angle <= 90) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle <= 180) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle <= 270) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
        }

        // angle negative
        else {
            while (angle <= -360) {
                angle += 360;
            }
            if (angle >= -90) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle>= -180) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else if (angle >= -270) {
                //p2
                p2.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()-(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
            else {
                //p2
                p2.setYCoordinate(p.getYCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*width)));
                p2.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*width)));

                //p3
                p3.setYCoordinate(p.getYCoordinate()-(int)Math.round(Math.abs(Math.sin(Math.toRadians(angle))*high)));
                p3.setXCoordinate(p.getXCoordinate()+(int)Math.round(Math.abs(Math.cos(Math.toRadians(angle))*high)));
            }
        }

        //p4
        p4.setYCoordinate(p3.getYCoordinate()+p2.getYCoordinate()-p.getYCoordinate());
        p4.setXCoordinate(p3.getXCoordinate()+p2.getXCoordinate()-p.getXCoordinate());

        coordinates.add(p);
        coordinates.add(p2);
        coordinates.add(p4);
        coordinates.add(p3);
        return coordinates;
    }

    public static boolean checkOverlapWithAllWalls(Coordinate startingPoint, int width, int height, double angle) {
        ArrayList<Wall> walls = new ArrayList<>();
//        walls.addAll(indestructibleWalls);
//        walls.addAll(destructibleWalls);
        for (int i = 0; i < indestructibleWalls.size(); i++) {
            Wall wallToCheck = indestructibleWalls.get(i);
            if (distanceBetweenTwpPoints(startingPoint,wallToCheck.startingPoint)<=2*Constants.WALL_WIDTH_HORIZONTAL){
                walls.add(wallToCheck);
            }
        }
        for(Wall wall: walls){
            if(wall.getDirection().equals("HORIZONTAL")) {
                if (checkOverLap(findRectangleFromStartingPointAndAngle(Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL, wall.getStartingPoint(), 0), findRectangleFromStartingPointAndAngle(width, height, startingPoint, angle))) {
                    return true;
                }
            }
            else {
                if (checkOverLap(findRectangleFromStartingPointAndAngle(Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL, wall.getStartingPoint(), 0), findRectangleFromStartingPointAndAngle(width, height, startingPoint, angle))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkOverlapWithAllPrizes(Coordinate startingPoint, int width, int height, double angle) {
        for(Prize prize: prizes){
            if(checkOverLap(findRectangleFromStartingPointAndAngle(Constants.PRIZE_SIZE,Constants.PRIZE_SIZE,prize.getCoordinate(),0),findRectangleFromStartingPointAndAngle(width,height,startingPoint,angle))) return true;
        }
        return false;
    }

    public static boolean checkOverlapWithAllTanks(Tank tankToIgnore,Coordinate startingPoint, int width, int height, double angle) {
        ArrayList<Tank>tanks = new ArrayList<>();
        tanks.addAll(userTanks);
        tanks.addAll(AITanks);
        tanks.remove(tankToIgnore);
        for(Tank tank: tanks){
            if(checkOverLap(findRectangleFromStartingPointAndAngle(Constants.TANK_SIZE,Constants.TANK_SIZE,tank.getPixelCoordinate(),0),findRectangleFromStartingPointAndAngle(width,height,startingPoint,angle))) return true;
        }
        return false;
    }

    private static Coordinate freePlaceToPut(int width, int height, int angle) {
        SecureRandom random = new SecureRandom();
        boolean coordinateIsGood = false;
        Coordinate goodCoordinate = new Coordinate();
        while (!coordinateIsGood) {
            goodCoordinate.setXCoordinate(random.nextInt(Constants.GAME_WIDTH_REAL));
            goodCoordinate.setYCoordinate(random.nextInt(Constants.GAME_HEIGHT_REAL));
            coordinateIsGood = !checkOverlapWithAllWalls(goodCoordinate, width, height, angle) && !checkOverlapWithAllPrizes(goodCoordinate, width, height, angle) /*&& !checkOverlapWithAllTanks(goodCoordinate, width, height, angle)*/;
        }
        return goodCoordinate;
    }

    public static ArrayList<Bullets> getBullets() {
        return bullets;
    }

    public static ArrayList<Prize> getPrizes() {
        return prizes;
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

}
