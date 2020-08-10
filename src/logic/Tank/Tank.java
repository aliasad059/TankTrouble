package logic.Tank;

import logic.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
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
public class Tank implements Serializable {
    private int health;
    private boolean blasted;
    private boolean hasShield;
    private transient ArrayList<Bullet> bulletArrayList;
    private int prizeType;
    private String bulletType;
    private int bulletDamage;
    private int numberOfFiredBullets;
    private transient Image tankImage;
    private String tankImagePath;
    private double angle;
    private Coordinate centerPointCoordinate;
    private ArrayList<Coordinate> tankCoordinates;
    private transient Image prizeImage;
    private String prizeImagePath;
    protected transient TankTroubleMap tankTroubleMap;
    private int numberOfDestroyedTank;

    /**
     * This constructor set valid random place for tank and initialize fields based on game rules and input parameters.
     *
     * @param tankImagePath is a string that shows path of image tank
     */
    public Tank(String tankImagePath, TankTroubleMap tankTroubleMap) {
        numberOfDestroyedTank = 0;
        this.health = Constants.TANK_HEALTH;
        this.tankImagePath = tankImagePath;
        bulletType = "NORMAL";
        hasShield = false;
        this.bulletDamage = Constants.BULLET_DAMAGE;
        this.tankTroubleMap = tankTroubleMap;
        tankCoordinates = new ArrayList<>();
        centerPointCoordinate = tankTroubleMap.freePlaceToPut(Constants.TANK_SIZE, Constants.TANK_SIZE);
        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));

        tankCoordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerPointCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));
        this.angle = 0;
        prizeImagePath = "kit/tankStatus";
        bulletArrayList = new ArrayList<>();
        try {
            tankImage = ImageIO.read(new File(tankImagePath + "/normal.png"));
            prizeImage = ImageIO.read(new File(prizeImagePath + "/noPrize.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method catch a prize based on overlap of tank and prize.
     */
    public void catchPrize() {
        if (prizeType == 0) {
            for (int i = 0; i < TankTroubleMap.getPrizes().size(); i++) {
                if (TankTroubleMap.checkOverLap(tankCoordinates, TankTroubleMap.getPrizes().get(i).getCoordinates())) {
                    prizeType = TankTroubleMap.getPrizes().get(i).getType();
                    prizeImage = TankTroubleMap.getPrizes().get(i).getPrizeImage();
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
            bulletType = "LASER";
            try {
                tankImage = ImageIO.read(new File(tankImagePath + "/laser.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(3000);
                    bulletType = "NORMAL";
                    try {
                        tankImage = ImageIO.read(new File(tankImagePath + "/normal.png"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
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
            SoundsOfGame noPrize = new SoundsOfGame("noPrize", false);
            noPrize.playSound();
        }
        prizeType = 0;
        try {
            prizeImage = ImageIO.read(new File(prizeImagePath + "/noPrize.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method increase bullet power
     *
     * @param howManyTimes how many time should the bullet power multiplied
     */
    private void increaseBulletPower(int howManyTimes) {
        bulletDamage *= howManyTimes;
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
        thread.start();
    }


    /**
     * This method lunch or fire a bullets based on rules of game (2 bullets in a second).
     */
    public void fire() {
        Coordinate bulletCoordinate = new Coordinate();
        bulletCoordinate.setXCoordinate(centerPointCoordinate.getXCoordinate() - Constants.LOOLE_TANK_SIZE * Math.sin(Math.toRadians(angle)));
        bulletCoordinate.setYCoordinate(centerPointCoordinate.getYCoordinate() - Constants.LOOLE_TANK_SIZE * Math.cos(Math.toRadians(angle)));

        Bullet bulletToFire = new Bullet(bulletDamage, bulletType, bulletCoordinate, angle, tankTroubleMap, false, 1);

        if (bulletArrayList.size() < 2) {
            bulletArrayList.add(bulletToFire);
            //tankTroubleMap.getBullets().add(bulletToFire);
            //numberOfFiredBullets++;
            SoundsOfGame soundsOfGame;
            if (bulletType.equals("NORMAL")) {
                soundsOfGame = new SoundsOfGame("normal", false);
            } else {
                soundsOfGame = new SoundsOfGame("laser", false);
            }
            soundsOfGame.playSound();
            tankTroubleMap.getBullets().add(bulletToFire);
            numberOfFiredBullets++;

        } else if (numberOfFiredBullets % 2 == 1) {
            bulletArrayList.add(0, bulletArrayList.get(1));
            bulletArrayList.add(1, bulletToFire);
            SoundsOfGame soundsOfGame;
            if (bulletType.equals("NORMAL")) {
                soundsOfGame = new SoundsOfGame("normal", false);
            } else {
                soundsOfGame = new SoundsOfGame("laser", false);
            }
            soundsOfGame.playSound();
            tankTroubleMap.getBullets().add(bulletToFire);
            numberOfFiredBullets++;


        } else if (numberOfFiredBullets % 2 == 0) {
            Duration diff = Duration.between(bulletArrayList.get(0).getFireTime(), bulletToFire.getFireTime());
            long diffMilliSecond = diff.toMillis();
            if (diffMilliSecond >= 1000) { //is ready
                bulletArrayList.add(0, bulletArrayList.get(1));
                bulletArrayList.add(1, bulletToFire);
                SoundsOfGame soundsOfGame;
                if (bulletType.equals("NORMAL")) {
                    soundsOfGame = new SoundsOfGame("normal", false);
                } else {
                    soundsOfGame = new SoundsOfGame("laser", false);
                }
                soundsOfGame.playSound();
                tankTroubleMap.getBullets().add(bulletToFire);
                //numberOfFiredBullets++; // new changes................... test shavad

            } else {
                SoundsOfGame soundsOfGame = new SoundsOfGame("notReady", false);
                soundsOfGame.playSound();
            }
        }
    }

    /**
     * This method will lower tank's heath if the tank doesn't have protecting sheield
     *
     * @param bulletDamage is damage of bullet
     */
    public void receiveDamage(int bulletDamage) {
        if (!hasShield) {
            health -= bulletDamage;
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
     * @param pointToMove is center point of shape that player wanna move
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
        if (tankTroubleMap == null)
            System.out.println("NULLLLLL");
        return !TankTroubleMap.checkOverlapWithAllWalls(coordinates)
                && !tankTroubleMap.checkOverlapWithAllTanks(this);
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


    /**
     * Getter method of prizeImage field
     *
     * @return an image as image of prize that user catch it
     */
    public Image getPrizeImage() {
        return prizeImage;
    }

    /**
     * This is setter method for angle field.
     *
     * @param bulletDamage is an integer as damage of bullet
     */
    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    /**
     * Getter method of tankTroubleMap field.
     *
     * @return tankTroubleMap of game
     */
    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    /**
     * This is setter method for tankTroubleMap field.
     *
     * @param tankTroubleMap is tankTrouble map as map of game
     */
    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }

    /**
     * This is setter method for tankImage field.
     *
     * @param tankImage is image of tank
     */
    public void setTankImage(Image tankImage) {
        this.tankImage = tankImage;
    }

    /**
     * Getter method of tankTroubleMap field.
     *
     * @return tankTroubleMap of game
     */
    public int getBulletDamage() {
        return bulletDamage;
    }

    /**
     * Getter method of tankTroubleMap field.
     *
     * @return tankTroubleMap of game
     */
    public boolean isBlasted() {
        return blasted;
    }

    /**
     * This method get path of image of prize based on its type.
     *
     * @return string as path of image of prize
     */
    public String getPrizeImagePath() {
        if (prizeType == 1) {
            return "kit/prizes/shield.png";
        } else if (prizeType == 2) {
            return "kit/prizes/laser.png";
        } else if (prizeType == 3) {
            return "kit/prizes/health.png";
        } else if (prizeType == 4) {
            return "kit/prizes/damage2x.png";
        } else if (prizeType == 5) {
            return "kit/prizes/damage3x.png";
        } else
            return "kit/tankStatus/noPrize.png";
    }

    /**
     * This method get path of image of tank based on its type.
     *
     * @return string as path of image of tank
     */
    public String getTankImagePath() {
        if (prizeType == 2) {
            return tankImagePath + "/laser.png";
        }
        return tankImagePath + "/normal.png";
    }

    /**
     * Getter method of numberOfDestroyedTank field.
     *
     * @return an integer as  number of tank that player destroyed
     */
    public int getNumberOfDestroyedTank() {
        return numberOfDestroyedTank;
    }

    /**
     * This is setter method for numberOfDestroyedTank field.
     *
     * @param numberOfDestroyedTank is an integer as  number of tank that player destroyed
     */
    public void setNumberOfDestroyedTank(int numberOfDestroyedTank) {
        this.numberOfDestroyedTank = numberOfDestroyedTank;
    }

}
