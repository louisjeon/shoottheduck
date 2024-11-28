package kr.jbnu.se.std;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static kr.jbnu.se.std.GameConfig.font;

public class GameView {
    private static final Logger log = LoggerFactory.getLogger(GameView.class);
    BufferedImage sightImg = GameModel.getSightImg();
    URL blueUrl = this.getClass().getResource("/images/blue_fire.gif");
    Image blueFeverFire = new ImageIcon(Objects.requireNonNull(blueUrl)).getImage();
    URL redUrl = this.getClass().getResource("/images/red_fire.gif");
    Image redFeverFire = new ImageIcon(Objects.requireNonNull(redUrl)).getImage();
    URL yellowUrl = this.getClass().getResource("/images/yellow_fire.gif");
    Image yellowFeverFire = new ImageIcon(Objects.requireNonNull(yellowUrl)).getImage();
    private final int sightImgMiddleWidth = sightImg.getWidth() / 2;
    private final int sightImgMiddleHeight = sightImg.getHeight() / 2;
    private static double rotationCenterX;
    private static double rotationCenterY;
    private double xDiff;
    private double yDiff;
    private final Frog frog = Frog.getInstance();
    private static boolean showShotEffect;
    private static int maxFever;
    private static GameView gameView;

    private GameView() {
    }

    public static GameView instance() {
        if (gameView == null) {
            gameView = new GameView();
        }
        return gameView;
    }

    private static BufferedImage flip(BufferedImage img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }

    public void drawBack(Graphics2D g2d) throws IOException {
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawImage(GameModel.stage(0).getBackgroundImg(), 0, 0, Framework.getFrameWidth(), Framework.getFrameHeight(), null);
        MovingBossObject boss = GameController.getBoss();
        if (boss != null) {
            boss.draw(g2d);
        }
        for (Duck duck : GameController.getMovingDucks()) {
            duck.draw(g2d);
        }
        for (PotionDuck potionDuck: GameController.getMovingPotionDucks()) {
            potionDuck.draw(g2d);
        }
    }

    public void drawCombo(Graphics2D g2d, Point mousePosition) throws IOException {
        if (showShotEffect) {
            rotationCenterX = Framework.getFrameWidth() + (double) frog.getXChange();
            rotationCenterY = Framework.getFrameHeight() + (double) frog.getYChange();
            xDiff = rotationCenterX - mousePosition.x;
            yDiff = rotationCenterY - mousePosition.y;
            AffineTransform old = g2d.getTransform();
            g2d.rotate(Math.atan2(yDiff, xDiff) - 0.53,rotationCenterX, rotationCenterY );
            int x = 0;
            int y = 0;
            switch (GameConfig.getGunType()) {
                case REVOLVER:
                    x = -25;
                    y = -10;
                    break;
                case SHORT:
                    x = -80;
                    y = -27;
                    break;
                case WOODEN:
                    x = -100;
                    y = -36;
                    break;
                case AK47:
                    x = -100;
                    y = -39;
                    break;
                case MACHINEGUN:
                    x = -116;
                    y = -60;
                    break;
            }
            BufferedImage frogImg = GameModel.getFrogImg();
            g2d.drawImage(GameModel.getGunEffectImg(), Framework.getFrameWidth() - frogImg.getWidth() + x + frog.getXChange(), Framework.getFrameHeight()  - frogImg.getHeight() + y + frog.getYChange(), null);
            g2d.setTransform(old);
        }
        BufferedImage combo1stDigitImg = GameModel.getCombo1stDigitImg();
        BufferedImage combo2ndDigitImg = GameModel.getCombo2ndDigitImg();
        BufferedImage combo3rdDigitImg = GameModel.getCombo3rdDigitImg();
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

    public void drawFront(Graphics2D g2d, Point mousePosition) throws IOException {
        AffineTransform old;
        g2d.setColor(Color.white);
        double scoreMultiplier = GameController.getScoreMultiplier();
        int feverCnt = GameController.getFeverCnt();
        g2d.drawString("HP: " + GameController.getHealth() + " | " + "KILLS: " + GameController.getKilledObjects() + " | " + "SHOOTS: " + GameController.getShoots() + " | " + "SCORE: " + GameController.getScore() + " | " + "BULLETS: " + Guns.getBullets(GameConfig.getGunType()) + "/" + Guns.getDefaultBullets(GameConfig.getGunType()), 10, 21);
        g2d.drawString("FEVERx" + scoreMultiplier, Framework.getFrameWidth() - GameModel.getFeverBarImg().getWidth(), 80 + GameModel.getFeverBarImg().getHeight());
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

        BufferedImage grassImg = GameModel.stage(GameConfig.getStage()).getGrassImg();
        BufferedImage healthBarImg = GameModel.getHealthBarImg();
        BufferedImage feverBarImg = GameModel.getFeverBarImg();

        g2d.drawImage(grassImg, 0, Framework.getFrameHeight() - grassImg.getHeight(), Framework.getFrameWidth(), grassImg.getHeight(), null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);
        g2d.drawImage(healthBarImg, Framework.getFrameWidth() - healthBarImg.getWidth(), 0, null);
        g2d.drawImage(feverBarImg, Framework.getFrameWidth()  - feverBarImg.getWidth(), healthBarImg.getHeight() - 5, null);

        if (feverCnt % 10 == 0 && Math.min(50, feverCnt) > maxFever) {
            maxFever = Math.min(50, feverCnt);
        }

        URL weaponsImgUrl = this.getClass().getResource("/images/weapons" + Math.min(5, maxFever / 10 + 1) + ".png");
        BufferedImage weaponsImg = ImageIO.read(Objects.requireNonNull(weaponsImgUrl));
        g2d.drawImage(weaponsImg, 0, 20, null);
        g2d.setColor(Color.RED);
        g2d.fillRect(Framework.getFrameWidth()  - healthBarImg.getWidth() + 58, 5, (healthBarImg.getWidth() - 63) * (int) GameController.getHealth() / 100, healthBarImg.getHeight() - 10);

        URL frogImgUrl = this.getClass().getResource("/images/frog_" + Guns.getGunName(GameConfig.getGunType()) + ".png");
        BufferedImage frogImg = ImageIO.read(Objects.requireNonNull(frogImgUrl));
        rotationCenterX = Framework.getFrameWidth() + (double) frog.getXChange();
        rotationCenterY = Framework.getFrameHeight()+ (double) frog.getYChange();
        xDiff = rotationCenterX  - mousePosition.x;
        yDiff = rotationCenterY  - mousePosition.y;
        old = g2d.getTransform();
        if (Math.atan2(yDiff, xDiff) - 0.53 > Math.PI / 2 - 0.4) {
            frogImg = flip(frogImg);
            g2d.rotate(Math.atan2(yDiff, xDiff) + 0.53,rotationCenterX , rotationCenterY);
            frog.setX(Framework.getFrameWidth()  - frogImg.getWidth() - 30 + frog.getXChange());
            frog.setY(Framework.getFrameHeight() + frog.getYChange());
            g2d.drawImage(frogImg, frog.getX(), frog.getY(), null);
        } else {
            g2d.rotate(Math.atan2(yDiff, xDiff) - 0.53,rotationCenterX , rotationCenterY);
            frog.setX(Framework.getFrameWidth()  - frogImg.getWidth() - 30 +  frog.getXChange());
            frog.setY(Framework.getFrameHeight() - frogImg.getHeight() + frog.getYChange());
            g2d.drawImage(frogImg, frog.getX(), frog.getY(), null);
        }
        g2d.setTransform(old);

        if (GameController.getBossAttacking()) {
            BufferedImage bossAttackImg = GameModel.stage(GameConfig.getStage()).getBossAttackImg();
            MovingBossObject boss = GameController.getBoss();
            BufferedImage bossImg = GameModel.stage(GameConfig.getStage()).getBossImg();

            switch (GameConfig.getStage()) {
                case 1:
                    old = g2d.getTransform();
                    if (Math.atan2(yDiff, xDiff) - 0.53 > Math.PI / 2 - 0.4) {
                        g2d.rotate(Math.atan2(yDiff, xDiff) + 1.03,rotationCenterX , rotationCenterY);
                        g2d.drawImage(bossAttackImg, frog.getX(), frog.getY(), null);
                    } else {
                        g2d.rotate(Math.atan2(yDiff, xDiff) + 0.2,rotationCenterX , rotationCenterY);
                        g2d.drawImage(bossAttackImg, frog.getX(), frog.getY(), null);
                    }
                    g2d.setTransform(old);
                    break;
                case 2:
                case 5:
                    g2d.drawImage(bossAttackImg, boss.getX() + bossImg.getWidth() / 2 - bossAttackImg.getWidth() / 2, boss.getY() + bossImg.getHeight() - 10, null);
                    break;
                case 3:
                    g2d.drawImage(bossAttackImg, boss.getX() + bossImg.getWidth() / 2 - bossAttackImg.getWidth() / 2, boss.getY() + bossImg.getHeight() +100, null);
                    break;
                case 4:
                    g2d.drawImage(bossAttackImg, boss.getX() + bossImg.getWidth() / 2 - bossAttackImg.getWidth() / 2, 0, null);
                    break;

            }
        }

        Image feverFireGif = GameModel.getFeverFireGif();

        if (feverFireGif != null) {
            g2d.drawImage(feverFireGif, Framework.getFrameWidth()  - feverFireGif.getWidth(null) - 430 + Math.min(feverCnt, 10) * 44, -10 + feverBarImg.getHeight(), null);
        }

        drawCombo(g2d, mousePosition);
    }

    public static void drawShot() {
        ScheduledThreadPoolExecutor exec;
        showShotEffect = true;

        exec = new ScheduledThreadPoolExecutor(1);
        exec.schedule(new Runnable() {
            public void run() {
                showShotEffect = false;
            }
        }, 100, TimeUnit.MILLISECONDS);
    }

    public void drawFever() throws IOException {
        ScheduledThreadPoolExecutor exec2;
        int feverCnt = GameController.getFeverCnt();

        if (feverCnt >= 100) {
            URL combo1stDigitUrl = this.getClass().getResource("/images/number" + (feverCnt / 100) + ".png");
            GameModel.setCombo1stDigitImg(ImageIO.read(Objects.requireNonNull(combo1stDigitUrl)));
        } else {
            GameModel.setCombo1stDigitImg(null);
        }
        if (feverCnt >= 10) {
            URL combo2ndDigitUrl = this.getClass().getResource("/images/number" + ((feverCnt % 100) / 10) + ".png");
            GameModel.setCombo2ndDigitImg(ImageIO.read(Objects.requireNonNull(combo2ndDigitUrl)));
        } else {
            GameModel.setCombo2ndDigitImg(null);
        }
        if (feverCnt >= 9) {
            GameController.setScoreMultiplier(2.5);
            GameModel.setFeverFireGif(blueFeverFire);
        } else if (feverCnt >= 6) {
            GameController.setScoreMultiplier(2);
            GameModel.setFeverFireGif(redFeverFire);
        } else if (feverCnt >= 3) {
            GameController.setScoreMultiplier(1.5);
            GameModel.setFeverFireGif(yellowFeverFire);
        } else {
            GameController.setScoreMultiplier(1);
            GameModel.setFeverFireGif(null);
            if (feverCnt == 0) {
                GameModel.setCombo3rdDigitImg(null);
            }
        }

        exec2 = new ScheduledThreadPoolExecutor(1);
        exec2.schedule(new Runnable() {
            public void run() {
                GameModel.setCombo1stDigitImg(null);
                GameModel.setCombo2ndDigitImg(null);
                GameModel.setCombo3rdDigitImg(null);
            }
        }, 500, TimeUnit.MILLISECONDS);
    }


    public void draw(Graphics2D g2d, Point mousePosition) throws IOException {
        drawBack(g2d);
        drawFront(g2d, mousePosition);
    }

    public void drawScoreWindow(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, Framework.getFrameWidth() , Framework.getFrameHeight());
        g2d.setColor(Color.WHITE);
        log.info("Drawing Score Board...");
        int stage = GameConfig.getStage();
        g2d.drawString("Stage " + stage + " best result: " + ScoreBoard.getScore(stage), Framework.getFrameWidth()  / 2 - 39, (int)(Framework.getFrameHeight() * 0.65) + 1);
    }

    public void drawGameOver(Graphics2D g2d, Point mousePosition) throws IOException {
        draw(g2d, mousePosition);
        int score = GameController.getScore();
        int stage = GameConfig.getStage();
        if (score > ScoreBoard.getScore(stage)) {
            ScoreBoard.setScore(stage, score);
        }

        g2d.setColor(Color.black);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.getFrameWidth() / 2 - 39, (int)(Framework.getFrameHeight() * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.getFrameWidth()  / 2 - 149, (int)(Framework.getFrameHeight() * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.getFrameWidth()  / 2 - 40, (int)(Framework.getFrameHeight() * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.getFrameWidth()  / 2 - 150, (int)(Framework.getFrameHeight() * 0.70));
    }

    public static double getRotationCenterX() {
        return rotationCenterX;
    }

    public static double getRotationCenterY() {
        return rotationCenterY;
    }

    public static int getMaxFever() {
        return maxFever;
    }
}
