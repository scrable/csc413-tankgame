package World;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public abstract class Wall extends WorldItem{
    Wall(String img) throws IOException {
       this.setImg(ImageIO.read(new File(img)));
    }


}
