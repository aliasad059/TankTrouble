package logic;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represent a coordinate in 2d palate contain x and y coordinate.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Coordinate implements Serializable {
    // coordinate based on x axis
    private double xCoordinate;
    // coordinate based on y axis
    private double yCoordinate;

    /**
     * This first is constructor of this class and set -1 for x and y of coordinate.
     */
    public Coordinate() {
        this.xCoordinate = -1;
        this.yCoordinate = -1;
    }

    /**
     * second constructor fill field based on input parameters.
     */
    public Coordinate(double xCoordinate, double yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }


    /**
     * Getter method of xCoordinate field
     *
     * @return x coordinate of point coordinate
     */
    public double getXCoordinate() {
        return xCoordinate;
    }

    /**
     * This is setter method for xCoordinate field.
     *
     * @param xCoordinate is a double as x coordinate of point coordinate
     */
    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     * Getter method of yCoordinate field
     *
     * @return y coordinate of point coordinate
     */
    public double getYCoordinate() {
        return yCoordinate;
    }

    /**
     * This is setter method for angle field.
     *
     * @param yCoordinate is a double as x coordinate of point coordinate
     */
    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @Override
    public String toString() {
        return "Coordinate:" +
                "{x= " + xCoordinate +
                ", y= " + yCoordinate +"}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.xCoordinate, xCoordinate) == 0 &&
                Double.compare(that.yCoordinate, yCoordinate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate);
    }
}
