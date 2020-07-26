package logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class represent bullets for tanks include player's and bot's tank.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Bullets {
    // type of Bullets
    private String type;
    // damage of bullets
    private int damage;
    // speed of bullets
    private int speed;
    private ArrayList<Coordinate> coordinates;
    //time that bullets fired from tank
    private LocalDateTime fireTime;
    // the time that this kind of bullets stay in map
    private float lifeTime;
    private double angle;
    private Image bulletsImage;
    private boolean bulletsBlasted;

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     *
     * @param BulletsDamage is an integer as dame of bullets
     * @param BulletsType   is a string that show type of bullets
     * @param coordinates   is primary coordinate of bullets
     * @param angle         is a double as angle of bullets based on y axis
     */
    public Bullets(int BulletsDamage, String BulletsType, ArrayList<Coordinate> coordinates, double angle) {
        this.coordinates = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            this.coordinates.add(new Coordinate(coordinate.getXCoordinate(), coordinate.getYCoordinate()));
        }
        bulletsBlasted = false;
        damage = BulletsDamage;
        type = "";
        type = BulletsType;
        if (BulletsType.equals("NORMAL")) {
            speed = Constants.BULLET_SPEED;
            try {
                bulletsImage = ImageIO.read(new File("kit\\bullets\\normal.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (BulletsType.equals("LASER")) { // ba else nazadam yevakht khastim emtiazi ezafe konim ye golule
            speed = 3 * Constants.BULLET_SPEED;
            try {
                bulletsImage = ImageIO.read(new File("kit\\bullets\\laser.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.angle = angle;

        fireTime = LocalDateTime.now();
        Thread thread = new Thread(() -> { //change to swing worker
            try {
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(1000);
                    lifeTime++;
                }
                TankTroubleMap.getBullets().remove(0); //?????????????
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * This method update bullets state in every moment.
     * state include coordinate and angle life time.
     * And also check overlap with tanks and walls and do something based on result
     */
    public void update() {
        double newAngle = angle;
        if (newAngle >= 0) {
            while (newAngle >= 360) {
                newAngle -= 360;
            }

        } else {
            while (newAngle >= 360) {
                newAngle += 360;
            }
            if (newAngle > 360) newAngle -= 360;
        }
        if (newAngle <= 90) {
            for (Coordinate coordinate : coordinates) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() - (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() + (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
            }
        } else if (newAngle <= 180) {
            for (Coordinate coordinate : coordinates) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() + (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() + (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
            }
        } else if (newAngle <= 270) {
            for (Coordinate coordinate : coordinates) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() + (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() - (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
            }
        } else {
            for (Coordinate coordinate : coordinates) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() - (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() - (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
            }
        }

        // IndestructibleWalls
        boolean flag = true;
        for (Wall wall : TankTroubleMap.getIndestructibleWalls()) {
            if (TankTroubleMap.checkOverLap(wall.getPointsArray(), coordinates)) {
                flag = false;
                angle = 180 - angle;
                break;
            }
        }

        // DestructibleWalls
        if (flag) {
            for (int i = 0; i < TankTroubleMap.getDestructibleWalls().size(); i++) {
                if (TankTroubleMap.checkOverLap(TankTroubleMap.getDestructibleWalls().get(i).getPointsArray(), coordinates)) {
                    DestructibleWall destructibleWall = (DestructibleWall) TankTroubleMap.getDestructibleWalls().get(i);
                    destructibleWall.receiveDamage(damage);
                    if (destructibleWall.getHealth() <= 0) {
                        TankTroubleMap.getDestructibleWalls().remove(i);
                    }
                    bulletsBlasted = true;
                    for (Bullets bullets : TankTroubleMap.getBullets()) {
                        if (bullets.bulletsBlasted) TankTroubleMap.getBullets().remove(bullets);
                        break;
                    }
                    flag = false;
                }
            }
        }


        // Tanks
        if (flag) {
            ArrayList<Tank> tanks = new ArrayList<>();
            tanks.addAll(TankTroubleMap.getUserTanks());
            tanks.addAll(TankTroubleMap.getAITanks());
            for (int i = 0; i < tanks.size(); i++) {
                if (TankTroubleMap.checkOverLap(coordinates, tanks.get(i).getTankCoordinates())) {
                    tanks.get(i).receiveDamage(damage);
                    if (tanks.get(i).getHealth() <= 0) {
                        tanks.remove(i);
                    }
                    bulletsBlasted = true;
                    for (Bullets bullets : TankTroubleMap.getBullets()) {
                        if (bullets.bulletsBlasted) TankTroubleMap.getBullets().remove(bullets);
                        break;
                    }
                    flag = false;
                }
            }
        }
    }


    /**
     * Getter method of bulletsImage field
     *
     * @return image of bullets
     */
    public Image getBulletsImage() {
        return bulletsImage;
    }

    /**
     * Getter method of fireTime field
     *
     * @return time that bullets fired from tank
     */
    public LocalDateTime getFireTime() {
        return fireTime;
    }

    /**
     * Getter method of angle field
     *
     * @return angle of bullets based on y axis
     */
    public double getAngle() {
        return angle;
    }

    /**
     * This is setter method for angle field.
     *
     * @param angle is a double as angle of bullets
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Getter method of coordinates field
     *
     * @return array list that contain four bullets coordinate
     */
    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
