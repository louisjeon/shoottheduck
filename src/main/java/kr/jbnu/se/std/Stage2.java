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

public class Stage2 extends Game {
    private BufferedImage hawkImg;
    protected ArrayList<Hawk> movingHawks;

    public Stage2(){
        stage = 2;
    }

    protected void Initialize() {
        super.Initialize();
        this.movingHawks = new ArrayList<>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL hawkImgUrl = this.getClass().getResource("/images/hawk.png");
            hawkImg = ImageIO.read(Objects.requireNonNull(hawkImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingHawks.clear();
        Hawk.lastObjectTime = 0;
    }

     protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
         for(int i = 0; i < movingHawks.size(); i++)
         {
             if(new Rectangle(movingHawks.get(i).x, movingHawks.get(i).y, 200, 100).contains(mousePosition))
             {
                 Shot(movingHawks, i);
                 return;
             }
         }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - Hawk.lastObjectTime >= Hawk.timeBetweenObjects)
        {
            movingHawks.add(new Hawk(hawkImg));

            Hawk.nextObjectLines++;
            if(Hawk.nextObjectLines >= Hawk.objectLines.length)
                Hawk.nextObjectLines = 0;

            Hawk.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingHawks.size(); i++)
        {
            movingHawks.get(i).Update();

            if(movingHawks.get(i).x < -hawkImg.getWidth())
            {
                movingHawks.remove(i);
                RanAway();
            }
        }

        if (boss == null && System.nanoTime() - lastBossDeathTime > Framework.secInNanosec * 20) {
            boss = new Eagle(bossImg);
            lastBossAttackTime = System.nanoTime();
        } else if (boss != null) {
            boss.Update();
        }

        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.DrawBack(g2d);
        for (Hawk hawk : movingHawks) {
            hawk.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
