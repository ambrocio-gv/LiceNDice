/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/


/*
This class represents a unit of the screen either for the player or the Apple(lice). There is also a colliding method used as well as general getter and setter methods.
 */


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GridUnit {
    public double x;
    public double y;
    public double size;
    public Color color;
    public Image image;


    public GridUnit() {
    }


    public boolean isColliding(Player other){
        return !(this.x + this.size <= other.getX() ||
                this.x >= other.getX() + other.getSize() ||
                this.y + this.size <= other.getY() ||
                this.y >= other.getY() + other.getSize());
    }



    public void moveH(double n) {
        x += n;
    }

    public void moveV(double n) {
        y += n;
    }

    public void setX(double n) {
        x = n;
    }

    public void setY(double n) {
        y = n;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize(){return size;}


}
