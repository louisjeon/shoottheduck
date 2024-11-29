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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage4Controller extends Stage3Controller {
    protected static ArrayList<Witch> movingWitches = new ArrayList<>();;

    public Stage4Controller() {
        super();
        GameConfig.setStage(4);
    }

    public static void restartGame() {
        GameController.restartGame();
        movingWitches.clear();
        Witch.setLastObjectTime(0);
    }

    @Override
    protected void checkShot(Point mousePosition) {
        super.checkShot(mousePosition);
        for(int i = 0; i < movingWitches.size(); i++)
        {
            if(new Rectangle(movingWitches.get(i).getX(), movingWitches.get(i).getY(), 400, 200).contains(mousePosition))
            {
                shot(movingWitches, i);
                return;
            }
        }
    }

    @Override
    public void updateGame(Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
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

        super.updateGame(mousePosition);
    }

    @Override
    public void draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.draw(g2d, mousePosition);
        for (Witch witch : movingWitches) {
            witch.draw(g2d);
        }
    }
}
