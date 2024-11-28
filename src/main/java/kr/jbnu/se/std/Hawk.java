package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Hawk extends MovingObject {
    private static int[][] objectLines = {
                                       {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.20), -17, 50},
                                       {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.25), -16, 50},
                                       {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.30), -15, 50},
                                       {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.35), -14, 50}
                                      };
    public Hawk(BufferedImage objectImg)
    {
        super(objectImg);
    }
}
