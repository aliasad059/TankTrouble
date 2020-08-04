package graphic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This class extend "JFrame" class.
 * Constructor of this class get a string as path of backGround.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class FrameWithBackGround extends JFrame {
    Image backGround;

    public FrameWithBackGround(String backGroundPath) {
        try {
            backGround = ImageIO.read(new File(backGroundPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setContentPane(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backGround, 0, 0, null);
            }
        });
    }
}
