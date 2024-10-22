package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage5 extends Game {

    public Stage5(){
        stage = 5;}

    protected void Initialize() {
        super.Initialize();
    }

    public void RestartGame() {
        super.RestartGame();
        boss = new UFO(bossImg);
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null) {
            boss = new UFO(bossImg);
        } else {
            boss.Update();
        }
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
        if (bossAttacking) {
            if(new Rectangle(boss.x + bossImg.getWidth() / 2 - bossAttackImg.getWidth() / 2, boss.y + bossImg.getHeight() - 10, bossAttackImg.getWidth(), bossAttackImg.getHeight()).contains(rotationCenterX, rotationCenterY)) {
                health -= 0.5f;
            }

        }
        super.UpdateGame(gameTime, mousePosition);
    }
}
