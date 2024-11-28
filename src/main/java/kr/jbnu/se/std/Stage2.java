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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage2 extends Game {
    private BufferedImage hawkImg;
    protected static ArrayList<Hawk> movingHawks;

    public Stage2() {
        super();
        stage = 2;
        movingHawks = new ArrayList<>();
    }

    @Override
    protected void loadContent()
    {
        super.loadContent();
        try
        {
            URL hawkImgUrl = this.getClass().getResource("/images/hawk.png");
            hawkImg = ImageIO.read(Objects.requireNonNull(hawkImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void restartGame() {
        super.restartGame();
        movingHawks.clear();
        Hawk.setLastObjectTime(0);
    }

    @Override
     protected void checkShot(Point mousePosition) {
        super.checkShot(mousePosition);
         for(int i = 0; i < movingHawks.size(); i++)
         {
             if(new Rectangle(movingHawks.get(i).getX(), movingHawks.get(i).getY(), 200, 100).contains(mousePosition))
             {
                 shot(movingHawks, i);
                 return;
             }
         }
    }

    @Override
    public void updateGame(Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - Hawk.getLastObjectTime() >= Hawk.getTimeBetweenObjects())
        {
            movingHawks.add(new Hawk(hawkImg));

            Hawk.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingHawks.size(); i++)
        {
            movingHawks.get(i).update();

            if(movingHawks.get(i).getX() < -hawkImg.getWidth())
            {
                movingHawks.remove(i);
                ranAway();
            }
        }

        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 20) {
            boss = new Eagle(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }

        super.updateGame(mousePosition);
    }

    @Override
    public void draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.drawBack(g2d);
        for (Hawk hawk : movingHawks) {
            hawk.draw(g2d);
        }
        super.drawFront(g2d, mousePosition);
    }
}
