package kr.jbnu.se.std;

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
    private static BufferedImage sightImg = GameModel.getSightImg();
    private static BufferedImage healthBarImg = GameModel.getHealthBarImg();
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

    public static void reset() {
        maxFever = 0;
    }

    private static BufferedImage flip(BufferedImage img) {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -img.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }

    public static void drawBack(Graphics2D g2d) throws IOException {
        g2d.setFont(font);
        g2d.setColor(Color.darkGray);
        g2d.drawImage(GameModel.stage(GameConfig.getStage()).getBackgroundImg(), 0, 0, Framework.getFrameWidth(), Framework.getFrameHeight(), null);
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

    public void drawCombo(Graphics2D g2d, Point mousePosition) {
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
        BufferedImage combo1stDigitImg = GameModel.getComboDigitImg(1);
        BufferedImage combo2ndDigitImg = GameModel.getComboDigitImg(2);
        BufferedImage combo3rdDigitImg = GameModel.getComboDigitImg(3);
        if (combo1stDigitImg != null) {
            System.out.println("A");
            g2d.drawImage(combo1stDigitImg, (int) mousePosition.getX() - 50, (int) mousePosition.getY() - 80, null);
            g2d.drawImage(combo2ndDigitImg, (int) mousePosition.getX() - 20, (int) mousePosition.getY() - 80, null);
            g2d.drawImage(combo3rdDigitImg, (int) mousePosition.getX() + 10, (int) mousePosition.getY() - 80, null);
        } else if (combo2ndDigitImg != null) {
            System.out.println("B");
            g2d.drawImage(combo2ndDigitImg, (int) mousePosition.getX() - 35, (int) mousePosition.getY() - 80, null);
            g2d.drawImage(combo3rdDigitImg, (int) mousePosition.getX() -5, (int) mousePosition.getY() - 80, null);
        } else if (combo3rdDigitImg != null) {
            System.out.println("C");
            g2d.drawImage(combo3rdDigitImg, (int) mousePosition.getX()-20 , (int) mousePosition.getY() - 80, null);
        }
    }

    public void drawFront(Graphics2D g2d, Point mousePosition) throws IOException {
        AffineTransform old;
        g2d.setColor(Color.white);
        double scoreMultiplier = GameController.getScoreMultiplier();
        int feverCnt = GameController.getFeverCnt();
        g2d.drawString("HP: " + GameController.getHealth() + " | " + "KILLS: " + GameController.getKilledObjects() + " | " + "SHOOTS: " + GameController.getShoots() + " | " + "SCORE: " + GameController.getScore() + " | " + "BULLETS: " + Guns.getBullets(GameConfig.getGunType()) + "/" + Guns.getDefaultBullets(GameConfig.getGunType()), 10, 21);
        g2d.drawString("FEVERx" + scoreMultiplier, Framework.getFrameWidth() - GameModel.getFeverBarImg(0).getWidth(), 80 + GameModel.getFeverBarImg(0).getHeight());
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
        BufferedImage feverBarImg = GameModel.getFeverBarImg(Math.min(feverCnt, 10));
        BufferedImage grassImg = GameModel.stage(GameConfig.getStage()).getGrassImg();

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
            GameModel.setComboDigitImg(1, feverCnt / 100);
        } else {
            GameModel.setComboDigitImg(1, -1);
        }
        if (feverCnt >= 10) {
            GameModel.setComboDigitImg(2, (feverCnt % 100) / 10);
        } else {
            GameModel.setComboDigitImg(2, -1);
        }
        if (feverCnt > 0) {
            GameModel.setComboDigitImg(3, feverCnt % 10);
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
                GameModel.setComboDigitImg(3, -1);
            }
        }

        exec2 = new ScheduledThreadPoolExecutor(1);
        exec2.schedule(new Runnable() {
            public void run() {
                GameModel.resetComboDigitImgs();
            }
        }, 500, TimeUnit.MILLISECONDS);
    }


    public void draw(Graphics2D g2d, Point mousePosition) throws IOException {
        drawBack(g2d);
        switch (GameConfig.getStage()) {
            case 2:
                Stage2Controller.draw(g2d);
                break;
            case 3:
                Stage3Controller.draw(g2d);
                break;
            case 4:
                Stage4Controller.draw(g2d);
                break;
            default:
                break;
        }
        drawFront(g2d, mousePosition);
    }

    public void drawScoreWindow(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, Framework.getFrameWidth() , Framework.getFrameHeight());
        g2d.setColor(Color.WHITE);
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
