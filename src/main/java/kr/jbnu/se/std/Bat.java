package kr.jbnu.se.std;

public class Bat extends MovingObject {
    private static final long TIME_BETWEEN_OBJECTS = Framework.SEC_IN_NANOSEC * 5;
    private static long lastObjectTime;
    private static int objectLine = 0;
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.20), -3, 50},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.25), -3, 50},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.30), -3, 50},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.35), -3, 50}
    };

    public Bat() {
        super(GameModel.getBatImg());
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

