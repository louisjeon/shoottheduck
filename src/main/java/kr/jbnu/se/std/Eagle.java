package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Eagle extends MovingObject {
    public static long lastObjectTime;
    /**
     * How much time must pass in order to create a new eagle?
     */
    public static long timeBetweenObjects = Framework.secInNanosec * 5;

    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.02), -30, 200},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.04), -30, 200},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.06), -30, 200},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.08), -30, 200}
                                      };
    /**
     * Creates new duck.

     * @param objectImg Image of the duck.
     */
    public Eagle(BufferedImage objectImg)
    {
        super(objectLines, objectImg);
    }
}