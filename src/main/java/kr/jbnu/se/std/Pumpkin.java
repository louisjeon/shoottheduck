package kr.jbnu.se.std;

import java.io.IOException;

public class Pumpkin extends MovingBossObject {
    private static final int[][] objectLines = {
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.10), -2, 2500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.15), -3, 2500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.20), -4, 2500},
            {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.25), -5, 2500}
    }; // 초기 체력 설정

    public Pumpkin() throws IOException {
        super(objectLines, 300,GameModel.stage(GameConfig.getStage()).getBossImg());
    }
}