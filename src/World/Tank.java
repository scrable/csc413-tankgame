package World;

import World.Powerup.Bullet;
import World.Powerup.DoubleDamage;
import World.Powerup.Heal;

import java.awt.*;
import java.util.ArrayList;

public class Tank extends WorldItem
{


    private static final int R = 3;
    private final int ROTATIONSPEED = 3;
    private int tx, ty;
    private boolean UpPressed,
            DownPressed,
            RightPressed,
            LeftPressed,
            shootPressed;
    private double timePressed = 0;
    private String shooter,
            bulletType = "bullet";

    //starting HP
    private int health = 100;

    //number of starting lives
    private int lives = 3;

    Tank(int x, int y, int vx, int vy, int angle, String shooter)
    {
        this.setX(x);
        this.setY(y);
        this.setAx(vx);
        this.setAy(vy);
        this.setA(angle);
        this.setShooter(shooter);

        //check border here to make sure the initial position is valid
        checkBorder();
    }

    static void collisions(Tank tank)
    {
        ArrayList<WorldItem> worldItems = World.worldItems;
        for (int i = 0; i < worldItems.size(); i++)
        {
            WorldItem item = worldItems.get(i);
            if (item instanceof Wall)
            {
                Rectangle tankRectangle = new Rectangle(tank.getX() + tank.getAx(), tank.getY() + tank.getAy(), tank.getImg().getWidth(null), tank.getImg().getHeight(null));
                Rectangle itemRectangle = new Rectangle(item.getX(), item.getY(), item.getImg().getWidth(null), item.getImg().getHeight(null));
                if (tankRectangle.intersects(itemRectangle))
                {
                    Rectangle intersection = tankRectangle.intersection(itemRectangle);

                    //from bottom into something
                    if (tank.getY() >= item.getY() + item.getImg().getHeight(null) - R)
                    {
                        if (tank.getX() + tank.getImg().getWidth(null) <= item.getX() + R)
                        {
                            tank.setX((int) intersection.getX() - tank.getImg().getWidth(null));
                        }
                        else if (tank.getX() >= item.getX() + item.getImg().getWidth(null) - R)
                        {
                            tank.setX((int) intersection.getX() + (int) intersection.getWidth());

                        }
                        else
                            tank.setY((int) intersection.getY() + (int) intersection.getHeight());
                    }
                    //from top into something
                    else if (tank.getY() <= item.getY() - tank.getImg().getHeight(null) + R)
                    {
                        if (tank.getX() >= item.getX() + item.getImg().getWidth(null) - R)
                        {
                            tank.setX((int) intersection.getX() + (int) intersection.getWidth());

                        }
                        else if (tank.getX() + tank.getImg().getWidth(null) <= item.getX() + R)
                        {
                            tank.setX((int) intersection.getX() - tank.getImg().getWidth(null));
                        }
                        else
                            tank.setY((int) intersection.getY() - tank.getImg().getHeight(null));
                    }
                    //from right into something
                    else if (tank.getX() >= item.getX() + item.getImg().getWidth(null) - R)
                    {
                        tank.setX((int) intersection.getX() + (int) intersection.getWidth());

                    }
                    //from left into something
                    else if (tank.getX() + tank.getImg().getWidth(null) <= item.getX() + R)
                    {
                        tank.setX((int) intersection.getX() - tank.getImg().getWidth(null));
                    }
                }
            }
            else if (item instanceof DoubleDamage)
            {
                Rectangle tankRectangle = new Rectangle(tank.getX(), tank.getY(), tank.getImg().getWidth(null), tank.getImg().getHeight(null));
                Rectangle itemRectangle = new Rectangle(item.getX(), item.getY(), item.getImg().getWidth(null), item.getImg().getHeight(null));
                if (tankRectangle.intersects(itemRectangle))
                {
                    tank.bulletType = "DoubleDamage";
                    World.worldItems.remove(item);
                }
            }
            else if (item instanceof Heal)
            {
                Rectangle tankRectangle = new Rectangle(tank.getX(), tank.getY(), tank.getImg().getWidth(null), tank.getImg().getHeight(null));
                Rectangle itemRectangle = new Rectangle(item.getX(), item.getY(), item.getImg().getWidth(null), item.getImg().getHeight(null));
                if (tankRectangle.intersects(itemRectangle))
                {
                    tank.setHealth(100);
                    tank.setLives(3);
                    World.worldItems.remove(item);
                }
            }
        }
    }

    void toggleUpPressed()
    {
        this.UpPressed = true;
    }

    void toggleDownPressed()
    {
        this.DownPressed = true;
    }

    void toggleRightPressed()
    {
        this.RightPressed = true;
    }

    void toggleLeftPressed()
    {
        this.LeftPressed = true;
    }

    void toggleShootPressed()
    {
        shootPressed = true;
    }

    void unToggleUpPressed()
    {
        this.UpPressed = false;
    }

    void unToggleDownPressed()
    {
        this.DownPressed = false;
    }

    void unToggleRightPressed()
    {
        this.RightPressed = false;
    }

    void unToggleLeftPressed()
    {
        this.LeftPressed = false;
    }

    void unToggleShootPressed()
    {
        this.shootPressed = false;
    }

    public String getShooter()
    {
        return shooter;
    }

    private void setShooter(String shooter)
    {
        this.shooter = shooter;
    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    boolean update()
    {
        boolean check = false;
        if (this.UpPressed)
        {
            this.moveForwards();
            check = true;
        }
        if (this.DownPressed)
        {
            this.moveBackwards();
            check = true;
        }

        if (this.LeftPressed)
        {
            this.rotateLeft();
            check = true;
        }
        if (this.RightPressed)
        {
            this.rotateRight();
            check = true;
        }
        if (this.shootPressed)
        {
            this.shootPressed();
            check = true;
        }
        return check;
    }

    private void rotateLeft()
    {
        this.setA(this.getA() - this.ROTATIONSPEED);
    }

    private void rotateRight()
    {
        this.setA(this.getA() + this.ROTATIONSPEED);
    }

    private void moveBackwards()
    {
        this.setAx((int) Math.round(R * Math.cos(Math.toRadians(this.getA()))));
        this.setAy((int) Math.round(R * Math.sin(Math.toRadians(this.getA()))));
        this.setX(getX() - getAx());
        this.setY(getY() - getAy());
        checkBorder();
    }

    private void moveForwards()
    {
        this.setAx((int) Math.round(R * Math.cos(Math.toRadians(this.getA()))));
        this.setAy((int) Math.round(R * Math.sin(Math.toRadians(this.getA()))));
        this.setX(getX() + getAx());
        this.setY(getY() + getAy());
        checkBorder();
    }

    private void shootPressed()
    {
        if (System.currentTimeMillis() - timePressed > 200)
        {
            Bullet.generateBullet(this.getX() + this.getImg().getWidth(null) / 2, this.getY() + this.getImg().getHeight(null) / 2, this.getA(), this.bulletType, this.getShooter());
            timePressed = System.currentTimeMillis();
        }
    }

    private void checkBorder()
    {
        if (this.getX() < 30)
        {
            this.setX(30);
        }
        if (this.getX() >= World.SCREEN_WIDTH - 88)
        {
            this.setX(World.SCREEN_WIDTH - 88);
        }
        if (this.getY() < 40)
        {
            this.setY(40);
        }
        if (this.getY() >= World.SCREEN_HEIGHT - 80)
        {
            this.setY(World.SCREEN_HEIGHT - 80);
        }
        checkScreenEdge();
    }

    private void checkScreenEdge()
    {
        this.tx = this.getX();
        this.ty = this.getY();

        //minimum X
        if (this.getX() < World.SPLITSCREEN_WIDTH / 4)
        {
            this.setTx(World.SPLITSCREEN_WIDTH / 4);
        }
        //maximum X
        if (this.getX() > World.SCREEN_WIDTH - World.SPLITSCREEN_WIDTH / 4)
        {
            this.setTx(World.SCREEN_WIDTH - World.SPLITSCREEN_WIDTH / 4);
        }
        //minimum Y
        if (this.getY() < World.SPLITSCREEN_HEIGHT / 2)
        {
            this.setTy(World.SPLITSCREEN_HEIGHT / 2);
        }
        //maximum Y
        if (this.getY() > World.SCREEN_HEIGHT - World.SPLITSCREEN_HEIGHT / 2)
        {
            this.setTy(World.SCREEN_HEIGHT - World.SPLITSCREEN_HEIGHT / 2);
        }
    }

    int getTx()
    {
        return tx;
    }

    private void setTx(int tx)
    {
        this.tx = tx;
    }

    int getTy()
    {
        return ty;
    }

    private void setTy(int ty)
    {
        this.ty = ty;
    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    @Override
    public void spawn()
    {
        //add the tanks to the ArrayList
        World.worldItems.add(this);
    }
}
