package logic;

import java.util.ArrayList;

public class Prize {
    private int type;
    private Coordinate centerCoordinate;
    private ArrayList<Coordinate>coordinates;
    public Prize(int type, Coordinate coordinate){
        this.type=type;
        this.centerCoordinate=new Coordinate(coordinate.getXCoordinate(),coordinate.getYCoordinate());
        coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));

        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() + (double) Constants.TANK_SIZE / 2
                , centerCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));

        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerCoordinate.getYCoordinate() + (double) Constants.TANK_SIZE / 2));

        coordinates.add(new Coordinate(centerCoordinate.getXCoordinate() - (double) Constants.TANK_SIZE / 2
                , centerCoordinate.getYCoordinate() - (double) Constants.TANK_SIZE / 2));
    }
    public ArrayList<Coordinate>getPointsCoordinate(){
        return null;
    }
    public int getType() {
        return type;
    }

    public Coordinate getCenterCoordinate() {
        return centerCoordinate;
    }
}
