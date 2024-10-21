package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class MovingObject {
    public static Random random;
    public static long timeBetweenObjects;
    public int x;
    public int y;
    public static long lastObjectTime;
    protected int speed;
    public int score;
    protected BufferedImage objectImg;

    public MovingObject(int[][] objectLines,int nextObjectLines, BufferedImage objectImg)
    {
        random = new Random();
        this.x = objectLines[nextObjectLines][0] + random.nextInt(200);
        this.y = objectLines[nextObjectLines][1];

        this.speed = objectLines[nextObjectLines][2];

        this.score = objectLines[nextObjectLines][3];

        this.objectImg = objectImg;
    }

    public void Update()
    {
        x += speed;
    }

    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(objectImg, x, y, null);
    }
}
