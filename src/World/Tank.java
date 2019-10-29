package World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tank extends WorldItem{


    private final int R = 2;
    private final int ROTATIONSPEED = 4;
    private Image img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;


    Tank(int x, int y, int vx, int vy, int angle, String img) throws IOException {
        this.setX(x);
        this.setY(y);
        this.setAx(vx);
        this.setAy(vy);
        this.img = ImageIO.read(new File(img));
        this.setA(angle);
        //check border here to make sure the initial position is valid
        checkBorder();
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
        //minimum X
        if (this.getX() < World.SPLITSCREEN_WIDTH / 4) {
            this.setX(World.SPLITSCREEN_WIDTH / 4);
        }
        //maximum X
        if (this.getX() > World.SCREEN_WIDTH - World.SPLITSCREEN_WIDTH / 4) {
            this.setX(World.SCREEN_WIDTH - World.SPLITSCREEN_WIDTH / 4);
        }
        //minimum Y
        if (this.getY() < World.SPLITSCREEN_HEIGHT / 2) {
            this.setY(World.SPLITSCREEN_HEIGHT / 2);
        }
        //maximum Y
        if (this.getY() > World.SCREEN_HEIGHT - World.SPLITSCREEN_HEIGHT / 2) {
            this.setY(World.SCREEN_HEIGHT - World.SPLITSCREEN_HEIGHT / 2);
        }
    }

    public void checkForOtherTank(Tank t2){
//        if(this.getX() + Math.cos(Math.toRadians(50)) == t2.getX() - Math.cos(Math.toRadians(50)))
//        if(Math.abs(this.getX() - t2.getX()) == 40)
//            System.out.println("HELLO1");

    }

    @Override
    public String toString() {
        return "x=" + this.getX() + ", y=" + this.getY() + ", angle=" + this.getA();
    }


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getA()), this.img.getWidth(null) / 2.0, this.img.getHeight(null) / 2.0);
        Graphics2D g2d = (Graphics2D) g;


        g2d.drawImage(this.img, rotation, null);
    }
}
