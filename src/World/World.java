package World;

import World.Powerup.Bullet;
import World.Powerup.DoubleDamage;
import World.Powerup.Heal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class World extends JPanel
{

    public static final int SCREEN_WIDTH = 2200,
            SCREEN_HEIGHT = 2200,
            SPLITSCREEN_WIDTH = 900,
            SPLITSCREEN_HEIGHT = 700;
    private static boolean gameover = false;
    private BufferedImage world,
            p1w,
            p2w;
    private Graphics2D buffer;
    private Tank tank1, tank2;
    private Map m;

    //worlditems
    private UnbreakableWall ubw;
    private BreakableWall bw;
    private tankHealth life;
    private Bullet bullet;
    private DoubleDamage DD;
    private Heal heal;

    public static ArrayList<WorldItem> worldItems = new ArrayList<WorldItem>();
    private static ArrayList<WorldItem> worldItemsToSpawn = new ArrayList<WorldItem>();

    public static void main(String[] args)
    {
        World w = new World();
        w.init();
        try
        {
            while (!gameover)
            {
                //only run if a bullet is updating or a tank is updating
                if (Bullet.bullets.size() > 0)
                {
                    Bullet.collisions();
                    w.repaint();
                }
                if (w.tank1.update())
                {
                    Tank.collisions(w.tank1);
                    w.repaint();
                }
                if (w.tank2.update())
                {
                    Tank.collisions(w.tank2);
                    w.repaint();
                }
                Thread.sleep(1000 / 144);
            }
        }
        catch (InterruptedException ignored)
        {
        }
    }

    private void init()
    {
        JFrame jf = new JFrame("Tank Wars");
        this.world = new BufferedImage(World.SCREEN_WIDTH, World.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try
        {
            //load the tank images
            tank1 = new Tank(700, SCREEN_HEIGHT / 2, 0, 0, 0, "tank1");
            tank2 = new Tank(SCREEN_WIDTH - 700, SCREEN_HEIGHT / 2, 0, 0, 180, "tank2");
            tank1.setImg(ImageIO.read(getClass().getResource("/resources/Tank1.png")));
            tank2.setImg(ImageIO.read(getClass().getResource("/resources/Tank1.png")));
            worldItemsToSpawn.add(tank1);
            worldItemsToSpawn.add(tank2);

            //load the background
            m = new Map();
            m.setImg(ImageIO.read(getClass().getResource("/resources/Background.bmp")));

            //load the wall images
            ubw = new UnbreakableWall();
            ubw.setImg(ImageIO.read(getClass().getResource("/resources/Wall1.gif")));
            worldItemsToSpawn.add(ubw);

            bw = new BreakableWall();
            bw.setImg(ImageIO.read(getClass().getResource("/resources/Wall2.gif")));
            worldItemsToSpawn.add(bw);

            //load heart icon for lives
            life = new tankHealth();
            life.setImg(ImageIO.read(getClass().getResource("/resources/Heart1.png")));

            //load bullet image
            bullet = new Bullet();
            bullet.setImg(ImageIO.read(getClass().getResource("/resources/Weapon.gif")));

            //load the image for the double damage powerup icon and bullet
            DD = new DoubleDamage();
            DD.setImg(ImageIO.read(getClass().getResource("/resources/Pickup.gif")));
            worldItemsToSpawn.add(DD);

            //load the image for the healing powerup icon
            heal = new Heal();
            heal.setImg(ImageIO.read(getClass().getResource("/resources/Heart2.png")));
            worldItemsToSpawn.add(heal);

            //load each player winning image
            p1w = ImageIO.read(getClass().getResource("/resources/p1w.png"));
            p2w = ImageIO.read(getClass().getResource("/resources/p2w.png"));

            for (WorldItem worldItem : worldItemsToSpawn)
            {
                worldItem.spawn();
            }

        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }

        //spawn instances of keylisteners
        UserInput tankInput1 = new UserInput(tank1, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        UserInput tankInput2 = new UserInput(tank2, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);

        jf.setLayout(new BorderLayout());

        //add the world to the jframe
        jf.add(this);

        //ensure preferred sizes
        jf.pack();

        //add the keylisteners to the jframe
        jf.addKeyListener(tankInput1);
        jf.addKeyListener(tankInput2);

        jf.setSize(World.SPLITSCREEN_WIDTH, World.SPLITSCREEN_HEIGHT + 30);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jf.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        buffer = this.world.createGraphics();
        super.paintComponent(g2);

        //draw the background
        this.m.drawImage(buffer);

        //draw all active bullets
        if (Bullet.bullets.size() > 0)
        {
            for (int i = 0; i < Bullet.bullets.size(); i++)
            {
                Bullet.bullets.get(i).drawImage(buffer, Bullet.bullets.get(i).getX(), Bullet.bullets.get(i).getY());
            }
        }

        //get the screens for both sides
        BufferedImage left = world.getSubimage(tank1.getTx() - SPLITSCREEN_WIDTH / 4, tank1.getTy() - SPLITSCREEN_HEIGHT / 2, SPLITSCREEN_WIDTH / 2, SPLITSCREEN_HEIGHT);
        BufferedImage right = world.getSubimage(tank2.getTx() - SPLITSCREEN_WIDTH / 4, tank2.getTy() - SPLITSCREEN_HEIGHT / 2, SPLITSCREEN_WIDTH / 2, SPLITSCREEN_HEIGHT);
        BufferedImage mini = world.getSubimage(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //draw each instance of WorldItem
        for (int i = 0; i < worldItems.size(); i++)
        {
            WorldItem worldItem = worldItems.get(i);
            worldItem.drawImage(buffer, worldItem.getX(), worldItem.getY());
        }

        //draw each splitscreen
        g2.drawImage(left, 0, 0, null);
        g2.drawImage(right, SPLITSCREEN_WIDTH / 2, 0, null);

        //draw each tank's lives
        int placement;
        for (int i = 1; i <= this.tank1.getLives(); i++)
        {
            placement = (life.getImg().getWidth(null) + 10) * i;
            life.drawImage(g2, placement / 2, 10);
        }
        for (int i = this.tank2.getLives(); i >= 1; i--)
        {
            placement = (life.getImg().getWidth(null) + 10) * i;
            life.drawImage(g2, placement / 2 + SPLITSCREEN_WIDTH - 130, 10);
        }

        //draw healthbars
        g2.setColor(Color.green);
        g2.fillRect(SPLITSCREEN_WIDTH / 4 - 60, 30, 2 * this.tank1.getHealth(), 30);
        g2.fillRect(3 * SPLITSCREEN_WIDTH / 4 - 140, 30, 2 * this.tank2.getHealth(), 30);

        //draw minimap
        g2.drawImage(mini, SPLITSCREEN_WIDTH / 2 - SPLITSCREEN_WIDTH / 8 + 10, SPLITSCREEN_HEIGHT - 210, 200, 200, null);


        //game over screens for each player winning
        if (this.tank1.getLives() == 0)
        {
            g2.drawImage(p2w, 0, 0, SPLITSCREEN_WIDTH, SPLITSCREEN_HEIGHT, null);
            gameover = true;
        }
        else if (this.tank2.getLives() == 0)
        {
            g2.drawImage(p1w, 0, 0, SPLITSCREEN_WIDTH, SPLITSCREEN_HEIGHT, null);
            gameover = true;
        }
    }
}
