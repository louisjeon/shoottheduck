package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Pumpkin extends MovingBossObject {
    public static final int INITIAL_HEALTH = 100;
    public static int nextObjectLines = 0;
    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -17, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -16, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.40), -15, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.45), -14, 500}
    }; // 초기 체력 설정

    public Pumpkin(BufferedImage objectImg) {
        super(objectLines,nextObjectLines,objectImg);
        this.health = INITIAL_HEALTH;
    }
}