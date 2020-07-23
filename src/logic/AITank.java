package logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class AITank extends Tank {
    TankState tankState;
    Random random;

    public AITank(int health, Coordinate pixelCoordinate, String tankImagePass) {
        super(health, pixelCoordinate, tankImagePass);
        random = new Random();
        tankState = new TankState();
    }

    public TankState getTankState() {
        return tankState;
    }

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
            if (!tankBlasted) {
                int nextMoveH = random.nextInt(2);
                int nexMoveV = random.nextInt(2);
                if (nextMoveH == 0) {
                    if (canMove(pixelCoordinate.getXCoordinate() + (int) Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED
                            , pixelCoordinate.getYCoordinate() - (int) Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED)) {
                        moveUp();
                    } else {
                        moveDown();
                    }
                } else if (nextMoveH == 1) {
                    if (canMove(pixelCoordinate.getXCoordinate() - (int) (Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED)
                            , pixelCoordinate.getYCoordinate() + (int) (Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED))
                    ) {
                        moveDown();
                    } else {
                        moveUp();
                    }
                }
                if (nexMoveV == 1) {
                    if (canRotate()) {
                        rotateClockwise();
                    } else {
                        rotateCounterClockwise();
                    }
                } else {
                    if (canRotate()) {
                        rotateCounterClockwise();
                    } else {
                        rotateClockwise();
                    }
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

        private void moveUp() {
            pixelCoordinate.setXCoordinate(pixelCoordinate.getXCoordinate() + (int) (Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED));
            pixelCoordinate.setYCoordinate(pixelCoordinate.getYCoordinate() - (int) (Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED));
        }

        private void moveDown() {
            pixelCoordinate.setXCoordinate(pixelCoordinate.getXCoordinate() - (int) (Math.sin(angle / 180 * Math.PI) * Constants.TANK_SPEED));
            pixelCoordinate.setYCoordinate(pixelCoordinate.getYCoordinate() + (int) (Math.cos(angle / 180 * Math.PI) * Constants.TANK_SPEED));
        }

        private void moveRight() {

        }

        private void moveLeft() {

        }

        private boolean canMove(double finalX, double finalY) {
            return !TankTroubleMap.checkOverlapWithAllPrizes(new Coordinate(finalX, finalY), Constants.TANK_SIZE, Constants.TANK_SIZE, (int) angle);
        }

        private boolean canRotate() {
            return true;
        }

        private void rotateClockwise() {
            angle -= Constants.TANK_ROTATION_SPEED;
        }

        private void rotateCounterClockwise() {
            angle += Constants.TANK_ROTATION_SPEED;
        }
    }
}
