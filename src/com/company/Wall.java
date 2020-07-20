package com.company;

public class Wall {
    // up point for vertical and left point for horizontal
    private Coordinate startingPoint;
    private boolean destroyable;
    // horizontal or vertical
    private String direction;

    public Wall(int x, int y,boolean destroyable, String direction) {
        this.startingPoint = new Coordinate();
        this.startingPoint.setXCoordinate(x);
        this.startingPoint.setYCoordinate(y);
        this.destroyable = destroyable;
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
}
