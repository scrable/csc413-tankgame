package World.Powerup;

import World.World;
import World.WorldItem;
import World.UnbreakableWall;
import World.Tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Bullet extends WorldItem {
    private static Image img;
    private String shooter;

    private int bulletSpeed = 5;

    public static ArrayList<Bullet> bullets = new ArrayList<>();

    public static void generateBullet(int x, int y, int a, String shooter) {
        Bullet b = new Bullet();
        b.setX(x);
        b.setY(y);
        b.setA(a);
        b.setImg(img);
        b.setShooter(shooter);
        bullets.add(b);
    }

    @Override
    public void drawImage(Graphics g, int x, int y) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getA()), this.getImg().getWidth(null) / 2.0, this.getImg().getHeight(null) / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImg(), rotation, null);
    }

    public void setImg(String s) throws IOException {
        img = ImageIO.read(new File(s));
    }

    private void setShooter(String shooter){
        this.shooter = shooter;
    }

    public static void collisions() {
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.setAx((int) Math.round(b.bulletSpeed * Math.cos(Math.toRadians(b.getA()))));
            b.setAy((int) Math.round(b.bulletSpeed * Math.sin(Math.toRadians(b.getA()))));
            b.setX(b.getX() + b.getAx());
            b.setY(b.getY() + b.getAy());
            if (b.getX() < 30) {
                bullets.remove(i);
            } else if (b.getX() >= World.SCREEN_WIDTH - 88) {
                bullets.remove(i);
            } else if (b.getY() < 40) {
                bullets.remove(i);
            } else if (b.getY() >= World.SCREEN_HEIGHT - 80) {
                bullets.remove(i);
            } else {
                for (int j = 0; j < World.worldItems.size(); j++) {
                    WorldItem item = World.worldItems.get(j);
                    Rectangle bulletRectangle = new Rectangle(b.getX(), b.getY(), b.getImg().getWidth(null), b.getImg().getHeight(null));
                    Rectangle itemRectangle = new Rectangle(item.getX(), item.getY(), item.getImg().getWidth(null), item.getImg().getHeight(null));
                    if (item instanceof UnbreakableWall) {
                        if (bulletRectangle.intersects(itemRectangle)) {
                            bullets.remove(i);
                            break;
                        }
                    }
                    //don't collide with the tank that shot the bullet
                    else if (item instanceof Tank && !(b.shooter.equals(((Tank) item).getShooter()))){
                        if (bulletRectangle.intersects(itemRectangle)) {
                            int lives = ((Tank) item).getLives();
                            bullets.remove(i);
                            if (lives > 0) {
                                int health = ((Tank) item).getHealth();
                                health-=25;
                                if(health != 0){
                                    ((Tank) item).setHealth(health);
                                }
                                else{
                                    lives--;
                                    ((Tank) item).setLives(lives);
                                    if(lives != 0)
                                        ((Tank) item).setHealth(100);
                                }
                            }
                            break;
                        }
                    }
                }
            }

        }
    }
}
