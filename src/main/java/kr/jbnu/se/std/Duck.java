package kr.jbnu.se.std;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Duck extends MovingObject {
    private static final long TIME_BETWEEN_OBJECTS = Framework.SEC_IN_NANOSEC;
    private static long lastObjectTime;
    private static int objectLine = 0;
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.60), -8, 20},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.65), -9, 30},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.70), -10, 40},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.78), -11, 50}
    };

    public Duck() throws IOException {
        super(GameModel.stage(GameConfig.getStage()).getDuckImg());
        this.x = objectLines[objectLine][0] + this.random.nextInt(200);
        this.y = objectLines[objectLine][1];
        this.speed = objectLines[objectLine][2];
        this.score = objectLines[objectLine][3];
        updateObjectLine();
    }

    protected Duck(BufferedImage image){
        super(image);
        this.x = objectLines[objectLine][0] + this.random.nextInt(200);
        this.y = objectLines[objectLine][1];
        this.speed = -15;
        this.score = 50;
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
