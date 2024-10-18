package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 * 
 * @author www.gametutorial.net
 */

public class UFO extends MovingObject {
    public int ufo_strength=10000;
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
                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -25, 1000},
//                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.18), -10, 1000},
//                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -10, 1000},
//                                       {Framework.frameWidth, (int)(Framework.frameHeight * 0.32), -10, 1000}
                                      }; //길이,위치,속도,점수

    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */

    public static final int STOP_POSITION = Framework.frameWidth / 3;
    private boolean stopped = false;
    private int health; // 체력 추가
    public static final int INITIAL_HEALTH = 5; // 초기 체력 설정

    public UFO(BufferedImage objectImg) {
        super(objectLines, nextObjectLines, objectImg);
        this.health = INITIAL_HEALTH; // 초기 체력 설정
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

    public boolean hit() {
        health--;
        return health <= 0;
    }

    private static final int HEALTH_BAR_WIDTH = 50;
    private static final int HEALTH_BAR_HEIGHT = 5;

    public void drawHealthBar(Graphics2D g2d) {
        int healthBarY = y - 15; // UFO 위에 체력바 위치

        // 체력바 배경 (회색)
        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        // 현재 체력에 따라 색상 변경
        if (health > 0.7 * INITIAL_HEALTH) {
            g2d.setColor(Color.GREEN);
        } else if (health > 0.3 * INITIAL_HEALTH) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }

        int currentWidth = (int)((health / (float)INITIAL_HEALTH) * HEALTH_BAR_WIDTH);
        g2d.fillRect(x, healthBarY, currentWidth, HEALTH_BAR_HEIGHT);

        // 체력바 테두리
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    @Override
    public void Draw(Graphics2D g2d) {
        super.Draw(g2d);
        drawHealthBar(g2d);
    }
}
