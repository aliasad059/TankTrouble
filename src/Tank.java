import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.Duration;
import java.util.ArrayList;

/**
 *
 */
public class Tank {
    private String color;
    private String pathOfTankPicture;// change the pass when player choose another tank shape or color
    private int health;
    //    private final int tankNumber; // ezafe...
    private boolean hasShield;
    //NOTE: the coordinate may not be updated as the tank moves
    private Coordinate pixelCoordinate;
    private int size;
    private ArrayList<Bullets> bulletsArrayList; // there is 2 bullets in this array
    private int prizeType;
    private String bulletsType;
    private int bulletsDamage;
    private int numberOfFiredBullets;
    private TankState tankState;

    /**
     * //     * @param tankNumber
     *
     * @param health
     * @param pixelCoordinate
     * @param size
     * @param color
     */
    public Tank(/*int tankNumber, */int health, Coordinate pixelCoordinate, int size, String color) {
        this.health = health;
//        this.tankNumber = tankNumber;
        this.size = size;
        this.color = color;
        bulletsType = "NORMAL";
        hasShield = false;
        bulletsDamage = Constants.BULLET_POWER;
        this.pixelCoordinate = pixelCoordinate;
        tankState = new TankState();
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
        Bullets bulletToFire = new Bullets(bulletsDamage, bulletsType, pixelCoordinate);
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
            if (health <= 0){
                tankState.tankBlasted = true;
            }
        } else {
            //TODO: reflect the bullet
        }
    }


    public class TankState {

        public int diam;
        public boolean tankBlasted;

        private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT, keyFIRE, keyPrize;
        private KeyHandler keyHandler;

        public TankState() {
            this.diam = diam;
            tankBlasted = false;
            diam = Constants.TANK_SIZE;
            //
            keyUP = false;
            keyDOWN = false;
            keyRIGHT = false;
            keyLEFT = false;
            keyFIRE = false;
            keyPrize = false;
            //
            keyHandler = new KeyHandler();
        }

        /**
         * The method which updates the game state.
         */
        public void update() {
            if (keyFIRE)
                fire();
            if (keyPrize)
                usePrize();
            if (keyUP)
                pixelCoordinate.setYCoordinate(pixelCoordinate.getYCoordinate() - Constants.TANK_SPEED);
            if (keyDOWN)
                pixelCoordinate.setYCoordinate(pixelCoordinate.getYCoordinate() + Constants.TANK_SPEED);
            if (keyLEFT)
                pixelCoordinate.setXCoordinate(pixelCoordinate.getXCoordinate() - Constants.TANK_SPEED);
            if (keyRIGHT)
                pixelCoordinate.setXCoordinate(pixelCoordinate.getXCoordinate() + Constants.TANK_SPEED);

            //checking if the tank do not leave the map
            pixelCoordinate.setXCoordinate(Math.max(pixelCoordinate.getXCoordinate(), 0));
            pixelCoordinate.setXCoordinate(Math.min(pixelCoordinate.getXCoordinate(),
                    MapFrame.getMap().getWidth() - diam));
            pixelCoordinate.setYCoordinate(Math.max(pixelCoordinate.getYCoordinate(), 0));
            pixelCoordinate.setYCoordinate(Math.min(pixelCoordinate.getYCoordinate(),
                    MapFrame.getMap().getHeight() - diam));
        }


        public KeyListener getKeyListener() {
            return keyHandler;
        }

        /**
         * The keyboard handler.
         */
        class KeyHandler extends KeyAdapter {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        keyFIRE = true;
                        break;
                    case KeyEvent.VK_ENTER:
                        keyPrize = true;
                        break;
                    case KeyEvent.VK_UP:
                        keyUP = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDOWN = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLEFT = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRIGHT = true;
                        break;
//                    case KeyEvent.VK_ESCAPE:
//                        tankBlasted = true;
//                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        keyFIRE = false;
                        break;
                    case KeyEvent.VK_ENTER:
                        keyPrize = false;
                        break;
                    case KeyEvent.VK_UP:
                        keyUP = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDOWN = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLEFT = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRIGHT = false;
                        break;
                }
            }
        }
    }
}

