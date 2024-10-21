package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Crow extends MovingBossObject {
    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.30), -5, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.35), -6, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.40), -7, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.45), -8, 500}
    };

    public Crow(BufferedImage objectImg) {
        super(objectLines,nextObjectLines,objectImg);
        INITIAL_HEALTH = 30;
        this.health = INITIAL_HEALTH;
    }
}