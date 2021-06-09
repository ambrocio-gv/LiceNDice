/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/

/*
This class contains the code that manages the server's functionality. It also contains the main method that
instantiates and starts the server.
 */

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GameServer {
    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    private Socket p1Socket;
    private Socket p2Socket;

    private ReadFromClient p1ReadRunnable;
    private ReadFromClient p2ReadRunnable;
    private WriteToClient p1WriteRunnable;
    private WriteToClient p2WriteRunnable;

    private Socket aSocket;
    private ReadFromClient aReadRunnable;
    private WriteToClient aWriteRunnable;


    private Boolean appleColliding;

    private double p1x, p1y, p2x, p2y, ax, ay;
    private int p1points, p2points;

    public GameServer() {
        System.out.println("===== GAME SERVER =====");
        numPlayers = 0;
        maxPlayers = 2;

        p1x = 0;
        p1y = 0;

        p2x = Constants.screenwidth - Constants.gameunits;
        p2y = Constants.screenheight - Constants.gameunits;

        Random random = new Random();
        ax = Constants.Xinner[new Random().nextInt(Constants.Xinner.length)];
        ay = Constants.Yinner[new Random().nextInt(Constants.Yinner.length)];


        try {
            ss = new ServerSocket(45371);
        } catch (IOException ex){
            System.out.println("IOException from GameServer constructor");
        }
    }

    //class to accept connections
    public void acceptConnections() {
        try{
            System.out.println("Waiting for connections...");

            while (numPlayers < maxPlayers) {
                Socket s = ss.accept();

                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());


                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has connected.");

                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);


                if(numPlayers == 1){
                    p1Socket = s;
                    p1ReadRunnable = rfc;
                    p1WriteRunnable = wtc;

                } else {
                    p2Socket = s;
                    p2ReadRunnable = rfc;
                    p2WriteRunnable = wtc;
                    p1WriteRunnable.sendStartMsg();
                    p2WriteRunnable.sendStartMsg();



                    Thread readThread1 = new Thread(p1ReadRunnable);
                    Thread readThread2 = new Thread(p2ReadRunnable);

                    readThread1.start();
                    readThread2.start();

                    Thread writeThread1 = new Thread(p1WriteRunnable);
                    Thread writeThread2 = new Thread(p2WriteRunnable);

                    writeThread1.start();
                    writeThread2.start();

                }



            }
            System.out.println("No longer accepting connections");

        } catch (IOException ex) {
            System.out.println("IOException from acceptConnections()");
        }
    }

    // class that implements a runnable that reads from the client
    private class ReadFromClient implements Runnable {

        private int playerID;
        private DataInputStream dataIn;

        public ReadFromClient(int pid, DataInputStream in) {
            playerID = pid;
            dataIn = in;
            System.out.println("RFC" + playerID + " Runnable created");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ax = dataIn.readDouble();
                    ay = dataIn.readDouble();
                    if (playerID == 1) {
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();
                        p1points = dataIn.readInt();

                    } else {
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();
                        p2points = dataIn.readInt();

                    }

                    if(p1points >= Constants.goal || p2points >= Constants.goal){
                        closeConnection();
                        break;
                    }
                }

            } catch (IOException ex) {
                System.out.println("IOException from RFC run()");
            }
        }
    }

    //class that implements a runnable that writes to the client
    private class WriteToClient implements Runnable {

        private int playerID;
        private DataOutputStream dataOut;

        public WriteToClient(int pid, DataOutputStream out) {
            playerID = pid;
            dataOut = out;
            System.out.println("RFC" + playerID + " Runnable created");
        }

        @Override
        public void run() {
            try {
                while(true) {
                    dataOut.writeDouble(ax);
                    dataOut.writeDouble(ay);
                    if(playerID == 1) {
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);
                        dataOut.writeInt(p2points);


                    } else {
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);
                        dataOut.writeInt(p1points);
                    }




                    dataOut.flush();

                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException ex) {
                        System.out.println("InterruptedException from WTC run()");
                    }
                }
            } catch (IOException ex) {
                System.out.println("IOException from WTC run()");
            }
        }

        //inner class that sends the start message to the players that the server is ready to accept connections.
        public void sendStartMsg() {
            try {
                dataOut.writeUTF("We now have 2 players. Ready to start the game!");
            } catch (IOException ex) {
                System.out.println("IOException from sendStartMsg()");
            }

        }


    }

    // method to close server side connection
    public void closeConnection() {

            try {
                ss.close();
                System.out.println("Connection closed");
            } catch (IOException ex) {
                System.out.println("IOException on closeConnect() SSC");
            }

    }



    public static void main(String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();

    }















}
