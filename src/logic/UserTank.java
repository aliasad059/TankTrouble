package logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This class represent a tank for a user player with extend tank class.
 * This class have some method for receive user command for move tank and etc.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 18-7-2020
 */
public class UserTank extends Tank {
    TankState tankState;

    /**
     * This is constructor of UserTank class and new some fields (allocate) and based on input parameters fill them.
     *
     * @param health          is an integer as health of tank
     * @param pixelCoordinate is coordinate of tank in the map
     * @param tankImagePass   is image of tank
     */
    public UserTank(int health, Coordinate pixelCoordinate, String tankImagePass) {
        super(health, pixelCoordinate, tankImagePass);
        tankState = new TankState();
    }

    /**
     * This is first inner class of UserTank class and represent and also update state of user tank in every moment with user command.
     * state contain coordinate, health and etc.
     *
     * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
     * @version 1.0.0
     * @since 18-7-2020
     */
    public class TankState {

        private int diam;
        private boolean tankBlasted;
        private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT, keyFIRE, keyPrize;
        private KeyHandler keyHandler;

        /**
         * This constructor set all boolean key to false and also new (allocate) key handel.
         */
        public TankState() {
            this.diam = Constants.TANK_SIZE / 2;
            tankBlasted = false;
            diam = Constants.TANK_SIZE;
            //
            keyUP = false;
            keyDOWN = false;
            keyRIGHT = false;
            keyLEFT = false;
            keyFIRE = false;
            keyPrize = false;
            //
            keyHandler = new KeyHandler();
        }

        /**
         * The method which updates the game state.
         */
        public void update() {
            if (keyFIRE)
                fire();
            if (keyPrize)
                usePrize();
            if (keyUP) {
                ArrayList<Coordinate> movedPoints = movePoints(getTankCoordinates(), "UP", getAngle());
                Coordinate movedCenter = movePoint(getCenterPointOfTank(), "UP", getAngle());
                if (canMove(movedPoints)) {
//                    catchPrize();
                    setTankCoordinates(movedPoints);
                    setCenterPointCoordinate(movedCenter);
                }
            }
            if (keyDOWN) {
                ArrayList<Coordinate> movedPoints = movePoints(getTankCoordinates(), "DOWN", getAngle());
                Coordinate movedCenter = movePoint(getCenterPointOfTank(), "DOWN", getAngle());
                if (canMove(movedPoints)) {
//                    catchPrize();
                    setTankCoordinates(movedPoints);
                    setCenterPointCoordinate(movedCenter);
                }
            }
            if (keyLEFT) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(getTankCoordinates(), getCenterPointOfTank(), Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    setTankCoordinates(rotatedPoints);
                    rotateCounterClockwise();
                }
            }
            if (keyRIGHT) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(getTankCoordinates(), getCenterPointOfTank(), -Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    setTankCoordinates(rotatedPoints);
                    rotateClockwise();
                }
            }
        }


        /**
         * This is second inner class of UserTank class and handel user command for move tank or fire bullet and etc.
         */
        class KeyHandler extends KeyAdapter {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        keyFIRE = true;
                        break;
                    case KeyEvent.VK_ENTER:
                        keyPrize = true;
                        break;
                    case KeyEvent.VK_UP:
                        keyUP = true;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDOWN = true;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLEFT = true;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRIGHT = true;
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SPACE:
                        keyFIRE = false;
                        break;
                    case KeyEvent.VK_ENTER:
                        keyPrize = false;
                        break;
                    case KeyEvent.VK_UP:
                        keyUP = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        keyDOWN = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        keyLEFT = false;
                        break;
                    case KeyEvent.VK_RIGHT:
                        keyRIGHT = false;
                        break;
                }
            }
        }

        public KeyHandler getKeyHandler() {
            return keyHandler;
        }
    }

    /**
     * Getter method of tankState field
     *
     * @return current state of tank
     */
    public TankState getTankState() {
        return tankState;
    }

}
