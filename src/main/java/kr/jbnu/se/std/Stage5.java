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

    public Stage5(){
        stage = 5;}

    protected void Initialize() {
        super.Initialize();
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
}
