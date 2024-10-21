package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class BossDuck extends MovingBossObject {
    public static final int INITIAL_HEALTH = 100;
    public static int nextObjectLines = 0;
    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -3, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -4, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.40), -5, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.45), -6, 500}
    }; // 초기 체력 설정

    public BossDuck(BufferedImage objectImg) {
        super(objectLines,nextObjectLines,objectImg);
        this.health = INITIAL_HEALTH;
        this.soundName = "quack";
    }
}