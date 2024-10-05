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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Stage2 extends Stage1 {
    private BufferedImage eagleImg;
    private ArrayList<Eagle> movingEagles;

    public Stage2(){}

    protected void Initialize() {
        super.Initialize();
        this.movingEagles = new ArrayList<Eagle>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background2.jpg");
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL eagleImgUrl = this.getClass().getResource("/images/eagle.png");
            eagleImg = ImageIO.read(Objects.requireNonNull(eagleImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingEagles.clear();
        Eagle.lastObjectTime = 0;
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        super.UpdateGame(gameTime, mousePosition);

        if(System.nanoTime() - Eagle.lastObjectTime >= Eagle.timeBetweenObjects)
        {
            // Here we create new duck and add it to the array list.
            movingEagles.add(new Eagle(Eagle.objectLines[Eagle.nextObjectLines][0] + random.nextInt(200), Eagle.objectLines[Eagle.nextObjectLines][1], Eagle.objectLines[Eagle.nextObjectLines][2], Eagle.objectLines[Eagle.nextObjectLines][3], eagleImg));

            // Here we increase nextDuckLines so that next duck will be created in next line.
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

        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;

                for(int i = 0; i < movingEagles.size(); i++)
                {
                    if(new Rectangle(movingEagles.get(i).x + 18, movingEagles.get(i).y     , 27, 30).contains(mousePosition) ||
                            new Rectangle(movingEagles.get(i).x + 30, movingEagles.get(i).y + 30, 100, 35).contains(mousePosition))
                    {
                        killedObjects++;
                        score += movingEagles.get(i).score;

                        movingEagles.remove(i);

                        break;
                    }
                }
                lastTimeShoot = System.nanoTime();
            }
        }
    }

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        super.DrawBack(g2d);
        for (Duck duck : this.movingDucks) {
            duck.Draw(g2d);
        }
        for (Eagle eagle : movingEagles) {
            eagle.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
