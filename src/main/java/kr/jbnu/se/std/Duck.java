package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Duck extends MovingObject {
    public static long lastObjectTime;
    /**
     * How much time must pass in order to create a new duck?
     */
    public static long timeBetweenObjects = Framework.secInNanosec / 2;

    public static int nextObjectLines = 0;
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -2, 20},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -3, 30},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -4, 40},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -5, 50}
                                      };
    /**
     * Creates new duck.
     *
     * @param objectImg Image of the duck.
     */
    public Duck(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
