package logic;

import logic.Player.UserPlayer;

import java.awt.*;

/**
 * This class represent topography of ground of map game include different kind of rocks and trees.
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class Topography {
    private Image image;
    private Coordinate centerPoint;

    /**
     * This constructor just fii fields with input parameters.
     *
     * @param image       is image of topography
     * @param centerPoint is center point of its image in the map
     */
    public Topography(Image image, Coordinate centerPoint) {
        this.image = image;
        this.centerPoint = new Coordinate();
        this.centerPoint = centerPoint;
    }

    /**
     * Getter method of image field
     *
     * @return image of this topography
     */
    public Image getImage() {
        return image;
    }

    /**
     * Getter method of centerPoint field
     *
     * @return a coordinate as center point of topography
     */
    public Coordinate getCenterPoint() {
        return centerPoint;
    }
}
