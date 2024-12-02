package kr.jbnu.se.std;

import java.awt.Point;
import java.io.IOException;

public class Stage1Controller extends GameController {
    public Stage1Controller() {
        super();
        GameConfig.setStage(1);
    }

    public static void updateGame() throws IOException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 20) {
            boss = new BossDuck();
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
    }
}