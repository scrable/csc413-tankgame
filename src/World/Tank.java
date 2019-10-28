package World;

import World.World;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tank extends WorldItem{


    private final int R = 2;
    private final int ROTATIONSPEED = 4;
    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;


    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img) {
        this.setX(x);
        this.setY(y);
        this.setAx(vx);
        this.setAy(vy);
        this.img = img;
        this.setA(angle);
    }


    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }



    public boolean update() {
        boolean check = false;
        if (this.UpPressed) {
            this.moveForwards();
            check = true;
        }
        if (this.DownPressed) {
            this.moveBackwards();
            check = true;
        }

        if (this.LeftPressed) {
            this.rotateLeft();
            check = true;
        }
        if (this.RightPressed) {
            this.rotateRight();
            check = true;
        }
        return check;

    }

    private void rotateLeft() {
        this.setA(this.getA()-this.ROTATIONSPEED);
    }

    private void rotateRight() {
        this.setA(this.getA()+this.ROTATIONSPEED);
    }

    private void moveBackwards() {
        this.setAx((int) Math.round(R * Math.cos(Math.toRadians(this.getA()))));
        this.setAy((int) Math.round(R * Math.sin(Math.toRadians(this.getA()))));
        this.setX(getX()-getAx());
        this.setY(getY()-getAy());
        checkBorder();
    }

    private void moveForwards() {
        this.setAx((int) Math.round(R * Math.cos(Math.toRadians(this.getA()))));
        this.setAy((int) Math.round(R * Math.sin(Math.toRadians(this.getA()))));
        this.setX(getX()+getAx());
        this.setY(getY()+getAy());
        checkBorder();
    }

    private void checkBorder() {
        if (this.getX() < 30) {
            this.setX(30);
        }
        if (this.getX() >= World.SCREEN_WIDTH - 88) {
            this.setX(World.SCREEN_WIDTH - 88);
        }
        if (this.getY() < 40) {
            this.setY(40);
        }
        if (this.getY() >= World.SCREEN_HEIGHT - 80) {
            this.setY(World.SCREEN_HEIGHT - 80);
        }
    }

    @Override
    public String toString() {
        return "x=" + this.getX() + ", y=" + this.getY() + ", angle=" + this.getA();
    }


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getA()), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;


        g2d.drawImage(this.img, rotation, null);
    }
}
