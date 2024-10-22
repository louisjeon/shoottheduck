package kr.jbnu.se.std;

import java.awt.Point;
import java.io.IOException;
import javax.sound.sampled.*;

public class Stage1 extends Game {

    public Stage1() {
        stage = 1;
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.secInNanosec * 20) {
            boss = new BossDuck(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.Update();
        }
        super.UpdateGame(gameTime, mousePosition);
    }
}