package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Stage1 extends Game {

    protected BufferedImage duckImg;
    protected ArrayList<Duck> movingDucks;

    public Stage1() {}

    protected void Initialize()
    {
        super.Initialize();
        this.movingDucks = new ArrayList<Duck>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
            backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame()
    {
        super.RestartGame();
        movingDucks.clear();
        Duck.lastObjectTime = 0;
    }

     protected void CheckShot(Point mousePosition) {
        for(int i = 0; i < movingDucks.size(); i++)
        {
            if(new Rectangle(movingDucks.get(i).x + 18, movingDucks.get(i).y     , 27, 30).contains(mousePosition) ||
                    new Rectangle(movingDucks.get(i).x + 30, movingDucks.get(i).y + 30, 100, 35).contains(mousePosition))
            {
                Shot(movingDucks, i);
                return;
            }
        }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if(System.nanoTime() - Duck.lastObjectTime >= Duck.timeBetweenObjects)
        {
            movingDucks.add(new Duck(duckImg));

            Duck.nextObjectLines++;
            if(Duck.nextObjectLines >= Duck.objectLines.length)
                Duck.nextObjectLines = 0;

            Duck.lastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingDucks.size(); i++)
        {
            movingDucks.get(i).Update();

            if(movingDucks.get(i).x < -duckImg.getWidth())
            {
                movingDucks.remove(i);
                runawayObjects++;
            }
        }

        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;
                CheckShot(mousePosition);
                if (!hit) {
                    feverCnt = 0;
                } else {
                    killedObjects++;
                    feverCnt++;
                    hit = false;
                }
                DrawFever();
                lastTimeShoot = System.nanoTime();
            }
        }
        if(runawayObjects >= 200)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        super.DrawBack(g2d);
        for (Duck duck : movingDucks) {
            duck.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
