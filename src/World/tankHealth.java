package World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class tankHealth {
    private Image image;

    tankHealth(String img) throws IOException {
        image = ImageIO.read(new File(img));
    }

    public Image getImg() {
        return image;
    }

    public void drawImage(Graphics g, int x, int y) {
        g.drawImage(getImg(), x, y, null);
    }

}
