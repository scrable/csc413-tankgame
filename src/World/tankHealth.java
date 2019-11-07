package World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class tankHealth {
    private Image image;

    public Image getImg() {
        return image;
    }

    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, null);
    }

    public void setImg(BufferedImage img) {
        image = img;
    }
}
