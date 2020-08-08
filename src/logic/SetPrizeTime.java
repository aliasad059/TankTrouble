package logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * This class handle set prize in map.
 * in a random time between 40-60 sec set a random prize in the random valid place of the map.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class SetPrizeTime implements Runnable {
    private TankTroubleMap tankTroubleMap;
    private LocalDateTime dataOfLastPrize;
    private int prizeTime;

    /**
     * This is constructor of this class and initialize some field and fill tankTroubleMap fields.
     *
     * @param tankTroubleMap is tankTrouble map that tank of bot is in
     */
    public SetPrizeTime(TankTroubleMap tankTroubleMap) {
        dataOfLastPrize = LocalDateTime.now();
        Random random = new Random();
        prizeTime = random.nextInt(20);
        this.tankTroubleMap = tankTroubleMap;
    }

    /**
     * This is override of run method and set prize in map based on starting time of game.
     */
    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(dataOfLastPrize, now);
        if (duration.getSeconds() >= 40 + prizeTime) {
            Random random = new Random();
            prizeTime = random.nextInt(20);
            tankTroubleMap.prizeSetter();
            dataOfLastPrize = LocalDateTime.now();
        }
    }
}
