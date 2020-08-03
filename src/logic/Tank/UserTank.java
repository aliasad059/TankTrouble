package logic.Tank;

import Network.NetworkData;
import logic.Constants;
import logic.Coordinate;
import logic.KeyHandler;
import logic.Player.UserPlayer;
import logic.TankTroubleMap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represent a tank for a user player with extend tank class.
 * This class have some method for receive user command for move tank and etc.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class UserTank extends Tank implements Serializable {
    private TankState tankState;

    /**
     * This is constructor of UserTank class and new some fields (allocate) and based on input parameters fill them.
     *
     * @param tankImagePath is image of tank
     */
    public UserTank(String tankImagePath, TankTroubleMap tankTroubleMap) {
        super(tankImagePath, tankTroubleMap);
        tankState = new TankState();
    }
    /**
     * Getter method of tankState field
     *
     * @return current state of tank
     */
    public TankState getTankState() {
        return tankState;
    }

    /**
     * This is first inner class of UserTank class and represent and also update state of user tank in every moment with user command.
     * state contain coordinate, health and etc.
     *
     * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
     * @version 1.0.0
     * @since 18-7-2020
     */
    public class TankState implements Serializable{

        private boolean keyUp, keyDown, keyRight, keyLeft, keyPrize, keyFire;
        private KeyHandler keyHandler;

        /**
         * This constructor set all boolean key to false and also new (allocate) key handel.
         */
        public TankState() {
            keyUp = false;
            keyDown = false;
            keyRight = false;
            keyLeft = false;
            keyPrize = false;
            keyFire = false;
        }

        /**
         * The method which updates the game state.
         */
        public void update() {
            if (keyFire){
                fire();
            }
            if (keyPrize)
                usePrize();
            if (keyUp) {
                ArrayList<Coordinate> movedPoints = movePoints(getTankCoordinates(), "UP", getAngle());
                Coordinate movedCenter = movePoint(getCenterPointOfTank(), "UP", getAngle());
                if (canMove(movedPoints)) {
                    catchPrize();
                    setTankCoordinates(movedPoints);
                    setCenterPointCoordinate(movedCenter);
                }
            }
            if (keyDown) {
                ArrayList<Coordinate> movedPoints = movePoints(getTankCoordinates(), "DOWN", getAngle());
                Coordinate movedCenter = movePoint(getCenterPointOfTank(), "DOWN", getAngle());
                if (canMove(movedPoints)) {
                    catchPrize();
                    setTankCoordinates(movedPoints);
                    setCenterPointCoordinate(movedCenter);
                }
            }
            if (keyLeft) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(getTankCoordinates(), getCenterPointOfTank(), Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    setTankCoordinates(rotatedPoints);
                    rotateCounterClockwise();
                }
            }
            if (keyRight) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(getTankCoordinates(), getCenterPointOfTank(), -Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    setTankCoordinates(rotatedPoints);
                    rotateClockwise();
                }
            }
        }

        public void updateKeys() {
            keyDown = keyHandler.isKeyDown();
            keyLeft = keyHandler.isKeyLeft();
            keyRight = keyHandler.isKeyRight();
            keyUp = keyHandler.isKeyUp();
            keyFire = keyHandler.isKeyFire();
            keyPrize = keyHandler.isKeyPrize();
        }

        public void updateKeys(NetworkData networkData) {
            keyDown = networkData.isKeyDown();
            keyLeft = networkData.isKeyLeft();
            keyRight = networkData.isKeyRight();
            keyUp = networkData.isKeyUp();
            keyFire = networkData.isKeyFire();
            keyPrize = networkData.isKeyPrize();
        }

        public KeyHandler getKeyHandler() {
            return keyHandler;
        }

        public void setKeyHandler(KeyHandler keyHandler) {
            this.keyHandler = keyHandler;
        }
    }
}