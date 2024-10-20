package kr.jbnu.se.std;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import static kr.jbnu.se.std.Game.gunDamage;

public class MovingBossObject {
    protected static int STOP_POSITION;
    protected boolean stopped;
    protected int health; // 체력 추가
    protected int INITIAL_HEALTH;
    public int score;
    public int x;
    public int y;
    public static Random random;
    protected BufferedImage objectImg;
    protected int speed;
    private static final int HEALTH_BAR_WIDTH = 50;
    private static final int HEALTH_BAR_HEIGHT = 5;

    public MovingBossObject(int[][] objectLines,int nextObjectLines,BufferedImage objectImg)
    {
        random = new Random();
        stopped = false;
        this.objectImg = objectImg;
        this.x = objectLines[nextObjectLines][0] + random.nextInt(200);
        this.y = objectLines[nextObjectLines][1];
        this.speed = objectLines[nextObjectLines][2];
        this.score = objectLines[nextObjectLines][3];
        STOP_POSITION = Framework.frameWidth / 2 - objectImg.getWidth() / 2;
        INITIAL_HEALTH = 100;
    }

    public void Update() {
        if (!stopped && x <= STOP_POSITION) {
            stopped = true;
            x = STOP_POSITION;
        } else if (!stopped) {
            x += speed;
        }
    }

    public boolean hit(Game.GunTypes gunType) {
        health-= gunDamage.get(gunType);
        return health <= 0;
    }

    public void drawHealthBar(Graphics2D g2d) {
        int healthBarY = y - 15;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(x, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        if (health > 0.7 * INITIAL_HEALTH) {
            g2d.setColor(Color.GREEN);
        } else if (health > 0.3 * INITIAL_HEALTH) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }

        g2d.fillRect(x, healthBarY, (int) (HEALTH_BAR_WIDTH * (double)health/(double)INITIAL_HEALTH), HEALTH_BAR_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(objectImg, x, y, null);
        drawHealthBar(g2d);
    }
}
