package logic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    private BufferedImage backgroundImage, HDestructibleWall, HIndestructibleWall, VDestructibleWall, VIndestructibleWall, shield,health,damage2x,damage3x,laser;
    private BufferStrategy bufferStrategy;
    private LocalDateTime startTime;

    public MapFrame(String title) {
        super(title);
        map = new TankTroubleMap("./maps/map1.txt");
        setResizable(false);
        setSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        File dir = new File(".\\kit++\\kit\\ground");
        File[] backgrounds = dir.listFiles();
        Random rand = new Random();
        File backgroundFile = backgrounds[rand.nextInt(backgrounds.length)];
        try {
            backgroundImage = ImageIO.read(backgroundFile);
            HDestructibleWall = ImageIO.read(new File(".\\kit++\\kit\\walls\\HDestructibleWall.png"));
            VDestructibleWall = ImageIO.read(new File(".\\kit++\\kit\\walls\\VDestructibleWall.png"));
            VIndestructibleWall = ImageIO.read(new File(".\\kit++\\kit\\walls\\VIndestructibleWall.png"));
            HIndestructibleWall = ImageIO.read(new File(".\\kit++\\kit\\walls\\HIndestructibleWall.png"));
            health = ImageIO.read(new File(".\\kit++\\kit\\prizes\\health.png"));
            shield = ImageIO.read(new File(".\\kit++\\kit\\prizes\\shield.png"));
            damage2x = ImageIO.read(new File(".\\kit++\\kit\\prizes\\damage2x.png"));
            damage3x = ImageIO.read(new File(".\\kit++\\kit\\prizes\\damage3x.png"));
            laser = ImageIO.read(new File(".\\kit++\\kit\\prizes\\laser.png"));
            startTime=LocalDateTime.now();
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
        // TODO: draw all components of the frame
        for (int i = 0; i < TankTroubleMap.getWalls().size(); i++) {
            Wall wallToDraw = TankTroubleMap.getWalls().get(i);
            g2d.setColor(Color.BLUE);
            if (wallToDraw.getDirection().equals("HORIZONTAL")) {
                if (wallToDraw.isDestroyable()) {
                    g2d.drawImage(
                            HDestructibleWall,
                            wallToDraw.getStartingPoint().getXCoordinate() * Constants.WALL_WIDTH_HORIZONTAL + 50
                            , wallToDraw.getStartingPoint().getYCoordinate() * Constants.WALL_HEIGHT_VERTICAL + 50
                            , Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL
                            , null
                    );
                } else {
                    g2d.drawImage(
                            HIndestructibleWall,
                            wallToDraw.getStartingPoint().getXCoordinate() * Constants.WALL_WIDTH_HORIZONTAL + 50
                            , wallToDraw.getStartingPoint().getYCoordinate() * Constants.WALL_HEIGHT_VERTICAL + 50
                            , Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL
                            , null
                    );
                }
            } else if (wallToDraw.getDirection().equals("VERTICAL")) {
                if (wallToDraw.isDestroyable()) {
                    g2d.drawImage(
                            VDestructibleWall,
                            wallToDraw.getStartingPoint().getXCoordinate() * Constants.WALL_WIDTH_HORIZONTAL + 50
                            , wallToDraw.getStartingPoint().getYCoordinate() * Constants.WALL_HEIGHT_VERTICAL + 50
                            , Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL
                            , null
                    );
                } else {
                    g2d.drawImage(
                            VIndestructibleWall,
                            wallToDraw.getStartingPoint().getXCoordinate() * Constants.WALL_WIDTH_HORIZONTAL + 50
                            , wallToDraw.getStartingPoint().getYCoordinate() * Constants.WALL_HEIGHT_VERTICAL + 50
                            , Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL
                            , null
                    );
                }

            }
        }

        // draw tanks
        for (int i = 0; i < TankTroubleMap.getTanks().size(); i++) {
            Tank tankToDraw = TankTroubleMap.getTanks().get(i);
            g2d.drawImage(tankToDraw.getTankImage()
                    , tankToDraw.getPixelCoordinate().getXCoordinate()
                    , tankToDraw.getPixelCoordinate().getYCoordinate()
                    , Constants.TANK_SIZE, Constants.TANK_SIZE, null);
            System.out.println(tankToDraw.getPixelCoordinate().getXCoordinate()+" "+tankToDraw.getPixelCoordinate().getYCoordinate());
        }

        // Draw prizes
        for(Prize prize:map.getPrizes()){
            if(prize.getType()==1){
                g2d.drawImage(shield,prize.getCoordinate().getXCoordinate() * Constants.PRIZE_SIZE + 50
                        , prize.getCoordinate().getYCoordinate() * Constants.PRIZE_SIZE + 50
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            }
            else if(prize.getType()==2){
                g2d.drawImage(laser,prize.getCoordinate().getXCoordinate() * Constants.PRIZE_SIZE + 50
                        , prize.getCoordinate().getYCoordinate() * Constants.PRIZE_SIZE + 50
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            }
            else if(prize.getType()==3){
                g2d.drawImage(health,prize.getCoordinate().getXCoordinate() * Constants.PRIZE_SIZE + 50
                        , prize.getCoordinate().getYCoordinate() * Constants.PRIZE_SIZE + 50
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            }
            else if(prize.getType()==4){
                g2d.drawImage(damage2x,prize.getCoordinate().getXCoordinate() * Constants.PRIZE_SIZE + 50
                        , prize.getCoordinate().getYCoordinate() * Constants.PRIZE_SIZE + 50
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            }
            else if(prize.getType()==5){
                g2d.drawImage(damage3x,prize.getCoordinate().getXCoordinate() * Constants.PRIZE_SIZE + 50
                        , prize.getCoordinate().getYCoordinate() * Constants.PRIZE_SIZE + 50
                        , Constants.PRIZE_SIZE, Constants.PRIZE_SIZE, null);
            }
        }

        // Draw time
        LocalDateTime time=LocalDateTime.now();
        Duration duration = Duration.between(startTime,time);
        g2d.drawString(showTime((int)duration.getSeconds()),100, 100); //change location..........

        // Draw GAME OVER
        if (state.gameOver) {
            String str = "GAME OVER";
            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            g2d.drawString(str, (Constants.GAME_WIDTH - strWidth) / 2, Constants.GAME_HEIGHT / 2);
        }
    }

    protected String showTime(int s){
        int min=(int)Math.floor(s/60);
        int hour=(int)Math.floor(min/60);
        return "Time: "+clockForm(hour)+":"+clockForm(min%60)+":"+clockForm(s%60);
    }

    private String clockForm(int number){
        if(number<10){
            return "0"+number;
        }else {
            return ""+number;
        }
    }

}
