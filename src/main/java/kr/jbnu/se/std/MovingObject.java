package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class MovingObject {
    protected int x;
    protected int y;
    protected  int speed;
    protected final BufferedImage objectImg;
    protected int score;
    protected Random random = new Random();

    protected MovingObject(BufferedImage objectImg)
    {
        this.objectImg = objectImg;
    }

    public void update()
    {
        x += speed;
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

    public void draw(Graphics2D g2d)
    {
        g2d.drawImage(objectImg, x, y, null);
    }
}
