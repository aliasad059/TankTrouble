package logic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class represent bullet for tanks include player's and bot's tank.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class Bullet implements Serializable {
    private int damage;
    private int speed;
    private ArrayList<Coordinate> coordinates;
    private LocalDateTime fireTime;
    private double angle;
    private Image bulletsImage;
    private boolean bulletsBlasted;

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     *
     * @param bulletsDamage is an integer as damage of bullet
     * @param bulletsType   is a string that show type of bullet
     * @param coordinate    is primary coordinate of bullet
     * @param angle         is a double as angle of bullet based on y axis
     */
    public Bullet(int bulletsDamage, String bulletsType, Coordinate coordinate, double angle) {
        this.coordinates = new ArrayList<>();

        coordinates.add(new Coordinate(coordinate.getXCoordinate()-(Constants.BULLET_SIZE/2+0),coordinate.getYCoordinate() - (Constants.BULLET_SIZE/2+0)));
        coordinates.add(new Coordinate(coordinate.getXCoordinate()+(Constants.BULLET_SIZE/2+0),coordinate.getYCoordinate() - (Constants.BULLET_SIZE/2+0)));
        coordinates.add(new Coordinate(coordinate.getXCoordinate()+(Constants.BULLET_SIZE/2+0),coordinate.getYCoordinate() + (Constants.BULLET_SIZE/2+0)));
        coordinates.add(new Coordinate(coordinate.getXCoordinate()-(Constants.BULLET_SIZE/2+0),coordinate.getYCoordinate() + (Constants.BULLET_SIZE/2+0)));

        bulletsBlasted = false;
        damage = bulletsDamage;
        if (bulletsType.equals("NORMAL")) {
            speed = Constants.BULLET_SPEED;
            try {
                bulletsImage = ImageIO.read(new File("kit\\bullet\\normal.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (bulletsType.equals("LASER")) { // ba else nazadam yevakht khastim emtiazi ezafe konim ye golule
            speed = 3 * Constants.BULLET_SPEED;
            try {
                bulletsImage = ImageIO.read(new File("kit\\bullet\\laser.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.angle = angle;

        fireTime = LocalDateTime.now();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(4000);
                bulletsBlasted=true;
                for (Bullet bullet : TankTroubleMap.getBullets()) {
                    if (bullet.bulletsBlasted) TankTroubleMap.getBullets().remove(bullet);
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


    /**
     * This method update bullets state in every moment.
     * state include coordinate and angle life time.
     * And also check overlap with tanks and walls and do something based on result
     */
    public void update() {
        double newAngle = angle-90;
        ArrayList<Coordinate> replaceCoordinate=new ArrayList<>();
        replaceCoordinate.addAll(coordinates);

        for (Coordinate coordinate : replaceCoordinate) {
            coordinate.setYCoordinate(coordinate.getYCoordinate() + Math.sin(Math.toRadians(newAngle)) * speed);
            coordinate.setXCoordinate(coordinate.getXCoordinate() - Math.cos(Math.toRadians(newAngle)) * speed);
        }



        // IndestructibleWalls
        boolean wallFlag = true;
        final boolean[] flag = { true };
        for (Wall wall : TankTroubleMap.getIndestructibleWalls()) {
            if (TankTroubleMap.checkOverLap(wall.getPointsArray(), coordinates)) {

                //System.out.println("barkhord..............");
                wallFlag = false;
                flag[0] =false;
                if(wall.getDirection().equals("VERTICAL")){
                    //System.out.println("amudi..............");
                    angle = - angle;

                }
                else {
                    //System.out.println("afoghi..............");
                    angle =180 - angle;
                }
                break;
            }
        }

        // DestructibleWalls
        if (wallFlag) {
            for (int i = 0; i < TankTroubleMap.getDestructibleWalls().size(); i++) {
                if (TankTroubleMap.checkOverLap(TankTroubleMap.getDestructibleWalls().get(i).getPointsArray(), coordinates)) {
                    DestructibleWall destructibleWall = (DestructibleWall) TankTroubleMap.getDestructibleWalls().get(i);
                    destructibleWall.receiveDamage(damage);
                    if (destructibleWall.getHealth() <= 0) {
                        TankTroubleMap.getDestructibleWalls().remove(i);
                    }
                    bulletsBlasted = true;
                    for (Bullet bullet : TankTroubleMap.getBullets()) {
                        if (bullet.bulletsBlasted) TankTroubleMap.getBullets().remove(bullet);
                        break;
                    }
                    flag[0] =false;
                    wallFlag = false;
                }
            }
        }

        if(wallFlag){
            coordinates.clear();
            coordinates.addAll(replaceCoordinate);
        }
        /*

        int wallC=0;
        for(Wall wall: TankTroubleMap.getIndestructibleWalls()){
            wallC++;
            System.out.println("......................"+wallC+".........................");
            for(Coordinate coordinate: wall.getPointsArray()){
                System.out.println("("+coordinate.getXCoordinate()+", "+coordinate.getYCoordinate()+")");
            }
        }


         */


        /*
        for(Coordinate coordinate:coordinates){
            System.out.println("("+coordinate.getXCoordinate()+", "+coordinate.getYCoordinate()+")");
        }
        System.out.println("................................................");

         */

        // Tanks
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                if (flag[0]) {
                    ArrayList<Tank> tanks = new ArrayList<>();
                    tanks.addAll(TankTroubleMap.getUserTanks());
                    tanks.addAll(TankTroubleMap.getAITanks());
                    for (int i = 0; i < tanks.size(); i++) {
                        if (TankTroubleMap.checkOverLap(coordinates, tanks.get(i).getTankCoordinates())) {
                            bulletsBlasted = true;
                            for (Bullet bullet : TankTroubleMap.getBullets()) {
                                if (bullet.bulletsBlasted) TankTroubleMap.getBullets().remove(bullet);
                                break;
                            }
                            tanks.get(i).receiveDamage(damage);
                            System.out.println("health: "+TankTroubleMap.getUserTanks().get(i).getHealth());
                            if (tanks.get(i).getHealth() <= 0) {
                                System.out.println("Blasted Tank.......");
                                TankTroubleMap.getUserTanks().remove(i);
                            }

                            flag[0] = false;
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
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
