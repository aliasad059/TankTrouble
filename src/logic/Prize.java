package logic;

public class Prize {
    private int type;
    private Coordinate coordinate;

    public Prize(int type, Coordinate coordinate){
        this.type=type;
        this.coordinate=new Coordinate();
        this.coordinate=coordinate;
    }

    public int getType() {
        return type;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
