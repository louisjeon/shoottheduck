package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Eagle extends MovingBossObject {
    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -17, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -16, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.40), -15, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.45), -14, 500}
    };

    public Eagle(BufferedImage objectImg) {
        super(objectLines,nextObjectLines,objectImg);
        INITIAL_HEALTH = 50;
        this.health = INITIAL_HEALTH;
    }
}