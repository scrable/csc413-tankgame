package World.Powerup;

import World.WorldItem;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DoubleDamage extends WorldItem
{

    private static Image img;

    public Image getImg()
    {
        return img;
    }

    public void setImg(BufferedImage image)
    {
        img = image;
    }

    @Override
    public void spawn()
    {
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
