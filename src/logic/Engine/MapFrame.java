package logic.Engine;

import logic.*;
import logic.Player.BotPlayer;
import logic.Player.UserPlayer;
import logic.Tank.Tank;
import logic.Wall.Wall;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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

    private TankTroubleMap tankTroubleMap;
    private BufferedImage backgroundImage, HDestructibleWall, HIndestructibleWall, VDestructibleWall,
            VIndestructibleWall, health, kills;
    private BufferStrategy bufferStrategy;
    private LocalDateTime startTime;

    /**
     * this constructor set all buffered image fields based on game kit and also pick random ground for map.
     * Also initialize some of fields as start time.
     *
     * @param title          is title of frame that show map of game
     * @param isNetwork      is boolean that show this game is network or vs bot
     * @param runGameHandler is a object of "RunGameHandler" class that handle game and its finish and also update
     *                       user after game
     */
    public MapFrame(String title, boolean isNetwork, RunGameHandler runGameHandler) {
        super(title);

        setLocationRelativeTo(null); // put frame at center of screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        initBufferStrategy();

        startTime = LocalDateTime.now();
        tankTroubleMap = new TankTroubleMap("./maps/map3.txt", isNetwork, startTime, runGameHandler);
        setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        setResizable(false);
        setExtendedState(Frame.MAXIMIZED_BOTH);
        File dir = new File("kit/ground");
        File[] backgrounds = dir.listFiles();
        Random rand = new Random();
        File backgroundFile = backgrounds[rand.nextInt(backgrounds.length)];
        try {
            backgroundImage = ImageIO.read(backgroundFile);
            HDestructibleWall = ImageIO.read(new File("kit/walls/HDestructibleWall.png"));
            VDestructibleWall = ImageIO.read(new File("kit/walls/VDestructibleWall.png"));
            VIndestructibleWall = ImageIO.read(new File("kit/walls/VIndestructibleWall.png"));
            HIndestructibleWall = ImageIO.read(new File("kit/walls/HIndestructibleWall.png"));
            health = ImageIO.read(new File("kit/tankStatus/health.png"));
            kills = ImageIO.read(new File("kit/tankStatus/kills.png"));
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

        // Draw topographies
        for (int i = 0; i < TankTroubleMap.getTopographies().size(); i++) {
            g2d.drawImage(TankTroubleMap.getTopographies().get(i).getImage(), (int) TankTroubleMap.getTopographies().get(i).getCenterPoint().getXCoordinate() - Constants.TOPOGRAPHY_SIZE / 2 + Constants.LEFT_MARGIN
                    , (int) TankTroubleMap.getTopographies().get(i).getCenterPoint().getYCoordinate() - Constants.TOPOGRAPHY_SIZE / 2 + Constants.TOP_MARGIN
                    , Constants.TOPOGRAPHY_SIZE, Constants.TOPOGRAPHY_SIZE, null);
        }

        // Draw prizes
        for (Prize prize : TankTroubleMap.getPrizes()) {
            Image prizeImage = prize.getPrizeImage();
            if (prizeImage == null) {
                prizeImage = loadImage(prize.getPrizeImagePath());
            }
            g2d.drawImage(prizeImage, (int) prize.getCenterCoordinate().getXCoordinate() - Constants.PRIZE_SIZE / 2 + Constants.LEFT_MARGIN
                    , (int) prize.getCenterCoordinate().getYCoordinate() - Constants.PRIZE_SIZE / 2 + Constants.TOP_MARGIN
                    , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
        }

        // Draw bullets
        for (Bullet bullets : tankTroubleMap.getBullets()) {
            g2d.drawImage(bullets.getBulletsImage()
                    , (int) bullets.getCoordinates().get(0).getXCoordinate() + Constants.LEFT_MARGIN
                    , (int) bullets.getCoordinates().get(0).getYCoordinate() + Constants.TOP_MARGIN
                    , Constants.BULLET_SIZE, Constants.BULLET_SIZE, null);
        }

        // Draw time
        LocalDateTime time = LocalDateTime.now();
        Duration duration = Duration.between(startTime, time);
        g2d.setColor(Color.blue);
        g2d.drawString(showTime((int) duration.getSeconds()), Constants.LEFT_MARGIN, Constants.TOP_MARGIN); //change location..........

        //draw tanksStatus
        ArrayList<Tank> tanks = new ArrayList<>();
        for (UserPlayer userPlayer : tankTroubleMap.getUsers()) {
            tanks.add(userPlayer.getUserTank());
        }
        for (BotPlayer bot : tankTroubleMap.getBots()) {
            tanks.add(bot.getAiTank());
        }

        int tankImageSize = ((BufferedImage) tanks.get(0).getTankImage()).getWidth();

        for (int i = 0; i < tanks.size(); i++) {

            int width = tankImageSize / 2;
            int height = Constants.GAME_HEIGHT - Constants.GAME_HEIGHT_REAL - Constants.STATUS_MARGIN;
            g2d.setColor(Color.BLACK);
            Image tankImage = tanks.get(i).getTankImage();
            if (tankImage == null) {
                tankImage = loadImage(tanks.get(i).getTankImagePath());
            }
            g2d.drawImage(tankImage,
                    2 * (i) * (tankImageSize) + Constants.STATUS_MARGIN,
                    Constants.GAME_HEIGHT_REAL - Constants.WALL_WIDTH_HORIZONTAL + Constants.STATUS_MARGIN, width, height, null);
            g2d.drawImage(kills,
                    2 * (i) * (tankImageSize) + 5 * Constants.STATUS_MARGIN,
                    Constants.GAME_HEIGHT_REAL - Constants.WALL_WIDTH_HORIZONTAL + Constants.STATUS_MARGIN,
                    Constants.STATUS_ICON_SIZE,
                    Constants.STATUS_ICON_SIZE, null);
            g2d.drawString(tanks.get(i).getNumberOfDestroyedTank() + ""
                    , 2 * (i) * (tankImageSize) + 6 * Constants.STATUS_MARGIN + Constants.STATUS_ICON_SIZE,
                    Constants.GAME_HEIGHT_REAL - Constants.WALL_WIDTH_HORIZONTAL + Constants.STATUS_MARGIN + Constants.STATUS_ICON_SIZE / 2);
            g2d.drawImage(health,
                    2 * (i) * (tankImageSize) + 5 * Constants.STATUS_MARGIN,
                    Constants.GAME_HEIGHT_REAL - Constants.WALL_WIDTH_HORIZONTAL + Constants.STATUS_ICON_SIZE + Constants.STATUS_MARGIN,
                    Constants.STATUS_ICON_SIZE,
                    Constants.STATUS_ICON_SIZE, null);
            g2d.drawString(tanks.get(i).getHealth() + ""
                    , 2 * (i) * (tankImageSize) + 6 * Constants.STATUS_MARGIN + Constants.STATUS_ICON_SIZE,
                    Constants.GAME_HEIGHT_REAL - Constants.WALL_WIDTH_HORIZONTAL + 3 * Constants.STATUS_ICON_SIZE / 2 + Constants.STATUS_MARGIN);
            Image tankPrizeImage = tanks.get(i).getPrizeImage();
            if (tankPrizeImage == null) {
//                System.out.println("\n\n" + tanks.get(i).getPrizeImagePath());
                tankPrizeImage = loadImage(tanks.get(i).getPrizeImagePath());
            }
            g2d.drawImage(tankPrizeImage,
                    2 * (i) * (tankImageSize) + 5 * Constants.STATUS_MARGIN,
                    Constants.GAME_HEIGHT_REAL - Constants.WALL_WIDTH_HORIZONTAL + 2 * Constants.STATUS_ICON_SIZE + Constants.STATUS_MARGIN
                    , Constants.STATUS_ICON_SIZE, Constants.STATUS_ICON_SIZE, null);
        }

        // draw tanks
        //System.out.println("users size in map frame class: "+ tankTroubleMap.getUsers().size());
        for (Tank tankToDraw : tanks) {
            Image tankImage = tankToDraw.getTankImage();
            if (tankImage == null) {
                tankImage = loadImage(tankToDraw.getTankImagePath());
            }
            g2d.rotate(-Math.toRadians(tankToDraw.getAngle())
                    , tankToDraw.getCenterPointOfTank().getXCoordinate() + Constants.LEFT_MARGIN
                    , tankToDraw.getCenterPointOfTank().getYCoordinate() + Constants.TOP_MARGIN);
            g2d.drawImage(tankImage
                    , (int) tankToDraw.getCenterPointOfTank().getXCoordinate() - Constants.TANK_SIZE / 2 + Constants.LEFT_MARGIN
                    , (int) tankToDraw.getCenterPointOfTank().getYCoordinate() - Constants.TANK_SIZE / 2 + Constants.TOP_MARGIN
                    , Constants.TANK_SIZE, Constants.TANK_SIZE, null);
            g2d.rotate(Math.toRadians(tankToDraw.getAngle())
                    , tankToDraw.getCenterPointOfTank().getXCoordinate() + Constants.LEFT_MARGIN
                    , tankToDraw.getCenterPointOfTank().getYCoordinate() + Constants.TOP_MARGIN);

        }

        // Draw GAME OVER
        if (tankTroubleMap.isGameOver()) {
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
                    (int) wallToDraw.getStartingPoint().getXCoordinate() + Constants.LEFT_MARGIN
                    , (int) wallToDraw.getStartingPoint().getYCoordinate() + Constants.TOP_MARGIN
                    , Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL
                    , null
            );
        } else if (wallToDraw.getDirection().equals("VERTICAL")) {
            g2d.drawImage(
                    vWall,
                    (int) wallToDraw.getStartingPoint().getXCoordinate() + Constants.LEFT_MARGIN
                    , (int) wallToDraw.getStartingPoint().getYCoordinate() + Constants.TOP_MARGIN
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

    /**
     * This method read image from file and return it as object from "Image" class.
     *
     * @param path is path of file
     * @return image that was in the file
     */
    private Image loadImage(String path) {
        Image imageToSend = null;
        try {
            imageToSend = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageToSend;
    }

    /**
     * Getter method of tankTroubleMap field.
     *
     * @return tankTroubleMap of game
     */
    public TankTroubleMap getTankTroubleMap() {
        return tankTroubleMap;
    }

    /**
     * This is setter method for tankTroubleMap field.
     *
     * @param tankTroubleMap is tankTrouble map as map of game
     */
    public void setTankTroubleMap(TankTroubleMap tankTroubleMap) {
        this.tankTroubleMap = tankTroubleMap;
    }
}
