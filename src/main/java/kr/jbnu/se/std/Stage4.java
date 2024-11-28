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

public class Stage4 extends Stage3 {
    private BufferedImage witchImg;
    protected static ArrayList<Witch> movingWitches;

    public Stage4() {
        super();
        stage = 4;
        movingWitches = new ArrayList<>();
    }

    @Override
    protected void loadContent()
    {
        super.loadContent();
        try
        {
            URL witchImgUrl = this.getClass().getResource("/images/witch.png");
            witchImg = ImageIO.read(Objects.requireNonNull(witchImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void restartGame() {
        super.restartGame();
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
            boss = new Pumpkin(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.update();
        }
        if(System.nanoTime() - Witch.getLastObjectTime() >= Witch.getTimeBetweenObjects())
        {
            movingWitches.add(new Witch(witchImg));

            Witch.setLastObjectTime(System.nanoTime());
        }

        for(int i = 0; i < movingWitches.size(); i++)
        {
            movingWitches.get(i).update();

            if(movingWitches.get(i).getX() < -witchImg.getWidth())
            {
                movingWitches.remove(i);
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
        for (Witch witch : movingWitches) {
            witch.draw(g2d);
        }
        super.drawFront(g2d, mousePosition);
    }
}
