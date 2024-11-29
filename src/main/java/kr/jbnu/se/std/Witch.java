package kr.jbnu.se.std;

import java.io.IOException;

public class Witch extends MovingObject {
    private static final long TIME_BETWEEN_OBJECTS = Framework.SEC_IN_NANOSEC;
    private static long lastObjectTime;
    private static int objectLine = 0;
    private static final int[][] objectLines = {
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.10), -20, 200},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.3), -20, 200},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.4), -20, 200},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.5), -20, 200}
                                      };

    public Witch() throws IOException {
        super(GameModel.getWitchImg());
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
