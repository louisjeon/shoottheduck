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
    private UFO movingUFO;

    public Stage5(){}

    protected void Initialize() {
        super.Initialize();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background5.jpg");
            backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL grassImgUrl = this.getClass().getResource("/images/grass5.png");
            grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));

            URL duckImgUrl = this.getClass().getResource("/images/duck5.png");
            duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));

            URL UFOImgUrl = this.getClass().getResource("/images/UFO.png");
            UFOImg = ImageIO.read(Objects.requireNonNull(UFOImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingUFO = new UFO(UFOImg);
    }

    protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
        if(new Rectangle(movingUFO.x, movingUFO.y, 260, 200).contains(mousePosition))
        {
            PlaySound("alien_scream", -10.0f);
            if(movingUFO.hit(gunType)) {
                score += (int) Math.floor(movingUFO.score * scoreMultiplier);
                movingUFO = null;
            }
        }
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (movingUFO == null) {
            movingUFO = new UFO(UFOImg);
        } else {
            movingUFO.Update();
        }
        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.DrawBack(g2d);
        for (Duck duck : this.movingDucks) {
            duck.Draw(g2d);
        }
        if (movingUFO != null) {
            movingUFO.Draw(g2d);
        };
        super.DrawFront(g2d, mousePosition);
    }
}
