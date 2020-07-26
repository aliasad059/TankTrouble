package logic;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

/**
 * The window on which the rendering is performed.
 * This example uses the modern BufferStrategy approach for double-buffering,
 * actually it performs triple-buffering!
 * For more information on BufferStrategy check out:
 * http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 * http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class MapFrame extends JFrame {

    private static TankTroubleMap map;
    private BufferedImage backgroundImage, HDestructibleWall, HIndestructibleWall, VDestructibleWall,
            VIndestructibleWall, shield, health, damage2x, damage3x, laser;
    private BufferStrategy bufferStrategy;
    private LocalDateTime startTime;

    /**
     * this constructor set all buffered image fields based on game kit and also pick random ground for map.
     *
     * @param title ????????
     */
    public MapFrame(String title) {
        super(title);
        map = new TankTroubleMap("./maps/map3.txt");
        setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        File dir = new File("kit\\ground");
        File[] backgrounds = dir.listFiles();
        Random rand = new Random();
        File backgroundFile = backgrounds[rand.nextInt(backgrounds.length)];
        try {
            backgroundImage = ImageIO.read(backgroundFile);
            HDestructibleWall = ImageIO.read(new File("kit\\walls\\HDestructibleWall.png"));
            VDestructibleWall = ImageIO.read(new File("kit\\walls\\VDestructibleWall.png"));
            VIndestructibleWall = ImageIO.read(new File("kit\\walls\\VIndestructibleWall.png"));
            HIndestructibleWall = ImageIO.read(new File("kit\\walls\\HIndestructibleWall.png"));
            health = ImageIO.read(new File("kit\\prizes\\health.png"));
            shield = ImageIO.read(new File("kit\\prizes\\shield.png"));
            damage2x = ImageIO.read(new File("kit\\prizes\\damage2x.png"));
            damage3x = ImageIO.read(new File("kit\\prizes\\damage3x.png"));
            laser = ImageIO.read(new File("kit\\prizes\\laser.png"));
            startTime = LocalDateTime.now();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * This must be called once after the JFrame is shown:
     * frame.setVisible(true);
     * and before any rendering is started.
     */
    public void initBufferStrategy() {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
    }


    /**
     * Game rendering with triple-buffering using BufferStrategy.
     */
    public void render(GameState state) {
        // Render single frame
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                try {
                    doRendering(graphics, state);
                } finally {
                    // Dispose the graphics
                    graphics.dispose();
                }
                // Repeat the rendering if the drawing buffer contents were restored
            } while (bufferStrategy.contentsRestored());
            // Display the buffer
            bufferStrategy.show();
            // Tell the system to do the drawing NOW;
            // otherwise it can take a few extra ms and will feel jerky!
            Toolkit.getDefaultToolkit().sync();

            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }

    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering(Graphics2D g2d, GameState state) {
        // Draw background
        for (int i = 0; i < Constants.GAME_WIDTH / backgroundImage.getWidth() + 1; i++) {
            for (int j = 0; j < Constants.GAME_HEIGHT / backgroundImage.getHeight() + 1; j++) {
                g2d.drawImage(backgroundImage, i * backgroundImage.getWidth(), j * backgroundImage.getHeight(), null);
            }
        }
        // Draw destructible wall
        for (int i = 0; i < TankTroubleMap.getDestructibleWalls().size(); i++) {
            Wall wallToDraw = TankTroubleMap.getDestructibleWalls().get(i);
            drawAWall(g2d, wallToDraw, HDestructibleWall, VDestructibleWall);
        }
        // Draw indestructible wall
        for (int i = 0; i < TankTroubleMap.getIndestructibleWalls().size(); i++) {
            Wall wallToDraw = TankTroubleMap.getIndestructibleWalls().get(i);
            drawAWall(g2d, wallToDraw, HIndestructibleWall, VIndestructibleWall);
        }

        // Draw prizes
        for (Prize prize : map.getPrizes()) {
            if (prize.getType() == 1) {
                g2d.drawImage(shield, (int) prize.getCenterCoordinate().getXCoordinate() - Constants.PRIZE_SIZE / 2 /*+ Constants.LEFT_MARGIN*/
                        , (int) prize.getCenterCoordinate().getYCoordinate() - Constants.PRIZE_SIZE / 2 /*+ Constants.TOP_MARGIN*/
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            } else if (prize.getType() == 2) {
                g2d.drawImage(laser, (int) prize.getCenterCoordinate().getXCoordinate() - Constants.PRIZE_SIZE / 2 /*+ Constants.LEFT_MARGIN*/
                        , (int) prize.getCenterCoordinate().getYCoordinate() - Constants.PRIZE_SIZE / 2 /*+ Constants.TOP_MARGIN*/
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            } else if (prize.getType() == 3) {
                g2d.drawImage(health, (int) prize.getCenterCoordinate().getXCoordinate() - Constants.PRIZE_SIZE / 2 /*+ Constants.LEFT_MARGIN*/
                        , (int) prize.getCenterCoordinate().getYCoordinate() - Constants.PRIZE_SIZE / 2 /* + Constants.TOP_MARGIN*/
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            } else if (prize.getType() == 4) {
                g2d.drawImage(damage2x, (int) prize.getCenterCoordinate().getXCoordinate() - Constants.PRIZE_SIZE / 2  /*+ Constants.LEFT_MARGIN*/
                        , (int) prize.getCenterCoordinate().getYCoordinate() - Constants.PRIZE_SIZE / 2  /*+ Constants.TOP_MARGIN*/
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            } else if (prize.getType() == 5) {
                g2d.drawImage(damage3x, (int) prize.getCenterCoordinate().getXCoordinate() - Constants.PRIZE_SIZE / 2 /* + Constants.LEFT_MARGIN*/
                        , (int) prize.getCenterCoordinate().getYCoordinate() - Constants.PRIZE_SIZE / 2/* + Constants.TOP_MARGIN*/
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            }
        }

        // Draw bullets
        for (Bullets bullets : TankTroubleMap.getBullets()) {
            g2d.drawImage(bullets.getBulletsImage()
                    , (int) bullets.getCoordinates().get(0).getXCoordinate()
                    , (int) bullets.getCoordinates().get(0).getYCoordinate()
                    , Constants.BULLET_SIZE, Constants.BULLET_SIZE, null);
        }


        // Draw time
        LocalDateTime time = LocalDateTime.now();
        Duration duration = Duration.between(startTime, time);
        g2d.drawString(showTime((int) duration.getSeconds()), 100, 100); //change location..........

        // draw tanks
        ArrayList<Tank> tanks = new ArrayList<>();
        tanks.addAll(TankTroubleMap.getAITanks());
        tanks.addAll(TankTroubleMap.getUserTanks());
        for (Tank tankToDraw : tanks) {

//            AffineTransform trans = new AffineTransform();
//            trans.rotate(Math.toRadians(-tankToDraw.getAngle()),tankToDraw.getCenterPointOfTank().getXCoordinate(),tankToDraw.getCenterPointOfTank().getYCoordinate());
//            trans.translate(tankToDraw.getCenterPointOfTank().getYCoordinate(), tankToDraw.getCenterPointOfTank().getYCoordinate());
//            g2d.setTransform(trans);
//            g2d.drawImage(tankToDraw.getTankImage(), (int)tankToDraw.getCenterPointOfTank().getXCoordinate(), (int)tankToDraw.getCenterPointOfTank().getYCoordinate(), (int)Constants.TANK_SIZE, Constants.TANK_SIZE/2, null);
//            trans.setToIdentity();
            g2d.rotate(-Math.toRadians(tankToDraw.getAngle())
                    , tankToDraw.getCenterPointOfTank().getXCoordinate()
                    , tankToDraw.getCenterPointOfTank().getYCoordinate());
            g2d.drawImage(tankToDraw.getTankImage()
                    , (int) tankToDraw.getCenterPointOfTank().getXCoordinate() - Constants.TANK_SIZE / 2
                    , (int) tankToDraw.getCenterPointOfTank().getYCoordinate() - Constants.TANK_SIZE / 2
                    , Constants.TANK_SIZE, Constants.TANK_SIZE, null);
//            trans.setToIdentity();

        }
        // Draw GAME OVER
        if (state.gameOver) {
            String str = "GAME OVER";
            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            g2d.drawString(str, (Constants.GAME_WIDTH - strWidth) / 2, Constants.GAME_HEIGHT / 2);
        }

    }

    /**
     * This method draw a wall based on following input parameters.
     *
     * @param g2d        is graphic2d that paint walls
     * @param wallToDraw is wall that we wanna draw
     * @param hWall      is image of horizontal wall
     * @param vWall      is image of vertical wall
     */
    private void drawAWall(Graphics2D g2d, @NotNull Wall wallToDraw, BufferedImage hWall, BufferedImage vWall) {
        if (wallToDraw.getDirection().equals("HORIZONTAL")) {
            g2d.drawImage(
                    hWall,
                    (int) wallToDraw.getStartingPoint().getXCoordinate()
                    , (int) wallToDraw.getStartingPoint().getYCoordinate()
                    , Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL
                    , null
            );
        } else if (wallToDraw.getDirection().equals("VERTICAL")) {
            g2d.drawImage(
                    vWall,
                    (int) wallToDraw.getStartingPoint().getXCoordinate()
                    , (int) wallToDraw.getStartingPoint().getYCoordinate()
                    , Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL
                    , null
            );

        }
    }

    /**
     * This class return a string as duration of game (clock form).
     *
     * @param duration is a integer as duration of game based on m seconds
     * @return a string that show time clock form
     */
    protected String showTime(int duration) {
        int min = (int) Math.floor(duration / 60);
        int hour = (int) Math.floor(min / 60);
        return "Time: " + clockForm(hour) + ":" + clockForm(min % 60) + ":" + clockForm(duration % 60);
    }

    /**
     * This method get a number and return it clock form (1-> 01 and 11->11).
     *
     * @param number is number that we wanna show it clock form
     * @return a string as clock form of the number
     */
    @NotNull
    @Contract(pure = true)
    private String clockForm(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

}
