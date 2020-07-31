package logic.Wall;

import logic.Coordinate;

/**
 * This class represent a indestructible wall with extend "Wall" class.
 * This class has no method.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class IndestructibleWall extends Wall {
    /**
     * This constructor fill fields based on input parameter and set type of wall.
     *
     * @param coordinate is starting point of wall
     * @param direction  is direction of wall (VERTICAL/HORIZONTAL)
     */
    public IndestructibleWall(Coordinate coordinate, String direction) {
        super(coordinate, direction);
        this.setDestroyable(false);
    }
}
