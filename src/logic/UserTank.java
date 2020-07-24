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
                double xToMove =(pixelCoordinate.getXCoordinate() + Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
                double yToMove =(pixelCoordinate.getYCoordinate() - Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
                if (canMove(xToMove, yToMove, 0)) {
//                    catchPrize();
                    pixelCoordinate.setXCoordinate(xToMove);
                    pixelCoordinate.setYCoordinate(yToMove);
                    centerPointCoordinate.setXCoordinate(centerPointCoordinate.getXCoordinate() + Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
                    centerPointCoordinate.setYCoordinate(centerPointCoordinate.getYCoordinate() - Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
                } else {
                    System.out.println("UP " + xToMove + " | " + yToMove);
                }
            }
            if (keyDOWN) {
                double xToMove = (pixelCoordinate.getXCoordinate() - Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
                double yToMove =  (pixelCoordinate.getYCoordinate() + Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
                if (canMove(xToMove, yToMove, 0)) {
//                    catchPrize();
                    pixelCoordinate.setXCoordinate(xToMove);
                    pixelCoordinate.setYCoordinate(yToMove);
                    centerPointCoordinate.setXCoordinate(centerPointCoordinate.getXCoordinate() - Math.sin(Math.toRadians(angle)) * Constants.TANK_SPEED);
                    centerPointCoordinate.setYCoordinate(centerPointCoordinate.getYCoordinate() + Math.cos(Math.toRadians(angle)) * Constants.TANK_SPEED);
                } else {
                    System.out.println("DOWN " + xToMove + " | " + yToMove);
                }
            }
            if (keyLEFT) {
                Coordinate coordinateToMove = getRotatedPixelCoordinate(Constants.TANK_ROTATION_SPEED);

                if (canMove(coordinateToMove.getXCoordinate(), coordinateToMove.getYCoordinate(), Constants.TANK_ROTATION_SPEED)) {
                    pixelCoordinate.setXCoordinate(coordinateToMove.getXCoordinate());
                    pixelCoordinate.setYCoordinate(coordinateToMove.getYCoordinate());
                    rotateClockwise();
                } else {
                    System.out.println("LEFT " + coordinateToMove.getXCoordinate() + " | " + coordinateToMove.getYCoordinate());
                }
            }
            if (keyRIGHT) {
                Coordinate coordinateToMove = getRotatedPixelCoordinate(-Constants.TANK_ROTATION_SPEED);

                if (canMove(coordinateToMove.getXCoordinate(), coordinateToMove.getYCoordinate(), -Constants.TANK_ROTATION_SPEED)
                ) {
                    pixelCoordinate.setXCoordinate(coordinateToMove.getXCoordinate());
                    pixelCoordinate.setYCoordinate(coordinateToMove.getYCoordinate());
                    rotateCounterClockwise();
                } else {
                    System.out.println("RIGHT " + coordinateToMove.getXCoordinate() + " | " + coordinateToMove.getYCoordinate());
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
            return !TankTroubleMap.checkOverlapWithAllWalls(new Coordinate(finalX, finalY), Constants.TANK_SIZE, Constants.TANK_SIZE, angle + rotationAmount)
                    && !TankTroubleMap.checkOverlapWithAllTanks(UserTank.this, new Coordinate(finalX, finalY), Constants.TANK_SIZE, Constants.TANK_SIZE, angle + rotationAmount);
        }

        private Coordinate getRotatedPixelCoordinate(double angleToRotate) {

//            double rotatedX = Math.cos(Math.toRadians(angleToRotate)) * (pixelCoordinate.getXCoordinate() - centerPointCoordinate.getXCoordinate()) - Math.sin(Math.toRadians(angleToRotate)) * (pixelCoordinate.getYCoordinate()-centerPointCoordinate.getYCoordinate()) + centerPointCoordinate.getXCoordinate();
//            double rotatedY = Math.sin(Math.toRadians(angleToRotate)) * (pixelCoordinate.getXCoordinate() - centerPointCoordinate.getXCoordinate()) + Math.cos(Math.toRadians(angleToRotate)) * (pixelCoordinate.getYCoordinate() - centerPointCoordinate.getYCoordinate()) + centerPointCoordinate.getYCoordinate();
//            return new Coordinate(rotatedX,rotatedY);

            double currentX = pixelCoordinate.getXCoordinate(), currentY = pixelCoordinate.getYCoordinate();
            double centerX = centerPointCoordinate.getXCoordinate(), centerY = centerPointCoordinate.getYCoordinate();
            System.out.println(centerX + " || " + centerY);
            double x = currentX - centerX, y = currentY - centerY;
            int xToMove = (int) (x * Math.cos(Math.toRadians(angleToRotate)) - y * Math.sin(Math.toRadians(angleToRotate)) + centerX);
            int yToMove = (int) (x * Math.sin(Math.toRadians(angleToRotate)) + y * Math.cos(Math.toRadians(angleToRotate)) + centerY);
            return new Coordinate(xToMove, yToMove);
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
