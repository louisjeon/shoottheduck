package kr.jbnu.se.std;

import java.awt.*;

public class GameConfig {
    private static int stage;
    public static final Font font = new Font("monospaced", Font.BOLD, 18);
    public enum GunType{REVOLVER, SHORT, WOODEN, AK47, MACHINEGUN}
    private static GunType gunType = GunType.REVOLVER;

    public static void setStage(int n) {
        stage = n;
    }

    public static int getStage() {
        return stage;
    }

    public static void setGunType(GunType type) {
        gunType = type;
    }

    public static GunType getGunType() {
        return gunType;
    }
}
