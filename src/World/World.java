package World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.imageio.ImageIO.read;

public class World extends JPanel {

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;
    private Tank tank1;
    private Map m = null;


    public static void main(String[] args) {
        World w = new World();
        w.init();
        int dl = 0;
        try {

            while (true) {

                if (w.tank1.update()) {
                    w.repaint();
                    dl++;
                    System.out.println(dl);
                }
               // w.repaint();
//                w.paintComponent(w.getGraphics());
                //System.out.println(w.tank1);

                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void init() {
        this.jf = new JFrame("Tank Rotation");
        this.world = new BufferedImage(World.SCREEN_WIDTH, World.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        BufferedImage t1img = null, t2img = null;

        try {
            System.out.println(System.getProperty("user.dir"));
            /*
             * note class loaders read files from the out folder (build folder in netbeans) and not the
             * current working directory.
             */
            t1img = read(new File("resources/Tank1.png"));
            m = new Map("resources/Background.bmp");


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        tank1 = new Tank(200, 200, 0, 0, 0, t1img);



        UserInput tankInput1 = new UserInput(tank1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        

        this.jf.setLayout(new BorderLayout());
        // this.lpane.add(m);
        // this.jf.add(lpane, BorderLayout.CENTER);

        this.jf.add(this);


        this.jf.pack();

        this.jf.addKeyListener(tankInput1);

        this.jf.setSize(World.SCREEN_WIDTH, World.SCREEN_HEIGHT + 30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // this.jf.getContentPane().add(m);

        this.jf.setVisible(true);


    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(buffer);

        this.m.drawImage(buffer);
        this.tank1.drawImage(buffer);

        g2.drawImage(world, 0, 0, null);
    }

    public static int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static int getScreenHeight() {
        return SCREEN_HEIGHT;
    }
}
