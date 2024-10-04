package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;

/**
 * Actual game.
 *
 * @author www.gametutorial.net
 */

public class Stage1 extends Game {

    /**
     * We use this to generate a random number.
     */
    private Random random;

    /**
     * Font that we will use to write statistic to the screen.
     */
    private Font font;

    /**
     * Array list of the ducks.
     */
    private ArrayList<MovingObject> movingObjects;

    /**
     * How many ducks leave the screen alive?
     */
    private int runawayObjects;

    /**
     * How many ducks the player killed?
     */
    private int killedObjects;

    /**
     * For each killed duck, the player gets points.
     */
    private int score;

    /**
     * How many times a player is shot?
     */
    private int shoots;

    /**
     * Last time of the shoot.
     */
    private long lastTimeShoot;
    /**
     * The time which must elapse between shots.
     */
    private long timeBetweenShots;

    /**
     * kr.jbnu.se.std.Game background image.
     */
    private BufferedImage backgroundImg;

    /**
     * Bottom grass.
     */
    private BufferedImage grassImg;

    /**
     * kr.jbnu.se.std.Duck image.
     */
    private BufferedImage movingObjectImg;

    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;

    /**
     * Middle width of the sight image.
     */
    private int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    private int sightImgMiddleHeight;


    public Stage1()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }


    /**
     * Set variables and objects for the game.
     */
    private void Initialize()
    {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);

        movingObjects = new ArrayList<MovingObject>();

        runawayObjects = 0;
        killedObjects = 0;
        score = 0;
        shoots = 0;

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
    }

    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);

            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);

            URL objectImgUrl = this.getClass().getResource("/images/duck.png");
            movingObjectImg = ImageIO.read(objectImgUrl);

            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        // Removes all of the ducks from this list.
        movingObjects.clear();

        // We set last duckt time to zero.
        MovingObject.lastObjectTime = 0;

        runawayObjects = 0;
        killedObjects = 0;
        score = 0;
        shoots = 0;

        lastTimeShoot = 0;
    }


    /**
     * Update game logic.
     *
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - MovingObject.lastObjectTime >= MovingObject.timeBetweenObjects)
        {
            // Here we create new duck and add it to the array list.
            movingObjects.add(new Duck(Duck.objectLines[Duck.nextObjectLines][0] + random.nextInt(200), Duck.objectLines[Duck.nextObjectLines][1], Duck.objectLines[Duck.nextObjectLines][2], Duck.objectLines[Duck.nextObjectLines][3], movingObjectImg));

            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextObjectLines++;
            if(Duck.nextObjectLines >= Duck.objectLines.length)
                Duck.nextObjectLines = 0;

            Duck.lastObjectTime = System.nanoTime();
        }

        // Update all of the ducks.
        for(int i = 0; i < movingObjects.size(); i++)
        {
            // Move the duck.
            movingObjects.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(movingObjects.get(i).x < 0 - movingObjectImg.getWidth())
            {
                movingObjects.remove(i);
                runawayObjects++;
            }
        }

        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
//                String soundName = "src/main/resources/audio/gunshot_1.mp3";
//                String path = new File(soundName).getAbsoluteFile().getPath();
//                System.out.println(path);
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
//                Clip clip = AudioSystem.getClip();
//                clip.open(audioInputStream);
//                clip.start();

//                String soundName = "src/main/resources/audio/gunshot_1.mp3";
//                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
//                DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
//                Clip clip = (Clip) AudioSystem.getLine(info);
//                clip.open(ais);
//                clip.start();
//                Thread.sleep(1000); // plays up to 6 seconds of sound before exiting
//                clip.close();

                shoots++;

                // We go over all the ducks and we look if any of them was shoot.
                for(int i = 0; i < movingObjects.size(); i++)
                {
                    // We check, if the mouse was over ducks head or body, when player has shot.
                    if(new Rectangle(movingObjects.get(i).x + 18, movingObjects.get(i).y     , 27, 30).contains(mousePosition) ||
                            new Rectangle(movingObjects.get(i).x + 30, movingObjects.get(i).y + 30, 100, 35).contains(mousePosition))
                    {
                        killedObjects++;
                        score += movingObjects.get(i).score;

                        // Remove the duck from the array list.
                        movingObjects.remove(i);

                        // We found the duck that player shoot so we can leave the for loop.
                        break;
                    }
                }

                lastTimeShoot = System.nanoTime();
            }
        }

        // When 200 ducks runaway, the game ends.
        if(runawayObjects >= 200)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }

    /**
     * Draw the game to the screen.
     *
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        // Here we draw all the ducks.
        for(int i = 0; i < movingObjects.size(); i++)
        {
            movingObjects.get(i).Draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);

        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("RUNAWAY: " + runawayObjects, 10, 21);
        g2d.drawString("KILLS: " + killedObjects, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
    }


    /**
     * Draw the game over screen.
     *
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);

        // The first text is used for shade.
        g2d.setColor(Color.black);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }
}
