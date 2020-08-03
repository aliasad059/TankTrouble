package logic.Player;

import Network.NetworkData;
import logic.Tank.AITank;
import logic.TankTroubleMap;

public class BotPlayer extends Player {
    private AITank aiTank;

    public BotPlayer(String name, String color, TankTroubleMap tankTroubleMap) {
        super(name, color, tankTroubleMap);
        aiTank = new AITank("kit\\tanks\\" + color, tankTroubleMap);
        setLevel(1);
    }

    /**
     * @param name           is name of player
     * @param color          is color of player's tank
     * @param tankTroubleMap
     */
    public BotPlayer(String name, String color, TankTroubleMap tankTroubleMap, int groupNumber) {
        super(name, color, tankTroubleMap);
        aiTank = new AITank("kit\\tanks\\" + color, tankTroubleMap);
        setLevel(1);
        setGroupNumber(groupNumber);
    }

    public AITank getAiTank() {
        return aiTank;
    }

    @Override
    public void updateFromServer(NetworkData data) {

    }

    @Override
    public NetworkData getPlayerState() {
        return null;
    }
}
