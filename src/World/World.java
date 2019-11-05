package World;

import World.Powerup.Bullet;
import World.Powerup.DoubleDamage;

import javax.imageio.ImageIO;
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
    private BufferedImage p1w;
    private BufferedImage p2w;
    private UserInput tankInput1;
    private UserInput tankInput2;
    private static boolean gameover = false;

    //worlditems
    private UnbreakableWall ubw;
    private BreakableWall bw;
    private tankHealth life;
    private Bullet bullet;
    private DoubleDamage DD;

    public static ArrayList<WorldItem> worldItems = new ArrayList<WorldItem>();
    public static ArrayList<Integer> itemsToRemove = new ArrayList<>();

    public static void main(String[] args) {
        World w = new World();
        w.init();
        try {
            while (!gameover) {
                //clear items that should be deleted from worldItems before we loop through spawning worldItems to prevent concurrent modification
                for (Integer integer : itemsToRemove) {
                    worldItems.remove(worldItems.get(integer));
                }
                itemsToRemove.clear();
                Thread.sleep(1000 / 144);
                //only run if a bullet is updating or a tank is updating
                if (Bullet.bullets.size() > 0) {
                    Bullet.collisions();
                    w.repaint(r);
                }
                if (w.tank1.update()) {
                    Tank.collisions(w.tank1);
                    w.repaint(r);
                }
                if(w.tank2.update()) {
                    Tank.collisions(w.tank2);
                    w.repaint(r);
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    private void init() {
        this.jf = new JFrame("Tank Wars");
        this.world = new BufferedImage(World.SCREEN_WIDTH, World.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {
            //load the tank images
            tank1 = new Tank(600, 660, 0, 0, 0, ImageIO.read(getClass().getResource("/resources/Tank1.png")), "tank1");
            tank2 = new Tank(800, 700, 0, 0, 180, ImageIO.read(getClass().getResource("/resources/Tank1.png")), "tank2");

            //load the background
            m = new Map(ImageIO.read(getClass().getResource("/resources/Background.bmp")));

            //load the wall images
            ubw = new UnbreakableWall(ImageIO.read(getClass().getResource("/resources/Wall1.gif")));
            bw = new BreakableWall(ImageIO.read(getClass().getResource("/resources/Wall2.gif")));

            //load heart icon for lives
            life = new tankHealth(ImageIO.read(getClass().getResource("/resources/Heart.png")));

            //load bullet image
            bullet = new Bullet();
            bullet.setImg(ImageIO.read(getClass().getResource("/resources/Weapon.gif")));

            DD = new DoubleDamage();
            DD.setImg(ImageIO.read(getClass().getResource("/resources/Pickup.gif")));

            //load each player winning image
            p1w = ImageIO.read(getClass().getResource("/resources/p1w.png"));
            p2w = ImageIO.read(getClass().getResource("/resources/p2w.png"));

            //create the unbreakable vertical walls to be used in the middle of the map
            int innerWallHeight = ubw.getImg().getHeight(null);
            for (int j = 400; j < SCREEN_HEIGHT - 400; j += innerWallHeight) {
                UnbreakableWall tempWallArea1 = new UnbreakableWall(ImageIO.read(getClass().getResource("/resources/Wall1.gif")));
                tempWallArea1.setX(400);
                tempWallArea1.setY(j);

                UnbreakableWall tempWallArea2 = new UnbreakableWall(ImageIO.read(getClass().getResource("/resources/Wall1.gif")));
                tempWallArea2.setX(SCREEN_WIDTH - 400);
                tempWallArea2.setY(j);

                //add the walls to the worldItem ArrayList to spawn later in paintComponent
                worldItems.add(tempWallArea1);
                worldItems.add(tempWallArea2);
            }

            innerWallHeight = bw.getImg().getHeight(null);
            for (int j = 1000; j < SCREEN_HEIGHT - 400; j += innerWallHeight){
                BreakableWall tempWallArea1 = new BreakableWall(ImageIO.read(getClass().getResource("/resources/Wall2.gif")));
                tempWallArea1.setX(1000);
                tempWallArea1.setY(j);

                BreakableWall tempWallArea2 = new BreakableWall(ImageIO.read(getClass().getResource("/resources/Wall2.gif")));
                tempWallArea2.setX(SCREEN_WIDTH - 1000);
                tempWallArea2.setY(j);

                worldItems.add(tempWallArea1);
                worldItems.add(tempWallArea2);
            }

            DoubleDamage d = new DoubleDamage();
            d.setX(700);
            d.setY(680);
            worldItems.add(d);

            //add the tanks to the ArrayList
            worldItems.add(tank1);
            worldItems.add(tank2);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //spawn instances of keylisteners
        tankInput1 = new UserInput(tank1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        tankInput2 = new UserInput(tank2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

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

        //draw all active bullets
        if (Bullet.bullets.size() > 0) {
            for (int i = 0; i < Bullet.bullets.size(); i++) {
                Bullet.bullets.get(i).drawImage(buffer, Bullet.bullets.get(i).getX(), Bullet.bullets.get(i).getY());
            }
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
        for (int i = 1; i <= this.tank1.getLives(); i++) {
            placement = (life.getImg().getWidth(null) + 10) * i;
            life.drawImage(g2, placement / 2, 10);
        }
        //draw tank2 lives
        for (int i = this.tank2.getLives(); i >= 1; i--) {
            placement = (life.getImg().getWidth(null) + 10) * i;
            life.drawImage(g2, placement / 2 + SPLITSCREEN_WIDTH - 130, 10);
        }

        g2.setColor(Color.green);
        g2.fillRect(SPLITSCREEN_WIDTH/4 - 60, 30, 2 * this.tank1.getHealth(), 30);
        g2.fillRect(3 * SPLITSCREEN_WIDTH/4 - 140, 30, 2 * this.tank2.getHealth(), 30);

        g2.drawImage(mini, SPLITSCREEN_WIDTH / 2 - SPLITSCREEN_WIDTH / 8 + 10, SPLITSCREEN_HEIGHT - 210, 200, 200, null);


        //game over screens for each player winning
        //remove keylisteners to prevent controlling things after game ends
        if(this.tank1.getLives() == 0){
            g2.drawImage(p2w, 0, 0, SPLITSCREEN_WIDTH, SPLITSCREEN_HEIGHT, null);
            this.jf.removeKeyListener(tankInput1);
            this.jf.removeKeyListener(tankInput2);
            gameover = true;
        }
        else if(this.tank2.getLives() == 0){
            g2.drawImage(p1w, 0, 0, SPLITSCREEN_WIDTH, SPLITSCREEN_HEIGHT, null);
            this.jf.removeKeyListener(tankInput1);
            this.jf.removeKeyListener(tankInput2);
            gameover = true;
        }

        //get a rectangle for repainting
        r = g.getClipBounds();
    }
}
