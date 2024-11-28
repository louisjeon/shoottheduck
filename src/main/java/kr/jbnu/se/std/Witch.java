package kr.jbnu.se.std;

import java.io.IOException;

public class Witch extends MovingObject {
    private static final int[][] objectLines = {
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.10), -20, 200},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.3), -20, 200},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.4), -20, 200},
                                       {Framework.getFrameWidth(), (int)(Framework.getFrameHeight() * 0.5), -20, 200}
                                      };

    public Witch() throws IOException {
        super( GameModel.stage(GameConfig.getStage()).getBossImg());
    }
}
