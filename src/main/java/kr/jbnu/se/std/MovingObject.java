package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The duck class.
 *
 * @author www.gametutorial.net
 */

public abstract class MovingObject {

    /**
     * kr.jbnu.se.std.Duck lines.
     * Where is starting location for the duck?
     * Speed of the duck?
     * How many points is a duck worth?
     */
    public static int[][] objectLines;
    /**
     * Indicate which is next duck line.
     */
    public static int nextObjectLines;


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
    private final int speed;

    /**
     * How many points this duck is worth?
     */
    public int score;

    /**
     * kr.jbnu.se.std.Duck image.
     */
    private final BufferedImage objectImg;


    /**
     * Creates new duck.
     *
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param speed The speed of this duck.
     * @param score How many points this duck is worth?
     * @param objectImg Image of the duck.
     */
    public MovingObject(int x, int y, int speed, int score, BufferedImage objectImg)
    {
        this.x = x;
        this.y = y;

        this.speed = speed;

        this.score = score;

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
