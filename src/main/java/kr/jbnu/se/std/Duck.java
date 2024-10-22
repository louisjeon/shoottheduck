package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Duck extends MovingObject {
    public static long lastObjectTime;
    public static long timeBetweenObjects = Framework.secInNanosec / 2;

    public static int nextObjectLines = 0;

    public static int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -2, 20},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -3, 30},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -4, 40},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -5, 50}
                                      };

    public Duck(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
