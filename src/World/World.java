package World;

import World.Powerup.Bullet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class World extends JPanel {

    public static final int SCREEN_WIDTH = 3200;
    public static final int SCREEN_HEIGHT = 3200;
    public static final int SPLITSCREEN_WIDTH = 900;
    public static final int SPLITSCREEN_HEIGHT = 700;
    private static Rectangle r;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;
    private Tank tank1;
    private Tank tank2;
    private Map m = null;

    //worlditems
    private UnbreakableWall ubw;
    private BreakableWall bw;
    private tankHealth life;
    private Bullet bullet;

    public static ArrayList<WorldItem> worldItems = new ArrayList<WorldItem>();

    public static void main(String[] args) {
        World w = new World();
        w.init();
        try {

            while (true) {
                if(Bullet.bullets.size() > 0)
                {
                    Bullet.collisions();
                    w.repaint(r);
                }
                //only run when there is an update to the tanks to prevent repainting when nothing changed
                if (w.tank1.update() && w.tank2.update()) {
                    w.repaint(r);
                    //check for collisions with each WorldItem
//                    for (WorldItem worldItem : worldItems) worldItem.collisions();
                    Tank.collisions(w.tank1);
                    Tank.collisions(w.tank2);
                }
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void init() {
        this.jf = new JFrame("Tank Rotation");
        this.world = new BufferedImage(World.SCREEN_WIDTH, World.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            System.out.println(System.getProperty("user.dir"));
            /*
             * note class loaders read files from the out folder (build folder in netbeans) and not the
             * current working directory.
             */

            //load the tank images
            tank1 = new Tank(600, 660, 0, 0, 0, "resources/Tank1.png", "tank1");
            tank2 = new Tank(800, 700, 0, 0, 180, "resources/Tank1.png", "tank2");

            //load the background
            m = new Map("resources/Background.bmp");

            //load the wall images
            ubw = new UnbreakableWall("resources/Wall1.gif");
            bw = new BreakableWall("resources/wall2.gif");

            life = new tankHealth("resources/Heart.png");

            bullet  = new Bullet();
            bullet.setImg("resources/Weapon.gif");

            //create the unbreakable vertical walls to be used in the middle of the map
            int innerWallHeight = ubw.getImg().getHeight(null);
            for(int j = 400; j < SCREEN_HEIGHT - 400; j += innerWallHeight){
                UnbreakableWall tempWallArea1 = new UnbreakableWall("resources/Wall1.gif");
                tempWallArea1.setX(400);
                tempWallArea1.setY(j);

                UnbreakableWall tempWallArea2 = new UnbreakableWall("resources/Wall1.gif");
                tempWallArea2.setX(SCREEN_WIDTH - 400);
                tempWallArea2.setY(j);

                //add the walls to the worldItem ArrayList to spawn later in paintComponent
                worldItems.add(tempWallArea1);
                worldItems.add(tempWallArea2);
            }

            //add the tanks to the ArrayList
            worldItems.add(tank1);
            worldItems.add(tank2);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //spawn instances of keylisteners
        UserInput tankInput1 = new UserInput(tank1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        UserInput tankInput2 = new UserInput(tank2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        this.jf.setLayout(new BorderLayout());

        //add the world to the jframe
        this.jf.add(this);

        //ensure preferred sizes
        this.jf.pack();

        //add the keylisteners to the jframe
        this.jf.addKeyListener(tankInput1);
        this.jf.addKeyListener(tankInput2);

        this.jf.setSize(World.SPLITSCREEN_WIDTH, World.SPLITSCREEN_HEIGHT + 30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.jf.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = this.world.createGraphics();
        super.paintComponent(g2);

        //draw the background
        this.m.drawImage(buffer);


        if(Bullet.bullets.size() > 0) {
            for(int i = 0; i < Bullet.bullets.size(); i++){
                Bullet.bullets.get(i).drawImage(buffer, Bullet.bullets.get(i).getX(), Bullet.bullets.get(i).getY());}
        }

        //get the screens for both sides
        BufferedImage left = world.getSubimage(tank1.getTx() - SPLITSCREEN_WIDTH / 4, tank1.getTy() - SPLITSCREEN_HEIGHT / 2, SPLITSCREEN_WIDTH / 2, SPLITSCREEN_HEIGHT);
        BufferedImage right = world.getSubimage(tank2.getTx() - SPLITSCREEN_WIDTH / 4, tank2.getTy() - SPLITSCREEN_HEIGHT / 2, SPLITSCREEN_WIDTH / 2, SPLITSCREEN_HEIGHT);
        BufferedImage mini = world.getSubimage(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //get the height for a wall once since it is always the same to reduce function calls in for loop
        int tempWidth = ubw.getImg().getWidth(null);
        int tempHeight = ubw.getImg().getHeight(null);

        //draw the walls bordering the top and bottom of the world
        //these don't need to check collisions since borderchecking is already done in tank class
        for (int i = 0; i < SCREEN_WIDTH; i += tempWidth) {
            ubw.drawImage(buffer, i, SCREEN_HEIGHT - tempWidth);
            ubw.drawImage(buffer, i, 0);
        }
        for (int i = 0; i < SCREEN_HEIGHT; i += tempHeight) {
            ubw.drawImage(buffer, SCREEN_WIDTH - tempWidth, i);
            ubw.drawImage(buffer, 0, i);
        }

        //draw each instance of WorldItem
        for (WorldItem worldItem : worldItems) {
            worldItem.drawImage(buffer, worldItem.getX(), worldItem.getY());
        }

        //draw each splitscreen
        g2.drawImage(left, 0, 0, null);
        g2.drawImage(right, SPLITSCREEN_WIDTH / 2, 0, null);

        int placement;
        //draw tank1 lives
        for(int i = 1; i <= this.tank1.getLives(); i++)
        {
            placement = (life.getImg().getWidth(null) + 10) * i;
            life.drawImage(g2, placement/2, 10);
        }
        //draw tank2 lives
        for(int i = this.tank2.getLives(); i >= 1; i--){
            placement = (life.getImg().getWidth(null) + 10) * i;
            life.drawImage(g2, placement/2 + SPLITSCREEN_WIDTH - 130, 10);
        }

        g2.drawImage(mini, SPLITSCREEN_WIDTH/2 - SPLITSCREEN_WIDTH/8 + 10, SPLITSCREEN_HEIGHT - 210, 200, 200, null);

        //get a rectangle for repainting
        r = g.getClipBounds();
    }
}
