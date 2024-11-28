package kr.jbnu.se.std;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Game {
    protected Thread threadForInitGame;

    protected Game() {
        Framework.setGameState(Framework.GameState.GAME_CONTENT_LOADING);
        new ScheduledThreadPoolExecutor(1).schedule(() -> threadForInitGame.start(), 3, TimeUnit.SECONDS);

        threadForInitGame = new Thread() {
            @Override
            public void run(){
                GameController.initialize();
                Framework.setGameState(Framework.GameState.PLAYING);
            }
        };
    }
}
