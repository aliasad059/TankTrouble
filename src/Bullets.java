
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

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     * ??????????????
     */
    public Bullets(int BulletsDamage, String BulletsType, Coordinate coordinate){
        type="";
        damage=BulletsDamage;
        type=BulletsType;
        if(BulletsType.equals("NORMAL")){
            speed=10;
        }
        else if(BulletsType.equals("LASER")){ // ba else nazadam yevakht khastim emtiazi ezafe konim ye golule
            speed=30;
        }
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
}
