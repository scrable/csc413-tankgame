package World.Powerup;

import World.WorldItem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bullet extends WorldItem {
    private static Image img;

    public Bullet(String img) throws IOException {
        Bullet.img = ImageIO.read(new File(img));
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {

    }

    @Override
    public void collisions() {

    }
}
