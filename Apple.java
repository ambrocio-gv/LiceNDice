/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/


/*
This class represents the apple or the lice in the game that are to be caught by the players,
it is randomly generated and their is a method to change its location
 */

import java.awt.*;
import java.util.Random;


public class Apple extends GridUnit {



    public Apple(double xh, double yv, double s, Image img) {
        this.x = xh;
        this.y = yv;
        this.size = s;

        this.image = img;
    }

    public void draw(Graphics2D g2d) {

        g2d.drawImage(image, (int)x, (int)y, (int)size,(int)size, null);

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHints(rh);
    }



    // methods to change the location of the apple/lice
    public int changelocationX(int x){
        Random random = new Random();
        int applex = Constants.X[new Random().nextInt(Constants.X.length)];
        return applex;
    }

    public int changelocationY(int y){
        Random random = new Random();
        int appley = Constants.Y[new Random().nextInt(Constants.Y.length)];
        return appley;
    }






}
