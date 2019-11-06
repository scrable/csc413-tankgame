package World.Powerup;

import World.WorldItem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Heal extends WorldItem {
    private static Image img;

    public void setImg(BufferedImage image) {
        img = image;
    }
    public Image getImg() {
        return img;
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, null);
    }

    @Override
    public void spawn() {

    }
}
