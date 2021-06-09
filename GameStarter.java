/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/


/*
This class contains the main method that will start the game from the player's side. One is to set up the GUI including its attatched canvas, the otehr is
to add key bindings that enable the user to press the keyboard to move the sprites in the game.
 */

public class GameStarter {
    public static void main(String[] args) {
        GameFrame f = new GameFrame();

        f.setUpGUI();
        f.addKeyBindings();

    }





}
