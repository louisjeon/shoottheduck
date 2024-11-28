package kr.jbnu.se.std;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameController {
    private static int runawayObjects;
    private static int killedObjects;
    private static int score;
    private static int shoots;
    private static int feverCnt;
    private static float health;
    private static double scoreMultiplier;

    private static boolean hit = false;

    private static long lastTimeShoot;
    private static long lastTimeReload;
    private static long timeBetweenShots;
    private static long timeBetweenReload;
    private static ArrayList<Duck> movingDucks;
    private static ArrayList<PotionDuck> movingPotionDucks;
    private static long lastBossAttackTime;
    private static boolean canShoot;
    private static boolean showShotEffect;
    protected static MovingBossObject boss;
    private static boolean bossAttacking;

    public static void setInitialValues() {
        runawayObjects = 0;
        killedObjects = 0;
        score = 0;
        scoreMultiplier = 1;
        shoots = 0;
        feverCnt = 0;
        health = 100f;

        lastTimeShoot = 0;
        lastTimeReload = 0;
        timeBetweenShots = Framework.SEC_IN_NANOSEC / 3;
        timeBetweenReload = (long) (Framework.SEC_IN_NANOSEC / 1.5);

        showShotEffect = false;

        movingDucks = new ArrayList<>();
        movingPotionDucks = new ArrayList<>();
        boss = null;
        canShoot = true;
        lastBossDeathTime= System.nanoTime();
    }

    public static void initialize() {
        setInitialValues();
    }

    public void playSound(String soundName, Float decibel) {
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

    public void shot(ArrayList<? extends MovingObject> arrayList, int i) {
        score += (int) Math.floor(arrayList.get(i).getScore() * scoreMultiplier);
        arrayList.remove(i);
        hit = true;
    }

    public void restartGame() {
        setInitialValues();
        movingDucks.clear();
        movingPotionDucks.clear();
//        Duck.updateObjectLine();
//        PotionDuck.setLastObjectTime(0);
        lastBossAttackTime = 0;
        bossAttacking = false;
        boss = null;
        int stage  = GameConfig.getStage();
        if (score > ScoreBoard.getScore(stage)) {
            ScoreBoard.setScore(stage, score);
        }

    };

    public void updateGame(Point mousePosition)throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        ScheduledThreadPoolExecutor exec3;
        if (boss != null) {
            boolean case1 = false;
            int yPlus = 0;
            float healthPlus = 0.0f;
            switch (GameConfig.getStage()) {
                case 1:
                    case1 = true;
                    break;
                case 2:
                    yPlus = -200;
                    healthPlus = -1f;
                    break;
                case 3:
                    yPlus = -200;
                    healthPlus = -2f;
                    break;
                case 4:
                case 5:
                    yPlus = 0;
                    healthPlus = -0.5f;
                    break;
            }
            if (case1) {
                if (System.nanoTime() - lastBossAttackTime >= 1000000000) {
                    bossAttacking = true;
                    canShoot = false;
                    exec3 = new ScheduledThreadPoolExecutor(1);
                    exec3.schedule(new Runnable() {
                        public void run() {
                            bossAttacking = false;
                            lastBossAttackTime = System.nanoTime();
                            canShoot = true;
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            } else {
                if (System.nanoTime() - lastBossAttackTime >= 500000000) {
                    bossAttacking = true;
                    exec3 = new ScheduledThreadPoolExecutor(1);
                    exec3.schedule(new Runnable() {
                        public void run() {
                            bossAttacking = false;
                            lastBossAttackTime = System.nanoTime();
                        }
                    }, 500, TimeUnit.MILLISECONDS);
                }
                int stage = GameConfig.getStage();
                BufferedImage bossImg = GameModel.stage(stage).getBossImg();
                BufferedImage bossAttackImg = GameModel.stage(stage).getBossAttackImg();

                if (bossAttacking && new Rectangle(boss.getX() + bossImg.getWidth() / 2 - bossAttackImg.getWidth() / 2, boss.getY() + bossImg.getHeight() - 10, bossAttackImg.getWidth(), bossAttackImg.getHeight()).contains(GameView.getRotationCenterX(), GameView.getRotationCenterY() + yPlus)) {
                    health += healthPlus;
                }
            }
        }
        int stage = GameConfig.getStage();

        if(System.nanoTime() - Duck.getLastObjectTime() >= Duck.getTimeBetweenObjects())
        {
            if (stage != 5) {
                movingDucks.add(new Duck());
            } else {
                movingDucks.add(new FastDuck(stage));
            }

            Duck.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingDucks.size(); i++)
        {
            movingDucks.get(i).update();

            if(movingDucks.get(i).getX() < -GameModel.stage(stage).getDuckImg().getWidth())
            {
                movingDucks.remove(i);
                ranAway();
            }
        }

        if(System.nanoTime() - PotionDuck.getLastObjectTime() >= PotionDuck.getTimeBetweenObjects())
        {
            movingPotionDucks.add(new PotionDuck());

            PotionDuck.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingPotionDucks.size(); i++)
        {
            movingPotionDucks.get(i).update();

            if(movingPotionDucks.get(i).getX() < -GameModel.stage(GameConfig.getStage()).getPotionDuckImg().getWidth())
            {
                movingPotionDucks.remove(i);
                ranAway();
            }
        }

        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            BufferedImage weaponsImg = GameModel.getWeaponsImg();
            int maxFever = GameView.getMaxFever();
            GameConfig.GunType gunType = GameConfig.getGunType();

            if (new Rectangle(0, 20, weaponsImg.getWidth(), weaponsImg.getHeight()).contains(mousePosition)) {
                if (new Rectangle(0, 20, (int) (weaponsImg.getWidth() * 0.1), weaponsImg.getHeight()).contains(mousePosition)) {
                    GameConfig.setGunType(GameConfig.GunType.REVOLVER);
                    timeBetweenShots = Framework.SEC_IN_NANOSEC / 3;
                    timeBetweenReload = (long) (Framework.SEC_IN_NANOSEC / 1.5);
                } else if (maxFever >=10 && new Rectangle((int) (weaponsImg.getWidth() * 0.1), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    GameConfig.setGunType(GameConfig.GunType.SHORT);
                    timeBetweenShots = Framework.SEC_IN_NANOSEC / 20;
                    timeBetweenReload = (Framework.SEC_IN_NANOSEC) * 2;
                } else if (maxFever >=20 && new Rectangle((int) (weaponsImg.getWidth() * 0.3), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    GameConfig.setGunType(GameConfig.GunType.WOODEN);
                    timeBetweenShots = Framework.SEC_IN_NANOSEC / 3;
                    timeBetweenReload = (Framework.SEC_IN_NANOSEC) / 5;
                } else if (maxFever >=30 && new Rectangle((int) (weaponsImg.getWidth() * 0.5), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    GameConfig.setGunType(GameConfig.GunType.AK47);
                    timeBetweenShots = Framework.SEC_IN_NANOSEC / 20;
                    timeBetweenReload = (Framework.SEC_IN_NANOSEC) * 2;
                } else if (maxFever >=40) {
                    GameConfig.setGunType(GameConfig.GunType.MACHINEGUN);
                    timeBetweenShots = Framework.SEC_IN_NANOSEC / 20;
                    timeBetweenReload = (Framework.SEC_IN_NANOSEC) * 3;
                };
                playSound("reload"+ (gunType.ordinal() + 1), Guns.getReloadDecibel(gunType));
            } else if (canShoot) {
                if (Guns.getBullets(gunType) == 0) {
                    playSound("reload"+ (gunType.ordinal() + 1), Guns.getReloadDecibel(gunType));
                    Guns.setBulletsDefault(gunType);
                    lastTimeReload = System.nanoTime();
                } else if(System.nanoTime() - lastTimeShoot >= timeBetweenShots && System.nanoTime() - lastTimeReload >= timeBetweenReload)
                {
                    GameView.drawShot();
                    shoots++;
                    Guns.useBullet(gunType);
                    playSound(Guns.getGunName(gunType), Guns.getDecibel(gunType));
                    checkShot(mousePosition);
                    if (hit) {
                        killedObjects++;
                        feverCnt++;
                        hit = false;
                        GameView.instance().drawFever();
                    }
                    lastTimeShoot = System.nanoTime();
                }
            }
        }
        if(health <= 0f)
            Framework.setGameState(Framework.GameState.GAMEOVER);
    }


    public void checkShot(Point mousePosition) {
        for(int i = 0; i < movingDucks.size(); i++)
        {
            if(new Rectangle(movingDucks.get(i).getX() + 18, movingDucks.get(i).getY()     , 27, 30).contains(mousePosition) ||
                    new Rectangle(movingDucks.get(i).getX() + 30, movingDucks.get(i).getY() + 30, 100, 35).contains(mousePosition))
            {
                shot(movingDucks, i);
                playSound("quack", -18.0f);
                return;
            }
        }
        for(int i = 0; i < movingPotionDucks.size(); i++)
        {
            if(new Rectangle(movingPotionDucks.get(i).getX() + 18, movingPotionDucks.get(i).getY()    , 27, 30).contains(mousePosition) ||
                    new Rectangle(movingPotionDucks.get(i).getX() + 30, movingPotionDucks.get(i).getY() + 30, 100, 35).contains(mousePosition))
            {
                shot(movingPotionDucks, i);
                playSound("quack", -18.0f);
                health = Math.min(100f, health + 30f);
                return;
            }
        }
        GameConfig.GunType gunType = GameConfig.getGunType();
        if (boss != null && new Rectangle(boss.getX(), boss.getY(), boss.getWidth(), boss.getHeight()).contains(mousePosition)) {
            if (boss.hit(gunType)) {
                score += (int) Math.floor(boss.getScore() * scoreMultiplier);
                boss = null;
                lastBossDeathTime = System.nanoTime();
            }
        }
    }

    public void ranAway() throws IOException {
        runawayObjects++;
        health -= 1f;
        feverCnt = 0;
        GameView.instance().drawFever();
    }

    public void saveScore() {
        int stage = GameConfig.getStage();
        if (score > ScoreBoard.getScore(stage)) {
            ScoreBoard.setScore(stage, score);
        }
    }

    public static MovingBossObject getBoss() {
        return boss;
    }

    public static List<Duck> getMovingDucks() {
        return movingDucks;
    }

    public static List<PotionDuck> getMovingPotionDucks() {
        return movingPotionDucks;
    }

    public static float getHealth() {
        return health;
    }

    public static int getKilledObjects() {
        return killedObjects;
    }

    public static int getShoots() {
        return shoots;
    }

    public static int getScore() {
        return score;
    }

    public static void setScoreMultiplier(double scoreMultiplier) {
        GameController.scoreMultiplier = scoreMultiplier;
    }

    public static double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public static int getFeverCnt() {
        return feverCnt;
    }

    public static boolean getBossAttacking() {
        return bossAttacking;
    }
}
