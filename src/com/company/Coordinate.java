package com.company;

/**
 * This class represent a coordinate in 2d palate contain x and y coordinate.
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Coordinate {
    // coordinate based on x axis
    private int xCoordinate;
    // coordinate based on y axis
    private int yCoordinate;

    /**
     * This is constructor of this class and initialize our field to -1.
     */
    public Coordinate(){
        this.xCoordinate=-1;
        this.yCoordinate=-1;
    }

    /**
     *
     * @return
     */
    public int getXCoordinate() {
        return xCoordinate;
    }

    /**
     *
     * @param xCoordinate
     */
    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     *
     * @return
     */
    public int getYCoordinate() {
        return yCoordinate;
    }

    /**
     *
     * @param yCoordinate
     */
    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
