package logic;

/**
 *
 */
public class Player {
    private String name;
    private String color;

    /**
     *
     * @param name
     * @param color
     */
    public Player(String name, String color){
        this.name=name;
        this.color=color;
    }

    public String getName() {
        return name;
    }
}
