import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * GameView.java
 * 
 * This class represents the View in the MVC architecture.
 * It extends JPanel and is responsible for rendering the game state.
 */
public class GameView extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private GameModel model;

    public GameView(GameModel model) {
        this.model = model;
        // Set the preferred size of the panel
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // Set a background color for the blank window
        this.setBackground(Color.DARK_GRAY);
        System.out.println("View initialized with Model reference.");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (model == null) return;

        // TODO: Render Player
        // g.setColor(Color.BLUE);
        // g.fillOval((int)model.getPlayer().getX(), (int)model.getPlayer().getY(), 20, 20);

        // TODO: Render Zombies
        // for (Zombie zombie : model.getZombies()) { ... }

        // TODO: Render Bullets
        // for (Bullet bullet : model.getBullets()) { ... }
    }
}
