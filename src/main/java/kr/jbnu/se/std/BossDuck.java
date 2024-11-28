package kr.jbnu.se.std;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BossDuck extends MovingBossObject {
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.30), -3, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.35), -4, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.40), -5, 500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.45), -6, 500}
    };

    public BossDuck() throws IOException {
        super(objectLines, 100, GameModel.stage(GameConfig.getStage()).getBossImg());
    }
}