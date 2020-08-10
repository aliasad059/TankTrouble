package Network;

import java.io.Serializable;

/**
 * client configs are used in network mode to sync data of the game between the tanks
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class ClientConfigs implements Serializable {
    private final int groupNumber, tankHealth, bulletDamage, DWallHealth;

    /**
     * client configs are used in network mode to sync data of the game between the tanks
     *
     * @param groupNumber  group number of the tank
     * @param tankHealth   health of the tank
     * @param bulletDamage bullet damage of the tank
     * @param DWallHealth  destructible wall health
     */
    public ClientConfigs(int groupNumber, int tankHealth, int bulletDamage, int DWallHealth) {
        this.groupNumber = groupNumber;
        this.tankHealth = tankHealth;
        this.bulletDamage = bulletDamage;
        this.DWallHealth = DWallHealth;
    }

    /**
     * getter of the tank's group number
     *
     * @return tank's group number
     */
    public int getGroupNumber() {
        return groupNumber;
    }

    /**
     * getter of the tank's health
     *
     * @return tank's health
     */
    public int getTankHealth() {
        return tankHealth;
    }

    /**
     * getter of the tank's bullet damage
     *
     * @return tank's group bullet damage
     */
    public int getBulletDamage() {
        return bulletDamage;
    }

    /**
     * getter of the health of destructible walls of the map
     *
     * @return health of destructible walls of the map
     */
    public int getDWallHealth() {
        return DWallHealth;
    }
}
