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

public abstract class Game {
    
    /**
     * We use this to generate a random number.
     */
    Random random;
    
    /**
     * Font that we will use to write statistic to the screen.
     */
    Font font;
    
    /**
     * How many ducks leave the screen alive?
     */
    int runawayObjects;
    
   /**
     * How many ducks the player killed?
     */
   int killedObjects;
    
    /**
     * For each killed duck, the player gets points.
     */
    int score;
    
   /**
     * How many times a player is shot?
     */
   int shoots;
    
    /**
     * Last time of the shoot.
     */
    long lastTimeShoot;
    /**
     * The time which must elapse between shots.
     */
    long timeBetweenShots;

    /**
     * kr.jbnu.se.std.Game background image.
     */
    BufferedImage backgroundImg;
    
    /**
     * Bottom grass.
     */
    BufferedImage grassImg;
    
    /**
     * kr.jbnu.se.std.Duck image.
     */
    
    /**
     * Shotgun sight image.
     */
    BufferedImage sightImg;
    
    /**
     * Middle width of the sight image.
     */
    int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    int sightImgMiddleHeight;
    

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
        this.random = new Random();
        this.font = new Font("monospaced", Font.BOLD, 18);

        this.runawayObjects = 0;
        this.killedObjects = 0;
        this.score = 0;
        this.shoots = 0;

        this.lastTimeShoot = 0;
        this.timeBetweenShots = Framework.secInNanosec / 3;
    };
    
    /**
     * Load game files - images, sounds, ...
     */
    protected void LoadContent()
    {
        try
        {
            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            this.grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));
            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            this.sightImg = ImageIO.read(Objects.requireNonNull(sightImgUrl));
            this.sightImgMiddleWidth = sightImg.getWidth() / 2;
            this.sightImgMiddleHeight = sightImg.getHeight() / 2;
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
    public void DrawBack(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
    };

    public void DrawFront(Graphics2D g2d, Point mousePosition) {
        g2d.drawString("RUNAWAY: " + runawayObjects, 10, 21);
        g2d.drawString("KILLS: " + killedObjects, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
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
