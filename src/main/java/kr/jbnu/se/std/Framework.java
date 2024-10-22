package kr.jbnu.se.std;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * kr.jbnu.se.std.Framework that controls the game (kr.jbnu.se.std.Game.java) that created it, update it and draw it on the screen.
 * 
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {
    
    /**
     * Width of the frame.
     */
    public static int frameWidth;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;
    
    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;
    
    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;

    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, UPDATE_FEVER}
    public static enum GameStage{STAGE1, STAGE2, STAGE3, STAGE4, STAGE5}
    /**
     * Current state of the game
     */
    public static GameState gameState;
    public static GameStage gameStage;
    
    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;
    
    // The actual game
    private Game game;
    
    
    /**
     * Image for menu.
     */
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
        Firestore db = FirebaseConfig.initialize();
        ApiFuture<QuerySnapshot> query = db.collection("scores").get();
        QuerySnapshot querySnapshot = query.get();
        querySnapshot.getDocuments().forEach(document -> {
            ScoreBoard.stage1 = Math.toIntExact(document.getLong("1"));
            ScoreBoard.stage1 = Math.toIntExact(document.getLong("2"));
            ScoreBoard.stage1 = Math.toIntExact(document.getLong("3"));
            ScoreBoard.stage1 = Math.toIntExact(document.getLong("4"));
            ScoreBoard.stage1 = Math.toIntExact(document.getLong("5"));
        });


//        DocumentReference docRef = db.collection("scores").document("stage");
//        Map<String, Object> data = new HashMap<>();
//        data.put("1", 999999);
//        ApiFuture<WriteResult> result = docRef.set(data);
//        System.out.println("Update time : " + result.get().getUpdateTime());
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                try {
                    GameLoop();
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in kr.jbnu.se.std.Game.java.
     */
    private void Initialize()
    {
        frog = Frog.getInstance();
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in kr.jbnu.se.std.Game.java.
     */
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
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    
                    game.UpdateGame(gameTime, mousePosition());
                    
                    lastTime = System.nanoTime();
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
                    // Load files - images, sounds, ...
                    LoadContent();

                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            // Repaint the screen.
            repaint();
            
            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            /**
             * Pause between updates. It is in nanoseconds.
             */
            long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10) 
                timeLeft = 10; //set a minimum
            try {
                 //Provides the necessary delay and also yields control so that other thread can do work.
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d) throws IOException {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition());
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
                game.DrawScoreWindow(g2d);
            break;
        }
    }
    
    /**
     * Starts new game.
     */
    private void newGame(int stage)
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();

        switch (stage) {
            case 1:
                game = new Stage1();
                break;
            case 2:
                game = new Stage2();
                break;
            case 3:
                game = new Stage3();
                break;
            case 4:
                game = new Stage4();
                break;
            case 5:
                game = new Stage5();
                break;
        }
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
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
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    game = null;
                    gameState = GameState.MAIN_MENU;
                }
                else if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
                break;
            case PLAYING:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (Objects.requireNonNull(gameState) == GameState.MAIN_MENU) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (new Rectangle(frameWidth / 2 - stage1BtnImg.getWidth() / 2, frameHeight / 2 - 160, stage1BtnImg.getWidth(), stage1BtnImg.getHeight()).contains(e.getPoint())) {
                    newGame(1);
                } else if (new Rectangle(frameWidth / 2 - stage2BtnImg.getWidth() / 2, frameHeight / 2 - 60, stage2BtnImg.getWidth(), stage2BtnImg.getHeight()).contains(e.getPoint())) {
                    newGame(2);
                } else if (new Rectangle(frameWidth / 2 - stage3BtnImg.getWidth() / 2, frameHeight / 2 + 40, stage3BtnImg.getWidth(), stage3BtnImg.getHeight()).contains(e.getPoint())) {
                    newGame(3);
                } else if (new Rectangle(frameWidth / 2 - stage4BtnImg.getWidth() / 2, frameHeight / 2 + 140, stage4BtnImg.getWidth(), stage4BtnImg.getHeight()).contains(e.getPoint())) {
                    newGame(4);
                } else if (new Rectangle(frameWidth / 2 - stage5BtnImg.getWidth() / 2, frameHeight / 2 + 240, stage5BtnImg.getWidth(), stage5BtnImg.getHeight()).contains(e.getPoint())) {
                    newGame(5);
                }
            }
        }
    }
}
