package World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class tankHealth
{
    private Image image;

    Image getImg()
    {
        return image;
    }

    void setImg(BufferedImage img)
    {
        image = img;
    }

    public void drawImage(Graphics g, int x, int y)
    {
        g.drawImage(getImg(), x, y, null);
    }
}
