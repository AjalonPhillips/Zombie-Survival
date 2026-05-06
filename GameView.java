import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.util.List;

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
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (model == null) return;

        switch (model.getState()) {
            case START -> drawStartScreen(g);
            case PLAYING -> drawGame(g);
            case UPGRADING -> {
                drawGame(g); // Draw game in background
                drawUpgradeScreen(g);
            }
            case GAME_OVER -> {
                drawGame(g); // Draw game in background
                drawGameOverScreen(g);
            }
        }
    }

    private void drawStartScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 60));
        drawCenteredString(g, "ZOMBIE SURVIVAL", HEIGHT / 2 - 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g, "Move with WASD - Shoot with Space", HEIGHT / 2 + 20);
        drawCenteredString(g, "Press ENTER to Start", HEIGHT / 2 + 80);
    }

    private void drawGame(Graphics g) {
        // Render Bullets
        g.setColor(Color.YELLOW);
        for (Bullet bullet : model.getBullets()) {
            g.fillOval((int)bullet.getX() - 5, (int)bullet.getY() - 5, 10, 10);
        }

        // Render Zombies
        g.setColor(new Color(34, 139, 34)); // Forest Green
        for (Zombie zombie : model.getZombies()) {
            g.fillOval((int)zombie.getX() - 15, (int)zombie.getY() - 15, 30, 30);
        }

        // Render Player
        Player player = model.getPlayer();
        g.setColor(Color.BLUE);
        g.fillRect((int)player.getX() - 15, (int)player.getY() - 15, 30, 30);
        
        // Render UI
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Health: " + player.getHealth(), 20, 30);
        g.drawString("Score: " + model.getScore(), 20, 60);
        
        String timeStr = "Survived: " + model.getSurvivalTime() + "s";
        int timeWidth = g.getFontMetrics().stringWidth(timeStr);
        g.drawString(timeStr, WIDTH - timeWidth - 20, 30);
    }

    private void drawUpgradeScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(g, "CHOOSE AN UPGRADE", 150);
        
        List<UpgradeManager.UpgradeType> choices = model.getCurrentChoices();
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        for (int i = 0; i < choices.size(); i++) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(WIDTH/2 - 200, 220 + i*80, 400, 60);
            g.setColor(Color.WHITE);
            g.drawRect(WIDTH/2 - 200, 220 + i*80, 400, 60);
            
            String choiceStr = (i + 1) + ". " + choices.get(i).description;
            drawCenteredString(g, choiceStr, 258 + i*80);
        }
        
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.ITALIC, 18));
        drawCenteredString(g, "Press 1, 2, or 3 to select", 500);
    }

    private void drawGameOverScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        drawCenteredString(g, "GAME OVER", HEIGHT / 2 - 60);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        drawCenteredString(g, "Final Score: " + model.getScore(), HEIGHT / 2 + 20);
        drawCenteredString(g, "Time Survived: " + model.getSurvivalTime() + "s", HEIGHT / 2 + 60);
        
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(g, "Press 'R' to Restart", HEIGHT / 2 + 130);
    }

    private void drawCenteredString(Graphics g, String text, int y) {
        int x = (WIDTH - g.getFontMetrics().stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
