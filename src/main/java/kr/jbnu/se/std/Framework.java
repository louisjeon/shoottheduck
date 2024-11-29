package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Framework extends Canvas {
    private static int frameWidth;
    private static int frameHeight;
    public static final long SEC_IN_NANOSEC = 1000000000L;
    public static final long MILISEC_IN_NANOSEC = 1000000L;
    public enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, UPDATE_FEVER}
    private static GameState gameState;
    private Game game;

    private BufferedImage shootTheDuckMenuImg;
    private BufferedImage titleImg;
    private  BufferedImage stage1BtnImg;
    private  BufferedImage stage2BtnImg;
    private  BufferedImage stage3BtnImg;
    private  BufferedImage stage4BtnImg;
    private  BufferedImage stage5BtnImg;

    private Frog frog;

    public Framework () throws IOException, ExecutionException, InterruptedException {
        super();
        ScoreBoard.getInstance();
        
        gameState = GameState.VISUALIZING;

        Thread gameThread = new Thread() {
            @Override
            public void run(){
                try {
                    GameLoop();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        gameThread.start();
    }

    private void Initialize()
    {
        frog = Frog.getInstance();
    }

    public static int getFrameWidth() {
        return frameWidth;
    }

    public static int getFrameHeight() {
        return frameHeight;
    }

    private void LoadContent()
    {
        try
        {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(Objects.requireNonNull(shootTheDuckMenuImgUrl));
            URL titleImgUrl = this.getClass().getResource("/images/title.png");
            titleImg = ImageIO.read(Objects.requireNonNull(titleImgUrl));
            URL stage1BtnImgUrl = this.getClass().getResource("/images/stage1.png");
            stage1BtnImg = ImageIO.read(Objects.requireNonNull(stage1BtnImgUrl));
            URL stage2BtnImgUrl = this.getClass().getResource("/images/stage2.png");
            stage2BtnImg = ImageIO.read(Objects.requireNonNull(stage2BtnImgUrl));
            URL stage3BtnImgUrl = this.getClass().getResource("/images/stage3.png");
            stage3BtnImg = ImageIO.read(Objects.requireNonNull(stage3BtnImgUrl));
            URL stage4BtnImgUrl = this.getClass().getResource("/images/stage4.png");
            stage4BtnImg = ImageIO.read(Objects.requireNonNull(stage4BtnImgUrl));
            URL stage5BtnImgUrl = this.getClass().getResource("/images/stage5.png");
            stage5BtnImg = ImageIO.read(Objects.requireNonNull(stage5BtnImgUrl));
        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GameLoop() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    GameController.updateGame(mousePosition());
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    //...
                break;
                case OPTIONS:
                    //...
                break;
                case GAME_CONTENT_LOADING:
                    //...
                break;
                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    if(this.getWidth() > 1 && visualizingTime > SEC_IN_NANOSEC)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }

            repaint();

            timeTaken = System.nanoTime() - beginTime;
            int GAME_FPS = 60;
            long GAME_UPDATE_PERIOD = SEC_IN_NANOSEC / GAME_FPS;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / MILISEC_IN_NANOSEC; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }

    public static void setGameState(GameState gs) {
        gameState = gs;
    }

    @Override
    public void Draw(Graphics2D g2d) throws IOException {
        switch (gameState)
        {
            case PLAYING:
                GameView.instance().draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                GameView.instance().drawGameOver(g2d, mousePosition());
            break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.setColor(Color.white);
                g2d.drawString("Use left mouse button to shot the duck.", frameWidth / 2 - 123, (int)(frameHeight * 0.94));
                g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 140, (int)(frameHeight * 0.96));
                g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 115, (int)(frameHeight * 0.98));
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
                g2d.drawImage(stage1BtnImg, frameWidth / 2 - stage1BtnImg.getWidth() / 2, frameHeight / 2 - 160, null);
                g2d.drawImage(stage2BtnImg, frameWidth / 2 - stage2BtnImg.getWidth() / 2, frameHeight / 2 - 60, null);
                g2d.drawImage(stage3BtnImg, frameWidth / 2 - stage3BtnImg.getWidth() / 2, frameHeight / 2 + 40, null);
                g2d.drawImage(stage4BtnImg, frameWidth / 2 - stage4BtnImg.getWidth() / 2, frameHeight / 2 + 140, null);
                g2d.drawImage(stage5BtnImg, frameWidth / 2 - stage5BtnImg.getWidth() / 2, frameHeight / 2 + 240, null);
                g2d.drawImage(titleImg, frameWidth / 2 - titleImg.getWidth() / 2, -50,null);
            break;
            case OPTIONS:
                //...
            break;
            case GAME_CONTENT_LOADING:
                if (game != null) {
                    GameView.instance().drawScoreWindow(g2d);
                }
            break;
        }
    }

    private void newGame() throws IOException, ExecutionException, InterruptedException {
        game = new Game();
    }

    private void restartGame() {
        GameController.restartGame();

        gameState = GameState.PLAYING;
    }

    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }

    @Override
    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState)
        {
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    GameController.saveScore();
                    game = null;
                    gameState = GameState.MAIN_MENU;
                }
                else if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
                break;
            case PLAYING:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    GameController.saveScore();
                    game = null;
                    gameState = GameState.MAIN_MENU;
                }
                break;
            case MAIN_MENU:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                break;
        }
    }

    @Override
    public void keyPressedFramework(KeyEvent e) {
        if (Objects.requireNonNull(gameState) == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                frog.setXChange(frog.getXChange() - 50);
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                frog.setXChange(frog.getXChange() + 50);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (Objects.requireNonNull(gameState) == GameState.MAIN_MENU) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                boolean clicked = false;
                if (new Rectangle(frameWidth / 2 - stage1BtnImg.getWidth() / 2, frameHeight / 2 - 160, stage1BtnImg.getWidth(), stage1BtnImg.getHeight()).contains(e.getPoint())) {
                    GameConfig.setStage(1);
                    clicked = true;
                } else if (new Rectangle(frameWidth / 2 - stage2BtnImg.getWidth() / 2, frameHeight / 2 - 60, stage2BtnImg.getWidth(), stage2BtnImg.getHeight()).contains(e.getPoint())) {
                    GameConfig.setStage(2);
                    clicked = true;
                } else if (new Rectangle(frameWidth / 2 - stage3BtnImg.getWidth() / 2, frameHeight / 2 + 40, stage3BtnImg.getWidth(), stage3BtnImg.getHeight()).contains(e.getPoint())) {
                    GameConfig.setStage(3);
                    clicked = true;
                } else if (new Rectangle(frameWidth / 2 - stage4BtnImg.getWidth() / 2, frameHeight / 2 + 140, stage4BtnImg.getWidth(), stage4BtnImg.getHeight()).contains(e.getPoint())) {
                    GameConfig.setStage(4);
                    clicked = true;
                } else if (new Rectangle(frameWidth / 2 - stage5BtnImg.getWidth() / 2, frameHeight / 2 + 240, stage5BtnImg.getWidth(), stage5BtnImg.getHeight()).contains(e.getPoint())) {
                    GameConfig.setStage(5);
                    clicked = true;
                }
                if (clicked) {
                    try {
                        newGame();
                    } catch (IOException | ExecutionException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }
}
