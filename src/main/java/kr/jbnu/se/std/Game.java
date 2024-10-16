package kr.jbnu.se.std;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    protected static double scoreMultiplier;
    
   /**
     * How many times a player is shot?
     */
   protected static int shoots;

   protected static int feverCnt;
   protected static int feverImgNum;

   protected static int health;

   protected static boolean hit = false;
    
    /**
     * Last time of the shoot.
     */
    protected static long lastTimeShoot;
    protected static long lastTimeReload;
    /**
     * The time which must elapse between shots.
     */
    protected static long timeBetweenShots;
    protected static long timeBetweenReload;
    /**
     * kr.jbnu.se.std.Game background image.
     */
    protected static BufferedImage backgroundImg;
    
    /**
     * Bottom grass.
     */
    protected static BufferedImage grassImg;

    /**
     * Shotgun sight image.
     */
    protected static BufferedImage sightImg;
    protected static BufferedImage healthBarImg;
    protected static BufferedImage feverBarImg;

    protected static BufferedImage combo1stDigitImg;
    protected static BufferedImage combo2ndDigitImg;
    protected static BufferedImage combo3rdDigitImg;

    protected static BufferedImage frogImg;
    protected static BufferedImage gunEffectImg;
    protected static BufferedImage weaponsImg;
    protected enum GunTypes{REVOLVER, SHORT, WOODEN, AK47, MACHINEGUN}
    protected static GunTypes gunType = null;
    protected static Map<GunTypes, Integer> defaultBullets = new HashMap<GunTypes, Integer>();
    protected static Map<GunTypes, Integer> bullets = new HashMap<GunTypes, Integer>();
    protected static Map<GunTypes, Integer> gunIdx = new HashMap<GunTypes, Integer>();
    protected static Map<GunTypes, String> gunName = new HashMap<GunTypes, String>();
    protected static Map<GunTypes, Float> gunDecibel = new HashMap<GunTypes, Float>();
    protected static Map<GunTypes, Float> reloadDecibel = new HashMap<GunTypes, Float>();

    protected static Image feverFireGif;

    /**
     * Middle width of the sight image.
     */
    protected static int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    protected static int sightImgMiddleHeight;

    protected static ScheduledThreadPoolExecutor exec;
    protected static ScheduledThreadPoolExecutor exec2;

    protected static double rotationCenterX;
    protected static double rotationCenterY;
    protected static double xDiff;
    protected static double yDiff;

    protected static AffineTransform old;
    protected static boolean showShotEffect;
    

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
        scoreMultiplier = 1;
        shoots = 0;
        feverCnt = 0;
        health = 100;

        gunType = GunTypes.REVOLVER;
        defaultBullets.put(GunTypes.REVOLVER, 6);
        defaultBullets.put(GunTypes.SHORT, 30);
        defaultBullets.put(GunTypes.WOODEN, 1);
        defaultBullets.put(GunTypes.AK47, 100);
        defaultBullets.put(GunTypes.MACHINEGUN, 200);
        bullets.put(GunTypes.REVOLVER, 6);
        bullets.put(GunTypes.SHORT, 30);
        bullets.put(GunTypes.WOODEN, 1);
        bullets.put(GunTypes.AK47, 30);
        bullets.put(GunTypes.MACHINEGUN, 200);
        gunIdx.put(GunTypes.REVOLVER, 1);
        gunIdx.put(GunTypes.SHORT, 2);
        gunIdx.put(GunTypes.WOODEN, 3);
        gunIdx.put(GunTypes.AK47, 4);
        gunIdx.put(GunTypes.MACHINEGUN, 5);
        gunName.put(GunTypes.REVOLVER, "revolver");
        gunName.put(GunTypes.SHORT, "short");
        gunName.put(GunTypes.WOODEN, "wooden");
        gunName.put(GunTypes.AK47, "ak47");
        gunName.put(GunTypes.MACHINEGUN, "machinegun");
        gunDecibel.put(GunTypes.REVOLVER, -20.0f);
        gunDecibel.put(GunTypes.SHORT, -10.0f);
        gunDecibel.put(GunTypes.WOODEN,  -15.0f);
        gunDecibel.put(GunTypes.AK47,  -15.0f);
        gunDecibel.put(GunTypes.MACHINEGUN,  -5.0f);
        reloadDecibel.put(GunTypes.REVOLVER, -20.0f);
        reloadDecibel.put(GunTypes.SHORT, -20.0f);
        reloadDecibel.put(GunTypes.WOODEN,  -15.0f);
        reloadDecibel.put(GunTypes.AK47,  -15.0f);
        reloadDecibel.put(GunTypes.MACHINEGUN,  -15.0f);

        lastTimeShoot = 0;
        lastTimeReload = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
        timeBetweenReload = (long) (Framework.secInNanosec / 1.5);

        showShotEffect = false;
    };
    
    /**
     * Load game files - images, sounds, ...
     */
    protected void LoadContent()
    {
        try
        {
            URL grassImgUrl = this.getClass().getResource("/images/ground.png");
            grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));
            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(Objects.requireNonNull(sightImgUrl));
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
            URL healthBarImgUrl = this.getClass().getResource("/images/health_bar.png");
            healthBarImg = ImageIO.read(Objects.requireNonNull(healthBarImgUrl));
            URL feverBarImgUrl = this.getClass().getResource("/images/fever0.png");
            feverBarImg = ImageIO.read(Objects.requireNonNull(feverBarImgUrl));
            feverFireGif = null;
            URL frogImgUrl = this.getClass().getResource("/images/frog_" + gunName.get(gunType) + ".png");
            frogImg = ImageIO.read(Objects.requireNonNull(frogImgUrl));
            URL gunEffectImgUrl = this.getClass().getResource("/images/gun_effect.png");
            gunEffectImg = ImageIO.read(Objects.requireNonNull(gunEffectImgUrl));
            URL weaponsImgUrl = this.getClass().getResource("/images/weapons.png");
            weaponsImg = ImageIO.read(Objects.requireNonNull(weaponsImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void PlaySound(String soundName, Float decibel) {
        try {
            File soundFile = new File("src/main/resources/audio/" + soundName + ".wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(decibel);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    protected void Shot(ArrayList<? extends MovingObject> arrayList, int i) {
        score += (int) Math.floor(arrayList.get(i).score * scoreMultiplier);
        arrayList.remove(i);
        hit = true;
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
        health = 100;
    };
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public abstract void UpdateGame(long gameTime, Point mousePosition)throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException;

    protected void RanAway() throws IOException {
        runawayObjects++;
        health -= 1;
        feverCnt = 0;
        DrawFever();
    }
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

    protected void DrawCombo(Graphics2D g2d, Point mousePosition) throws IOException {
        if (showShotEffect) {
            rotationCenterX = Framework.frameWidth;
            rotationCenterY = Framework.frameHeight;
            xDiff = rotationCenterX - mousePosition.x;
            yDiff = rotationCenterY - mousePosition.y;
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.atan2(yDiff, xDiff) - 0.53,rotationCenterX, rotationCenterY );
            switch (gunType) {
                case REVOLVER:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 25, Framework.frameHeight - frogImg.getHeight() - 10, null);
                    break;
                case SHORT:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 80, Framework.frameHeight - frogImg.getHeight() - 27, null);
                    break;
                case WOODEN:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 100, Framework.frameHeight - frogImg.getHeight() - 36, null);
                    break;
                case AK47:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 100, Framework.frameHeight - frogImg.getHeight() - 39, null);
                    break;
                case MACHINEGUN:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 116, Framework.frameHeight - frogImg.getHeight() - 60, null);
                    break;
            }
            g2d.setTransform(old);
        }
        if (combo1stDigitImg != null) {
            g2d.drawImage(combo1stDigitImg, (int) mousePosition.getX() - 50, (int) mousePosition.getY() - 80, null);
            g2d.drawImage(combo2ndDigitImg, (int) mousePosition.getX() - 20, (int) mousePosition.getY() - 80, null);
            g2d.drawImage(combo3rdDigitImg, (int) mousePosition.getX() + 10, (int) mousePosition.getY() - 80, null);
        } else if (combo2ndDigitImg != null) {
            g2d.drawImage(combo2ndDigitImg, (int) mousePosition.getX() - 35, (int) mousePosition.getY() - 80, null);
            g2d.drawImage(combo3rdDigitImg, (int) mousePosition.getX() -5, (int) mousePosition.getY() - 80, null);
        } else if (combo3rdDigitImg != null) {
            g2d.drawImage(combo3rdDigitImg, (int) mousePosition.getX()-20 , (int) mousePosition.getY() - 80, null);
        }
    }

    protected void DrawFront(Graphics2D g2d, Point mousePosition) throws IOException {
        g2d.setColor(Color.white);
        g2d.drawString("HP: " + health + " | " + "KILLS: " + killedObjects + " | " + "SHOOTS: " + shoots + " | " + "SCORE: " + score + " | " + "BULLETS: " + bullets.get(gunType) + "/" + defaultBullets.get(gunType), 10, 21);
        g2d.drawString("FEVERx" + scoreMultiplier, Framework.frameWidth - feverBarImg.getWidth(), 80 + feverBarImg.getHeight());
        if (scoreMultiplier > 1) {
            g2d.setFont(new Font("SanSerif", Font.BOLD, 30));
            if (feverCnt >= 9) {
                g2d.setColor(Color.RED);
            } else if (feverCnt >= 6) {
                g2d.setColor(Color.ORANGE);
            } else {
                g2d.setColor(Color.YELLOW);
            }
            g2d.drawString("x" + scoreMultiplier, mousePosition.x + 30, mousePosition.y);
        }
        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        g2d.drawImage(healthBarImg, Framework.frameWidth - healthBarImg.getWidth(), 0, null);
        g2d.drawImage(feverBarImg, Framework.frameWidth - feverBarImg.getWidth(), healthBarImg.getHeight() - 5, null);
        g2d.drawImage(weaponsImg, 0, 20, null);
        g2d.setColor(Color.RED);
        g2d.fillRect(Framework.frameWidth - healthBarImg.getWidth() + 58, 5, (healthBarImg.getWidth() - 63) * health / 100, healthBarImg.getHeight() - 10);

        URL frogImgUrl = this.getClass().getResource("/images/frog_" + gunName.get(gunType) + ".png");
        frogImg = ImageIO.read(Objects.requireNonNull(frogImgUrl));
        rotationCenterX = Framework.frameWidth;
        rotationCenterY = Framework.frameHeight;
        xDiff = rotationCenterX - mousePosition.x;
        yDiff = rotationCenterY - mousePosition.y;
        old = g2d.getTransform();
        g2d.rotate(Math.atan2(yDiff, xDiff) - 0.53,rotationCenterX, rotationCenterY );
        g2d.drawImage(frogImg, Framework.frameWidth - frogImg.getWidth() - 30, Framework.frameHeight - frogImg.getHeight(), null);
        g2d.setTransform(old);

        if (feverFireGif != null) {
            g2d.drawImage(feverFireGif, Framework.frameWidth - feverFireGif.getWidth(null) - 430 + Math.min(feverCnt, 10) * 44, -10 + feverBarImg.getHeight(), null);
        }

        DrawCombo(g2d, mousePosition);
    }

    protected void DrawShot() {
        showShotEffect = true;

        exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(new Runnable() {
            public void run() {
                showShotEffect = false;
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    protected void DrawFever() throws IOException {
        feverImgNum = Math.min(feverCnt, 10);
        URL feverImgUrl = this.getClass().getResource("/images/fever" + feverImgNum + ".png");
        feverBarImg = ImageIO.read(Objects.requireNonNull(feverImgUrl));
        URL combo3rdDigitUrl = this.getClass().getResource("/images/number" + feverCnt % 10 + ".png");
        combo3rdDigitImg = ImageIO.read(Objects.requireNonNull(combo3rdDigitUrl));
        if (feverCnt >= 100) {
            URL combo1stDigitUrl = this.getClass().getResource("/images/number" + (int) (double) (feverCnt / 100) + ".png");
            combo1stDigitImg = ImageIO.read(Objects.requireNonNull(combo1stDigitUrl));
        } else {
            combo1stDigitImg = null;
        }
        if (feverCnt >= 10) {
            scoreMultiplier = 3;
            URL combo2ndDigitUrl = this.getClass().getResource("/images/number" + (int) (double) ((feverCnt % 100) / 10) + ".png");
            combo2ndDigitImg = ImageIO.read(Objects.requireNonNull(combo2ndDigitUrl));
        } else {
            combo2ndDigitImg = null;
        }
        if (feverCnt >= 9) {
            scoreMultiplier = 2.5;
            URL feverFireGifUrl = this.getClass().getResource("/images/blue_fire.gif");
            feverFireGif = new ImageIcon(Objects.requireNonNull(feverFireGifUrl)).getImage();
        } else if (feverCnt >= 6) {
            scoreMultiplier = 2;
            URL feverFireGifUrl = this.getClass().getResource("/images/red_fire.gif");
            feverFireGif = new ImageIcon(Objects.requireNonNull(feverFireGifUrl)).getImage();
        } else if (feverCnt >= 3) {
            scoreMultiplier = 1.5;
            URL feverFireGifUrl = this.getClass().getResource("/images/yellow_fire.gif");
            feverFireGif = new ImageIcon(Objects.requireNonNull(feverFireGifUrl)).getImage();
        } else {
            scoreMultiplier = 1;
            feverFireGif = null;
            if (feverCnt == 0) {
                combo3rdDigitImg = null;
            }
        }

        exec2 = new ScheduledThreadPoolExecutor(1);
        exec2.schedule(new Runnable() {
            public void run() {
                combo1stDigitImg = null;
                combo2ndDigitImg = null;
                combo3rdDigitImg = null;
            }
            }, 500, TimeUnit.MILLISECONDS);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        DrawBack(g2d);
        DrawFront(g2d, mousePosition);
    }
    
    
    /**
     * Draw the game over screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition) throws IOException {
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
