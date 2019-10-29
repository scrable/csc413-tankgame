package World;

import java.awt.*;
import java.io.IOException;

public class BreakableWall extends Wall {
    BreakableWall(String img) throws IOException {
        super(img);
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, null);
    }
}
