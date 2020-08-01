package logic.Player;

import Network.NetworkData;
import logic.Tank.AITank;
import logic.TankTroubleMap;

public class BotPlayer extends Player {
    private AITank aiTank;

    /**
     * @param name           is name of player
     * @param color          is color of player's tank
     * @param tankTroubleMap
     */
    public BotPlayer(String name, String password, String color, int userID, int groupID
            , TankTroubleMap tankTroubleMap, int tankHealth, int bulletDamage, int wallHealth) {
        super(name, password, color, userID, groupID, tankTroubleMap, tankHealth, bulletDamage, wallHealth);
        aiTank = new AITank("kit\\tanks\\" + color, tankTroubleMap);
        setLevel(1);
        setGroupID(groupID);
    }

    public BotPlayer(String name, String color, TankTroubleMap tankTroubleMap, int groupID) {
        super(name, "password", color, tankTroubleMap);
        aiTank = new AITank("kit\\tanks\\" + color, tankTroubleMap);
        setLevel(1);
        setGroupID(groupID);
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
