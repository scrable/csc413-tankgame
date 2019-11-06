package World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnbreakableWall extends Wall {
    UnbreakableWall(BufferedImage img) {
        super(img);
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, null);
    }

    @Override
    public void spawn() {

    }
}
