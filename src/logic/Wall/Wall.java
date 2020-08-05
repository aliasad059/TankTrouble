package logic.Wall;

import logic.Constants;
import logic.Coordinate;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent a wall for game.
 * Every wall has a type (destructible or indestructible) and direction (horizontal or vertical).
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Wall implements Serializable {

    // up point for vertical and left point for horizontal
    private Coordinate startingPoint;
    private boolean destroyable;
    // horizontal or vertical
    private String direction;
    private int height, width;
    private ArrayList<Coordinate> pointsArray;

    /**
     * This is constructor of wall class and new (allocate) some fields and fill them based on input parameters.
     *
     * @param coordinate is coordinate (starting point) of wall
     * @param direction  is a string that shows direction of wall
     */
    public Wall(Coordinate coordinate, String direction) {
        if (direction.equals("HORIZONTAL")) {
            height = Constants.WALL_HEIGHT_HORIZONTAL;
            width = Constants.WALL_WIDTH_HORIZONTAL;
        } else {
            height = Constants.WALL_HEIGHT_VERTICAL;
            width = Constants.WALL_WIDTH_VERTICAL;
        }
        startingPoint = new Coordinate();
        startingPoint = coordinate;
        pointsArray = new ArrayList<>();
        pointsArray.add(startingPoint);
        pointsArray.add(new Coordinate(coordinate.getXCoordinate() +width, coordinate.getYCoordinate()));
        pointsArray.add(new Coordinate(coordinate.getXCoordinate() + width, coordinate.getYCoordinate()+height));
        pointsArray.add(new Coordinate(coordinate.getXCoordinate(), coordinate.getYCoordinate() + height));
        this.direction = direction;
    }

    /**
     * Getter method of startingPoint field
     *
     * @return coordinate as starting point of wall
     */
    public Coordinate getStartingPoint() {
        return startingPoint;
    }

    /**
     * Getter method of direction field
     *
     * @return direction of wall
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Getter method of pointsArray field
     *
     * @return array list of points of wall
     */
    public ArrayList<Coordinate> getPointsArray() {
        return pointsArray;
    }

    /**
     * This is setter method for destroyable field.
     *
     * @param destroyable is a boolean that show wall is destroyable or not (shows type of wall)
     */
    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    /**
     * Getter method of destroyable field
     *
     * @return boolean that show this wall is destroyable or not
     */
    public boolean isDestroyable() {
        return destroyable;
    }
}
