package logic;

import java.time.LocalDateTime;

/**
 * This class represent bullets for player's tank.
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Bullets {
    // type of Bullets
    private String type;
    // damage of bullets
    private float damage;
    // speed of bullets
    private float speed;
    Coordinate coordinate;
    //time that bullets fired from tank
    private LocalDateTime fireTime;
    // the time that this kind of bullets stay in map
    private float lifeTime;
    private double angle;

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     * ??????????????
     */
    public Bullets(int BulletsDamage, String BulletsType, Coordinate coordinate, double angle){
        type="";
        damage=BulletsDamage;
        type=BulletsType;
        if(BulletsType.equals("NORMAL")){
            speed = Constants.BULLET_SPEED;
        }
        else if(BulletsType.equals("LASER")){ // ba else nazadam yevakht khastim emtiazi ezafe konim ye golule
            speed = 3*Constants.BULLET_SPEED;
        }
        this.angle = angle;
        this.coordinate=new Coordinate();
        this.coordinate.setXCoordinate(coordinate.getXCoordinate());
        this.coordinate.setYCoordinate(coordinate.getYCoordinate());

        fireTime = LocalDateTime.now();
        Thread thread = new Thread(() -> { //change to swing worker
            try {
                for(int i=0; i<4; i++){
                    Thread.sleep(1000);
                    lifeTime++;
                }
                removeBullet();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public LocalDateTime getFireTime() {
        return fireTime;
    }

    private void move(){
        //@TODO something
    }

    private void removeBullet(){
        //@TODO something
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public class BulletState {
    }

}