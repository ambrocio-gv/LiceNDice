/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/

/*
This class represents the player class which determines the hand sprite in the game for each of the 2 players
and contains the code that manages the player's appearance and functionality. The player will be able to move at a speed in a given direction.
 */

import java.awt.*;

public class Player extends GridUnit {


    private boolean up, down, left, right;
    private int speed;
    private int myPoints;
    private int enemyPoints;

    public Player(double xh, double yv, double s, Image img) {
        this.x = xh;
        this.y = yv;
        this.size = s;
        //this.color = c;
        this.image = img;


        this.up = false;
        this.down = false;
        this.left = false;
        this.right = false;


    }




    public void draw(Graphics2D g2d) {
        //Rectangle2D.Double p = new Rectangle2D.Double(x, y, size, size);
        //g2d.setColor(color);
        //g2d.fill(p);

        g2d.drawImage(image, (int)x, (int)y, (int)size,(int)size, null);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHints(rh);
    }

    //Method used to move the player sprite
    public void move(){
        speed = 10;
        if (up) {
            y -= speed;

        }
        else if (down){
            y += speed;

        }
        else if(left){
            x -= speed;

        }
        else if(right){
            x += speed;

        }
    }

    //method used to set the direction of the player using booleans.
    public void setDirection(String dir){
        if(dir.equals("up")){
            up = true;
            down = false;
            left = false;
            right = false;
        }
        else if(dir.equals("down")){
            up = false;
            down = true;
            left = false;
            right = false;
        }
        else if(dir.equals("left")){
            up = false;
            down = false;
            left = true;
            right = false;
        }
        else if(dir.equals("right")){
            up = false;
            down = false;
            left = false;
            right = true;
        } else {
            up = false;
            down = false;
            left = false;
            right = false;
        }
    }


}
