package logic;

public class Wall {
    // up point for vertical and left point for horizontal
    protected Coordinate startingPoint;
    protected  boolean destroyable;
    // horizontal or vertical
    protected String direction;

    public Wall(int x, int y,String direction) {
        startingPoint = new Coordinate();
        startingPoint.setXCoordinate(x);
        startingPoint.setYCoordinate(y);
        this.direction = direction;
    }

    public Coordinate getStartingPoint() {
        System.out.println(startingPoint.getXCoordinate()+"//"+startingPoint.getYCoordinate());
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
}
