package World.Powerup;

import World.WorldItem;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class DoubleDamage extends WorldItem {

    public Image getImg() {
        return img;
    }

    public void setImg(BufferedImage image) {
        img = image;
    }

    private static Image img;

    @Override
    public void drawImage(Graphics g, int x, int y) {
        //we need rotation since the same image for the powerup
        //is used to replace the default bullet image
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getA()), this.getImg().getWidth(null) / 2.0, this.getImg().getHeight(null) / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImg(), rotation, null);
    }

    @Override
    public void spawn() {
        //add a double damage powerup
        DoubleDamage d = new DoubleDamage();
        d.setX(100);
        d.setY(100);
        World.World.worldItems.add(d);
        d = new DoubleDamage();
        d.setX(World.World.SCREEN_WIDTH - 200);
        d.setY(World.World.SCREEN_HEIGHT - 200);
        World.World.worldItems.add(d);
    }
}
