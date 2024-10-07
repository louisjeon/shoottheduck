package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Crow extends MovingObject {
    public static long lastObjectTime;
    /**
     * How much time must pass in order to create a new eagle?
     */
    public static long timeBetweenObjects = Framework.secInNanosec;

    public static int nextObjectLines = 0;
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.20), -5, 24},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -7, 24},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -10, 24},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -8, 24}
                                      };
    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */
    public Crow(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
