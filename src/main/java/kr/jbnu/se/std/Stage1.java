package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.print.attribute.standard.Media;
import javax.sound.sampled.*;

public class Stage1 extends Game {

    private BufferedImage duckImg;
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
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

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
                killedObjects++;
                feverCnt++;
                score += movingDucks.get(i).score;

                movingDucks.remove(i);

                return;
            }
        }
        feverCnt = 0;
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
//                String soundName = "src/main/resources/audio/gunshot_1.mp3";
//                String path = new File(soundName).getAbsoluteFile().getPath();
//                System.out.println(path);
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
//                Clip clip = AudioSystem.getClip();
//                clip.open(audioInputStream);
//                clip.start();

//                String soundName = "src/main/resources/audio/gunshot_1.mp3";
//                AudioInputStream ais = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
//                DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat());
//                Clip clip = (Clip) AudioSystem.getLine(info);
//                clip.open(ais);
//                clip.start();
//                Thread.sleep(1000); // plays up to 6 seconds of sound before exiting
//                clip.close();

                shoots++;

                CheckShot(mousePosition);
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
