public class Tank {
    private boolean isProtected;
    private int health;
    private Coordinate coordinate;
    private int size;
    private final int tankNumber;
    private String name, color;
    private Bullets lastBullet ;
    private int bulletPower;
    private String bulletType ;
    private int lastGiftType;
    private Player player;

    public Tank(int tankNumber, int health, int bulletPower, int x, int y, int size, Player player) {
        this.tankNumber = tankNumber;
        this.health = health;
        this.bulletPower = bulletPower;
        this.coordinate = new Coordinate(x,y);
        this.size = size;
        this.player = player;
        this.isProtected = false;
        this.name = player.getName;
        this.color = player.getColor;
        this.bulletType = "NORMAL";
        lastGiftType = 0;
    }

    public void eatGift(int xOfGift, int yOfGift) {
        if (lastGiftType != 0) {
            System.out.println("You haven't used your last gift!");
        } else {
            lastGiftType = Interface.getTankTroubleMap().getArrayMap()[xOfGift][yOfGift] - 2;
        }
    }

    public void useGift() {
        //protector
        if (lastGiftType == 1) {
            protectTank(15);
        }
        //laser
        else if (lastGiftType == 2) {
            bulletType = "LASER";
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                        bulletType = "NORMAL";
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        //increase health by 10%
        else if (lastGiftType == 3) {
            increaseHealth(1.1);
        }
        // 2X bullet power
        else if (lastGiftType == 4) {
            increaseBulletPower(2);
        }
        // 3X bullet power
        else if (lastGiftType == 5) {
            increaseBulletPower(3);
        } else {
            System.out.println("You have no gift to use");
        }
        lastGiftType = 0;
    }

    private void increaseBulletPower(int howManyTimes) {
        bulletPower *= howManyTimes;
    }

    private void increaseHealth(double howManyTimes) {
        health *= howManyTimes;
    }

    private void protectTank(int protectionTime) {
        isProtected = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(protectionTime * 1000);
                    isProtected = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void fire() {
        if (lastBullet.getFiredTime >= 2) {
            //TODO:maybe the bullet coordinate should be different from the tank
            Bullets bulletToFire = new Bullets(bulletPower, bulletType,coordinate.getXCoordinate(),coordinate.getYCoordinate());
            lastBullet = bulletToFire;
            lastBullet.move();
        }
    }

    public void move(String command) {
        if (command.equals("RIGHT")) {
            if (coordinate.getXCoordinate() < Interface.getTankTroubleMap().getxAxisSize()) {
            }
        } else if (command.equals("LEFT")) {
            if (coordinate.getXCoordinate() != 0) {

            }
        } else if (command.equals("UP")) {
            if (coordinate.getYCoordinate() != 0) {

            }
        } else if (command.equals("DOWN")) {
            if (coordinate.getYCoordinate() < Interface.getTankTroubleMap().getyAxisSize()) {

            }
        }
    }

    public void getDamage(int damageAmount) {
        if (!isProtected) {
            health -= damageAmount;
        } else System.out.println("Bilakh!");
    }
}
