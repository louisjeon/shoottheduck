package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class GameModel {
    private static final GameModel[] gameModels = new GameModel[5];
    private final BufferedImage duckImg;
    private final BufferedImage grassImg;
    private final BufferedImage backgroundImg;
    private static BufferedImage healthBarImg;
    private static BufferedImage feverBarImg;
    private static BufferedImage gunEffectImg;
    private final BufferedImage bossImg;
    private final BufferedImage bossAttackImg;
    private final BufferedImage potionDuckImg;
    private static Image feverFireGif;
    private static BufferedImage combo1stDigitImg;
    private static BufferedImage combo2ndDigitImg;
    private static BufferedImage combo3rdDigitImg;
    private static BufferedImage frogImg;
    private static BufferedImage sightImg;
    private static BufferedImage weaponsImg;
    private static BufferedImage hawkImg;
    private static BufferedImage batImg;
    private static BufferedImage witchImg;

    private GameModel(int stage) throws IOException {
        URL duckImgUrl = this.getClass().getResource("/images/duck" + stage + ".png");
        URL grassImgUrl = this.getClass().getResource("/images/grass" + stage + ".png");
        URL healthBarImgUrl = this.getClass().getResource("/images/health_bar.png");
        URL feverBarImgUrl = this.getClass().getResource("/images/fever0.png");
        URL backgroundImgUrl = this.getClass().getResource("/images/background" + stage + ".jpg");
        URL potionDuckImgUrl = this.getClass().getResource("/images/duck" + stage +"_potion.png");
        URL bossImgUrl = this.getClass().getResource("/images/boss" + stage + ".png");
        URL bossAttackImgUrl = this.getClass().getResource("/images/boss_attack" + stage + ".png");
        URL gunEffectImgUrl = this.getClass().getResource("/images/gun_effect.png");
        URL sightImgUrl = this.getClass().getResource("/images/sight.png");
        URL frogImgUrl = this.getClass().getResource("/images/frog_revolver.png");
        URL weaponsImgUrl = this.getClass().getResource("/images/weapons1.png");
        URL hawkImgUrl = this.getClass().getResource("/images/hawk.png");
        URL batImgUrl = this.getClass().getResource("/images/bat.png");
        URL witchImgUrl = this.getClass().getResource("/images/witch.png");

        duckImg = ImageIO.read(Objects.requireNonNull(duckImgUrl));
        grassImg = ImageIO.read(Objects.requireNonNull(grassImgUrl));
        healthBarImg = ImageIO.read(Objects.requireNonNull(healthBarImgUrl));
        feverBarImg = ImageIO.read(Objects.requireNonNull(feverBarImgUrl));
        backgroundImg = ImageIO.read(Objects.requireNonNull(backgroundImgUrl));
        potionDuckImg = ImageIO.read(Objects.requireNonNull(potionDuckImgUrl));
        bossImg = ImageIO.read(Objects.requireNonNull(bossImgUrl));
        bossAttackImg = ImageIO.read(Objects.requireNonNull(bossAttackImgUrl));
        gunEffectImg = ImageIO.read(Objects.requireNonNull(gunEffectImgUrl));
        sightImg = ImageIO.read(Objects.requireNonNull(sightImgUrl));
        frogImg = ImageIO.read(Objects.requireNonNull(frogImgUrl));
        weaponsImg = ImageIO.read(Objects.requireNonNull(weaponsImgUrl));
        hawkImg = ImageIO.read(Objects.requireNonNull(hawkImgUrl));
        batImg = ImageIO.read(Objects.requireNonNull(batImgUrl));
        witchImg = ImageIO.read(Objects.requireNonNull(witchImgUrl));
    }

    public static GameModel stage(int stage) throws IOException {
        if (gameModels[stage] == null) {
            gameModels[stage] = new GameModel(stage);
        }
        return gameModels[stage];
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

    public static BufferedImage getFeverBarImg() {
        return feverBarImg;
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

    public static void setCombo1stDigitImg(BufferedImage img) {
        combo1stDigitImg = img;
    }

    public static BufferedImage getCombo1stDigitImg() {
        return combo1stDigitImg;
    }

    public static void setCombo2ndDigitImg(BufferedImage img) {
        combo2ndDigitImg = img;
    }

    public static BufferedImage getCombo2ndDigitImg() {
        return combo2ndDigitImg;
    }

    public static void setCombo3rdDigitImg(BufferedImage img) {
        combo3rdDigitImg = img;
    }

    public static BufferedImage getCombo3rdDigitImg() {
        return combo3rdDigitImg;
    }

    public static void setFrogImg(BufferedImage img) {
        frogImg = img;
    }

    public static BufferedImage getFrogImg() {
        return frogImg;
    }

    public static void setWeaponsImg(BufferedImage img) {
        weaponsImg = img;
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
