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

public class Stage5 extends Game {

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

            URL bossImgUrl = this.getClass().getResource("/images/UFO.png");
            bossImg = ImageIO.read(Objects.requireNonNull(bossImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        boss = new UFO(bossImg);
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null) {
            boss = new UFO(bossImg);
        } else {
            boss.Update();
        }
        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.Draw(g2d, mousePosition);
    }
}
