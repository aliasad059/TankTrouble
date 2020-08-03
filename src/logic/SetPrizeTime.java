package logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class SetPrizeTime implements Runnable {
    private TankTroubleMap tankTroubleMap;
    private LocalDateTime dataOfLastPrize;
    private int prizeTime; // time that prize have to set in the map. (is a random number between 0 and 20)

    public SetPrizeTime(TankTroubleMap tankTroubleMap) {
        dataOfLastPrize = LocalDateTime.now();
        Random random = new Random();
        prizeTime = random.nextInt(20);
        this.tankTroubleMap = tankTroubleMap;
    }

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
