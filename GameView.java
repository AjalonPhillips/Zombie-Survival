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

        // Render Player
        Player player = model.getPlayer();
        g.setColor(Color.BLUE);
        g.fillRect((int)player.getX() - 15, (int)player.getY() - 15, 30, 30);

        // Render Zombies
        g.setColor(new Color(34, 139, 34)); // Forest Green
        for (Zombie zombie : model.getZombies()) {
            g.fillOval((int)zombie.getX() - 15, (int)zombie.getY() - 15, 30, 30);
        }

        // Render Bullets
        g.setColor(Color.YELLOW);
        for (Bullet bullet : model.getBullets()) {
            g.fillOval((int)bullet.getX() - 5, (int)bullet.getY() - 5, 10, 10);
        }
    }
}
