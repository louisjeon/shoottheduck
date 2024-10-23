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
    protected ArrayList<Bat> movingBats;

    public Stage3() throws IOException, ExecutionException, InterruptedException {
        super();
        stage = 3;}

    protected void Initialize() {
        super.Initialize();
        this.movingBats = new ArrayList<>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL batImgUrl = this.getClass().getResource("/images/bat.png");
            batImg = ImageIO.read(Objects.requireNonNull(batImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() throws ExecutionException, InterruptedException {
        super.RestartGame();
        movingBats.clear();
        Bat.lastObjectTime = 0;
    }

    protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
        for(int i = 0; i < movingBats.size(); i++)
        {
            if(new Rectangle(movingBats.get(i).x, movingBats.get(i).y, 40, 30).contains(mousePosition))
            {
                Shot(movingBats, i);
                return;
            }
        }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.secInNanosec * 20) {
            boss = new Crow(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.Update();
        }
        if(System.nanoTime() - Bat.lastObjectTime >= Bat.timeBetweenObjects)
        {
            movingBats.add(new Bat(batImg));

            Bat.nextObjectLines++;
            if(Bat.nextObjectLines >= Bat.objectLines.length)
                Bat.nextObjectLines = 0;

            Bat.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingBats.size(); i++)
        {
            movingBats.get(i).Update();

            if(movingBats.get(i).x < -batImg.getWidth())
            {
                movingBats.remove(i);
                RanAway();
            }
        }
        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.DrawBack(g2d);
        for (Hawk hawk : movingHawks) {
            hawk.Draw(g2d);
        }
        for (Bat bat : movingBats) {
            bat.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
