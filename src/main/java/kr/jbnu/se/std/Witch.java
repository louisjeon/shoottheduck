package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class Witch extends MovingObject {
    private static long lastObjectTime; //public->private
    public static long getLastObjectTime() {
        return lastObjectTime;
    }
    public static void setLastObjectTime(long lastObjectTime) {
        Witch.lastObjectTime = lastObjectTime;
    }
    /**
     * How much time must pass in order to create a new eagle?
     */
    public static final long TIME_BETWEEN_OBJECTS = Framework.secInNanosec * 3;

    public static long getTimeBetweenObjects() {
        return TIME_BETWEEN_OBJECTS;
    }

    private static int nextObjectLines = 0;
    public static int getNextObjectLines(){
        return nextObjectLines;
    }
    public static void setNextObjectLines(int nextObjectLines){
        Witch.nextObjectLines = nextObjectLines;
    }
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */

    private static final int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.10), -20, 200},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.3), -20, 200},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.4), -20, 200},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.5), -20, 200}
    };
    public static int[][] getObjectLines() {
        return objectLines.clone();
    }
    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */
    public Witch(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
