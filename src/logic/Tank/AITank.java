package logic.Tank;

import logic.Constants;
import logic.Coordinate;
import logic.Tank.Tank;
import logic.TankTroubleMap;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class represent a bot's tank.
 * this class extend tank class and
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 6/10/2020
 */
public class AITank extends Tank {
    TankState tankState;
    Random random;

    /**
     * this is constructor of this class and initialize some fields and fill them with input parameter.
     *
     * @param tankImagePath is a string as path of image tank
     */
    public AITank(String tankImagePath, TankTroubleMap tankTroubleMap) {
        super(tankImagePath, tankTroubleMap);
        random = new Random();
        tankState = new TankState();
    }

    /**
     * This class show state of tank in every moment.
     * This class have one main method update that update state of tank
     *
     * @author Ali asd & Sayed Mohammad Ali Mirkazemi
     * @version 1.0.0
     * @since 6/10/2020
     */
    public class TankState {

        public int diam;
        public boolean tankBlasted;

        public TankState() {
            this.diam = Constants.TANK_SIZE / 2;
            tankBlasted = false;
            diam = Constants.TANK_SIZE;
        }

        /**
         * The method which updates the game state.
         */
        public void update() {
            //shoot target
            //Use its prize every 10 seconds
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(10000);
                        usePrize();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            int moveUp = random.nextInt(2),moveDown = random.nextInt(2)
                    ,moveRight = random.nextInt(2),moveLeft = random.nextInt(2);
            if (moveUp == 1) {
                ArrayList<Coordinate> movedPoints = movePoints(getTankCoordinates(), "UP", getAngle());
                Coordinate movedCenter = movePoint(getCenterPointOfTank(), "UP", getAngle());
                if (canMove(movedPoints)) {
                    catchPrize();
                    setTankCoordinates(movedPoints);
                    setCenterPointCoordinate(movedCenter);
                }
            }
            if (moveDown == 1) {
                ArrayList<Coordinate> movedPoints = movePoints(getTankCoordinates(), "DOWN", getAngle());
                Coordinate movedCenter = movePoint(getCenterPointOfTank(), "DOWN", getAngle());
                if (canMove(movedPoints)) {
                    catchPrize();
                    setTankCoordinates(movedPoints);
                    setCenterPointCoordinate(movedCenter);
                }
            }
            if (moveLeft == 1) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(getTankCoordinates(), getCenterPointOfTank(), Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    setTankCoordinates(rotatedPoints);
                    rotateCounterClockwise();
                }
            }
            if (moveRight == 1) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(getTankCoordinates(), getCenterPointOfTank(), -Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    setTankCoordinates(rotatedPoints);
                    rotateClockwise();
                }
            }

        }
    }

    public TankState getTankState() {
        return tankState;
    }
}
