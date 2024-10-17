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

public class Stage3 extends Stage2 {
    private BufferedImage crowImg;
    protected ArrayList<Crow> movingCrows;

    public Stage3(){}

    protected void Initialize() {
        super.Initialize();
        this.movingCrows = new ArrayList<Crow>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background3.jpg");
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL grassImgUrl = this.getClass().getResource("/images/grass3.png");
            this.grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));

            URL duckImgUrl = this.getClass().getResource("/images/duck3.png");
            this.duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));

            URL crowImgUrl = this.getClass().getResource("/images/crow.png");
            crowImg = ImageIO.read(Objects.requireNonNull(crowImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingCrows.clear();
        Crow.lastObjectTime = 0;
    }

     protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
         for(int i = 0; i < movingCrows.size(); i++)
         {
             if(new Rectangle(movingCrows.get(i).x, movingCrows.get(i).y, 100, 100).contains(mousePosition))
             {
                 Shot(movingCrows, i);
                 return;
             }
         }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - Crow.lastObjectTime >= Crow.timeBetweenObjects)
        {
            movingCrows.add(new Crow(crowImg));

            Crow.nextObjectLines++;
            if(Crow.nextObjectLines >= Crow.objectLines.length)
                Crow.nextObjectLines = 0;

            Crow.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingCrows.size(); i++)
        {
            movingCrows.get(i).Update();

            if(movingCrows.get(i).x < -crowImg.getWidth())
            {
                movingCrows.remove(i);
                RanAway();
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
        for (Crow crow : movingCrows) {
            crow.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
