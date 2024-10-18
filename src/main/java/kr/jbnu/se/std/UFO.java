package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class UFO extends MovingObject {
    public static long lastObjectTime;
    /**
     * How much time must pass in order to create a new eagle?
     */
    //public static long timeBetweenObjects = Framework.secInNanosec;

    public static int nextObjectLines = 0;
    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[][] objectLines = {
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -5, 1000},
//                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.18), -10, 1000},
//                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -10, 1000},
//                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.32), -10, 1000}
                                      }; //길이,위치,속도,점수

    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */

    public static final int STOP_POSITION = Framework.frameWidth / 3; // 화면 1/3 지점에서 멈춤
    private boolean stopped = false;

    public UFO(BufferedImage objectImg) {
        super(objectLines, nextObjectLines, objectImg);
    }

    @Override
    public void Update() {
        if (!stopped && x <= STOP_POSITION) {
            stopped = true;
            x = STOP_POSITION;
        } else if (!stopped) {
            super.Update();
        }
    }
}
