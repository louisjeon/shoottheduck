package kr.jbnu.se.std;

public class Frog {
    private static Frog frog;
    private int xChange = 0;
    private int yChange = 0;
    private int x;
    private int y;

    private Frog() {}

    public static Frog getInstance() {
        if (frog == null) {
            frog = new Frog();
        }
        return frog;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public int getXChange() {
        return xChange;
    }
    public void setXChange(int x) {
        if (x > - 1250 && x <= 0) {
            this.xChange = x;
        }
    }

    public int getYChange() {
        return yChange;
    }
    public void setYChange(int y) {
        this.yChange = y;
    }
}
