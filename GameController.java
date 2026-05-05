import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * GameController.java
 * 
 * This class represents the Controller in the MVC architecture.
 * It contains the main method, connects the Model and View, and handles user input.
 */
public class GameController implements KeyListener, ActionListener {
    private GameModel model;
    private GameView view;
    private JFrame frame;
    
    // Movement flags
    private boolean up, down, left, right;
    
    // Game loop timer (approx 60 FPS)
    private Timer timer;

    public GameController() {
        // Initialize Model and View
        this.model = new GameModel();
        this.view = new GameView(model);

        // Set up the JFrame
        setupWindow();
        
        // Register key listener
        frame.addKeyListener(this);
        
        // Initialize and start the game loop
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
        
        System.out.println("Controller initialized with game loop.");
    }

    /**
     * Action performed by the Timer (the game loop).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        model.update(up, down, left, right);
        view.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> model.shoot();
            case KeyEvent.VK_1 -> model.selectUpgrade(0);
            case KeyEvent.VK_2 -> model.selectUpgrade(1);
            case KeyEvent.VK_3 -> model.selectUpgrade(2);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    /**
     * Configures and displays the main game window.
     */
    private void setupWindow() {
        frame = new JFrame("Zombie Survival Roguelike");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add the View (JPanel) to the JFrame
        frame.add(view);
        
        // Adjust the window size to fit the preferred size of the View
        frame.pack();
        
        // Center the window on the screen
        frame.setLocationRelativeTo(null);
        
        // Make the window visible
        frame.setVisible(true);
    }

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        // Ensure the UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameController();
            }
        });
    }
}
