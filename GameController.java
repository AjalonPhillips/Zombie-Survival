import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * GameController.java
 * 
 * This class represents the Controller in the MVC architecture.
 * It contains the main method, connects the Model and View, and handles user input.
 */
public class GameController {
    private GameModel model;
    private GameView view;
    private JFrame frame;

    public GameController() {
        // Initialize Model and View
        this.model = new GameModel();
        this.view = new GameView(model);

        // Set up the JFrame
        setupWindow();
        
        System.out.println("Controller initialized.");
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
