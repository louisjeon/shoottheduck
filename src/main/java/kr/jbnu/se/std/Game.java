package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

public abstract class Game {
    protected static Random random;
    private static Font font;

    protected static int runawayObjects;
   protected static int killedObjects;
    protected static int score;
   protected static int shoots;
   protected static int feverCnt;
   protected static int feverImgNum;
   protected static int health;
    protected static double scoreMultiplier;

   protected static boolean hit = false;

    protected static long lastTimeShoot;
    protected static long lastTimeReload;
    protected static long timeBetweenShots;
    protected static long timeBetweenReload;

    protected static BufferedImage backgroundImg;
    protected static BufferedImage grassImg;
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
    protected static Map<GunTypes, Integer> defaultBullets = new HashMap<>();
    protected static Map<GunTypes, Integer> bullets = new HashMap<>();
    protected static Map<GunTypes, Integer> gunIdx = new HashMap<>();
    protected static Map<GunTypes, String> gunName = new HashMap<>();
    protected static Map<GunTypes, Float> gunDecibel = new HashMap<>();
    protected static Map<GunTypes, Float> reloadDecibel = new HashMap<>();
    public static Map<GunTypes, Integer> gunDamage = new HashMap<>();

    protected static Image feverFireGif;
    protected static int sightImgMiddleWidth;
    protected static int sightImgMiddleHeight;

    protected static ScheduledThreadPoolExecutor exec;
    protected static ScheduledThreadPoolExecutor exec2;

    protected static double rotationCenterX;
    protected static double rotationCenterY;
    protected static double xDiff;
    protected static double yDiff;

    protected static AffineTransform old;
    protected static boolean showShotEffect;

    protected static Frog frog;
    protected static int maxFever;
    protected static BufferedImage duckImg;
    protected static ArrayList<Duck> movingDucks;
    protected static BufferedImage bossImg;
    protected static MovingBossObject boss;

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

    protected void SetInitialValues() {
        runawayObjects = 0;
        killedObjects = 0;
        score = 0;
        scoreMultiplier = 1;
        shoots = 0;
        feverCnt = 0;
        health = 100;

        gunType = GunTypes.REVOLVER;
        bullets.put(GunTypes.REVOLVER, 6);
        bullets.put(GunTypes.SHORT, 100);
        bullets.put(GunTypes.WOODEN, 1);
        bullets.put(GunTypes.AK47, 100);
        bullets.put(GunTypes.MACHINEGUN, 200);

        lastTimeShoot = 0;
        lastTimeReload = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
        timeBetweenReload = (long) (Framework.secInNanosec / 1.5);

        showShotEffect = false;
        frog = Frog.getInstance();
    }

    protected void Initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        defaultBullets.put(GunTypes.REVOLVER, 6);
        defaultBullets.put(GunTypes.SHORT, 100);
        defaultBullets.put(GunTypes.WOODEN, 1);
        defaultBullets.put(GunTypes.AK47, 100);
        defaultBullets.put(GunTypes.MACHINEGUN, 200);
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
        gunDamage.put(GunTypes.REVOLVER, 10);
        gunDamage.put(GunTypes.SHORT, 1);
        gunDamage.put(GunTypes.WOODEN,  20);
        gunDamage.put(GunTypes.AK47,  2);
        gunDamage.put(GunTypes.MACHINEGUN, 3);
        movingDucks = new ArrayList<Duck>();
        SetInitialValues();
    };

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
            URL weaponsImgUrl = this.getClass().getResource("/images/weapons1.png");
            weaponsImg = ImageIO.read(Objects.requireNonNull(weaponsImgUrl));

            frog.setXChange(0);
            frog.setYChange(0);
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

    public void RestartGame() {
        SetInitialValues();
        movingDucks.clear();
        Duck.lastObjectTime = 0;
    };

    public void UpdateGame(long gameTime, Point mousePosition)throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if(System.nanoTime() - Duck.lastObjectTime >= Duck.timeBetweenObjects)
        {
            movingDucks.add(new Duck(duckImg));

            Duck.nextObjectLines++;
            if(Duck.nextObjectLines >= Duck.objectLines.length)
                Duck.nextObjectLines = 0;

            Duck.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingDucks.size(); i++)
        {
            movingDucks.get(i).Update();

            if(movingDucks.get(i).x < -duckImg.getWidth())
            {
                movingDucks.remove(i);
                RanAway();
            }
        }

        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            if (new Rectangle(0, 20, weaponsImg.getWidth(), weaponsImg.getHeight()).contains(mousePosition)) {
                if (new Rectangle(0, 20, (int) (weaponsImg.getWidth() * 0.1), weaponsImg.getHeight()).contains(mousePosition)) {
                    gunType = GunTypes.REVOLVER;
                    timeBetweenShots = Framework.secInNanosec / 3;
                    timeBetweenReload = (long) (Framework.secInNanosec / 1.5);
                    PlaySound("reload" + gunIdx.get(gunType), reloadDecibel.get(gunType));
                } else if (maxFever >=10 && new Rectangle((int) (weaponsImg.getWidth() * 0.1), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    gunType = GunTypes.SHORT;
                    timeBetweenShots = Framework.secInNanosec / 20;
                    timeBetweenReload = (Framework.secInNanosec) * 2;
                    PlaySound("reload" + gunIdx.get(gunType), reloadDecibel.get(gunType));
                } else if (maxFever >=20 && new Rectangle((int) (weaponsImg.getWidth() * 0.3), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    gunType = GunTypes.WOODEN;
                    timeBetweenShots = Framework.secInNanosec / 3;
                    timeBetweenReload = (Framework.secInNanosec) / 5;
                    PlaySound("reload" + gunIdx.get(gunType), reloadDecibel.get(gunType));
                } else if (maxFever >=30 && new Rectangle((int) (weaponsImg.getWidth() * 0.5), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    gunType = GunTypes.AK47;
                    timeBetweenShots = Framework.secInNanosec / 20;
                    timeBetweenReload = (Framework.secInNanosec) * 2;
                    PlaySound("reload" + gunIdx.get(gunType), reloadDecibel.get(gunType));
                } else if (maxFever >=40) {
                    gunType = GunTypes.MACHINEGUN;
                    timeBetweenShots = Framework.secInNanosec / 20;
                    timeBetweenReload = (Framework.secInNanosec) * 3;
                    PlaySound("reload" + gunIdx.get(gunType), reloadDecibel.get(gunType));
                };
            } else if (bullets.get(gunType) == 0) {
                PlaySound("reload" + gunIdx.get(gunType), reloadDecibel.get(gunType));
                bullets.replace(gunType, defaultBullets.get(gunType));
                lastTimeReload = System.nanoTime();
            } else if(System.nanoTime() - lastTimeShoot >= timeBetweenShots && System.nanoTime() - lastTimeReload >= timeBetweenReload)
            {
                DrawShot();
                shoots++;
                bullets.replace(gunType, bullets.get(gunType) - 1);
                PlaySound(gunName.get(gunType), gunDecibel.get(gunType));
                CheckShot(mousePosition);
                if (hit) {
                    killedObjects++;
                    feverCnt++;
                    hit = false;
                    DrawFever();
                }
                lastTimeShoot = System.nanoTime();
            }
        }
        if(health == 0)
            Framework.gameState = Framework.GameState.GAMEOVER;
    };


    protected void CheckShot(Point mousePosition) {
        for(int i = 0; i < movingDucks.size(); i++)
        {
            if(new Rectangle(movingDucks.get(i).x + 18, movingDucks.get(i).y     , 27, 30).contains(mousePosition) ||
                    new Rectangle(movingDucks.get(i).x + 30, movingDucks.get(i).y + 30, 100, 35).contains(mousePosition))
            {
                Shot(movingDucks, i);
                PlaySound("quack", -18.0f);
                return;
            }
        }
        if (boss != null && new Rectangle(boss.x, boss.y, boss.width, boss.height).contains(mousePosition)) {
            PlaySound(boss.soundName, -13.0f);
            if (boss.hit(gunType)) {
                score += (int) Math.floor(boss.score * scoreMultiplier);
                boss = null;
            }
        }
    }

    protected void RanAway() throws IOException {
        runawayObjects++;
        health -= 1;
        feverCnt = 0;
        DrawFever();
    }

    protected void DrawBack(Graphics2D g2d) {
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        if (boss != null) {
            boss.Draw(g2d);
        };
        for (Duck duck : movingDucks) {
            duck.Draw(g2d);
        }
    };

    protected void DrawCombo(Graphics2D g2d, Point mousePosition) throws IOException {
        if (showShotEffect) {
            rotationCenterX = Framework.frameWidth + frog.getXChange();
            rotationCenterY = Framework.frameHeight + frog.getYChange();
            xDiff = rotationCenterX - mousePosition.x;
            yDiff = rotationCenterY - mousePosition.y;
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.atan2(yDiff, xDiff) - 0.53,rotationCenterX, rotationCenterY );
            switch (gunType) {
                case REVOLVER:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 25 + frog.getXChange(), Framework.frameHeight - frogImg.getHeight() - 10 + frog.getYChange(), null);
                    break;
                case SHORT:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 80 + frog.getXChange(), Framework.frameHeight - frogImg.getHeight() - 27 + frog.getYChange(), null);
                    break;
                case WOODEN:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 100 + frog.getXChange(), Framework.frameHeight - frogImg.getHeight() - 36 + frog.getYChange(), null);
                    break;
                case AK47:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 100 + frog.getXChange(), Framework.frameHeight - frogImg.getHeight() - 39 + frog.getYChange(), null);
                    break;
                case MACHINEGUN:
                    g2d.drawImage(gunEffectImg, Framework.frameWidth - frogImg.getWidth() - 116 + frog.getXChange(), Framework.frameHeight - frogImg.getHeight() - 60 + frog.getYChange(), null);
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

    protected BufferedImage Flip(BufferedImage img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
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

        if (feverCnt % 10 == 0 && Math.min(50, feverCnt) > maxFever) {
            maxFever = Math.min(50, feverCnt);
        }
        URL weaponsImgUrl = this.getClass().getResource("/images/weapons" + Math.min(5, maxFever / 10 + 1) + ".png");
        weaponsImg = ImageIO.read(Objects.requireNonNull(weaponsImgUrl));
        g2d.drawImage(weaponsImg, 0, 20, null);
        g2d.setColor(Color.RED);
        g2d.fillRect(Framework.frameWidth - healthBarImg.getWidth() + 58, 5, (healthBarImg.getWidth() - 63) * health / 100, healthBarImg.getHeight() - 10);

        URL frogImgUrl = this.getClass().getResource("/images/frog_" + gunName.get(gunType) + ".png");
        frogImg = ImageIO.read(Objects.requireNonNull(frogImgUrl));
        rotationCenterX = Framework.frameWidth+ frog.getXChange();
        rotationCenterY = Framework.frameHeight+ frog.getYChange();
        xDiff = rotationCenterX  - mousePosition.x;
        yDiff = rotationCenterY  - mousePosition.y;
        old = g2d.getTransform();
        if (Math.atan2(yDiff, xDiff) - 0.53 > Math.PI / 2 - 0.4) {
            frogImg = Flip(frogImg);
            g2d.rotate(Math.atan2(yDiff, xDiff) + 0.53,rotationCenterX , rotationCenterY);
            g2d.drawImage(frogImg, Framework.frameWidth - frogImg.getWidth() - 30 +  frog.getXChange(), Framework.frameHeight + frog.getYChange(), null);
        } else {
            g2d.rotate(Math.atan2(yDiff, xDiff) - 0.53,rotationCenterX , rotationCenterY);
            g2d.drawImage(frogImg, Framework.frameWidth - frogImg.getWidth() - 30 +  frog.getXChange(), Framework.frameHeight - frogImg.getHeight() + frog.getYChange(), null);
        }
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
