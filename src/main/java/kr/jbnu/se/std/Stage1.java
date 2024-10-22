package kr.jbnu.se.std;

import java.awt.Point;
import java.io.IOException;
import javax.sound.sampled.*;

public class Stage1 extends Game {

    public Stage1() {
        stage = 1;
    }

    protected void Initialize()
    {
        super.Initialize();
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
}