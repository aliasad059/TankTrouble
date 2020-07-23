package logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
                if (canMove(pixelCoordinate.getXCoordinate() + (int) Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED
                        , pixelCoordinate.getYCoordinate() - (int) Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED, 0)) {
//                    catchPrize();
                    pixelCoordinate.setXCoordinate(pixelCoordinate.getXCoordinate() + (int) (Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED));
                    pixelCoordinate.setYCoordinate(pixelCoordinate.getYCoordinate() - (int) (Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED));
                }
            }
            if (keyDOWN) {
                if (canMove(pixelCoordinate.getXCoordinate() - Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED
                        , pixelCoordinate.getYCoordinate() +  Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED, 0)) {
//                    catchPrize();
                    pixelCoordinate.setXCoordinate(pixelCoordinate.getXCoordinate() -  (Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED));
                    pixelCoordinate.setYCoordinate(pixelCoordinate.getYCoordinate() +  (Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED));
                }
            }
            if (keyLEFT) {
                if (canMove((int) (pixelCoordinate.getXCoordinate() * Math.cos(Math.toRadians(Constants.TANK_ROTATION_SPEED)) - pixelCoordinate.getYCoordinate() * Math.sin(Math.toRadians(Constants.TANK_ROTATION_SPEED)))
                        , (int) (pixelCoordinate.getXCoordinate() * Math.sin(Math.toRadians(Constants.TANK_ROTATION_SPEED)) + pixelCoordinate.getYCoordinate() * Math.cos(Math.toRadians(Constants.TANK_ROTATION_SPEED)))
                        , -5)) {
                    rotateClockwise();
                }
            }
            if (keyRIGHT) {
                if (canMove((int) (pixelCoordinate.getXCoordinate() * Math.cos(Math.toRadians(Constants.TANK_ROTATION_SPEED)) + pixelCoordinate.getYCoordinate() * Math.sin(Math.toRadians(Constants.TANK_ROTATION_SPEED)))
                        , (int) (-pixelCoordinate.getXCoordinate() * Math.sin(Math.toRadians(Constants.TANK_ROTATION_SPEED)) + pixelCoordinate.getYCoordinate() * Math.cos(Math.toRadians(Constants.TANK_ROTATION_SPEED)))
                        , 5)
                ) {
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

        private boolean canMove(double finalX, double finalY, double rotationAmount) {
            return !TankTroubleMap.checkOverlapWithAllWalls(new Coordinate(finalX, finalY), Constants.TANK_SIZE, Constants.TANK_SIZE, angle + rotationAmount);
//                    && !TankTroubleMap.checkOverlapWithAllTanks(new Coordinate(finalX,finalY),Constants.TANK_SIZE,Constants.TANK_SIZE,angle +rotationAmount);
        }

        private void rotateClockwise() {
            angle -= Constants.TANK_ROTATION_SPEED;
        }

        private void rotateCounterClockwise() {
            angle += Constants.TANK_ROTATION_SPEED;
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
