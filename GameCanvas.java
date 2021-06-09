/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/


/*
This class extends Jcomponent and overrides the paint Component method in order to create the custom drawing,
additionally this containes the methods to connect to the server as well as run the threads.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class GameCanvas extends JComponent {
    private Player me;
    private Player enemy;
    private Apple apple;

    private Socket socket;
    private int playerID;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;


    private Random random = new Random();
    private int applex = Constants.X[new Random().nextInt(Constants.X.length)];
    private int appley = Constants.Y[new Random().nextInt(Constants.Y.length)];

    public int mePoints = 0;
    public int enemyPoints = 0;
    public Image lice;
    public Image hand;
    public Image hand2;
    public Image hair;

    private int delay = Constants.delay;
    private int unit_size = Constants.unitsize;
    private int screen_height = Constants.screenheight;
    private int screen_width = Constants.screenwidth;
    private Color meColor = Color.cyan;
    private Color enemyColor = Color.red;

    private boolean appleColliding = false;

    private boolean checkWinner = false;
    private String myScoreMessage = "You LOST! and Didn't catch 50 ";



    //Constructor for the canvas as well as where images are loaded for the objects to use
    public GameCanvas() {


        loadImages();
        me = new Player(0,0, unit_size, hand);
        enemy = new Player(0,0,unit_size,hand2);
        apple = new Apple(applex, appley, unit_size, lice);


    }

    //loads the saved images from the designated files
    public void loadImages() {
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("lice.png"));
        lice = i1.getImage();

        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("hand.png"));
        hand = i2.getImage();

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("hand2.png"));
        hand2 = i3.getImage();

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("hair.jpg"));
        hair = i4.getImage();
    }

    // creates a player
    public void createPlayer(int pid) {
        if(playerID == 1){
            me = new Player(Constants.Xinner[0], Constants.Yinner[0], unit_size, hand);
            enemy = new Player(Constants.Xinner[7],Constants.Yinner[7],unit_size,hand2);
        }else{
            enemy = new Player(Constants.Xinner[0], Constants.Yinner[0], unit_size, hand);
            me = new Player(Constants.Xinner[7],Constants.Yinner[7],unit_size,hand2);
        }
    }


    // class that overrides the paint component and draws on the canvas
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(hair, 0, 0, null);

        Graphics2D g2d = (Graphics2D) g;
        apple.draw(g2d);
        me.draw(g2d);
        enemy.draw(g2d);


        if(checkTheWinner() == true){
            gameOver(g);

        }


//TO SEE TEMPORARY GRID:
//        g.setColor(Color.blue);
//        for(int i=0; i < screen_height/unit_size; i++){
//            g.drawLine(i*unit_size, 0,i*unit_size, screen_height);
//        }
//        for(int i=0; i < screen_width/unit_size; i++){
//            g.drawLine(0, i*unit_size, screen_width, i*unit_size);
//        }



    }

    // the main animation which makes the player move and also traverse walls
    public void canvasAnimation(){
        Timer animationTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {


                me.move();

                if(me.getX() <= Constants.X[0]){
                    me.setX(Constants.X[9]);
                }
                else if(me.getX() > Constants.X[9]){
                    me.setX(Constants.X[0]);
                }
                else if(me.getY() <= Constants.Y[0]){
                    me.setY(Constants.Y[9]);
                }
                else if(me.getY() > Constants.Y[9]){
                    me.setY(Constants.Y[0]);
                }



                repaint();
            }
        });
        animationTimer.start();
    }

    // separate animation for the lice/apple to randomly change location depending on the collsion by a player
    public void appleAnimation(){
        Timer appleTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(apple.isColliding(me)){
                    apple.setX(apple.changelocationX(applex));
                    apple.setY(apple.changelocationY(appley));
                    //appleColliding = true;
                    mePoints++;
                }




                repaint();
            }
        });
        appleTimer.start();
    }



    // class that connects to the server and also assigns player number as well as create the instances of the player once connected
    public void connectToServer() {
        try {
//            socket = new Socket("localhost", 55555);
            Scanner console = new Scanner(System.in);
            System.out.print("IP Address: ");
            String ipAddress = console.nextLine();
            System.out.print("Port Number: ");
            int portNumber = Integer.parseInt(console.nextLine());
            socket = new Socket(ipAddress,portNumber);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("You are player# " + playerID);
            if(playerID == 1) {
                System.out.println("Waiting for Player #2 to connect...");
            }
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);


            rfsRunnable.waitForStartMsg();


        } catch (IOException ex) {
            System.out.println("IOException from connectServer()");
        }
        createPlayer(playerID);



    }


    //class that implements a runnable that reads from the server
    private class ReadFromServer implements Runnable {

        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream in) {
            dataIn = in;
            System.out.println("RFS Runnable created");
        }

        @Override
        public void run() {
            try {
                if (enemy != null){
                    while (true) {
                        apple.setX(dataIn.readDouble());
                        apple.setY(dataIn.readDouble());
                        enemy.setX(dataIn.readDouble());
                        enemy.setY(dataIn.readDouble());
                        enemyPoints = dataIn.readInt();

                    }
                }

            } catch (IOException ex) {
                System.out.println("IOException from RFS run()");
            }
        }

        // an inner class that waits for the start message string from the server and once initiated will run the threads and start the game
        public void waitForStartMsg() {
            try {
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();

            } catch (IOException ex){
                System.out.println("IOException from waitForStartMsg()");
            }
        }
    }

    //class that implements a runnable that reads from the server
    private class WriteToServer implements Runnable {

        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out) {
            dataOut = out;
            System.out.println("WTS Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if(me != null){
                        dataOut.writeDouble(apple.getX());
                        dataOut.writeDouble(apple.getY());
                        dataOut.writeDouble(me.getX());
                        dataOut.writeDouble(me.getY());
                        dataOut.writeInt(mePoints);
                        dataOut.flush();
                    }
                    if(checkTheWinner() == true){
                        closeConnection();
                        break;
                    }
                    try {
                        Thread.sleep(Constants.thread);
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException from WTS run()");
                    }
                }
            }catch (IOException ex){
                System.out.println("IOException from WTS run()");
            }

        }



    }

    // method that will overwrite the current display to show a game over screen and declare the winner/loser
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.black);
        g.fillRect(0, 0, Constants.screenwidth, Constants.screenheight);
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString(myScoreMessage, (Constants.screenwidth - metrics1.stringWidth("You LOST! and Didn't Reach 50" ))/2,Constants.screenheight/4);
        //GameOver text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (Constants.screenwidth - metrics2.stringWidth("Game Over"))/2, Constants.screenheight/2);
        //Thank you text
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Thank you for playing Lice n' Dice!", (Constants.screenwidth - metrics1.stringWidth("Thank you for playing Lice n' Dice!" ))/2,3* Constants.screenheight/4);

    }

    // method that finds out the winner or the loser based on the player score
    public boolean checkTheWinner() {

        if (mePoints >= Constants.goal) {
            myScoreMessage = ("   You WON! and Caught " + Constants.goal + " !");
            closeConnection();
            return true;
        } else if (enemyPoints >= Constants.goal){
            myScoreMessage = ("You LOST! and Didn't catch " + Constants.goal + "!");
            closeConnection();
            return true;
        } else {
            myScoreMessage = "JOKE LANG!";
        }


        return false;


    }

    //method that closes the connection in the client side
    public void closeConnection() {
        try {
            socket.close();
            System.out.println("--CONNECTION CLOSED");


        } catch (IOException ex) {
            System.out.println("IOException on closeConnection() CSC");
        }
    }




    public int getMePoints(){return mePoints;}
    public int getEnemyPoints(){return enemyPoints;}


    public Player getme(){return me;}
    public Player getenemy(){return enemy;}

    public int getScreenWidth(){return screen_width;}
    public int getScreenHeight(){return screen_height;}
    public int getUnit_size() {return unit_size;}
    public int getPlayerID() {return playerID;}
}

