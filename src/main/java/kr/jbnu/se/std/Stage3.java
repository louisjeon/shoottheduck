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

public class Stage3 extends Stage2 {
    private BufferedImage batImg;
    protected static ArrayList<Bat> movingBats;

    public Stage3() {
        super();
        stage = 3;
        movingBats = new ArrayList<>();
    }

    @Override
    protected void loadContent()
    {
        super.loadContent();
        try
        {
            URL batImgUrl = this.getClass().getResource("/images/bat.png");
            batImg = ImageIO.read(Objects.requireNonNull(batImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void restartGame()  {
        super.restartGame();
        movingBats.clear();
        Bat.setLastObjectTime(0);
    }

    @Override
    protected void checkShot(Point mousePosition) {
        super.checkShot(mousePosition);
        for(int i = 0; i < movingBats.size(); i++)
        {
            if(new Rectangle(movingBats.get(i).getX(), movingBats.get(i).getY(), 40, 30).contains(mousePosition))
            {
                shot(movingBats, i);
                return;
            }
        }
    }

    @Override
    public void updateGame(Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.SEC_IN_NANOSEC * 20) {
            boss = new Crow(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
        if(System.nanoTime() - Bat.getLastObjectTime() >= Bat.getTimeBetweenObjects())
        {
            movingBats.add(new Bat(batImg));

            Bat.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingBats.size(); i++)
        {
            movingBats.get(i).update();

            if(movingBats.get(i).getX() < -batImg.getWidth())
            {
                movingBats.remove(i);
                ranAway();
            }
        }
        super.updateGame(mousePosition);
    }

    @Override
    public void draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.drawBack(g2d);
        for (Hawk hawk : movingHawks) {
            hawk.draw(g2d);
        }
        for (Bat bat : movingBats) {
            bat.draw(g2d);
        }
        super.drawFront(g2d, mousePosition);
    }
}
