package kr.jbnu.se.std;

public class Guns {
    private static final int[] defaultBullets = new int[]{6, 100, 1, 100, 200};
    private static final int[] bullets= new int[]{6, 100, 1, 100, 200};
    private static final String[] gunNames = new String[]{"revolver", "short", "wooden", "ak47", "machinegun"};
    private static final float[] gunDecibel = new float[]{-20.0f, -10.0f, -15.0f, -15.0f, -5.0f};
    private static final float[] reloadDecibel = new float[]{-20.0f, -20.0f, -15.0f, -15.0f, -15.0f};
    private static final int[] gunDamage = new int[]{10, 1, 20, 2, 3};

    public static int getDefaultBullets(GameConfig.GunType type) {
        return defaultBullets[type.ordinal()];
    }

    public static int getBullets(GameConfig.GunType type) {
        return bullets[type.ordinal()];
    }

    public static void setBulletsDefault(GameConfig.GunType type) {
        bullets[type.ordinal()] = defaultBullets[type.ordinal()];
    }

    public static void useBullet(GameConfig.GunType type) {
        bullets[type.ordinal()]--;
    }

    public static void resetBullets() {
        System.arraycopy(defaultBullets, 0, bullets, 0, bullets.length);
    }

    public static String getGunName(GameConfig.GunType type) {
        return gunNames[type.ordinal()];
    }

    public static float getDecibel(GameConfig.GunType type) {
        return gunDecibel[type.ordinal()];
    }

    public static float getReloadDecibel(GameConfig.GunType type) {
        return reloadDecibel[type.ordinal()];
    }

    public static int getDamage(GameConfig.GunType type) {
        return gunDamage[type.ordinal()];
    }
}
