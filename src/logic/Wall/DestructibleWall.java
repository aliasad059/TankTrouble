package logic.Wall;

import logic.Coordinate;

/**
 * This class represent a destructible wall with extend "Wall" class.
 * This type of wall has health parameter.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class DestructibleWall extends Wall {

    private int health;

    /**
     * this is constructor of "DestructibleWall" class and fill coordinate and direction field with input parameter and set type of wall.
     *
     * @param coordinate is starting point of wall
     * @param direction  is direction of wall (VERTICAL/HORIZONTAL)
     * @param health     is health of this wall
     */
    public DestructibleWall(Coordinate coordinate, String direction, int health) {
        super(coordinate, direction);
        this.setDestroyable(true);
        this.health = health;
    }

    /**
     * This method effect received damage from bullets on health of wall.
     *
     * @param bulletsDamage damage of bullets
     */
    public void receiveDamage(int bulletsDamage) {
        health -= bulletsDamage;
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
     * This is setter method for health field.
     *
     * @param health is an integer as health of wall
     */
    public void setHealth(int health) {
        this.health = health;
    }
}
