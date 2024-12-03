package kr.jbnu.se.std;

import java.io.IOException;

public class UFO extends MovingBossObject {
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.30), -25, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.35), -25, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.40), -25, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.45), -25, 500}
    };

    public UFO() throws IOException {
        super(objectLines, 100, GameModel.stage(GameConfig.getStage()).getBossImg());
    }
}