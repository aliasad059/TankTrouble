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
    protected String color;
    protected String pathOfTankPicture;// change the pass when player choose another tank shape or color
    protected int health;
    protected boolean hasShield;
    protected Coordinate pixelCoordinate;
    protected int size;
    protected ArrayList<Bullets> bulletsArrayList; // there is 2 bullets in this array
    protected int prizeType;
    protected String bulletsType;
    protected int bulletsDamage;
    protected int numberOfFiredBullets;
//    protected TankState tankState;
    protected Image tankImage;
    protected double angle; //Angle to the Y-axis
    protected boolean tankBlasted ;


    public Tank(int health, Coordinate pixelCoordinate, String tankImagePass) {
        this.health = health;
        this.size = size;
        this.color = color;
        bulletsType = "NORMAL";
        hasShield = false;
        bulletsDamage = Constants.BULLET_POWER;
        this.pixelCoordinate = pixelCoordinate;
        this.angle = 0;
        try {
            tankImage = ImageIO.read(new File(tankImagePass));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        tankState = new TankState();
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
            //prizeType = Interface.getTankTroubleMap().getArrayMap()[xOfPrize][yOfPrize] - 2;
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
        Bullets bulletToFire = new Bullets(bulletsDamage, bulletsType, pixelCoordinate, angle);
        if (bulletsArrayList.size() < 2) {
            bulletsArrayList.add(bulletToFire);
            numberOfFiredBullets++;
        } else if (numberOfFiredBullets % 2 == 1) {
            bulletsArrayList.add(0, bulletsArrayList.get(1));
            bulletsArrayList.add(1, bulletToFire);
            numberOfFiredBullets++;
        } else if (numberOfFiredBullets % 2 == 0) {
            Duration diff = Duration.between(bulletsArrayList.get(0).getFireTime(), bulletToFire.getFireTime());
            long diffMilliSecond = diff.toMillis();
            if (diffMilliSecond >= 1000) { //is ready
                bulletsArrayList.add(0, bulletsArrayList.get(1));
                bulletsArrayList.add(1, bulletToFire);
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

    public Coordinate getPixelCoordinate() {
        return pixelCoordinate;
    }

//    public TankState getTankState() {
//        return tankState;
//    }

//    public KeyListener getTankKeyListener() {
//        return this.getTankState().getKeyListener();
//    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

}



