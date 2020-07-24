package logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;

/**
 *
 */
public class Tank {
    //    protected String color;
    protected String pathOfTankPicture;// change the pass when player choose another tank shape or color
    protected int health;
    protected boolean hasShield;
    //    protected Coordinate pixelCoordinate;
//    protected int size;
    protected ArrayList<Bullets> bulletsArrayList; // there is 2 bullets in this array
    protected int prizeType;
    protected String bulletsType;
    protected int bulletsDamage;
    protected int numberOfFiredBullets;
    protected Image tankImage;
    protected double angle; //Angle to the Y-axis
    protected boolean tankBlasted;
    protected Coordinate centerPointCoordinate;
    protected ArrayList<Coordinate> tankCoordinates;


    public Tank(int health, Coordinate centerPointCoordinate, String tankImagePass) {
        this.health = health;
        bulletsType = "NORMAL";
        hasShield = false;
        bulletsDamage = Constants.BULLET_POWER;
        tankCoordinates = new ArrayList<>();
        this.centerPointCoordinate = centerPointCoordinate;
        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));
        this.angle = 0;
        bulletsArrayList = new ArrayList<>();
        try {
            tankImage = ImageIO.read(new File(tankImagePass));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * catch a prize in the map
     *
     * @param xOfPrize x of prize
     * @param yOfPrize y of prize
     */
    public void catchPrize(int xOfPrize, int yOfPrize) {
        if (prizeType != 0) {
            System.out.println("You haven't used your last prize...!"); // need graphic
        } else {

            for (int i = 0; i < TankTroubleMap.getPrizes().size(); i++) {
                if (TankTroubleMap.checkOverLap(tankCoordinates, TankTroubleMap.getPrizes().get(i).getPointsCoordinate()))
                    ;
                prizeType = TankTroubleMap.getPrizes().get(i).getType();
                TankTroubleMap.getPrizes().remove(i);
                break;
            }
        }
    }

    /**
     * use the caught prize
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
     * increase bullet power
     *
     * @param howManyTimes how many time should the bullet power multiplied
     */
    private void increaseBulletPower(int howManyTimes) {
        bulletsDamage *= howManyTimes;
    }

    /**
     * increase tank's health
     *
     * @param howManyTimes how many time should the tank's health multiplied
     */
    private void increaseHealth(double howManyTimes) {
        health *= howManyTimes;
    }

    /**
     * when the tank use its shield prize
     */
    private void protectTank() {
        //TODO: change graphic of tank
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
     * fire a bullet
     */
    public void fire() {
        System.out.println("Bullets in map:  " + TankTroubleMap.getBullets().size());
        Coordinate bulletCoordinate = new Coordinate();
        bulletCoordinate.setXCoordinate(centerPointCoordinate.getXCoordinate() + Constants.LOOLE_TANK_SIZE * Math.sin(Math.toRadians(angle)));
        bulletCoordinate.setYCoordinate(centerPointCoordinate.getYCoordinate() + Constants.LOOLE_TANK_SIZE * Math.cos(Math.toRadians(angle)));

        Bullets bulletToFire = new Bullets(bulletsDamage, bulletsType, centerPointCoordinate, angle);
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
        // now fire last bullets of
    }


    /**
     * when a bullet hits the tank
     *
     * @param damageAmount the bullet power
     */
    public void getDamage(int damageAmount) {
        if (!hasShield) {
            health -= damageAmount;
            if (health <= 0) {
                tankBlasted = true;
            }
        } else {
            //TODO: reflect the bullet
        }
    }

    public Image getTankImage() {
        return tankImage;
    }

//    public Coordinate getPixelCoordinate() {
//        return pixelCoordinate;
//    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public Coordinate getCenterPointOfTank() {
        return centerPointCoordinate;
    }

    public ArrayList<Coordinate> rotatePoints(ArrayList<Coordinate> pointsToRotate, Coordinate center, double rotationAngle) {
        ArrayList<Coordinate> rotatedCoordinates = new ArrayList<>();
        for (int i = 0; i < pointsToRotate.size(); i++) {
            Coordinate pointToRotate = pointsToRotate.get(i);
            double currentX = pointToRotate.getXCoordinate(), currentY = pointToRotate.getYCoordinate();
            double centerX = center.getXCoordinate(), centerY = center.getYCoordinate();
            double x = currentX - centerX, y = currentY - centerY;
            double xToMove = (x * Math.cos(Math.toRadians(rotationAngle)) - y * Math.sin(Math.toRadians(rotationAngle)) + centerX);
            double yToMove = (x * Math.sin(Math.toRadians(rotationAngle)) + y * Math.cos(Math.toRadians(rotationAngle)) + centerY);
            rotatedCoordinates.add(new Coordinate(xToMove, yToMove));
        }
        return rotatedCoordinates;
    }

    public Coordinate movePoint(Coordinate pointToMove, String command, double angle) {
        Coordinate movedCoordinate = new Coordinate();
        if (command.equals("UP")) {
            movedCoordinate.setXCoordinate(pointToMove.getXCoordinate() + Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
            movedCoordinate.setYCoordinate(pointToMove.getYCoordinate() - Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
        } else {
            movedCoordinate.setXCoordinate(pointToMove.getXCoordinate() - Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
            movedCoordinate.setYCoordinate(pointToMove.getYCoordinate() + Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
        }
        return movedCoordinate;
    }

    public ArrayList<Coordinate> movePoints(ArrayList<Coordinate> pointsToMove, String command, double angle) {
        ArrayList<Coordinate> movedCoordinates = new ArrayList<>();
        for (int i = 0; i < pointsToMove.size(); i++) {
            movedCoordinates.add(movePoint(pointsToMove.get(i), command, angle));
        }
        return movedCoordinates;
    }

    public boolean canMove(ArrayList<Coordinate> coordinates) {
        return !TankTroubleMap.checkOverlapWithAllWalls(coordinates)
                && !TankTroubleMap.checkOverlapWithAllTanks(this);
    }

    public ArrayList<Coordinate> getTankCoordinates() {
        return tankCoordinates;
    }

    public void rotateClockwise() {
        angle -= Constants.TANK_ROTATION_SPEED;
    }

    public void rotateCounterClockwise() {
        angle += Constants.TANK_ROTATION_SPEED;
    }
}
