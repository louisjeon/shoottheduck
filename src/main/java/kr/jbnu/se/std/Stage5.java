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
    private ArrayList<UFO> movingUFOs;

    public Stage5(){}

    protected void Initialize() {
        super.Initialize();
        movingUFOs = new ArrayList<UFO>();
    }

    protected void LoadContent()
    {
        super.LoadContent();
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background5.jpg");
            this.backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));

            URL grassImgUrl = this.getClass().getResource("/images/grass5.png");
            this.grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));

            URL duckImgUrl = this.getClass().getResource("/images/duck5.png");
            this.duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));

            URL UFOImgUrl = this.getClass().getResource("/images/UFO.png");
            UFOImg = ImageIO.read(Objects.requireNonNull(UFOImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Stage5.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RestartGame() {
        super.RestartGame();
        movingUFOs.clear();
        UFO.lastObjectTime = 0;
    }

    protected void CheckShot(Point mousePosition) {
        super.CheckShot(mousePosition);
        for(int i = 0; i < movingUFOs.size(); i++)
        {
            if(new Rectangle(movingUFOs.get(i).x, movingUFOs.get(i).y, 260, 200).contains(mousePosition))
            {
                Shot(movingUFOs, i);
                return;
            }
        }
    }

    int ufo=0;//ufo가 한 번 나올 수 있도록 도와주는 변수
    public void UpdateGame(long gameTime, Point mousePosition) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (ufo == 0) {
            movingUFOs.add(new UFO(UFOImg));
            ufo++;
        }

        for (int i = 0; i < movingUFOs.size(); i++) {
            movingUFOs.get(i).Update();

            // UFO가 화면을 벗어나는 조건을 제거합니다.
            // 대신 UFO가 멈춘 상태에서 일정 시간이 지나면 제거할 수 있습니다.
            // 예를 들어:
            // if (movingUFOs.get(i).stopped && gameTime - movingUFOs.get(i).stoppedTime > 10000) {
            //     movingUFOs.remove(i);
            //     i--;
            // }
        }
        super.UpdateGame(gameTime, mousePosition);
    }

    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        super.DrawBack(g2d);
        for (Duck duck : this.movingDucks) {
            duck.Draw(g2d);
        }
        for (UFO ufo : this.movingUFOs) {
            ufo.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}
