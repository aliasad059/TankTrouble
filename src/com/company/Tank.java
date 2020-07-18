package com.company;

import java.time.Duration;
import java.util.ArrayList;

/**
 *
 */
public class Tank {
    private String color;
    private int health;
    private final int tankNumber; // ezafe...
    private boolean hasShield;
    private Coordinate coordinate;
    private int size;
    private ArrayList<Bullets> bulletsArrayList; // there is 2 bullets in this array
    private int prizeType;
    private String bulletsType;
    private int bulletsDamage;
    private int numberOfFiredBullets;

    /**
     *
     * @param tankNumber
     * @param health
     * @param coordinate
     * @param size
     * @param color
     */
    public Tank(int tankNumber, int health, Coordinate coordinate, int size,String color) {
        this.health = health;
        this.tankNumber = tankNumber;
        this.size = size;
        this.color = color;
        bulletsType="NORMAL";
        hasShield=false;
        bulletsDamage=20; //???????????????
    }

    public void getPrize(int xOfPrize, int yOfPrize) {
        if (prizeType != 0) {
            System.out.println("You haven't used your last prize...!"); // need graphic
        } else {
            prizeType = Interface.getMap()[xOfPrize][yOfPrize] - 2;
        }
    }

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

    private void increaseBulletPower(int howManyTimes) {
        bulletsDamage *= howManyTimes;
    }

    private void increaseHealth(double howManyTimes) {
        health *= howManyTimes;
    }

    private void protectTank() {
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

    public void fire() {
        Bullets bulletToFire = new Bullets(bulletsDamage, bulletsType, coordinate);
        if(bulletsArrayList.size()<2){
            bulletsArrayList.add(bulletToFire);
            numberOfFiredBullets++;
        }
        else if(numberOfFiredBullets%2==1){
            bulletsArrayList.add(0,bulletsArrayList.get(1));
            bulletsArrayList.add(1,bulletToFire);
            numberOfFiredBullets++;
        }
        else if(numberOfFiredBullets%2==0){
            Duration diff = Duration.between(bulletsArrayList.get(0).getFireTime(), bulletToFire.getFireTime());
            long diffMilliSecond = diff.toMillis();
            if(diffMilliSecond>=1000){ //is ready
                bulletsArrayList.add(0,bulletsArrayList.get(1));
                bulletsArrayList.add(1,bulletToFire);
            }
            else {
                System.out.println("Not ready to lunch...!"); // need graphic
            }
        }
        // now fire last bullets of
    }

    public void move(String command) {
        if (command.equals("RIGHT")){
            if (x<Interface.getMap().getWidth()){
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {

                    }
                }
            }
        }else if (command.equals("LEFT")){
            if (x!=0){

            }
        }else if (command.equals("UP")){
            if (y != 0){

            }
        }else if (command.equals("DOWN")){
            if (y<Interface.getMap().getHeight){

            }
        }
    }

    public void getDamage(int damageAmount) {
        if (!hasShield) {
            health -= damageAmount;
        }
    }
}
