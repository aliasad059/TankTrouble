package logic.Player;

import Network.NetworkData;
import logic.Tank.AITank;
import logic.TankTroubleMap;

/**
 * This class represent a bot for game with extend "Player" class.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class BotPlayer extends Player {

    private AITank aiTank;

    /**
     * This is first constructor of this class and initialize some of our fields.
     *
     * @param name           is name of bot
     * @param color          is color of bot tank
     * @param tankTroubleMap is tankTrouble map that tank of bot is in
     */
    public BotPlayer(String name, String color, TankTroubleMap tankTroubleMap) {
        super(name, color, tankTroubleMap);
        aiTank = new AITank("kit/tanks/" + color, tankTroubleMap);
        setLevel(1);
    }

    /**
     * Second constructor in addition of  first constructor job, set group number for bot.
     *
     * @param name           is name of bot
     * @param color          is color of bot tank
     * @param tankTroubleMap is tankTrouble map that tank of bot is in
     */
    public BotPlayer(String name, String color, TankTroubleMap tankTroubleMap, int groupNumber) {
        this(name, color, tankTroubleMap);
        setGroupNumber(groupNumber);
    }

    /**
     * Getter method of aiTank field.
     *
     * @return an object from AITank as tank of bot
     */
    public AITank getAiTank() {
        return aiTank;
    }

    /**
     * This method is for update network game and update tank state.
     *
     * @param data is an object from NetworkData that update bot tate
     */
    @Override
    public void updateFromServer(NetworkData data) {

    }

    /**
     * This method get player state for sharing to another player in network game.
     */
    @Override
    public NetworkData getPlayerState() {
        return null;
    }
}
