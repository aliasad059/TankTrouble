public class GameState {
    //TODO:should give the Map frame coordinate, height and width of the picture
    //TODO: have tank states and bullets states
    //TODO:have update method that calls other components update method
    //TODO: in constructor it will get the state of the components
    boolean gameOver = false;

    public GameState() {
    }

    public void update(){
        for (int i = 0; i < TankTroubleMap.getTanks().size(); i++) {
            TankTroubleMap.getTanks().get(i).getTankState().update();
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
