package logic.Engine;


import Network.NetworkData;
import logic.Player.BotPlayer;
import logic.Player.Player;
import logic.Player.UserPlayer;
import logic.TankTroubleMap;

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
    private TankTroubleMap tankTroubleMap;


    public GameState(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }

    /**
     * This method call update method of all tanks, bullets
     */

    public void update() {
        //user tank
        //System.out.println("users size in game state class: "+ tankTroubleMap.getUsers().size());
        for (int i = 0; i < tankTroubleMap.getUsers().size(); i++) {
            tankTroubleMap.getUsers().get(i).getUserTank().getTankState().updateKeys();
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
        Player player = data.getSenderPlayer();
        if (data.isUser()) {
            if (!tankTroubleMap.getUsers().contains((UserPlayer) player)) {
                tankTroubleMap.getUsers().add((UserPlayer) player);
                ((UserPlayer) player).getUserTank().setTankTroubleMap(tankTroubleMap);
                ((UserPlayer) player).setTankTroubleMap(tankTroubleMap);
//                System.out.println("added");
//                System.out.println(tankTroubleMap.getUsers().size());
            } else {
//                if (tankTroubleMap == null){
//                    System.out.println("NULL");
//                }

                ((UserPlayer) player).updateFromServer(data);
            }
        } else {
            if (!tankTroubleMap.getBots().contains((BotPlayer) player)) {
                tankTroubleMap.getBots().add((BotPlayer) player);
            } else {
                ((BotPlayer) player).updateFromServer(data);
            }
        }
        //bullets
        for (int i = 0; i < tankTroubleMap.getBullets().size(); i++) {
            tankTroubleMap.getBullets().get(i).update();
        }
    }

}
