package logic;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundsOfGame {
    private String pathOfSound;
    private Clip clip;
    /*
    help:
     normal: fire normal bullet
     laser: fire laser bullet
     movement: movement of tank
     explosion: explosion of tank
     gameStart: game start
     gameOver: game over
     notReady: not ready to lunch
     background: background music of game (in menu) !!! not implement
     */
    private boolean hasRepeat;

    public SoundsOfGame(String type, boolean hasRepeat) {
        pathOfSound = "kit\\voice\\" + type + ".wav";
        this.hasRepeat = hasRepeat;
    }

    public void playSound() {
        new Thread(() -> {
            try {
                clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(pathOfSound).getAbsoluteFile());
                clip.open(inputStream);
                if (hasRepeat) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
                clip.start();
                while (true) {
                    Thread.sleep(clip.getMicrosecondLength());
                    break;
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    public void pause() {
        clip.stop();
    }
}
