package logic;

import java.util.ArrayList;

public class Wall {
    // up point for vertical and left point for horizontal
    protected Coordinate startingPoint;
    protected  boolean destroyable;
    // horizontal or vertical
    protected String direction;
    protected int height, width;
    protected ArrayList<Coordinate> pointsArray;

    public Wall(int x, int y,String direction) {
        if (direction.equals("HORIZONTAL")){
            height = Constants.WALL_HEIGHT_HORIZONTAL;
            width = Constants.WALL_WIDTH_HORIZONTAL;
        }else {
            height = Constants.WALL_HEIGHT_VERTICAL;
            width = Constants.WALL_WIDTH_VERTICAL;
        }
        startingPoint = new Coordinate();
        startingPoint.setXCoordinate(x);
        startingPoint.setYCoordinate(y);
        pointsArray = new ArrayList<>();
        pointsArray.add(startingPoint);
        pointsArray.add(new Coordinate(x+width,y));
        pointsArray.add(new Coordinate(x+width,y+height));
        pointsArray.add(new Coordinate(x,y+height));
        this.direction = direction;
    }

    public Coordinate getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(Coordinate location) {
        this.startingPoint = location;
    }

    public boolean isDestroyable() {
        return destroyable;
    }

    public void setDestroyable(boolean destroyable) {
        this.destroyable = destroyable;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public ArrayList<Coordinate> getPointsArray(){
        return pointsArray;
    }
}
