package logic;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

/**
 * This class represent a tank for players.
 * Player can be bot or user.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Tank {

    private int health;
    private boolean hasShield;
    private ArrayList<Bullets> bulletsArrayList;
    private int prizeType;
    private String bulletsType;
    private int bulletsDamage;
    private int numberOfFiredBullets;
    private Image tankImage;
    private double angle;
    private boolean tankBlasted;
    private Coordinate centerPointCoordinate;
    private ArrayList<Coordinate> tankCoordinates;

    /**
     * This constructor set valid random place for tank and initialize fields based on game rules and input parameters.
     *
     * @param health                is health of tank
     * @param centerPointCoordinate is coordinate of center point of tank
     * @param tankImagePath         is a string that shows path of image tank
     */
    public Tank(int health, @NotNull Coordinate centerPointCoordinate, String tankImagePath) {
        this.health = health;
        bulletsType = "NORMAL";
        hasShield = false;
        bulletsDamage = Constants.BULLET_POWER;
        tankCoordinates = new ArrayList<>();
        this.centerPointCoordinate = centerPointCoordinate;
        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));
        this.angle = 0;
        bulletsArrayList = new ArrayList<>();
        try {
            tankImage = ImageIO.read(new File(tankImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method catch a prize based on overlap of tank and prize.
     */
    public void catchPrize() {
        if (prizeType != 0) {
            System.out.println("You haven't used your last prize...!");
        } else {
            for (int i = 0; i < TankTroubleMap.getPrizes().size(); i++) {
                if (TankTroubleMap.checkOverLap(tankCoordinates, TankTroubleMap.getPrizes().get(i).getCoordinates())) {
                    prizeType = TankTroubleMap.getPrizes().get(i).getType();
                    TankTroubleMap.getPrizes().remove(i);
                    break;
                }
            }
        }
    }


    /**
     * This method effect cached prize on the tank based on type prize.
     */
    public void usePrize() {
        //protector
        if (prizeType == 1) {
            protectTank();
        }

        //laser
        else if (prizeType == 2) {
            bulletsType = "LASER";
            Thread thread = new Thread(() -> { //change to swing worker
                try {
                    Thread.sleep(3000);
                    bulletsType = "NORMAL";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        //increase health by 10%
        else if (prizeType == 3) {
            increaseHealth(1.1);
        }

        // 2X bullet power
        else if (prizeType == 4) {
            increaseBulletPower(2);
        }

        // 3X bullet power
        else if (prizeType == 5) {
            increaseBulletPower(3);
        } else {
            System.out.println("You have no gift to use");
        }
        prizeType = 0;
    }

    /**
     * this method increase bullet power
     *
     * @param howManyTimes how many time should the bullet power multiplied
     */
    private void increaseBulletPower(int howManyTimes) {
        bulletsDamage *= howManyTimes;
    }

    /**
     * this method increase health of tank
     *
     * @param howManyTimes how many time should the bullet health multiplied
     */
    private void increaseHealth(double howManyTimes) {
        health *= howManyTimes;
    }


    /**
     * This method set shield for tank for 15 seconds.
     */
    private void protectTank() {
        //TODO: change graphic of tank /// we change graphic of tank that show bottom of map game ...........................................
        hasShield = true;
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(15000);
                hasShield = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * This method lunch or fire a bullets based on rules of game (2 bullets in a second).
     */
    public void fire() {
        System.out.println("Bullets in map:  " + TankTroubleMap.getBullets().size());
        Coordinate bulletCoordinate = new Coordinate();
        bulletCoordinate.setXCoordinate(centerPointCoordinate.getXCoordinate() + Constants.LOOLE_TANK_SIZE * Math.sin(Math.toRadians(angle)));
        bulletCoordinate.setYCoordinate(centerPointCoordinate.getYCoordinate() - Constants.LOOLE_TANK_SIZE * Math.cos(Math.toRadians(angle)));

        Bullets bulletToFire = new Bullets(bulletsDamage, bulletsType, bulletCoordinate, angle);
        if (bulletsArrayList.size() < 2) {
            bulletsArrayList.add(bulletToFire);
            TankTroubleMap.getBullets().add(bulletToFire);
            System.out.println("bullets lunch shod............");
            numberOfFiredBullets++;
        } else if (numberOfFiredBullets % 2 == 1) {
            bulletsArrayList.add(0, bulletsArrayList.get(1));
            bulletsArrayList.add(1, bulletToFire);
            TankTroubleMap.getBullets().add(bulletToFire);
            System.out.println("bullets lunch shod............");
            numberOfFiredBullets++;
        } else if (numberOfFiredBullets % 2 == 0) {
            Duration diff = Duration.between(bulletsArrayList.get(0).getFireTime(), bulletToFire.getFireTime());
            long diffMilliSecond = diff.toMillis();
            if (diffMilliSecond >= 1000) { //is ready
                bulletsArrayList.add(0, bulletsArrayList.get(1));
                bulletsArrayList.add(1, bulletToFire);
                TankTroubleMap.getBullets().add(bulletToFire);
                System.out.println("bullets lunch shod............");
            } else {
                System.out.println("Not ready to lunch...!"); // need graphic
            }
        }
    }

    /**
     * This methood will lower tank's heath if the tank doesn't have protecting sheield
     *
     * @param bulletDamage is damage of bullet
     */
    public void receiveDamage(int bulletDamage) {
        if (!hasShield) {
            health -= bulletDamage;
        } else {
            //TODO: reflect the bullet
        }
    }

    /**
     * This method rotate four points of tank based on input angle.
     *
     * @param pointsToRotate is points to be rotated (here are 4 points of tank )
     * @param centerPoint    is center point of tank
     * @param rotationAngle  is angle of rotation
     * @return array list of new coordinate after rotation
     */
    public ArrayList<Coordinate> rotatePoints(@NotNull ArrayList<Coordinate> pointsToRotate, Coordinate centerPoint, double rotationAngle) {
        ArrayList<Coordinate> rotatedCoordinates = new ArrayList<>();
        for (Coordinate pointToRotate : pointsToRotate) {
            double currentX = pointToRotate.getXCoordinate(), currentY = pointToRotate.getYCoordinate();
            double centerX = centerPoint.getXCoordinate(), centerY = centerPoint.getYCoordinate();
            double x = currentX - centerX, y = currentY - centerY;
            double xToMove = (x * Math.cos(Math.toRadians(rotationAngle)) - y * Math.sin(Math.toRadians(rotationAngle)) + centerX);
            double yToMove = (x * Math.sin(Math.toRadians(rotationAngle)) + y * Math.cos(Math.toRadians(rotationAngle)) + centerY);
            rotatedCoordinates.add(new Coordinate(xToMove, yToMove));
        }
        return rotatedCoordinates;
    }


    /**
     * This method move a point based on angle, speed and etc.
     *
     * @param pointToMove //???????????????????/
     * @param command     is as string that show type of command (up or down)
     * @param angle       is angle of vector of move
     * @return new coordinate after movement
     */
    public Coordinate movePoint(Coordinate pointToMove, @NotNull String command, double angle) {
        Coordinate movedCoordinate = new Coordinate();
        if (command.equals("UP")) {
            movedCoordinate.setXCoordinate(pointToMove.getXCoordinate() - Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
            movedCoordinate.setYCoordinate(pointToMove.getYCoordinate() - Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
        } else {
            movedCoordinate.setXCoordinate(pointToMove.getXCoordinate() + Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
            movedCoordinate.setYCoordinate(pointToMove.getYCoordinate() + Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
        }
        return movedCoordinate;
    }

    /**
     * This method move tank or it's coordinate based on angle, speed and etc.
     *
     * @param pointsToMove points to move (here are 4 tank's points)
     * @param command      is as string that show type of command (up or down)
     * @param angle        is angle of vector of move
     * @return array list of new coordinate after movement
     */
    public ArrayList<Coordinate> movePoints(@NotNull ArrayList<Coordinate> pointsToMove, String command, double angle) {
        ArrayList<Coordinate> movedCoordinates = new ArrayList<>();
        for (Coordinate coordinate : pointsToMove) {
            movedCoordinates.add(movePoint(coordinate, command, angle));
        }
        return movedCoordinates;
    }

    public boolean canMove(ArrayList<Coordinate> coordinates) {
        return !TankTroubleMap.checkOverlapWithAllWalls(coordinates)
//                && !TankTroubleMap.checkOverlapWithAllTanks(coordinates);
        && !TankTroubleMap.checkOverlapWithAllTanks(this);
    }

    /* update the tank angle
     * the positive direction is suppesed counter-clockwise
     */
    public void rotateClockwise() {
        angle -= Constants.TANK_ROTATION_SPEED; // + or - for clock wise?????
    }

    /* update the tank angle
     * the positive direction is suppesed counter-clockwise
     */
    public void rotateCounterClockwise() {
        angle += Constants.TANK_ROTATION_SPEED; //is it anti clock wise????  or - for clock wise?????
    }


    /**
     * Getter method of tankCoordinates field
     *
     * @return four coordinates of tank
     */
    public ArrayList<Coordinate> getTankCoordinates() {
        return tankCoordinates;
    }

    /**
     * Getter method of health field
     *
     * @return health of wall
     */
    public int getHealth() {
        return health;
    }

    /**
     * Getter method of tankImage field
     *
     * @return image of tank
     */
    public Image getTankImage() {
        return tankImage;
    }

    /**
     * Getter method of angle field
     *
     * @return angle of tank based on y axis
     */
    public double getAngle() {
        return angle;
    }

    /**
     * This is setter method for angle field.
     *
     * @param angle is an integer as angle of tank
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Getter method of centerPointCoordinate field
     *
     * @return coordinate of center point of tank
     */
    public Coordinate getCenterPointOfTank() {
        return centerPointCoordinate;
    }

    /**
     * This is setter method for health field.
     *
     * @param health is an integer as health of tank
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * This is setter method for centerPointCoordinate field.
     *
     * @param centerPointCoordinate is coordinate of center point of tank
     */
    public void setCenterPointCoordinate(Coordinate centerPointCoordinate) {
        this.centerPointCoordinate = centerPointCoordinate;
    }


    /**
     * This is setter method for tankCoordinates field.
     *
     * @param tankCoordinates is array list of coordinate of four tank points
     */
    public void setTankCoordinates(ArrayList<Coordinate> tankCoordinates) {
        this.tankCoordinates = tankCoordinates;
    }
}
