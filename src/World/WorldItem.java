package World;

import java.awt.*;
import java.util.ArrayList;

public abstract class WorldItem {
    private int a;
    private int x;
    private int y;
    private int ax;
    private int ay;
    private Image img;

    //private static ArrayList<WorldItem> WorldItemArray = new ArrayList<WorldItem>();

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
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

    public int getAx() {
        return ax;
    }

    public void setAx(int ax) {
        this.ax = ax;
    }

    public int getAy() {
        return ay;
    }

    public void setAy(int ay) {
        this.ay = ay;
    }

//    public static ArrayList<WorldItem> getWorldItemArray() {
//        return WorldItemArray;
//    }
//
//    public static void setWorldItemArray(ArrayList<WorldItem> worldItemArray) {
//        WorldItemArray = worldItemArray;
//    }
    
    public abstract void drawImage(Graphics g, int x, int y);

    public abstract void collisions();
}
