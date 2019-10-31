package World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Tank extends WorldItem{


    private final int R = 2;
    private final int ROTATIONSPEED = 4;
    private int tx;
    private int ty;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    Tank(int x, int y, int vx, int vy, int angle, String img) throws IOException {
        this.setX(x);
        this.setY(y);
        this.setAx(vx);
        this.setAy(vy);
        this.setImg(ImageIO.read(new File(img)));
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
        if(this.getX() < 30){
            this.setX(30);
        }
        if (this.getX() >= World.SCREEN_WIDTH - 88) {
            this.setX(World.SCREEN_WIDTH - 88);
        }
        if(this.getY() < 40){
            this.setY(40);
        }
        if(this.getY() >= World.SCREEN_HEIGHT - 80){
            this.setY(World.SCREEN_HEIGHT - 80);
        }

    }

    public void checkScreenEdge(){
        this.tx = this.getX();
        this.ty = this.getY();

        //minimum X
        if (this.getX() < World.SPLITSCREEN_WIDTH / 4) {
            this.setTx(World.SPLITSCREEN_WIDTH / 4);
        }
        //maximum X
        if (this.getX() > World.SCREEN_WIDTH - World.SPLITSCREEN_WIDTH / 4) {
            this.setTx(World.SCREEN_WIDTH - World.SPLITSCREEN_WIDTH / 4);
        }
        //minimum Y
        if (this.getY() < World.SPLITSCREEN_HEIGHT / 2) {
            this.setTy(World.SPLITSCREEN_HEIGHT / 2);
        }
        //maximum Y
        if (this.getY() > World.SCREEN_HEIGHT - World.SPLITSCREEN_HEIGHT / 2) {
            this.setTy(World.SCREEN_HEIGHT - World.SPLITSCREEN_HEIGHT / 2);
        }
    }

    public int getTx() {
        return tx;
    }

    public void setTx(int tx) {
        this.tx = tx;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }

    @Override
    public String toString() {
        return "x=" + this.getX() + ", y=" + this.getY() + ", angle=" + this.getA();
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getA()), this.getImg().getWidth(null) / 2.0, this.getImg().getHeight(null) / 2.0);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(getImg(), rotation, null);
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {

    }

    @Override
    public void collisions(){
        ArrayList<WorldItem> itemA = World.getWorldItems();

        for (WorldItem item : itemA) {

            Rectangle tankRectangle = new Rectangle(this.getX(), this.getY(), this.getImg().getWidth(null), this.getImg().getHeight(null));
            Rectangle itemRectangle = new Rectangle(item.getX(), item.getY(), item.getImg().getWidth(null), item.getImg().getHeight(null));
            if (tankRectangle.intersects(itemRectangle)) {
                Rectangle intersection = tankRectangle.intersection(itemRectangle);

                //from bottom into something
                if (this.getY() > item.getY()) {
                    //topleft
                    if (this.getX() < intersection.getX() + intersection.getWidth() && this.getX() > intersection.getWidth()) {
                        this.setY((int) intersection.getY() + (int) intersection.getHeight());
                    }
                }
                //from top into something
//            if(this.getY() < item.getY()){
////                if(this.getX() < intersection.getX())
////                    ;
////                else
//                    this.setY((int) intersection.getY() -  this.getImg().getHeight(null));// - (int) intersection.getHeight());
//            }
//            //from right into something
//            if(this.getX() > item.getX()){
//                this.setX((int) intersection.getX() + (int) intersection.getWidth());
//            }
//            //from left into something
//            if(this.getX() < item.getX()){
//                this.setX((int) intersection.getX() -  this.getImg().getWidth(null));// - (int) intersection.getHeight());
//            }

            }
        }
    }
}
