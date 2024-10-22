package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class PotionDuck extends MovingObject {
    public static long lastObjectTime;
    public static long timeBetweenObjects = Framework.secInNanosec * 10;

    public static int nextObjectLines = 0;

    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -10, 50},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -10, 50},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -10, 50},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -10, 50}
    };

    public PotionDuck(BufferedImage objectImg)
    {
        super(objectLines, nextObjectLines, objectImg);
    }
}
