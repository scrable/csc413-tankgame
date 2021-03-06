package World;

import java.awt.*;
import java.awt.geom.AffineTransform;

public abstract class WorldItem
{
    private int a;
    private int x;
    private int y;
    private int ax;
    private int ay;
    private Image img;

    public Image getImg()
    {
        return img;
    }

    public void setImg(Image img)
    {
        this.img = img;
    }

    public int getA()
    {
        return a;
    }

    public void setA(int a)
    {
        this.a = a;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getAx()
    {
        return ax;
    }

    public void setAx(int ax)
    {
        this.ax = ax;
    }

    public int getAy()
    {
        return ay;
    }

    public void setAy(int ay)
    {
        this.ay = ay;
    }

    public void drawImage(Graphics g, int x, int y)
    {
        //we need rotation since the same image for the powerup
        //is used to replace the default bullet image
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getA()), this.getImg().getWidth(null) / 2.0, this.getImg().getHeight(null) / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImg(), rotation, null);
    }

    public abstract void spawn();
}
