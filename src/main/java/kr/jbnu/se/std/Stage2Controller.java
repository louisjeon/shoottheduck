package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage2Controller extends GameController {
    protected static ArrayList<Hawk> movingHawks = new ArrayList<>();

    public Stage2Controller() {
        super();
        GameConfig.setStage(2);
    }

    public static void restartGame() {
        GameController.restartGame();
        movingHawks.clear();
        Hawk.setLastObjectTime(0);
    }

     protected static void checkShot(Point mousePosition) {
        GameController.checkShot(mousePosition);
         for(int i = 0; i < movingHawks.size(); i++)
         {
             if(new Rectangle(movingHawks.get(i).getX(), movingHawks.get(i).getY(), 200, 100).contains(mousePosition))
             {
                 shot(movingHawks, i);
                 return;
             }
         }
    }

    public static void updateGame(Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if(System.nanoTime() - Hawk.getLastObjectTime() >= Hawk.getTimeBetweenObjects())
        {
            movingHawks.add(new Hawk());

            Hawk.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingHawks.size(); i++)
        {
            movingHawks.get(i).update();

            if(movingHawks.get(i).getX() < -GameModel.getHawkImg().getWidth())
            {
                movingHawks.remove(i);
                ranAway();
            }
        }

        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 20) {
            boss = new Eagle();
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }

        GameController.updateGame(mousePosition);
    }

    public void draw(Graphics2D g2d, Point mousePosition) throws IOException {
        for (Hawk hawk : movingHawks) {
            hawk.draw(g2d);
        }
    }
}
