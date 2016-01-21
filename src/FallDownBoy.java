
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author dawsr2694
 */
public class FallDownBoy extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    //Player position variables
    int x = 100;
    int y = 500;
    //Create player
    Rectangle player = new Rectangle(375, 375, 50, 50);
    int moveX = 0;
    int moveY = 0;
    //Button pressed variable
    boolean buttonPressed = false;
    //Keyboard variables
    boolean right = false;
    boolean left = false;
    boolean enter = false;
    boolean restart = false;
    //Array for creating blocks
    ArrayList<Rectangle> blocks = new ArrayList<>();
    //Image variable
    BufferedImage title = loadImage("ICS-3UI Title Screen.png");
    //Load in game over screen
    BufferedImage gameOver = loadImage("Game over.png");
    //Create collision variable
    boolean collision = false;

    //Load in image
    public BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println("Error loading " + filename);
        }
        return img;
    }
    //Create speed variable 
    int speed = 1;
    //Create random number generator for x value
    public int randomX() {
        int random = (int) (Math.random() * (600 - 1 + 1)) + 1;
        return random;
    }
    //Create random number generator for y value
        public int randomY() {
        int random = (int) (Math.random() * (1000000 - 600 + 1)) + 600;
        return random;
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE 

        //Make screen white
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //Draw title 
        g.drawImage(title, 75, -40, 768, 614, null);
        //If enter is true, set background to black
        if (enter == true) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
        }

        //Set colour for player
        g.setColor(Color.red);
        //Draw player
        g.fillRect(player.x, player.y, player.width, player.height);

        //Set colour for blocks
        g.setColor(Color.WHITE);
        // go through each block
        for (Rectangle block : blocks) {
            // draw the block
            g.setColor(Color.WHITE);
            g.fillRect(block.x, block.y, block.width, block.height);
        }
        //Draw game over image
        if (collision == true) {
            g.drawImage(gameOver, 65, -40, 768, 614, null);
        }
        //If player leaves screen draw game over image
        if(player.x >800 || player.x < 0){
            g.drawImage(gameOver, 65, -40, 768, 614, null);
        }

        // GAME DRAWING ENDS HERE
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {

        //Initial things to do before game starts
        //add blocks
        initialize();

        //Any changes made to width height need to be subtracted from x y



        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 

            //if left button has been pressed/held down
            if (left) {
                //Move left at specified speed
                moveX = -30;
                //if right button has been pressed/held down
            } else if (right) {
                //Move right at specified speed
                moveX = 30;
                //if no button is being pressed or held
            } else {
                //No movement
                moveX = 0;
            }
            //Check if enter key or restart key has been pressed
            if (enter == true) {
                // move the player
                player.x = player.x + moveX;

                // go through all blocks
                for (Rectangle block : blocks) {
                    // is the player hitting a block
                    if (!player.intersects(block)) {
                        block.y -= speed;
                    } else {
                        //Stop player from moving left and right
                        left = false;
                        right = false;
                        //Set variable
                        collision = true;

                    }
                }
                //else do nothing
            } else {
            }
            //If restart is pressed, call initialize method
            if (restart == true) {
                initialize();
            }
            //If player leaves screen, end game
            if(player.x >800 || player.x < 0){
                left = false;
                right = false;
            }

            // GAME LOGIC ENDS HERE 

            // update the drawing (calls paintComponent)
            repaint();



            // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
            // USING SOME SIMPLE MATH
            deltaTime = System.currentTimeMillis() - startTime;
            if (deltaTime > desiredTime) {
                //took too much time, don't wait
            } else {
                try {
                    Thread.sleep(desiredTime - deltaTime);
                } catch (Exception e) {
                };
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        FallDownBoy game = new FallDownBoy();
        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);
        frame.addKeyListener(game); //keyboard
        // starts my game loop
        game.run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Find key
        int key = e.getKeyCode();
        //If left arrow key is pressed, the player moves left
        if (key == KeyEvent.VK_LEFT) {
            //Change left boolean to true
            left = true;
            //If right arrow key is pressed, the player moves right
        } else if (key == KeyEvent.VK_RIGHT) {
            //Change right boolean to true
            right = true;
        }
        //If enter key is pressed
        if (key == KeyEvent.VK_ENTER) {
            //Change enter boolean to true
            enter = true;
        }
        //If r (restart) is pressed
        if (key == KeyEvent.VK_R) {
            //Change restart boolean to true
            restart = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //Find key
        int key = e.getKeyCode();
        //If left arrow key is released, the player stops moving
        if (key == KeyEvent.VK_LEFT) {
            //Change left boolean to false
            left = false;
            //if right arrow key is released, the player stops moving
        } else if (key == KeyEvent.VK_RIGHT) {
            //Change right boolean to false
            right = false;
        }
    }
    //create initialize method

    public void initialize() {
        //Clear blocks list
        blocks.clear();
        //Reset player to starting position
        player = new Rectangle(375, 375, 50, 50);
        //Set rectangles back to starting position
        for (int i = 0; i < 10000; i++) {
            Rectangle block = new Rectangle(randomX(), randomY(), 100, 50);
            while(block.intersects(player)){
                block = new Rectangle(randomX(), randomY(), 100, 50);
            }
            
            blocks.add(block);
        }
        //Set all booleans to false
        right = false;
        left = false;
        enter = false;
        restart = false;
        buttonPressed = false;
        collision = false;
        //Set speed to 1
        speed = 1;
    }
}