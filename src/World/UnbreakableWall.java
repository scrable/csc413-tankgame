package World;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnbreakableWall extends Wall
{
    private static Image img;

    public Image getImg()
    {
        return img;
    }

    void setImg(BufferedImage image)
    {
        img = image;
    }

    @Override
    public void drawImage(Graphics g, int x, int y)
    {
        g.drawImage(getImg(), x, y, null);
    }

    @Override
    public void spawn()
    {
        //create the unbreakable vertical walls to be used in the middle of the map
        int innerWallHeight = img.getHeight(null);
        for (int j = 400; j < World.SCREEN_HEIGHT - 400; j += innerWallHeight)
        {
            UnbreakableWall tempWallArea1 = new UnbreakableWall();
            tempWallArea1.setX(400);
            tempWallArea1.setY(j);
            tempWallArea1.setImg(img);

            UnbreakableWall tempWallArea2 = new UnbreakableWall();
            tempWallArea2.setX(World.SCREEN_WIDTH - 400);
            tempWallArea2.setY(j);
            tempWallArea2.setImg(img);

            //add the walls to the worldItem ArrayList to spawn later in paintComponent
            World.worldItems.add(tempWallArea1);
            World.worldItems.add(tempWallArea2);
        }

        //get the height for a wall once since it is always the same to reduce function calls in for loop
        int tempWidth = img.getWidth(null);
        int tempHeight = img.getHeight(null);

        //draw the walls bordering the world
        for (int i = 0; i < World.SCREEN_WIDTH; i += tempWidth)
        {
            UnbreakableWall tempWallArea1 = new UnbreakableWall();
            tempWallArea1.setX(i);
            tempWallArea1.setY(World.SCREEN_HEIGHT - tempWidth);
            tempWallArea1.setImg(img);

            UnbreakableWall tempWallArea2 = new UnbreakableWall();
            tempWallArea2.setX(i);
            tempWallArea2.setY(0);
            tempWallArea2.setImg(img);

            World.worldItems.add(tempWallArea1);
            World.worldItems.add(tempWallArea2);
        }
        for (int i = 0; i < World.SCREEN_HEIGHT; i += tempHeight)
        {
            UnbreakableWall tempWallArea1 = new UnbreakableWall();
            tempWallArea1.setX(World.SCREEN_WIDTH - tempWidth);
            tempWallArea1.setY(i);
            tempWallArea1.setImg(img);

            UnbreakableWall tempWallArea2 = new UnbreakableWall();
            tempWallArea2.setX(0);
            tempWallArea2.setY(i);
            tempWallArea2.setImg(img);

            World.worldItems.add(tempWallArea1);
            World.worldItems.add(tempWallArea2);
        }
    }
}
