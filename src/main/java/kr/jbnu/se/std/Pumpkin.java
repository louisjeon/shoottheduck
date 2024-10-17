package kr.jbnu.se.std;

import java.awt.image.BufferedImage;

public class Pumpkin extends MovingObject {
    public static int nextObjectLines = 0;
    public static boolean isBoss = true;
    private int pumpkinHealth;

    public static long pumpkinLastObjectTime;
    /**
     * How much time must pass in order to create a new eagle?
     */
    public static long pumpkinTimeBetweenObjects = Framework.secInNanosec * 3;

    public static int[][] objectLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.025), -5, 500},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.1), +5, 500}
    };



    public Pumpkin(BufferedImage pumpkinImg) {
        super(objectLines, nextObjectLines, pumpkinImg);
        pumpkinHealth = 1500;
    }

    // 이거 객체지향적으로 수정할것!
    public int getPumpkinHealth() {
        System.out.println(pumpkinHealth);
        return --pumpkinHealth;
    }
}
