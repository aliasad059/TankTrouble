package logic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Time implements Runnable {
    private LocalDateTime dataOfLastPrize;
    private int prizeTime; // time that prize have to set in the map. (is a random number between 0 and 20)
    protected Time(){
        dataOfLastPrize=LocalDateTime.now();
        Random random=new Random();
        prizeTime=random.nextInt(2);
    }
    @Override
    public void run() {
        LocalDateTime now=LocalDateTime.now();
        Duration duration=Duration.between(dataOfLastPrize,now);
        if(duration.getSeconds()>=4+prizeTime){
            Random random=new Random();
            prizeTime=random.nextInt(2);

            //System.out.println("prize set................"); //test
            dataOfLastPrize=LocalDateTime.now();
        }
    }
}