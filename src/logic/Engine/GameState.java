package logic.Engine;


import Network.NetworkData;
import logic.Player.BotPlayer;
import logic.Player.Player;
import logic.Player.UserPlayer;
import logic.TankTroubleMap;
import org.jetbrains.annotations.NotNull;

/**
 * This class represent state of game include tanks and bullets state.
 * this class has a method that update all these state.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class GameState {

    private TankTroubleMap tankTroubleMap;

    /**
     * This constructor just set input TankTroubleMap object for field.
     *
     * @param tankTroubleMap is tankTroubleMap that we wanna set on our field
     */
    public GameState(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }

    /**
     * This method call update method of all tanks amd bullets.
     */
    public void update() {
        //user tank
        //System.out.println("users size in game state class: "+ tankTroubleMap.getUsers().size());
        for (int i = 0; i < tankTroubleMap.getUsers().size(); i++) {
            tankTroubleMap.getUsers().get(i).getUserTank().getTankState().updateKeys();
            tankTroubleMap.getUsers().get(i).getUserTank().getTankState().update();
            if (tankTroubleMap.isNetwork()) {
                break;
            }
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

    /**
     * This method update state of tank and it's player in network game.
     *
     * @param data is a object that with it's help we update state of player in network game
     */
    public void update(NetworkData data) {
        UserPlayer player = data.getSenderPlayer();
        if (!tankTroubleMap.getUsers().contains(player)) {
            tankTroubleMap.getUsers().add(player);
            (player).getUserTank().setTankTroubleMap(tankTroubleMap);
            (player).setTankTroubleMap(tankTroubleMap);
            System.out.println("added:" + tankTroubleMap.getUsers().size());
        } else {

            (player).updateFromServer(data);
            System.out.println("updated");
        }
        //bullets
        for (int i = 0; i < tankTroubleMap.getBullets().size(); i++) {
            tankTroubleMap.getBullets().get(i).update();
        }
    }

}
