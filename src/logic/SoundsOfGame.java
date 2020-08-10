package logic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * This class represent a music player for sounds that there are in the game such as fire,.....
 * <p>
 * normal: fire normal bullet
 * laser: fire laser bullet
 * movement: movement of tank
 * explosion: explosion of tank
 * gameStart: game start
 * gameOver: game over
 * notReady: not ready to lunch
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class SoundsOfGame {
    private String pathOfSound;
    private Clip clip;
    private boolean hasRepeat;

    /**
     * This constructor set path of sound based on type sound.
     *
     * @param type      is type of music or sound
     * @param hasRepeat show this music has repeat or not
     */
    public SoundsOfGame(String type, boolean hasRepeat) {
        pathOfSound = "kit/voice/" + type + ".wav";
        this.hasRepeat = hasRepeat;
    }

    /**
     * This method read sound or music and play it with thread.
     */
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

    /**
     * This method pause music.
     */
    public void pause() {
        clip.stop();
    }
}
