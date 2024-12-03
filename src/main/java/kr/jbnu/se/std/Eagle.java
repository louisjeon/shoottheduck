package kr.jbnu.se.std;

import java.io.IOException;

public class Eagle extends MovingBossObject {
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.30), -17, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.35), -16, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.40), -15, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.45), -14, 500}
    };

    public Eagle() throws IOException {
        super(objectLines, 50, GameModel.stage(GameConfig.getStage()).getBossImg());
    }
}