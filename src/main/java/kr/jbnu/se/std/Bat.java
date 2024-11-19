package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Bat extends MovingObject {
    private static long lastObjectTime; //public->private
    public static long getLastObjectTime() {
        return lastObjectTime;
    }
    public static void setLastObjectTime(long lastObjectTime) {
        Bat.lastObjectTime = lastObjectTime;
    }

    /**
     * How much time must pass in order to create a new eagle?
     */
    public static final long TIME_BETWEEN_OBJECTS = Framework.secInNanosec * 5;

    private static int nextObjectLines = 0;
    public static int getNextObjectLines(){
        return nextObjectLines;
    }
    public static void setNextObjectLines(int nextObjectLines){
        Bat.nextObjectLines = nextObjectLines;
    }
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    protected static final int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.20), -3, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -3, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -3, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -3, 50}
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
