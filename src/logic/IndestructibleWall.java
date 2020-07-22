package logic;

public class IndestructibleWall extends Wall{
    public IndestructibleWall(int x, int y, String direction) {
        super(x, y, direction);
        this.setDestroyable(false);
    }
}
