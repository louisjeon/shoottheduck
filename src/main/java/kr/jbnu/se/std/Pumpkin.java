package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Pumpkin extends MovingBossObject {
    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.10), -2, 2500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.15), -3, 2500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.20), -4, 2500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.25), -5, 2500}
    }; // 초기 체력 설정

    public Pumpkin(BufferedImage objectImg) {
        super(objectLines,nextObjectLines,objectImg);
        INITIAL_HEALTH = 300;
        this.health = INITIAL_HEALTH;
    }
}