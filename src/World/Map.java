package World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Map extends JPanel {
    private static Image image;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(320, 240);
    }

    void drawImage(Graphics g) {
        super.paintComponent(g);
        int width = World.SCREEN_WIDTH;
        int height = World.SCREEN_HEIGHT;
        int imageW = image.getWidth(this);
        int imageH = image.getHeight(this);

        for (int y = 0; y < width; y += imageH) {
            for (int x = 0; x < height; x += imageW) {
                g.drawImage(image, x, y, this);
            }
        }
    }

    public void setImg(BufferedImage img) {
        image = img;
    }
}
