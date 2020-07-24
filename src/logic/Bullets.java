package logic;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class represent bullets for player's tank.
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
    Coordinate coordinate;
    //time that bullets fired from tank
    private LocalDateTime fireTime;
    // the time that this kind of bullets stay in map
    private float lifeTime;
    private double angle;
    private Image bulletsImage;
    private boolean bulletsBlasted;

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     * ??????????????
     */
    public Bullets(int BulletsDamage, String BulletsType, Coordinate coordinate, double angle){
        bulletsBlasted=false;
        damage=BulletsDamage;
        type="";
        type=BulletsType;
        if(BulletsType.equals("NORMAL")){
            speed = Constants.BULLET_SPEED;
            try {
                bulletsImage = ImageIO.read(new File("kit++\\kit\\bullets\\normal.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(BulletsType.equals("LASER")){ // ba else nazadam yevakht khastim emtiazi ezafe konim ye golule
            speed = 3*Constants.BULLET_SPEED;
            try {
                bulletsImage = ImageIO.read(new File("kit++\\kit\\bullets\\laser.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.angle = angle;
        this.coordinate=new Coordinate();
        this.coordinate.setXCoordinate(coordinate.getXCoordinate()+(Constants.TANK_SIZE/4));
        this.coordinate.setYCoordinate(coordinate.getYCoordinate());

        fireTime = LocalDateTime.now();
        Thread thread = new Thread(() -> { //change to swing worker
            try {
                for(int i=0; i<4; i++){
                    Thread.sleep(1000);
                    lifeTime++;
                }
                TankTroubleMap.getBullets().remove(0); //?????????????
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public LocalDateTime getFireTime() {
        return fireTime;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void update() {
        double newAngle = angle - 90;
        if (newAngle >= 0) {
            while (newAngle >= 360) {
                newAngle -= 360;
            }
            if (newAngle <= 90) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() + (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() - (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            } else if (newAngle <= 180) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() + (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() + (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            } else if (newAngle <= 270) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() - (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() + (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            } else {
                coordinate.setYCoordinate(coordinate.getYCoordinate() - (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() - (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            }
        } else {
            while (newAngle <= -360) {
                newAngle += 360;
            }

            if (newAngle >= -90) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() - (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() - (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            } else if (newAngle >= -180) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() - (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() + (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            } else if (newAngle >= -270) {
                coordinate.setYCoordinate(coordinate.getYCoordinate() + (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() + (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            } else {
                coordinate.setYCoordinate(coordinate.getYCoordinate() + (int) Math.round(Math.abs(Math.sin(Math.toRadians(newAngle)) * speed)));
                coordinate.setXCoordinate(coordinate.getXCoordinate() - (int) Math.round(Math.abs(Math.cos(Math.toRadians(newAngle)) * speed)));
            }
        }

        boolean flag = true;
        for (Wall wall : TankTroubleMap.getIndestructibleWalls()) {
            if(wall.getDirection().equals("HORIZONTAL")){
                if (TankTroubleMap.checkOverLap(TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL, wall.getStartingPoint(), 0), TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.BULLET_SIZE, Constants.BULLET_SIZE, coordinate, angle))) {
                    flag = false;
                    System.out.println("barkhord");
                    angle=-angle; //bug
                    break;
                }
            }
            else {
                if (TankTroubleMap.checkOverLap(TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL, wall.getStartingPoint(), 0), TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.BULLET_SIZE, Constants.BULLET_SIZE, coordinate, angle))) {
                    flag = false;
                    System.out.println("barkhord");
                    angle=-angle; //bug
                    break;
                }
            }

        }
        if (flag) {
            for (int i = 0; i < TankTroubleMap.getDestructibleWalls().size(); i++) {
                if (TankTroubleMap.getDestructibleWalls().get(i).getDirection().equals("HORIZONTAL")) {
                    if (TankTroubleMap.checkOverLap(TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.WALL_WIDTH_HORIZONTAL, Constants.WALL_HEIGHT_HORIZONTAL, TankTroubleMap.getDestructibleWalls().get(i).getStartingPoint(), 0), TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.BULLET_SIZE, Constants.BULLET_SIZE, coordinate, angle))) {
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
                        break;
                    }
                }
                else {
                    if (TankTroubleMap.checkOverLap(TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.WALL_WIDTH_VERTICAL, Constants.WALL_HEIGHT_VERTICAL, TankTroubleMap.getDestructibleWalls().get(i).getStartingPoint(), 0), TankTroubleMap.findRectangleFromStartingPointAndAngle(Constants.BULLET_SIZE, Constants.BULLET_SIZE, coordinate, angle))) {
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
                        break;
                    }
                }
            }
        }
    }

    public Image getBulletsImage() {
        return bulletsImage;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public boolean isBulletsBlasted() {
        return bulletsBlasted;
    }
}
