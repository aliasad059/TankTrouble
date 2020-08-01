package logic.Engine;


import Network.NetworkData;
import logic.Player.UserPlayer;
import logic.TankTroubleMap;

import java.time.Duration;
import java.time.LocalDateTime;

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
    private boolean gameOver = false;
    private TankTroubleMap tankTroubleMap;
    private int winnerGroup;

    public GameState(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
        winnerGroup = -1;
    }

    /**
     * This method call update method of all tanks, bullets
     */

    public void update() {
        //user tank
        for (int i = 0; i < tankTroubleMap.getUsers().size(); i++) {
            tankTroubleMap.getUsers().get(i).getUserTank().getTankState().update();
        }
        //AI tank
        for (int i = 0; i < tankTroubleMap.getBots().size(); i++) {
            tankTroubleMap.getBots().get(i).getAiTank().getTankState().update();
        }
        //bullets
        for (int i = 0; i < tankTroubleMap.getBullets().size(); i++) {
            tankTroubleMap.getBullets().get(i).update();
        }

    }

    public void update(NetworkData data) {
        tankTroubleMap.getUsers().get(data.getSenderID()).updateFromServer(data);
        //bullets
        for (int i = 0; i < tankTroubleMap.getBullets().size(); i++) {
            tankTroubleMap.getBullets().get(i).update();
        }
    }

    public void gameOverCheck() {
        //users
        if (tankTroubleMap.getUsers().size() != 0) {
            winnerGroup = tankTroubleMap.getUsers().get(0).getGroupID();
            for (int i = 1; i < tankTroubleMap.getUsers().size(); i++) {
                if (tankTroubleMap.getUsers().get(i).getGroupID() != winnerGroup)
                    return;
            }
        }
        //bots
        if (tankTroubleMap.getBots().size() != 0) {
            if (winnerGroup != -1) {
                winnerGroup = tankTroubleMap.getBots().get(0).getGroupID();
            }
            for (int i = 1; i < tankTroubleMap.getBots().size(); i++) {
                if (tankTroubleMap.getBots().get(i).getGroupID() != winnerGroup)
                    return;
            }
        }
        gameOver = true;
        actionAfterGameOver();
    }

    private void actionAfterGameOver() {
        for (UserPlayer userPlayer : tankTroubleMap.getUsers()) {
            // set time play
            LocalDateTime time = LocalDateTime.now();
            Duration duration = Duration.between(tankTroubleMap.getStartTime(), time);
            userPlayer.setTimePlay(userPlayer.getTimePlay() + duration.getSeconds());
            //set XP or level and number of win/lose for players
            //win
            if (userPlayer.getGroupID() == winnerGroup) {
                userPlayer.setXP(userPlayer.getXP() + 0.5); // winner prize
                double newXP = (double) userPlayer.getUserTank().getNumberOfDestroyedTank() / 2; // 2.............
                userPlayer.setXP(userPlayer.getXP() + newXP);
                userPlayer.XPToLevel();
                if (tankTroubleMap.isNetwork()) {
                    userPlayer.setWinInNetworkMatch(userPlayer.getWinInNetworkMatch() + 1);
                } else {
                    userPlayer.setWinInBotMatch(userPlayer.getWinInBotMatch() + 1);
                }
            }
            //lose
            else {
                double newXP = (double) userPlayer.getUserTank().getNumberOfDestroyedTank() / 5; // 5...........
                userPlayer.setXP(userPlayer.getXP() + newXP);
                userPlayer.XPToLevel();
                if (tankTroubleMap.isNetwork()) {
                    userPlayer.setLoseInNetworkMatch(userPlayer.getLoseInNetworkMatch() + 1);
                } else {
                    userPlayer.setLoseInBotMatch(userPlayer.getLoseInBotMatch() + 1);
                }
            }
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


}
