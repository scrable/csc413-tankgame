package World.Powerup;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bullet {
    private static Image img;

    public Bullet(String img) throws IOException {
        Bullet.img = ImageIO.read(new File(img));
    }
}
