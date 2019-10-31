package World;

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

    //holds instances of walls for horizontal border
    private UnbreakableWall[] horizontalWalls;

    //holds instances of walls for vertical border
    private UnbreakableWall[] verticalWalls;

    public static ArrayList<WorldItem> getWorldItems() {
        return worldItems;
    }

    public static void setWorldItems(ArrayList<WorldItem> a) {
        worldItems = a;
    }

    private static ArrayList<WorldItem> worldItems = new ArrayList<WorldItem>();

    public static void main(String[] args) {
        World w = new World();
        w.init();
        try {

            while (true) {
                //only run when there is an update to the tanks to prevent repainting when nothing changed
                if (w.tank1.update() && w.tank2.update()) {
                    w.repaint(r);

                    //check for collisions with each WorldItem
                    for (WorldItem worldItem : worldItems) worldItem.collisions();

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
            tank1 = new Tank(600, 660, 0, 0, 0, "resources/Tank1.png");
            tank2 = new Tank(800, 700, 0, 0, 180, "resources/Tank1.png");

            //load the background
            m = new Map("resources/Background.bmp");

            //load the wall image
            UnbreakableWall ubw = new UnbreakableWall("resources/Wall1.gif");

            //need a counter here since i is being used to change the X value for each wall
            int counter = 0;

            //get the height once since it is always the same to reduce function calls in for loop
            int tempWidth = ubw.getImg().getHeight(null);

            int numWallsHorizontal = SCREEN_WIDTH / tempWidth;
            horizontalWalls = new UnbreakableWall[numWallsHorizontal];

            //add walls to an array to spawn later using drawimage for each element
            for (int i = 0; i < SCREEN_WIDTH; i += tempWidth) {
                UnbreakableWall tempubw = new UnbreakableWall("resources/Wall1.gif");
                tempubw.setX(i);
                horizontalWalls[counter] = tempubw;
                counter++;
            }

            //create the walls to be used in the map
            WorldItem f = new UnbreakableWall("resources/Wall1.gif");
            f.setX(200);
            f.setY(100);

            //add the tanks to the ArrayList
            worldItems.add(tank1);
            worldItems.add(tank2);

            //add the walls to the worldItem ArrayList to spawn later in paintComponent
            worldItems.add(f);

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

        //draw the tanks
        this.tank1.drawImage(buffer);
        this.tank2.drawImage(buffer);

        //checkfor screen edges when tank is near se we don't refresh where there is no image
        this.tank1.checkScreenEdge();
        this.tank2.checkScreenEdge();

        //get the screens for both sides
        BufferedImage left = world.getSubimage(tank1.getTx() - SPLITSCREEN_WIDTH / 4, tank1.getTy() - SPLITSCREEN_HEIGHT / 2, SPLITSCREEN_WIDTH / 2, SPLITSCREEN_HEIGHT);
        BufferedImage right = world.getSubimage(tank2.getTx() - SPLITSCREEN_WIDTH / 4, tank2.getTy() - SPLITSCREEN_HEIGHT / 2, SPLITSCREEN_WIDTH / 2, SPLITSCREEN_HEIGHT);

        //need a counter for the wall for loop since i is not incremental
        int count = 0;

        //get the height for a wall once since it is always the same to reduce function calls in for loop
        int tempWidth = this.horizontalWalls[0].getImg().getHeight(null);

        //draw the walls bordering the top and bottom of the world
        //these don't need to check collisions since borderchecking is already done in tank class
        for (int i = 0; i < SCREEN_WIDTH; i += tempWidth) {
            this.horizontalWalls[count].drawImage(buffer, i, SCREEN_HEIGHT - tempWidth - 4);
            this.horizontalWalls[count].drawImage(buffer, i, 0);
        }

        //draw each instance of WorldItem
        for (WorldItem worldItem : worldItems) worldItem.drawImage(buffer, worldItem.getX(), worldItem.getY());

        //draw each splitscreen
        g2.drawImage(left, 0, 0, null);
        g2.drawImage(right, SPLITSCREEN_WIDTH / 2, 0, null);

        //get a rectangle for repainting
        r = g.getClipBounds();
    }
}
