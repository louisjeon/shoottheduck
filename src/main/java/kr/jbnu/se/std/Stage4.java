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

public class Stage4 extends Stage3 {
    private BufferedImage witchImg;
    protected ArrayList<Witch> movingWitches;

    public Stage4(){
        stage = 4;}

    protected void Initialize() {
        super.Initialize();
        this.movingWitches = new ArrayList<>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL witchImgUrl = this.getClass().getResource("/images/witch.png");
            witchImg = ImageIO.read(Objects.requireNonNull(witchImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        boss = new Pumpkin(bossImg);
        movingWitches.clear();
        Witch.lastObjectTime = 0;
    }

     protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
         for(int i = 0; i < movingWitches.size(); i++)
         {
             if(new Rectangle(movingWitches.get(i).x, movingWitches.get(i).y, 400, 200).contains(mousePosition))
             {
                 Shot(movingWitches, i);
                 return;
             }
         }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null) {
            boss = new Pumpkin(bossImg);
        } else {
            boss.Update();
        }
        if(System.nanoTime() - Witch.lastObjectTime >= Witch.timeBetweenObjects)
        {
            movingWitches.add(new Witch(witchImg));

            Witch.nextObjectLines++;
            if(Witch.nextObjectLines >= Witch.objectLines.length)
                Witch.nextObjectLines = 0;

            Witch.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingWitches.size(); i++)
        {
            movingWitches.get(i).Update();

            if(movingWitches.get(i).x < -witchImg.getWidth())
            {
                movingWitches.remove(i);
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
        for (Witch witch : movingWitches) {
            witch.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
