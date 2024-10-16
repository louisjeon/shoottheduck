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
                PlaySound("quack", -18.0f);
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
                feverCnt = 0;
            }
        }

        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            if (new Rectangle(0, 20, weaponsImg.getWidth(), weaponsImg.getHeight()).contains(mousePosition)) {
                if (new Rectangle(0, 20, (int) (weaponsImg.getWidth() * 0.1), weaponsImg.getHeight()).contains(mousePosition)) {
                    PlaySound("reload1", -30.0f);
                    gunType = GunTypes.REVOLVER;
                    timeBetweenShots = Framework.secInNanosec / 3;
                    timeBetweenReload = (long) (Framework.secInNanosec / 1.5);
                } else if (new Rectangle((int) (weaponsImg.getWidth() * 0.1), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    PlaySound("reload2", -10.0f);
                    gunType = GunTypes.SHORT;
                    timeBetweenShots = Framework.secInNanosec / 20;
                    timeBetweenReload = (Framework.secInNanosec) * 2;
                } else if (new Rectangle((int) (weaponsImg.getWidth() * 0.3), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    PlaySound("reload3", -30.0f);
                    gunType = GunTypes.WOODEN;
                    timeBetweenShots = Framework.secInNanosec;
                    timeBetweenReload = (Framework.secInNanosec) / 3;
                } else if (new Rectangle((int) (weaponsImg.getWidth() * 0.5), 20, (int) (weaponsImg.getWidth() * 0.2), weaponsImg.getHeight()).contains(mousePosition)) {
                    PlaySound("reload4", -30.0f);
                    gunType = GunTypes.AK47;
                    timeBetweenShots = Framework.secInNanosec / 10;
                    timeBetweenReload = (Framework.secInNanosec) * 2;
                } else {
                    PlaySound("reload5", -30.0f);
                    gunType = GunTypes.MACHINEGUN;
                    timeBetweenShots = Framework.secInNanosec / 20;
                    timeBetweenReload = (Framework.secInNanosec) * 3;
                };
            } else if (bullets.get(gunType) == 0) {
                PlaySound("reload" + gunIdx.get(gunType), gunDecibel.get(gunType));
                bullets.replace(gunType, defaultBullets.get(gunType));
                lastTimeReload = System.nanoTime();
            } else if(System.nanoTime() - lastTimeShoot >= timeBetweenShots && System.nanoTime() - lastTimeReload >= timeBetweenReload)
            {
                shoots++;
                bullets.replace(gunType, bullets.get(gunType) - 1);
                PlaySound(gunName.get(gunType), gunDecibel.get(gunType));
                CheckShot(mousePosition);
                if (hit) {
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

    public void Draw(Graphics2D g2d, Point mousePosition) throws IOException {
        super.DrawBack(g2d);
        for (Duck duck : movingDucks) {
            duck.Draw(g2d);
        }
        super.DrawFront(g2d, mousePosition);
    }
}