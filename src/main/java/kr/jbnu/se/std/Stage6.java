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

public class Stage6 extends Stage1 {
    private BufferedImage pumpkinImg;
    protected ArrayList<Pumpkin> movingPumpkin;

    public Stage6(){
    }

    protected void Initialize() {
        super.Initialize();
        this.movingPumpkin = new ArrayList<Pumpkin>();
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

            URL pumpkinImgUrl = this.getClass().getResource("/images/pumpkin.png");
            pumpkinImg = ImageIO.read(Objects.requireNonNull(pumpkinImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingPumpkin.clear();
        Pumpkin.pumpkinLastObjectTime = 0;
    }

    boolean checkPumpkinDied(int index) {
        Pumpkin pumpkin = movingPumpkin.get(index);

        if(pumpkin.getPumpkinHealth() <= 0 ){

            return true;
        }
        return false;
    }

    protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
        for(int i = 0; i < movingPumpkin.size(); i++)
        {
            if(new Rectangle(movingPumpkin.get(i).x, movingPumpkin.get(i).y, 600, 600).contains(mousePosition) && checkPumpkinDied(i))
            {
                Shot(movingPumpkin, i);
                return;
            }
        }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        // Creates a new duck, if it's the time, and add it to the array list.
        Pumpkin pumpkin = new Pumpkin(pumpkinImg); //pumpkin 생성
        if(System.nanoTime() - Pumpkin.pumpkinLastObjectTime >= Pumpkin.pumpkinTimeBetweenObjects)
        {
            movingPumpkin.add(new Pumpkin(pumpkinImg));

            Pumpkin.nextObjectLines++;
            if(Pumpkin.nextObjectLines >= Pumpkin.objectLines.length)
                Pumpkin.nextObjectLines = 0;

            Pumpkin.pumpkinLastObjectTime = System.nanoTime();
        }

        for(int i = 0; i < movingPumpkin.size(); i++)
        {
            movingPumpkin.get(i).Update();

            if(movingPumpkin.get(i).x < -pumpkinImg.getWidth())
            {
                movingPumpkin.remove(i);
                RanAway();
            }
        }

        super.UpdateGame(gameTime, mousePosition);
    }

}
