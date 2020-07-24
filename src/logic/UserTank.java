package logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class UserTank extends Tank {
    TankState tankState;

    public UserTank(int health, Coordinate pixelCoordinate, String tankImagePass) {
        super(health, pixelCoordinate, tankImagePass);
        tankState = new TankState();
    }

    public TankState getTankState() {
        return tankState;
    }

    public class TankState {

        public int diam;
        public boolean tankBlasted;

        private boolean keyUP, keyDOWN, keyRIGHT, keyLEFT, keyFIRE, keyPrize;
        private KeyHandler keyHandler;

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

        public KeyHandler getKeyHandler() {
            return keyHandler;
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
                ArrayList<Coordinate> movedPoints = movePoints(tankCoordinates,"UP",angle);
                Coordinate movedCenter = movePoint(centerPointCoordinate,"UP",angle);
                if (canMove(movedPoints)) {
//                    catchPrize();
                    tankCoordinates = movedPoints;
                    centerPointCoordinate = movedCenter;
                }
            }
            if (keyDOWN) {
                ArrayList<Coordinate> movedPoints = movePoints(tankCoordinates,"DOWN",angle);
                Coordinate movedCenter = movePoint(centerPointCoordinate,"DOWN",angle);
                if (canMove(movedPoints)) {
//                    catchPrize();
                    tankCoordinates = movedPoints;
                    centerPointCoordinate = movedCenter;
                }
            }
            if (keyLEFT) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(tankCoordinates,centerPointCoordinate,Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    tankCoordinates = rotatedPoints;
                    rotateClockwise();
                }
            }
            if (keyRIGHT) {
                ArrayList<Coordinate> rotatedPoints = rotatePoints(tankCoordinates,centerPointCoordinate,-Constants.TANK_ROTATION_SPEED);
                if (canMove(rotatedPoints)) {
                    tankCoordinates = rotatedPoints;
                    rotateCounterClockwise();
                }
            }

//            //checking if the tank do not leave the map
//            pixelCoordinate.setXCoordinate(Math.max(pixelCoordinate.getXCoordinate(), 0));
//            pixelCoordinate.setXCoordinate(Math.min(pixelCoordinate.getXCoordinate(),
//                    TankTroubleMap.getWidth() - diam));
//            pixelCoordinate.setYCoordinate(Math.max(pixelCoordinate.getYCoordinate(), 0));
//            pixelCoordinate.setYCoordinate(Math.min(pixelCoordinate.getYCoordinate(),
//                    TankTroubleMap.getHeight() - diam));
        }

        public KeyListener getKeyListener() {
            return keyHandler;
        }

        /**
         * The keyboard handler.
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
//                    case KeyEvent.VK_ESCAPE:
//                        tankBlasted = true;
//                        break;
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
    }

}
