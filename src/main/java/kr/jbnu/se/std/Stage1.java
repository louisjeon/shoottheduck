package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Stage1 extends Game {

    public Stage1() {}

    protected void Initialize()
    {
        super.Initialize();
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

            URL bossImgUrl = this.getClass().getResource("/images/boss_duck.png");
            bossImg = ImageIO.read(Objects.requireNonNull(bossImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame()
    {
        super.RestartGame();
        boss = new BossDuck(bossImg);
    }

    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (boss == null) {
            boss = new BossDuck(bossImg);
        } else {
            boss.Update();
        }
        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.Draw(g2d, mousePosition);
    }
}