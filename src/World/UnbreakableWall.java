package World;

import java.awt.*;
import java.io.IOException;

public class UnbreakableWall extends Wall {
    UnbreakableWall(String img) throws IOException {
        super(img);
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, null);
    }

    @Override
    public void collisions(WorldItem item) {

    }
}
