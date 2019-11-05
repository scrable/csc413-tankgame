package World;

import java.awt.image.BufferedImage;

public abstract class Wall extends WorldItem{
    Wall(BufferedImage img) {
       this.setImg(img);
    }


}
