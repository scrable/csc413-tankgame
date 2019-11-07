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
        //add a heal powerup
        Heal h = new Heal();
        h.setX(700);
        h.setY(1000);
        World.World.worldItems.add(h);
    }
}
