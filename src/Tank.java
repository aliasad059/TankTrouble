
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

    /**
     * catch a prize in the map
     * @param xOfPrize x of prize
     * @param yOfPrize y of prize
     */
    public void catchPrize(int xOfPrize, int yOfPrize) {
        if (prizeType != 0) {
            System.out.println("You haven't used your last prize...!"); // need graphic
        } else {
            prizeType = Interface.getTankTroubleMap().getArrayMap()[xOfPrize][yOfPrize] - 2;
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
     * @param howManyTimes how many time should the bullet power multiplied
     */
    private void increaseBulletPower(int howManyTimes) {
        bulletsDamage *= howManyTimes;
    }
    /**
     * increase tank's health
     * @param howManyTimes how many time should the tank's health multiplied
     */
    private void increaseHealth(double howManyTimes) {
        health *= howManyTimes;
    }

    /**
     * when the tank use its shield prize
     */
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

    /**
     * fire a bullet
     */
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

    /**
     * move the tank
     * @param command where to move ? LEFT RIGHT UP DOWN
     */
    public void move(String command) {
        if (command.equals("RIGHT")){
            if (coordinate.getXCoordinate()<Interface.getTankTroubleMap().getyAxisSize()){

            }
        }else if (command.equals("LEFT")){
            if (coordinate.getXCoordinate()!=0){

            }
        }else if (command.equals("UP")){
            if (coordinate.getYCoordinate() != 0){

            }
        }else if (command.equals("DOWN")){
            if (coordinate.getYCoordinate()<Interface.getTankTroubleMap().getyAxisSize()){

            }
        }
    }

    /**
     * when a bullet hits the tank
     * @param damageAmount the bullet power
     */
    public void getDamage(int damageAmount) {
        if (!hasShield) {
            health -= damageAmount;
        }
    }
}
