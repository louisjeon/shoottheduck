package kr.jbnu.se.std;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;

public class Stage5 extends Game {

    public Stage5() {
        super();
        stage = 5;
    }

    @Override
    public void updateGame(Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 5) {
            boss = new UFO(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
        super.updateGame(mousePosition);
    }
}
