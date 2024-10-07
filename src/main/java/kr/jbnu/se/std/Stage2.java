package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage2 extends Stage1 {
    private BufferedImage hawkImg;
    private BufferedImage eagleImg;
    protected ArrayList<Hawk> movingHawks;
    protected ArrayList<Eagle> movingEagles;

    public Stage2(){}

    protected void Initialize() {
        super.Initialize();
        this.movingHawks = new ArrayList<Hawk>();
        this.movingEagles = new ArrayList<Eagle>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background2.jpg");
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL grassImgUrl = this.getClass().getResource("/images/grass2.png");
            this.grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));

            URL hawkImgUrl = this.getClass().getResource("/images/hawk.png");
            hawkImg = ImageIO.read(Objects.requireNonNull(hawkImgUrl));

            URL eagleImgUrl = this.getClass().getResource("/images/eagle.png");
            eagleImg = ImageIO.read(Objects.requireNonNull(eagleImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingHawks.clear();
        movingEagles.clear();
        Hawk.lastObjectTime = 0;
        Eagle.lastObjectTime = 0;
    }

     protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
         for(int i = 0; i < movingHawks.size(); i++)
         {
             if(new Rectangle(movingHawks.get(i).x, movingHawks.get(i).y, 200, 100).contains(mousePosition))
             {
                 killedObjects++;
                 feverCnt++;
                 score += (int) Math.floor(movingHawks.get(i).score * scoreMultiplier);
                 shotX = movingHawks.get(i).x;
                 shotY = movingHawks.get(i).y;

                 movingHawks.remove(i);

                 return;
             }
         }
         for(int i = 0; i < movingEagles.size(); i++)
         {
             if(new Rectangle(movingEagles.get(i).x + 30, movingEagles.get(i).y + 120 , 270, 40).contains(mousePosition) ||
                     new Rectangle(movingEagles.get(i).x, movingEagles.get(i).y, 300, 200).contains(mousePosition))
             {
                 killedObjects++;
                 feverCnt++;
                 score += (int) Math.floor(movingEagles.get(i).score * scoreMultiplier);
                 shotX = movingEagles.get(i).x;
                 shotY = movingEagles.get(i).y;

                 movingEagles.remove(i);

                 return;
             }
         }
         feverCnt = 0;
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
                runawayObjects++;
            }
        }

        if(System.nanoTime() - Eagle.lastObjectTime >= Eagle.timeBetweenObjects)
        {
            movingEagles.add(new Eagle(eagleImg));

            Eagle.nextObjectLines++;
            if(Eagle.nextObjectLines >= Eagle.objectLines.length)
                Eagle.nextObjectLines = 0;

            Eagle.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingEagles.size(); i++)
        {
            movingEagles.get(i).Update();

            if(movingEagles.get(i).x < -eagleImg.getWidth())
            {
                movingEagles.remove(i);
                runawayObjects++;
            }
        }

        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        super.DrawBack(g2d);
        for (Duck duck : this.movingDucks) {
            duck.Draw(g2d);
        }
        for (Hawk hawk : movingHawks) {
            hawk.Draw(g2d);
        }
        for (Eagle eagle : movingEagles) {
            eagle.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
