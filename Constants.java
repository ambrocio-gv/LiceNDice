/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/

/*
This class is used to have the constants easily changed throughout the project especially since this is grid based. It also contains a method to make an array
based on the size of the unit of the grid.
 */

public class Constants {
    public static final int unitsize = 60;
    public static final int screenwidth = 600;
    public static final int screenheight = 600;
    public static int gameunits = (screenwidth*screenheight)/unitsize;


    public static int[] gridpoints10x10 = new int []{0,1,2,3,4,5,6,7,8,9};
    public static int[] gridpointsinner = new int []{1,2,3,4,5,6,7,8};

    public static int[] multiplyArray(int[] v, int c) {
        int[] newArray = new int[v.length];
        for (int i = 0; i < v.length; i++) {
            newArray[i] = v[i] * c;
        }
        return newArray;
    }

    public static int X[] = Constants.multiplyArray(Constants.gridpoints10x10, Constants.unitsize);
    public static int Y[] = Constants.multiplyArray(Constants.gridpoints10x10, Constants.unitsize);

    public static int Xinner[] = Constants.multiplyArray(Constants.gridpointsinner, Constants.unitsize);
    public static int Yinner[] = Constants.multiplyArray(Constants.gridpointsinner, Constants.unitsize);



    public static final int delay = 10;
    public static final int thread = 10;

    public static final int goal = 50;


}
