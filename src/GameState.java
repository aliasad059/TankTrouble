public class GameState {
    //TODO:should give the Map frame coordinate, height and width of the picture
    //TODO: have tank states and bullets states
    //TODO:have update method that calls other components update method
    //TODO: in constructor it will get the state of the components
    boolean gameOver = false;
    public void update(){

    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
