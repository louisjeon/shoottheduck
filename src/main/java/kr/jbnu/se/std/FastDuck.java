package kr.jbnu.se.std;

import java.io.IOException;

public class FastDuck extends Duck {
    private static long timeBetweenObjects;
    private static long lastObjectTime;
    private static int objectLine = 0;
    private static final int[][] objectLines = {
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.60), -20, 40},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.65), -21, 44},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.70), -22, 48},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.78), -23, 52}
                                      };

    public FastDuck(int stage) throws IOException {
        super(stage, objectLines);
    }
}
