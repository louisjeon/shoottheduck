package kr.jbnu.se.std;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Stage3Controller extends Stage2Controller {
    protected static ArrayList<Bat> movingBats = new ArrayList<>();

    public Stage3Controller() {
        super();
        GameConfig.setStage(3);
    }

    public static void restartGame()  {
        movingBats.clear();
        Bat.setLastObjectTime(0);
    }

    protected static void checkShot(Point mousePosition) {
        Stage2Controller.checkShot(mousePosition);
        for(int i = 0; i < movingBats.size(); i++)
        {
            if(new Rectangle(movingBats.get(i).getX(), movingBats.get(i).getY(), 40, 30).contains(mousePosition))
            {
                shot(movingBats, i);
                return;
            }
        }
    }

    public static void updateGame() throws IOException {
        Stage2Controller.updateGame();
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 20) {
            boss = new Crow();
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
        if(System.nanoTime() - Bat.getLastObjectTime() >= Bat.getTimeBetweenObjects())
        {
            movingBats.add(new Bat());

            Bat.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingBats.size(); i++)
        {
            movingBats.get(i).update();

            if(movingBats.get(i).getX() < -GameModel.getBatImg().getWidth())
            {
                movingBats.remove(i);
                ranAway();
            }
        }
    }

    public static void draw(Graphics2D g2d) {
        Stage2Controller.draw(g2d);
        for (Bat bat : movingBats) {
            bat.draw(g2d);
        }
    }
}
