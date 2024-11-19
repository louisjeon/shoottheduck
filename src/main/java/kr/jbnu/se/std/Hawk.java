package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Hawk extends MovingObject {
    private static long lastObjectTime; //public->private
    public static long getLastObjectTime() {
        return lastObjectTime;
    }
    public static void setLastObjectTime(long lastObjectTime) {
        Hawk.lastObjectTime = lastObjectTime;
    }

    /**
     * How much time must pass in order to create a new eagle?
     */
    public static final long TIME_BETWEEN_OBJECTS = Framework.secInNanosec * 3;


    private static int nextObjectLines = 0;
    public static int getNextObjectLines(){
        return nextObjectLines;
    }
    public static void setNextObjectLines(int nextObjectLines){
        Hawk.nextObjectLines = nextObjectLines;
    }
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    private static final int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.20), -17, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -16, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -15, 50},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -14, 50}
                                      };
    public static int[][] getObjectLines() {
        return objectLines.clone();
    }
    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */
    public Hawk(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
