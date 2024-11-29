package kr.jbnu.se.std;

import java.io.IOException;

public class Hawk extends MovingObject {
    private static final long TIME_BETWEEN_OBJECTS = Framework.SEC_IN_NANOSEC;
    private static long lastObjectTime;
    private static int objectLine = 0;
    private static final int[][] objectLines = {
            {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.20), -17, 50},
            {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.25), -16, 50},
            {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.30), -15, 50},
            {Framework.getFrameWidth() , (int)(Framework.getFrameHeight() * 0.35), -14, 50}
    };

    public Hawk() throws IOException {
        super(GameModel.stage(GameConfig.getStage()).getDuckImg());
        this.x = objectLines[objectLine][0] + this.random.nextInt(200);
        this.y = objectLines[objectLine][1];
        this.speed = objectLines[objectLine][2];
        this.score = objectLines[objectLine][3];
        updateObjectLine();
    }

    public static long getLastObjectTime() {
        return lastObjectTime;
    }

    public static void setLastObjectTime(long time) {
        lastObjectTime = time;
    }

    private static void updateObjectLine() {
        if (objectLine < 3) {
            objectLine++;
        } else {
            objectLine = 0;
        }
    }

    public static long getTimeBetweenObjects() {
        return TIME_BETWEEN_OBJECTS;
    }
}
