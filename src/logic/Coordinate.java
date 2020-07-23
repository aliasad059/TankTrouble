package logic;

/**
 * This class represent a coordinate in 2d palate contain x and y coordinate.
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Coordinate {
    // coordinate based on x axis
    private double xCoordinate;
    // coordinate based on y axis
    private double yCoordinate;

    /**
     * This is constructor of this class and initialize our field to -1.
     */
    public Coordinate(){
        this.xCoordinate=-1;
        this.yCoordinate=-1;
    }
    public Coordinate(double xCoordinate, double yCoordinate){
        this.xCoordinate=xCoordinate;
        this.yCoordinate=yCoordinate;
    }


    /**
     *
     * @return
     */
    public double getXCoordinate() {
        return xCoordinate;
    }

    /**
     *
     * @param xCoordinate
     */
    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    /**
     *
     * @return
     */
    public double getYCoordinate() {
        return yCoordinate;
    }

    /**
     *
     * @param yCoordinate
     */
    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }
}
