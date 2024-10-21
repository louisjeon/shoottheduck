package kr.jbnu.se.std;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;
import static kr.jbnu.se.std.Game.gunDamage;

public class MovingBossObject {
    protected static int FLIP_POSITION1;
    protected static int FLIP_POSITION2;
    protected boolean flipped;
    protected int health; // 체력 추가
    protected int INITIAL_HEALTH;
    public int score;
    public int x;
    public int y;
    public static Random random;
    protected BufferedImage objectImg;
    protected  BufferedImage correctImg;
    protected BufferedImage flippedImg;
    protected int speed;
    private static final int HEALTH_BAR_WIDTH = 100;
    private static final int HEALTH_BAR_HEIGHT = 10;
    public int width;
    public int height;
    public String soundName;
    public static int nextObjectLines = 0;

    public MovingBossObject(int[][] objectLines,int nextObjectLines,BufferedImage objectImg)
    {
        random = new Random();
        flipped = false;
        correctImg = objectImg;
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-objectImg.getWidth(null),0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        flippedImg = op.filter(objectImg, null);
        this.objectImg = correctImg;
        this.x = objectLines[nextObjectLines][0] + random.nextInt(200);
        this.y = objectLines[nextObjectLines][1];
        this.speed = objectLines[nextObjectLines][2];
        this.score = objectLines[nextObjectLines][3];
        INITIAL_HEALTH = 100;
        width = objectImg.getWidth();
        height = objectImg.getHeight();
        FLIP_POSITION1 = 0;
        FLIP_POSITION2 = 1200 - width;
    }

    public void Update() {
        if (!flipped) {
            x += speed;
            if (x <= FLIP_POSITION1) {
                flipped = true;
                objectImg = flippedImg;
            }
        } else {
            x -= speed;
            if (x >= FLIP_POSITION2) {
                flipped = false;
                objectImg = correctImg;
            }
        }
    }

    public boolean hit(Game.GunTypes gunType) {
        health-= gunDamage.get(gunType);
        return health <= 0;
    }

    public void drawHealthBar(Graphics2D g2d) {
        int healthBarY = y - 15;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(x + objectImg.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        if (health > 0.7 * INITIAL_HEALTH) {
            g2d.setColor(Color.GREEN);
        } else if (health > 0.3 * INITIAL_HEALTH) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }

        g2d.fillRect(x + objectImg.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, healthBarY, (int) (HEALTH_BAR_WIDTH * (double)health/(double)INITIAL_HEALTH), HEALTH_BAR_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + objectImg.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(objectImg, x, y, null);
        drawHealthBar(g2d);
    }
}
