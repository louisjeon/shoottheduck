package kr.jbnu.se.std;

import java.io.IOException;

public class PotionDuck extends Duck {
    private static final long TIME_BETWEEN_OBJECTS = Framework.SEC_IN_NANOSEC * 10;
    private static long lastObjectTime;

    public PotionDuck() throws IOException
    {
        super(GameModel.stage(GameConfig.getStage()).getPotionDuckImg());
    }

    public static long getLastObjectTime() {
        return lastObjectTime;
    }

    public static void setLastObjectTime(long time) {
        lastObjectTime = time;
    }

    public static long getTimeBetweenObjects() {
        return TIME_BETWEEN_OBJECTS;
    }
}