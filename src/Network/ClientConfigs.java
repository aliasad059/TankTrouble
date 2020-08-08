package Network;

import java.io.Serializable;

public class ClientConfigs implements Serializable {
    private final int groupNumber, tankHealth, bulletDamage, DWallHealth;

    public ClientConfigs(int groupNumber, int tankHealth, int bulletDamage, int DWallHealth) {
        this.groupNumber = groupNumber;
        this.tankHealth = tankHealth;
        this.bulletDamage = bulletDamage;
        this.DWallHealth = DWallHealth;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public int getTankHealth() {
        return tankHealth;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public int getDWallHealth() {
        return DWallHealth;
    }
}
