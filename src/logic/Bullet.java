package logic;

import logic.Wall.DestructibleWall;
import logic.Wall.Wall;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class represent bullet for tanks include player's and bot's tank.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Bullet implements Serializable {
    private int damage;
    private ArrayList<Coordinate> coordinates;
    private Coordinate centerPointCoordinate;
    private LocalDateTime fireTime;
    private double angle;
    private Image bulletsImage;
    private boolean bulletsBlasted;


    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     *
     * @param bulletsDamage is an integer as damage of bullet
     * @param bulletsType   is a string that show type of bullet
     *                      //     * @param coordinate    is primary coordinate of bullet
     * @param angle         is a double as angle of bullet based on y axis
     */
    public Bullet(int bulletsDamage, String bulletsType, Coordinate centerPointCoordinate, double angle) {
        this.centerPointCoordinate = centerPointCoordinate;
        updateArraylistCoordinates();
        bulletsBlasted = false;
        damage = bulletsDamage;
        if (bulletsType.equals("NORMAL")) {
            try {
                bulletsImage = ImageIO.read(new File("kit\\bullet\\normal.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (bulletsType.equals("LASER")) { // ba else nazadam yevakht khastim emtiazi ezafe konim ye golule
            try {
                bulletsImage = ImageIO.read(new File("kit\\bullet\\laser.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.angle = angle;

        fireTime = LocalDateTime.now();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(4000);
                bulletsBlasted = true;
                for (Bullet bullet : TankTroubleMap.getBullets()) {
                    if (bullet.bulletsBlasted) TankTroubleMap.getBullets().remove(bullet);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }





    /**
     * Getter method of bulletsImage field
     *
     * @return image of bullets
     */
    public Image getBulletsImage() {
        return bulletsImage;
    }

    /**
     * Getter method of fireTime field
     *
     * @return time that bullets fired from tank
     */
    public LocalDateTime getFireTime() {
        return fireTime;
    }

    /**
     * Getter method of angle field
     *
     * @return angle of bullets based on y axis
     */
    public double getAngle() {
        return angle;
    }

    /**
     * This is setter method for angle field.
     *
     * @param angle is a double as angle of bullets
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Getter method of coordinates field
     *
     * @return array list that contain four bullets coordinate
     */
    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * This method update bullets state in every moment.
     * state include coordinate and angle life time.
     * And also check overlap with tanks and walls and do something based on result
     */
    public void update() {
        double newAngle = angle - 90;
        Coordinate nextCenterPointCoordinate = new Coordinate(
                this.centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SPEED * Math.cos(Math.toRadians(newAngle))),
                this.centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SPEED * Math.sin(Math.toRadians(newAngle)))
        );


        ArrayList<Wall> walls = new ArrayList<>();
        walls.addAll(TankTroubleMap.getDestructibleWalls());
        walls.addAll(TankTroubleMap.getIndestructibleWalls());
        ArrayList<Coordinate> nextCoordinatesArrayList = makeCoordinatesFromCenterCoordinate(nextCenterPointCoordinate);
        Wall wallToCheck = null;
        for (int i = 0; i < walls.size(); i++) {
            if (TankTroubleMap.checkOverLap(walls.get(i).getPointsArray(), nextCoordinatesArrayList)) {
                wallToCheck = walls.get(i);
            }
        }

        if (wallToCheck != null) {
            if (horizontalCrash(wallToCheck)) { //if the bullet would only go over horizontal side of any walL
                nextCenterPointCoordinate = flipH(wallToCheck);
            } else if (verticalCrash(wallToCheck)) { // if the bullet would only go over vertical side any  wall
                nextCenterPointCoordinate = flipV(wallToCheck);
            } else {// if the bullet would only go over corner of any wall
                int cornerCrashState = cornerCrash(wallToCheck);
                nextCenterPointCoordinate = flipCorner(wallToCheck, cornerCrashState);
            }

            if (wallToCheck.isDestroyable()) {//crashing destructible
                ((DestructibleWall) wallToCheck).receiveDamage(damage);
                if (((DestructibleWall) wallToCheck).getHealth() <= 0) {
                    TankTroubleMap.getDestructibleWalls().remove(wallToCheck);
                }
                bulletsBlasted = true;
                TankTroubleMap.getBullets().remove(this);
            }
        }
        this.centerPointCoordinate = nextCenterPointCoordinate;
        updateArraylistCoordinates();
    }


    private boolean horizontalCrash(Wall wallToCheck) {
        ArrayList<Coordinate> wallCoordinates = wallToCheck.getPointsArray();
        return wallCoordinates.get(0).getXCoordinate() <= coordinates.get(1).getXCoordinate() && coordinates.get(0).getXCoordinate() <= wallCoordinates.get(1).getXCoordinate();
    }

    private boolean verticalCrash(Wall wallToCheck) {
        ArrayList<Coordinate> wallCoordinates = wallToCheck.getPointsArray();
        return wallCoordinates.get(1).getYCoordinate() <= coordinates.get(2).getYCoordinate() && coordinates.get(1).getYCoordinate() <= wallCoordinates.get(2).getYCoordinate();
    }

    private int cornerCrash(Wall wallToCheck) {
        ArrayList<Coordinate> wallCoordinates = wallToCheck.getPointsArray();
        if (coordinates.get(2).getXCoordinate() < wallCoordinates.get(0).getXCoordinate() && coordinates.get(2).getYCoordinate() < wallCoordinates.get(0).getYCoordinate())
            return 0;
        else if (coordinates.get(0).getXCoordinate() > wallCoordinates.get(1).getXCoordinate() && coordinates.get(0).getYCoordinate() < wallCoordinates.get(1).getYCoordinate())
            return 1;
        else if (wallCoordinates.get(2).getXCoordinate() < coordinates.get(0).getXCoordinate() && wallCoordinates.get(2).getYCoordinate() < coordinates.get(0).getYCoordinate())
            return 2;
        else if (coordinates.get(1).getXCoordinate() < wallCoordinates.get(3).getXCoordinate() && coordinates.get(1).getYCoordinate() > wallCoordinates.get(3).getYCoordinate())
            return 3;
        else
            return -1;
    }

    private void updateArraylistCoordinates() {
        this.coordinates = makeCoordinatesFromCenterCoordinate(centerPointCoordinate);
    }

    private ArrayList<Coordinate> makeCoordinatesFromCenterCoordinate(Coordinate centerPointCoordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() - (Constants.BULLET_SIZE / 2)));
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() - (Constants.BULLET_SIZE / 2)));
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SIZE / 2)));
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SIZE / 2)));
        return coordinates;
    }

    private Coordinate flipH(Wall wall) {
        angle = 180 - angle;
        Coordinate nextCenterPointCoordinate = new Coordinate(
                this.centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SPEED * Math.cos(Math.toRadians(90 - angle))),
                this.centerPointCoordinate.getYCoordinate());
        return nextCenterPointCoordinate;
    }

    private Coordinate flipV(Wall wall) {
        angle = -angle;
        Coordinate nextCenterPointCoordinate = new Coordinate(this.centerPointCoordinate.getXCoordinate(),
                this.centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SPEED * Math.sin(Math.toRadians(90 - angle))));
        return nextCenterPointCoordinate;
    }

    private Coordinate flipCorner(Wall wall, int cornerState) {
        Coordinate coordinate;
        coordinate = centerPointCoordinate;
        if (cornerState == 0) {
            angle = 180 + angle;
        } else if (cornerState == 1) {
            angle = 180 - angle;
        } else if (cornerState == 2) {
            angle = 180 - angle;
        } else if (cornerState == 3) {
            angle = 180 + angle;
        }
        return coordinate;
    }
}
