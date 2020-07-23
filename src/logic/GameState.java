package logic;

public class GameState {
    //TODO:should give the Map frame coordinate, height and width of the picture
    //TODO: have tank states and bullets states
    //TODO:have update method that calls other components update method
    //TODO: in constructor it will get the state of the components
    boolean gameOver = false;

    public GameState() {
    }

    public void update(){
        for (int i = 0; i < TankTroubleMap.getUserTanks().size(); i++) {
            ( TankTroubleMap.getUserTanks().get(i)).getTankState().update();
        }
        for (int i = 0; i < TankTroubleMap.getAITanks().size(); i++) {
            TankTroubleMap.getAITanks().get(i).getTankState().update();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public void putItemRandomOrder(){

    }
}
