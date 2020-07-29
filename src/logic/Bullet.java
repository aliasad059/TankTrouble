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
    private Coordinate centerPointCoordinate;
    private LocalDateTime fireTime;
    private double angle;
    private Image bulletsImage;
    private boolean bulletsBlasted;
    private int freeFromTank;

    /**
     * This is constructor of Bullets class and allocate object coordinate and initialize other fields.
     *
     * @param bulletsDamage is an integer as damage of bullet
     * @param bulletsType   is a string that show type of bullet
     *                      //     * @param coordinate    is primary coordinate of bullet
     * @param angle         is a double as angle of bullet based on y axis
     */
    public Bullet(int bulletsDamage, String bulletsType, Coordinate centerPointCoordinate, double angle) {
        this.centerPointCoordinate = centerPointCoordinate;
        updateArraylistCoordinates();
        freeFromTank = 0;
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
                //System.out.println("In thread..................");
                Thread.sleep(4000);
                bulletsBlasted = true;
                //System.out.println("time out...............");
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
//    public void update() {
//        double newAngle = angle - 90;
//        ArrayList<Coordinate> replaceCoordinate = new ArrayList<>();
//        replaceCoordinate.addAll(coordinates);
//
//        for (Coordinate coordinate : replaceCoordinate) {
//            coordinate.setYCoordinate(coordinate.getYCoordinate() + Math.sin(Math.toRadians(newAngle)) * speed);
//            coordinate.setXCoordinate(coordinate.getXCoordinate() - Math.cos(Math.toRadians(newAngle)) * speed);
//        }
//
//
//        // IndestructibleWalls
//
//        if (!bulletsBlasted) {
//            boolean wallFlag = true;
//            boolean flag = true;
//            for (Wall wall : TankTroubleMap.getIndestructibleWalls()) {
//                if (TankTroubleMap.checkOverLap(wall.getPointsArray(), coordinates)) {
//
//                    //System.out.println("barkhord..............");
//                    wallFlag = false;
//                    flag = false;
//                    if (wall.getDirection().equals("VERTICAL")) {
//                        //System.out.println("amudi..............");
//                        angle = -angle;
//
//                    } else {
//                        //System.out.println("afoghi..............");
//                        angle = 180 - angle;
//                    }
//                    break;
//                }
//            }
//
//            // DestructibleWalls
//            if (wallFlag) {
//                for (int i = 0; i < TankTroubleMap.getDestructibleWalls().size(); i++) {
//                    if (TankTroubleMap.checkOverLap(TankTroubleMap.getDestructibleWalls().get(i).getPointsArray(), coordinates)) {
//                        DestructibleWall destructibleWall = (DestructibleWall) TankTroubleMap.getDestructibleWalls().get(i);
//                        destructibleWall.receiveDamage(damage);
//                        if (destructibleWall.getHealth() <= 0) {
//                            TankTroubleMap.getDestructibleWalls().remove(i);
//                        }
//                        bulletsBlasted = true;
//                        for (Bullet bullet : TankTroubleMap.getBullets()) {
//                            if (bullet.bulletsBlasted) TankTroubleMap.getBullets().remove(bullet);
//                            break;
//                        }
//                        flag = false;
//                        wallFlag = false;
//                        break;
//                    }
//                }
//            }
//
//            if (wallFlag) {
//                coordinates.clear();
//                coordinates.addAll(replaceCoordinate);
//            }
//        /*
//
//        int wallC=0;
//        for(Wall wall: TankTroubleMap.getIndestructibleWalls()){
//            wallC++;
//            System.out.println("......................"+wallC+".........................");
//            for(Coordinate coordinate: wall.getPointsArray()){
//                System.out.println("("+coordinate.getXCoordinate()+", "+coordinate.getYCoordinate()+")");
//            }
//        }
//
//
//         */
//
//
//        /*
//        for(Coordinate coordinate:coordinates){
//            System.out.println("("+coordinate.getXCoordinate()+", "+coordinate.getYCoordinate()+")");
//        }
//        System.out.println("................................................");
//
//         */
//
//            // Tanks
//            if (flag) {
//                ArrayList<Tank> tanks = new ArrayList<>();
//                tanks.addAll(TankTroubleMap.getUserTanks());
//                tanks.addAll(TankTroubleMap.getAITanks());
//                for (Tank tank : tanks) {
//                    if (TankTroubleMap.checkOverLap(coordinates, tank.getTankCoordinates())) {
//                        bulletsBlasted = true;
//                        for (Bullet bullet : TankTroubleMap.getBullets()) {
//                            if (bullet.bulletsBlasted) TankTroubleMap.getBullets().remove(bullet);
//                            break;
//                        }
//                        tank.receiveDamage(damage);
//                        System.out.println("health: " + tank.getHealth());
//                        if (tank.getHealth() <= 0) {
//                            System.out.println("Blasted Tank.......");
//                            TankTroubleMap.getUserTanks().remove(tank);
//                            TankTroubleMap.getAITanks().remove(tank);
//                        }
//
//                        //flag = false;
//                    }
//                }
//            }
//        }
//
//
//    }

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

    public void update() {
        double newAngle = angle - 90;
        Coordinate nextCenterPointCoordinate = new Coordinate(
                this.centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SPEED * Math.cos(Math.toRadians(newAngle))),
                this.centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SPEED * Math.sin(Math.toRadians(newAngle)))
        );

        Wall state1Wall = horizontalCrash(nextCenterPointCoordinate),
                state2Wall = verticalCrash(nextCenterPointCoordinate),
                state3Wall = cornerCrash(nextCenterPointCoordinate);
        if (state1Wall != null) { //if the bullet would go over horizontal side of any wall
            angle = 180 - angle;
            nextCenterPointCoordinate = new Coordinate(
                    this.centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SPEED * Math.cos(Math.toRadians(newAngle))),
                    this.centerPointCoordinate.getYCoordinate());
            if (state1Wall.isDestroyable()) {//cashing destructible
                ((DestructibleWall) state1Wall).receiveDamage(damage);
                if (((DestructibleWall) state1Wall).getHealth() <= 0) {
                    TankTroubleMap.getDestructibleWalls().remove(state1Wall);
                }
                bulletsBlasted = true;
                TankTroubleMap.getBullets().remove(this);
            }
        } else if (state2Wall != null) { // if the bullet would go over vertical side any  wall

            angle = -angle;
//            nextCenterPointCoordinate = new Coordinate(this.centerPointCoordinate.getXCoordinate(),
//                    this.centerPointCoordinate.getYCoordinate()
//                            + (Constants.BULLET_SPEED * Math.sin(M?ath.toRadians(newAngle))));
            if (state2Wall.isDestroyable()) {//cashing destructible
                ((DestructibleWall) state2Wall).receiveDamage(damage);
                if (((DestructibleWall) state2Wall).getHealth() <= 0) {
                    TankTroubleMap.getDestructibleWalls().remove(state2Wall);
                }
                bulletsBlasted = true;
                TankTroubleMap.getBullets().remove(this);
            }
        } else if (state3Wall != null) {// if the bullet would go over corner of any wall
//            if (this.currentYSquare() == (int) this.currentYSquare()) {
//                angle  = 180 - angle;
//                nextCenterPointCoordinate = new Coordinate(this.centerPointCoordinate.getXCoordinate()
//                        + (Constants.BULLET_SPEED * Math.cos(Math.toRadians(newAngle))),
//                        this.centerPointCoordinate.getYCoordinate());
//            } else {
//                angle = -angle;
//                nextCenterPointCoordinate = new Coordinate(this.centerPointCoordinate.getXCoordinate(),
//                        this.centerPointCoordinate.getYCoordinate()
//                                - (Constants.BULLET_SPEED * Math.sin(Math.toRadians(newAngle))));
//            }
//            if (state3Wall.isDestroyable()) {//cashing destructible
//                ((DestructibleWall)state3Wall).receiveDamage(damage);
//                if (((DestructibleWall)state3Wall).getHealth() <= 0) {
//                    TankTroubleMap.getDestructibleWalls().remove(state3Wall);
//                }
//                bulletsBlasted = true;
//                TankTroubleMap.getBullets().remove(this);
//            }
        }
        this.centerPointCoordinate = nextCenterPointCoordinate;
        updateArraylistCoordinates();
    }


    private Wall horizontalCrash(Coordinate nextCoordinate) {
        ArrayList<Wall> walls = new ArrayList<>();
        walls.addAll(TankTroubleMap.getDestructibleWalls());
        walls.addAll(TankTroubleMap.getIndestructibleWalls());
        for (int i = 0; i < walls.size(); i++) {
            if (TankTroubleMap.checkOverLap(walls.get(i).getPointsArray(), coordinates) && (horizontalOverlap(walls.get(i).getPointsArray(),makeCoordinatesFromCenterCoordinate(nextCoordinate))))
                return walls.get(i);
        }
        return null;
    }

    private Wall verticalCrash(Coordinate nextCoordinate) {
        ArrayList<Wall> walls = new ArrayList<>();
        walls.addAll(TankTroubleMap.getDestructibleWalls());
        walls.addAll(TankTroubleMap.getIndestructibleWalls());
        for (int i = 0; i < walls.size(); i++) {
            if (TankTroubleMap.checkOverLap(walls.get(i).getPointsArray(), coordinates) && (verticalOverlap(walls.get(i).getPointsArray()
                    , makeCoordinatesFromCenterCoordinate(nextCoordinate))))
                return walls.get(i);
        }
        return null;
    }

    private Wall cornerCrash(Coordinate nextCoordinate) {
        return null;
    }

    private boolean verticalOverlap(ArrayList<Coordinate> wallCoordinates, ArrayList<Coordinate> nextCoordinates) {
        boolean isRight = (
                (wallCoordinates.get(0).getXCoordinate() < nextCoordinates.get(0).getXCoordinate()) &&
                        ((wallCoordinates.get(1).getYCoordinate() < nextCoordinates.get(1).getYCoordinate()) && (nextCoordinates.get(2).getYCoordinate() < wallCoordinates.get(2).getYCoordinate())||
                                ((nextCoordinates.get(1).getYCoordinate() < wallCoordinates.get(1).getYCoordinate()) && (wallCoordinates.get(2).getYCoordinate() < nextCoordinates.get(2).getYCoordinate()))
                        )
        );
        boolean isLeft =  (
                (wallCoordinates.get(0).getXCoordinate() > nextCoordinates.get(0).getXCoordinate()) &&
                        ((wallCoordinates.get(1).getYCoordinate() < nextCoordinates.get(1).getYCoordinate()) && (nextCoordinates.get(2).getYCoordinate() < wallCoordinates.get(2).getYCoordinate())||
                                ((nextCoordinates.get(1).getYCoordinate() < wallCoordinates.get(1).getYCoordinate()) && (wallCoordinates.get(2).getYCoordinate() < nextCoordinates.get(2).getYCoordinate()))
                        )
        );
        return isRight || isLeft;
    }

    private boolean horizontalOverlap(ArrayList<Coordinate> wallCoordinates,ArrayList<Coordinate> nextCoordinates) {
        boolean isTop =  (
                (wallCoordinates.get(0).getYCoordinate() < nextCoordinates.get(0).getYCoordinate()) &&
                        ((wallCoordinates.get(0).getXCoordinate() < nextCoordinates.get(0).getXCoordinate()) && (nextCoordinates.get(1).getXCoordinate() < wallCoordinates.get(1).getXCoordinate())||
                                ((nextCoordinates.get(0).getXCoordinate() < wallCoordinates.get(0).getYCoordinate()) && (wallCoordinates.get(1).getXCoordinate() < nextCoordinates.get(1).getXCoordinate()))
                        )
        );
        boolean isDown = (
                (wallCoordinates.get(0).getYCoordinate() > nextCoordinates.get(0).getYCoordinate()) &&
                        ((wallCoordinates.get(0).getXCoordinate() < nextCoordinates.get(0).getXCoordinate()) && (nextCoordinates.get(1).getXCoordinate() < wallCoordinates.get(1).getXCoordinate())||
                                ((nextCoordinates.get(0).getXCoordinate() < wallCoordinates.get(0).getYCoordinate()) && (wallCoordinates.get(1).getXCoordinate() < nextCoordinates.get(1).getXCoordinate()))
                        )
        );
        return isDown||isTop;
    }

    private void updateArraylistCoordinates() {
        this.coordinates = makeCoordinatesFromCenterCoordinate(centerPointCoordinate);
    }

    private ArrayList<Coordinate> makeCoordinatesFromCenterCoordinate(Coordinate centerPointCoordinate) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() - (Constants.BULLET_SIZE / 2)));
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() - (Constants.BULLET_SIZE / 2)));
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() + (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SIZE / 2)));
        coordinates.add(new Coordinate(centerPointCoordinate.getXCoordinate() - (Constants.BULLET_SIZE / 2), centerPointCoordinate.getYCoordinate() + (Constants.BULLET_SIZE / 2)));
        return coordinates;
    }
}
