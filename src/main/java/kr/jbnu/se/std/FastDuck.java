package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class FastDuck extends Duck {
    public static int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -20, 40},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -21, 44},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -22, 48},
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -23, 52}
                                      };

    public FastDuck(BufferedImage objectImg)
    {
        super(objectImg);
    }
}
