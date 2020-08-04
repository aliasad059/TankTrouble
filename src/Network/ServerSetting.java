package Network;

import java.io.Serializable;

public class ServerSetting implements Serializable {
    private int tankHealth;
    private int wallHealth;
    private int bulletDamage;

    public ServerSetting(int tankHealth, int wallHealth, int bulletDamage) {
        this.tankHealth = tankHealth;
        this.wallHealth = wallHealth;
        this.bulletDamage = bulletDamage;
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public void setTankHealth(int tankHealth) {
        this.tankHealth = tankHealth;
    }

    public int getWallHealth() {
        return wallHealth;
    }

    public void setWallHealth(int wallHealth) {
        this.wallHealth = wallHealth;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }
}
