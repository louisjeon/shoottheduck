package kr.jbnu.se.std;

import java.io.IOException;

public class Crow extends MovingBossObject {
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.30), -5, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.35), -6, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.40), -7, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.45), -8, 500}
    };

    public Crow() throws IOException {
        super(objectLines ,30, GameModel.stage(GameConfig.getStage()).getBossImg());
    }
}