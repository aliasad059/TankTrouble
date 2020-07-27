package logic;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent a prize of game.
 * every prize can has one type "health", "damage2x", "damage3x", "laser" and "shield".
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Prize implements Serializable {

    private int type;
    private Coordinate centerCoordinate;
    private ArrayList<Coordinate> coordinates;

    /**
     * This constructor set coordinate for prize based on center Coordinate. // is random????????????????????????????
     *
     * @param type       is type of prize
     * @param coordinate is coordinate of center point of prize
     */
    public Prize(int type, @NotNull Coordinate coordinate) {
        this.type = type;
        this.centerCoordinate = new Coordinate(coordinate.getXCoordinate(), coordinate.getYCoordinate());
        coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() - (double) Constants.PRIZE_SIZE / 2
                , centerCoordinate.getYCoordinate() - (double) Constants.PRIZE_SIZE / 2));

        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() + (double) Constants.PRIZE_SIZE / 2
                , centerCoordinate.getYCoordinate() - (double) Constants.PRIZE_SIZE / 2));

        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() + (double) Constants.PRIZE_SIZE / 2
                , centerCoordinate.getYCoordinate() + (double) Constants.PRIZE_SIZE / 2));

        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() - (double) Constants.PRIZE_SIZE / 2
                , centerCoordinate.getYCoordinate() + (double) Constants.PRIZE_SIZE / 2));
    }

    /**
     * Getter method of type field
     *
     * @return type of prize
     */
    public int getType() {
        return type;
    }

    /**
     * Getter method of centerCoordinate field
     *
     * @return a coordinate that shows center point of prize
     */
    public Coordinate getCenterCoordinate() {
        return centerCoordinate;
    }

    /**
     * Getter method of coordinates field
     *
     * @return an array list of four point's of prize
     */
    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }
}
