package World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Map extends JPanel {
    private Image image = null;

    Map(String img) throws IOException {
        image = ImageIO.read(new File(img));
    }
//    Map() {
//        MediaTracker mt = new MediaTracker(this);
//        bg = Toolkit.getDefaultToolkit().getImage("Project/World/Resource/Background.bmp");
//        mt.addImage(bg, 0);
//        try {
//            mt.waitForAll();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//       // g.drawImage(bg, 0, 0, this);
//        g.drawImage(bg.getScaledInstance(1280, -1, Image. SCALE_SMOOTH), 0, 0, this);
//
//    }

void drawImage(Graphics g){
//        paintComponent(g);
       // g.drawImage(image, 0, 0, null);

    super.paintComponent(g);

    if (image != null) {

        int imgWidth, imgHeight;


        imgWidth = World.getScreenWidth();
        imgHeight = World.getScreenHeight();
        g.drawImage(image, 0, 0, imgWidth, imgHeight, this);
    }
    }

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//            g.drawImage(image, x, y, imgWidth, imgHeight, this);
//        }
//    }
}
