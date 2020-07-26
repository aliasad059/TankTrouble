package logic;

/**
 * this class represent state of game include tanks and bullets state.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class GameState {
    //TODO:should give the Map frame coordinate, height and width of the picture
    //TODO: have tank states and bullets states
    //TODO:have update method that calls other components update method
    //TODO: in constructor it will get the state of the components
    boolean gameOver = false;

    /**
     * This method call update method of all tanks, bullets
     */
    public void update() {
        //user tank
        for (int i = 0; i < TankTroubleMap.getUserTanks().size(); i++) {
            (TankTroubleMap.getUserTanks().get(i)).getTankState().update();
        }
        //AI tank
        for (int i = 0; i < TankTroubleMap.getAITanks().size(); i++) {
            TankTroubleMap.getAITanks().get(i).getTankState().update();
        }
        //bullets
        for (int i = 0; i < TankTroubleMap.getBullets().size(); i++) {
            TankTroubleMap.getBullets().get(i).update();
        }

    }

    /**
     * Getter method of gameOver field
     *
     * @return a boolean as value of gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * This is setter method for GameOver field.
     *
     * @param gameOver is a boolean as value of gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }


    public void putItemRandomOrder() { //idk???

    }
}
