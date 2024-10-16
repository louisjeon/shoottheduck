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
    private BufferedImage batImg;
    protected ArrayList<Bat> movingBats;

    public Stage4(){}

    protected void Initialize() {
        super.Initialize();
        this.movingWitches = new ArrayList<Witch>();
        this.movingBats = new ArrayList<Bat>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background4.jpg");
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL grassImgUrl = this.getClass().getResource("/images/grass4.png");
            this.grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));

            URL duckImgUrl = this.getClass().getResource("/images/duck4.png");
            this.duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));

            URL witchImgUrl = this.getClass().getResource("/images/witch.png");
            witchImg = ImageIO.read(Objects.requireNonNull(witchImgUrl));

            URL batImgUrl = this.getClass().getResource("/images/bat.png");
            batImg = ImageIO.read(Objects.requireNonNull(batImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingWitches.clear();
        movingBats.clear();
        Witch.lastObjectTime = 0;
        Bat.lastObjectTime = 0;
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
        // Creates a new duck, if it's the time, and add it to the array list.
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
                runawayObjects++;
                feverCnt = 0;
            }
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
                runawayObjects++;
                feverCnt = 0;
            }
        }

        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.DrawBack(g2d);
        for (Duck duck : this.movingDucks) {
            duck.Draw(g2d);
        }
        for (Hawk hawk : this.movingHawks) {
            hawk.Draw(g2d);
        }
        for (Eagle eagle : this.movingEagles) {
            eagle.Draw(g2d);
        }
        for (Crow crow : this.movingCrows) {
            crow.Draw(g2d);
        }
        for (Witch witch : movingWitches) {
            witch.Draw(g2d);
        }
        for (Bat bat : movingBats) {
            bat.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
