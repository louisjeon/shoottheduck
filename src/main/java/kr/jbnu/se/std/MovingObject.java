package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * The duck class.
 *
 * @author www.gametutorial.net
 */

public abstract class MovingObject {
    /**
     * Indicate which is next duck line.
     */
    public static int nextObjectLines = 0;
    public static long timeBetweenObjects;
    public static Random random;

    /**
     * X coordinate of the duck.
     */
    public int x;
    /**
     * Y coordinate of the duck.
     */
    public int y;

    /**
     * How fast the duck should move? And to which direction?
     */
    int speed;

    /**
     * How many points this duck is worth?
     */
    public int score;

    /**
     * kr.jbnu.se.std.Duck image.
     */
    BufferedImage objectImg;

    /**
     * Creates new duck.
     * @param objectImg Image of the duck.
     */
    public MovingObject(int[][] objectLines, BufferedImage objectImg)
    {
        random = new Random();
        this.x = objectLines[nextObjectLines][0] + random.nextInt(200);
        this.y = objectLines[nextObjectLines][1];

        this.speed = objectLines[nextObjectLines][2];

        this.score = objectLines[nextObjectLines][3];

        this.objectImg = objectImg;
    }


    /**
     * Move the duck.
     */
    public void Update()
    {
        x += speed;
    }

    /**
     * Draw the duck to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(objectImg, x, y, null);
    }
}
