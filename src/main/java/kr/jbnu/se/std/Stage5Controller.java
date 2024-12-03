package kr.jbnu.se.std;

import java.io.IOException;

public class Stage5Controller extends GameController {

    public Stage5Controller() {
        super();
        GameConfig.setStage(5);
    }

    public static void updateGame() throws IOException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 5) {
            boss = new UFO();
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
    }
}
