package kr.jbnu.se.std;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MovingBossObject {
    protected boolean flipped;
    protected int health; // 체력 추가
    protected final int initialHealth;
    private static int score;
    private int x;
    private final int y;
    protected BufferedImage objectImg;
    protected  BufferedImage correctImg;
    protected BufferedImage flippedImg;
    protected int speed;
    private static final int HEALTH_BAR_WIDTH = 100;
    private static final int HEALTH_BAR_HEIGHT = 10;
    private final int width;
    private final int height;
    private int objectLine = 0;
    protected static int flipPosition1;
    protected static int flipPosition2;

    public MovingBossObject(int[][] objectLines, int health, BufferedImage objectImg)
    {
        Random random = new Random();
        flipped = false;
        correctImg = objectImg;
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-objectImg.getWidth(null),0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        flippedImg = op.filter(objectImg, null);
        this.objectImg = correctImg;
        this.x = objectLines[objectLine][0] + random.nextInt(200);
        this.y = objectLines[objectLine][1];
        this.speed = objectLines[objectLine][2];
        score = objectLines[objectLine][3];
        initialHealth = health;
        this.health = health;
        width = objectImg.getWidth();
        height = objectImg.getHeight();
        flipPosition1 = 0;
        flipPosition2 = 1200 - width;
        updateObjectLine();
    }

    public void update() {
        if (!flipped) {
            x += speed;
            if (x <= flipPosition1) {
                flipped = true;
                objectImg = flippedImg;
            }
        } else {
            x -= speed;
            if (x >= flipPosition2) {
                flipped = false;
                objectImg = correctImg;
            }
        }
    }

    private void updateObjectLine() {
        if (objectLine < 3) {
            objectLine++;
        } else {
            objectLine = 0;
        }
    }

    public boolean hit(GameConfig.GunType gunType) {
        health-= Guns.getDamage(gunType);
        return health <= 0;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void drawHealthBar(Graphics2D g2d) {
        int healthBarY = y - 15;

        g2d.setColor(Color.GRAY);
        g2d.fillRect(x + objectImg.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);

        if (health > 0.7 * initialHealth) {
            g2d.setColor(Color.GREEN);
        } else if (health > 0.3 * initialHealth) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }

        g2d.fillRect(x + objectImg.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, healthBarY, (int) (HEALTH_BAR_WIDTH * (double)health/(double)initialHealth), HEALTH_BAR_HEIGHT);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(x + objectImg.getWidth() / 2 - HEALTH_BAR_WIDTH / 2, healthBarY, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT);
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(objectImg, x, y, null);
        drawHealthBar(g2d);
    }
}
