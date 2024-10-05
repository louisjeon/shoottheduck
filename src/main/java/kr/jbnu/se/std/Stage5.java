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

public class Stage5 extends Stage1 {
    private BufferedImage UFOImg;
    private ArrayList<UFO> movingUFOs;

    public Stage5(){}

    protected void Initialize() {
        super.Initialize();
        movingUFOs = new ArrayList<UFO>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background5.jpg");
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL grassImgUrl = this.getClass().getResource("/images/grass5.png");
            this.grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));

            URL UFOImgUrl = this.getClass().getResource("/images/UFO.png");
            UFOImg = ImageIO.read(Objects.requireNonNull(UFOImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingUFOs.clear();
        UFO.lastObjectTime = 0;
    }

     protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
         for(int i = 0; i < movingUFOs.size(); i++)
         {
             if(new Rectangle(movingUFOs.get(i).x, movingUFOs.get(i).y, 260, 200).contains(mousePosition))
             {
                 killedObjects++;
                 score += movingUFOs.get(i).score;

                 movingUFOs.remove(i);

                 return;
             }
         }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        if(System.nanoTime() - UFO.lastObjectTime >= UFO.timeBetweenObjects)
        {
            movingUFOs.add(new UFO(UFOImg));

            UFO.nextObjectLines++;
            if(UFO.nextObjectLines >= UFO.objectLines.length)
                UFO.nextObjectLines = 0;

            UFO.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingUFOs.size(); i++)
        {
            movingUFOs.get(i).Update();

            if(movingUFOs.get(i).x < -UFOImg.getWidth())
            {
                movingUFOs.remove(i);
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
        for (UFO ufo : this.movingUFOs) {
            ufo.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
