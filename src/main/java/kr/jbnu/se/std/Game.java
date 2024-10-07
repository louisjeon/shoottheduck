package kr.jbnu.se.std;

import java.awt.*;
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
import javax.swing.*;

/**
 * Actual game.
 * 
 * @author www.gametutorial.net
 */

public abstract class Game {
    
    /**
     * We use this to generate a random number.
     */
    protected static Random random;
    
    /**
     * Font that we will use to write statistic to the screen.
     */
    private static Font font;
    
    /**
     * How many ducks leave the screen alive?
     */
    protected static int runawayObjects;
    
   /**
     * How many ducks the player killed?
     */
   protected static int killedObjects;
    
    /**
     * For each killed duck, the player gets points.
     */
    protected static int score;
    
   /**
     * How many times a player is shot?
     */
   protected static int shoots;

   protected static int feverCnt;
   protected static int feverImgNum;
    
    /**
     * Last time of the shoot.
     */
    protected static long lastTimeShoot;
    /**
     * The time which must elapse between shots.
     */
    protected static long timeBetweenShots;

    /**
     * kr.jbnu.se.std.Game background image.
     */
    protected static BufferedImage backgroundImg;
    
    /**
     * Bottom grass.
     */
    protected static BufferedImage grassImg;
    
    /**
     * kr.jbnu.se.std.Duck image.
     */
    
    /**
     * Shotgun sight image.
     */
    protected static BufferedImage sightImg;
    protected static BufferedImage feverBarImg;

    protected static Image feverFireGif;

    /**
     * Middle width of the sight image.
     */
    protected static int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    protected static int sightImgMiddleHeight;
    

    public Game()
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
    protected void Initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);

        runawayObjects = 0;
        killedObjects = 0;
        score = 0;
        shoots = 0;
        feverCnt = 0;

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
    };
    
    /**
     * Load game files - images, sounds, ...
     */
    protected void LoadContent()
    {
        try
        {
            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));
            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(Objects.requireNonNull(sightImgUrl));
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
            URL feverBarImgUrl = this.getClass().getResource("/images/fever0.png");
            feverBarImg = ImageIO.read(Objects.requireNonNull(feverBarImgUrl));
            feverFireGif = null;
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Restart game - reset some variables.
     */
    public void RestartGame() {
        runawayObjects = 0;
        killedObjects = 0;
        score = 0;
        shoots = 0;
        lastTimeShoot = 0;
        feverCnt = 0;
    };
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public abstract void UpdateGame(long gameTime, Point mousePosition)throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException ;
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     */
    protected void DrawBack(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
    };

    protected void DrawFront(Graphics2D g2d, Point mousePosition) {
        g2d.drawString("RUNAWAY: " + runawayObjects, 10, 21);
        g2d.drawString("KILLS: " + killedObjects, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        g2d.drawImage(feverBarImg, Framework.frameWidth - feverBarImg.getWidth(), 0, null);
        if (feverFireGif != null) {
            g2d.drawImage(feverFireGif, Framework.frameWidth - feverFireGif.getWidth(null) - 430 + Math.min(feverCnt, 10) * 44, -10, null);
        }
    }

    protected void DrawFever() throws IOException {
        feverImgNum = Math.min(feverCnt, 10);
        URL feverImgUrl = this.getClass().getResource("/images/fever" + feverImgNum + ".png");
        feverBarImg = ImageIO.read(Objects.requireNonNull(feverImgUrl));
        if (feverCnt >= 7) {
            URL feverFireGifUrl = this.getClass().getResource("/images/blue_fire.gif");
            feverFireGif = new ImageIcon(Objects.requireNonNull(feverFireGifUrl)).getImage();
        } else if (feverCnt >= 5) {
            URL feverFireGifUrl = this.getClass().getResource("/images/red_fire.gif");
            feverFireGif = new ImageIcon(Objects.requireNonNull(feverFireGifUrl)).getImage();
        } else if (feverCnt >= 3) {
            URL feverFireGifUrl = this.getClass().getResource("/images/yellow_fire.gif");
            feverFireGif = new ImageIcon(Objects.requireNonNull(feverFireGifUrl)).getImage();
        } else {
            feverFireGif = null;
        }
    }

    public void Draw(Graphics2D g2d, Point mousePosition) {
        DrawBack(g2d);
        DrawFront(g2d, mousePosition);
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
