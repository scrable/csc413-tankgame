package World.Powerup;

import World.World;
import World.WorldItem;
import World.UnbreakableWall;
import World.Tank;
import World.BreakableWall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bullet extends WorldItem {
    private static Image img;
    private String shooter;
    private String type;

    private int bulletSpeed = 5;
    private int bulletDamage = 25;

    public static ArrayList<Bullet> bullets = new ArrayList<>();

    public static void generateBullet(int x, int y, int a, String s, String shooter) {
        Bullet b = new Bullet();
        b.type = s;
        if (b.type.equals("DoubleDamage")) {
            DoubleDamage d = new DoubleDamage();
            b.setImg(d.getImg());
            b.bulletDamage = 50;

        } else {
            b.setImg(img);
        }
        b.setX(x- b.getImg().getWidth(null)/2);
        b.setY(y-b.getImg().getHeight(null)/2);
        b.setA(a);
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

    @Override
    public void spawn() {

    }

    public void setImg(BufferedImage image) {
        img = image;
    }

    private void setShooter(String shooter) {
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
                    } else if (item instanceof BreakableWall) {
                        if (bulletRectangle.intersects(itemRectangle)) {
                            bullets.remove(i);
                            World.worldItems.remove(j);
                            break;
                        }
                    }
                    //don't collide with the tank that shot the bullet
                    else if (item instanceof Tank && !(b.shooter.equals(((Tank) item).getShooter()))) {
                        if (bulletRectangle.intersects(itemRectangle)) {
                            int lives = ((Tank) item).getLives();
                            bullets.remove(i);
                            if (lives > 0) {
                                int health = ((Tank) item).getHealth();
                                health -= b.bulletDamage;
                                if (health != 0) {
                                    ((Tank) item).setHealth(health);
                                } else {
                                    lives--;
                                    ((Tank) item).setLives(lives);
                                    if (lives != 0)
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
