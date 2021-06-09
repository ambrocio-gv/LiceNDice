/**
 This is a template for a Java file.

 @author Gerard V. Ambrocio
 @version May 30, 2021
 **/

/*
This class containes the code that sets up the main Jframe for the player. This class contains the
 */


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


public class GameFrame {
    private JFrame frame;
    private GameCanvas gc;
    int playerId;
    Container contentPane;
    private JButton up;
    private JButton down;
    private JButton left;
    private JButton right;
    private JButton rotate;
    private JLabel myPoints;
    private JLabel enemyPoints;
    private JLabel myLabel;
    private JLabel enemyLabel;
    private JLabel goalLabel;
    private JLabel goalPoints;


    // Game frame constructor
    public GameFrame(){

        gc = new GameCanvas();
        frame = new JFrame();
        up = new JButton("UP");
        down = new JButton("DOWN");
        left= new JButton("LEFT");
        right = new JButton("RIGHT");
        rotate = new JButton("ROTATE");
        enemyPoints = new JLabel();
        myPoints = new JLabel();
        myLabel = new JLabel();
        enemyLabel = new JLabel();
        goalLabel = new JLabel();
        goalPoints = new JLabel();

        frame.setResizable(false);
    }

    //the main GUI that sets the game canvas and the scoring panel as well as the connection to the server
    public void setUpGUI(){

        gc.connectToServer();
        frame.setTitle("Player #" + gc.getPlayerID() + " - CATCH 50 LICE TO WIN!");


        Container contentPane = frame.getContentPane();
        JPanel scorePanel = new JPanel();


        scorePanel.setLayout((new GridLayout(1,4)));

        scorePanel.add(myLabel);
        myLabel.setText("My Points: ");
        myLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scorePanel.add(myPoints);
        myPoints.setHorizontalAlignment(SwingConstants.LEFT);

        scorePanel.add(enemyLabel);
        enemyLabel.setText("Enemy Points: ");
        enemyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scorePanel.add(enemyPoints);
        enemyPoints.setHorizontalAlignment(SwingConstants.LEFT);




        contentPane.add(gc, BorderLayout.CENTER);

        contentPane.add(scorePanel,BorderLayout.SOUTH);

        gc.setPreferredSize(new Dimension(gc.getScreenWidth(), gc.getScreenHeight()));
        frame.pack();
        frame.requestFocusInWindow();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        gc.canvasAnimation();
        gc.appleAnimation();
        frameAnimation();



    }

    //method to add key bindings that assigns keys in the keyboard to a direction
    public void addKeyBindings(){
        JPanel cp = (JPanel) frame.getContentPane();
        cp.setFocusable(true);
        cp.requestFocusInWindow();

        ActionMap am = cp.getActionMap();
        InputMap im = cp.getInputMap();

        am.put("upPress", new MoveAction("up"));
        am.put("downPress", new MoveAction("down"));
        am.put("leftPress", new MoveAction("left"));
        am.put("rightPress", new MoveAction("right"));
        am.put("spacePress", new MoveAction(""));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0,false),"upPress");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0,false),"downPress");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0,false),"leftPress");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0,false),"rightPress");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0,false),"spacePress");

    }

    //method to get the player and set the direction of the player
    private class MoveAction extends AbstractAction {
        private String direction;

        public MoveAction(String dir){
            direction = dir;
        }

        @Override
        public void actionPerformed(ActionEvent ae){
            gc.getme().setDirection(direction);

        }

    }

    // method to change the score in the score panel
    public void frameAnimation(){
        Timer animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                myPoints.setText(String.valueOf(gc.getMePoints()));
                enemyPoints.setText(String.valueOf(gc.getEnemyPoints()));

            }
        });
        animationTimer.start();
    }



}
