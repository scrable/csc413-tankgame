package World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall {
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
        int innerWallHeight = img.getHeight(null);
        for (int j = 1000; j < World.SCREEN_HEIGHT - 400; j += innerWallHeight) {
            BreakableWall tempWallArea1 = new BreakableWall();
            tempWallArea1.setX(1000);
            tempWallArea1.setY(j);
            tempWallArea1.setImg(img);

            BreakableWall tempWallArea2 = new BreakableWall();
            tempWallArea2.setX(World.SCREEN_WIDTH - 1000);
            tempWallArea2.setY(j);
            tempWallArea2.setImg(img);

            World.worldItems.add(tempWallArea1);
            World.worldItems.add(tempWallArea2);
        }
    }
}
