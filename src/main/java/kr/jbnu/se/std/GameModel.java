package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GameModel {
    private static final GameModel[] gameModels = new GameModel[5];
    static {
        try {
            new GameModel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private BufferedImage duckImg;
    private BufferedImage grassImg;
    private BufferedImage backgroundImg;
    private static BufferedImage healthBarImg;
    private static BufferedImage[] feverBarImgs;
    private static BufferedImage gunEffectImg;
    private BufferedImage bossImg;
    private BufferedImage bossAttackImg;
    private BufferedImage potionDuckImg;
    private static Image feverFireGif;
    private static BufferedImage[] comboDigitImgs;
    private static BufferedImage[] currentCombiDigitImgs;
    private static BufferedImage frogImg;
    private static BufferedImage sightImg;
    private static BufferedImage weaponsImg;
    private static BufferedImage hawkImg;
    private static BufferedImage batImg;
    private static BufferedImage witchImg;

    private GameModel() throws IOException {
        URL healthBarImgUrl = this.getClass().getResource("/images/health_bar.png");
        URL gunEffectImgUrl = this.getClass().getResource("/images/gun_effect.png");
        URL sightImgUrl = this.getClass().getResource("/images/sight.png");
        URL hawkImgUrl = this.getClass().getResource("/images/hawk.png");
        URL batImgUrl = this.getClass().getResource("/images/bat.png");
        URL witchImgUrl = this.getClass().getResource("/images/witch.png");
        URL frogImgUrl = this.getClass().getResource("/images/frog_revolver.png");
        URL weaponsImgUrl = this.getClass().getResource("/images/weapons1.png");

        BufferedImage[] tmp = new BufferedImage[11];
        for (int i = 0; i < 11; i++) {
            URL feverBarImgUrl = this.getClass().getResource("/images/fever" + i + ".png");
            tmp[i] = ImageIO.read(Objects.requireNonNull(feverBarImgUrl));
        }
        feverBarImgs = tmp;

        BufferedImage[] tmp2 = new BufferedImage[10];
        for (int i = 0; i < 10; i++) {
            URL feverBarImgUrl = this.getClass().getResource("/images/number" + i + ".png");
            tmp2[i] = ImageIO.read(Objects.requireNonNull(feverBarImgUrl));
        }
        comboDigitImgs = tmp2;

        BufferedImage[] tmp3 = new BufferedImage[3];
        currentCombiDigitImgs = tmp3;

        healthBarImg = ImageIO.read(Objects.requireNonNull(healthBarImgUrl));
        gunEffectImg = ImageIO.read(Objects.requireNonNull(gunEffectImgUrl));
        sightImg = ImageIO.read(Objects.requireNonNull(sightImgUrl));
        hawkImg = ImageIO.read(Objects.requireNonNull(hawkImgUrl));
        batImg = ImageIO.read(Objects.requireNonNull(batImgUrl));
        witchImg = ImageIO.read(Objects.requireNonNull(witchImgUrl));
        frogImg = ImageIO.read(Objects.requireNonNull(frogImgUrl));
        weaponsImg = ImageIO.read(Objects.requireNonNull(weaponsImgUrl));
    }

    private GameModel(int stage) throws IOException {
        URL duckImgUrl = this.getClass().getResource("/images/duck" + stage + ".png");
        URL grassImgUrl = this.getClass().getResource("/images/grass" + stage + ".png");
        URL backgroundImgUrl = this.getClass().getResource("/images/background" + stage + ".jpg");
        URL potionDuckImgUrl = this.getClass().getResource("/images/duck" + stage +"_potion.png");
        URL bossImgUrl = this.getClass().getResource("/images/boss" + stage + ".png");
        URL bossAttackImgUrl = this.getClass().getResource("/images/boss_attack" + stage + ".png");

        duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));
        grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));
        backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));
        potionDuckImg = ImageIO.read(Objects.requireNonNull(potionDuckImgUrl));
        bossImg = ImageIO.read(Objects.requireNonNull(bossImgUrl));
        bossAttackImg = ImageIO.read(Objects.requireNonNull(bossAttackImgUrl));
    }

    public static GameModel stage(int stage) throws IOException {
        if (gameModels[stage-1] == null) {
            gameModels[stage-1] = new GameModel(stage);
        }
        return gameModels[stage-1];
    }

    public BufferedImage getDuckImg() {
        return duckImg;
    }

    public BufferedImage getGrassImg() {
        return grassImg;
    }

    public BufferedImage getBackgroundImg() {
        return backgroundImg;
    }

    public static BufferedImage getHealthBarImg() {
        return healthBarImg;
    }

    public static BufferedImage getFeverBarImg(int n) {
        return feverBarImgs[n];
    }

    public static BufferedImage getGunEffectImg() {
        return gunEffectImg;
    }

    public BufferedImage getBossImg() {
        return bossImg;
    }

    public BufferedImage getBossAttackImg() {
        return bossAttackImg;
    }

    public BufferedImage getPotionDuckImg() {
        return potionDuckImg;
    }

    public static void setFeverFireGif(Image img) {
        feverFireGif = img;
    }

    public static Image getFeverFireGif() {
        return feverFireGif;
    }

    public static BufferedImage getComboDigitImg(int n) {
        return currentCombiDigitImgs[n-1];
    }

    public static void setComboDigitImg(int n, int digit) {
        if (digit >= 0) {
            currentCombiDigitImgs[n-1] = comboDigitImgs[digit];
        } else {
            currentCombiDigitImgs[n-1] = null;
        }
    }

    public static void resetComboDigitImgs() {
        for (int i = 0; i < 3; i++) {
            currentCombiDigitImgs[i] = null;
        }
    }

    public static BufferedImage getFrogImg() {
        return frogImg;
    }

    public static BufferedImage getWeaponsImg() {
        return weaponsImg;
    }

    public static BufferedImage getSightImg() {
        return sightImg;
    }

    public static BufferedImage getBatImg() {
        return batImg;
    }

    public static BufferedImage getHawkImg() {
        return hawkImg;
    }

    public static BufferedImage getWitchImg() {
        return witchImg;
    }
}
