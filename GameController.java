import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * GameController.java
 * 
 * This class represents the Controller in the MVC architecture.
 * It contains the main method, connects the Model and View, and handles user input.
 */
public class GameController implements KeyListener, ActionListener, MouseListener {
    private GameModel model;
    private GameView view;
    private JFrame frame;
    
    // Movement flags
    private boolean up, down, left, right;
    
    // Game loop timer (approx 60 FPS)
    private Timer timer;

    public GameController() {
        // Choice dialog
        String[] options = {"Windowed", "Fullscreen"};
        int choice = JOptionPane.showOptionDialog(null, 
                "Choose Display Mode", "Zombie Survival",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        int width = 800;
        int height = 600;
        boolean isFullscreen = (choice == 1);

        if (isFullscreen) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            width = screenSize.width;
            height = screenSize.height;
        }

        // Initialize Model and View
        this.model = new GameModel(width, height);
        this.view = new GameView(model);

        // Set up the JFrame
        setupWindow(isFullscreen);
        
        // Register listeners
        frame.addKeyListener(this);
        frame.addMouseListener(this);
        
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
            case KeyEvent.VK_SPACE -> {
                if (!model.isMouseShoot()) model.shoot();
            }
            case KeyEvent.VK_1 -> model.selectUpgrade(0);
            case KeyEvent.VK_2 -> model.selectUpgrade(1);
            case KeyEvent.VK_3 -> model.selectUpgrade(2);
            case KeyEvent.VK_UP -> model.navigateMenu(-1);
            case KeyEvent.VK_DOWN -> model.navigateMenu(1);
            case KeyEvent.VK_ENTER -> model.selectMenuOption();
            case KeyEvent.VK_T -> {
                if (model.getState() == GameModel.GameState.OPTIONS) {
                    model.toggleShootControl();
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                if (model.getState() == GameModel.GameState.OPTIONS || model.getState() == GameModel.GameState.OBJECTIVE) {
                    model.selectMenuOption(); // Goes back to menu
                }
            }
            case KeyEvent.VK_R -> {
                if (model.getState() == GameModel.GameState.GAME_OVER) {
                    model.reset();
                }
            }
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
    public void keyTyped(KeyEvent e) {}

    // MouseListener methods
    @Override
    public void mousePressed(MouseEvent e) {
        if (model.getState() == GameModel.GameState.PLAYING && model.isMouseShoot()) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                model.shoot();
            }
        }
    }

    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    /**
     * Configures and displays the main game window.
     */
    private void setupWindow(boolean isFullscreen) {
        frame = new JFrame("Zombie Survival Roguelike");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if (isFullscreen) {
            frame.setUndecorated(true);
            frame.setResizable(false);
            
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice();
            
            if (gd.isFullScreenSupported()) {
                gd.setFullScreenWindow(frame);
            } else {
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        }

        // Add the View (JPanel) to the JFrame
        frame.add(view);
        
        if (!isFullscreen) {
            // Adjust the window size to fit the preferred size of the View
            frame.pack();
            // Center the window on the screen
            frame.setLocationRelativeTo(null);
        }
        
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
