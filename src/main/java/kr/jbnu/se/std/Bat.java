package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Bat extends MovingObject {
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
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.13), -13, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.18), -15, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -20, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.32), -12, 50}
                                      };
    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */
    public Bat(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
