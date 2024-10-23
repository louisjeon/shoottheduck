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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage5 extends Game {

    public Stage5() throws IOException, ExecutionException, InterruptedException {
        super();
        stage = 5;}

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.secInNanosec * 5) {
            boss = new UFO(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.Update();
        }
        super.UpdateGame(gameTime, mousePosition);
    }
}
