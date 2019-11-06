package World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BreakableWall extends Wall {
    BreakableWall(BufferedImage img) throws IOException {
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
