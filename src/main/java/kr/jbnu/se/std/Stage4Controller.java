package kr.jbnu.se.std;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Stage4Controller extends Stage3Controller {
    protected static ArrayList<Witch> movingWitches = new ArrayList<>();

    public Stage4Controller() {
        super();
        GameConfig.setStage(4);
    }

    public static void restartGame() {
        movingWitches.clear();
        Witch.setLastObjectTime(0);
    }

    protected static void checkShot(Point mousePosition) {
        Stage3Controller.checkShot(mousePosition);
        for(int i = 0; i < movingWitches.size(); i++)
        {
            if(new Rectangle(movingWitches.get(i).getX(), movingWitches.get(i).getY(), 400, 200).contains(mousePosition))
            {
                shot(movingWitches, i);
                return;
            }
        }
    }

    public static void updateGame() throws IOException {
        Stage3Controller.updateGame();
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 20) {
            boss = new Pumpkin();
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
        if(System.nanoTime() - Witch.getLastObjectTime() >= Witch.getTimeBetweenObjects())
        {
            movingWitches.add(new Witch());

            Witch.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingWitches.size(); i++)
        {
            movingWitches.get(i).update();

            if(movingWitches.get(i).getX() < -GameModel.getWitchImg().getWidth())
            {
                movingWitches.remove(i);
                ranAway();
            }
        }
    }

    public static void draw(Graphics2D g2d) {
        Stage3Controller.draw(g2d);
        for (Witch witch : movingWitches) {
            witch.draw(g2d);
        }
    }
}
