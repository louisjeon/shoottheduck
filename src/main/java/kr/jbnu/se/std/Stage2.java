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
    private ArrayList<Hawk> movingHawks;
    private ArrayList<Eagle> movingEagles;

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
                 score += movingHawks.get(i).score;

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
                 score += movingEagles.get(i).score;

                 movingEagles.remove(i);

                 return;
             }
         }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        super.UpdateGame(gameTime, mousePosition);
        System.out.println(Duck.class);
//        addObject(, movingHawks, hawkImg);
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