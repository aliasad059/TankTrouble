package logic;

public class DestructibleWall extends Wall{
    private int health;
    public DestructibleWall(int x, int y, String direction) {
        super(x, y, direction);
        this.setDestroyable(true);
        this.health = Constants.WALL_HEALTH;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
