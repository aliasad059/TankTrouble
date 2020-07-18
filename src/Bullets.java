
/**
 * This class represent bullets for player's tank.
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Bullets {
    // kind of Bullets
    private String type;
    // damage of bullets
    private float damage;
    // speed of bullets
    private float speed;
    Coordinate coordinate;
    // the time that this kind of bullets stay in map
    private final static float lifeTime=4;

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     */
    public Bullets(int damage,String type,int x,int y){
        this.type = type;
        this.coordinate = coordinate;
        speed=30;
        if (type.equals("LASER")){
            speed*=3;
        }
        this.coordinate=new Coordinate(x,y);
    }

}
