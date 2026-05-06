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
    private GameModel model;

    public GameView(GameModel model) {
        this.model = model;
        this.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (model == null) return;

        int width = model.getWidth();
        int height = model.getHeight();

        switch (model.getState()) {
            case MENU -> drawMenuScreen(g, width, height);
            case OBJECTIVE -> drawObjectiveScreen(g, width, height);
            case OPTIONS -> drawOptionsScreen(g, width, height);
            case PLAYING -> drawGame(g, width, height);
            case UPGRADING -> {
                drawGame(g, width, height); 
                drawUpgradeScreen(g, width, height);
            }
            case GAME_OVER -> {
                drawGame(g, width, height); 
                drawGameOverScreen(g, width, height);
            }
        }
    }

    private void drawMenuScreen(Graphics g, int width, int height) {
        g.setColor(new Color(20, 20, 20)); // Darker background
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        drawCenteredString(g, "ZOMBIE SURVIVAL", height / 2 - 150, width);
        
        String[] options = model.getMenuOptions();
        int selected = model.getMenuIndex();
        
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            if (i == selected) {
                g.setColor(Color.YELLOW);
                drawCenteredString(g, "> " + options[i] + " <", height / 2 + i * 50, width);
            } else {
                g.setColor(Color.WHITE);
                drawCenteredString(g, options[i], height / 2 + i * 50, width);
            }
        }
        
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.ITALIC, 18));
        drawCenteredString(g, "Use Arrow Keys to Navigate - ENTER to Select", height - 50, width);
    }

    private void drawObjectiveScreen(Graphics g, int width, int height) {
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "OBJECTIVE", 100, width);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 22));
        int startY = 200;
        drawCenteredString(g, "Survive as long as possible against the growing zombie horde.", startY, width);
        drawCenteredString(g, "Kill zombies to earn score and stay alive.", startY + 40, width);
        drawCenteredString(g, "Every 30 seconds, you will be offered a powerful upgrade.", startY + 80, width);
        drawCenteredString(g, "As time passes, zombies will spawn faster and move quicker.", startY + 120, width);
        
        g.setColor(Color.YELLOW);
        drawCenteredString(g, "Controls: WASD to move, SPACE to shoot", startY + 200, width);
        
        g.setColor(Color.GRAY);
        drawCenteredString(g, "Press ENTER to return to Menu", height - 100, width);
    }

    private void drawOptionsScreen(Graphics g, int width, int height) {
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "OPTIONS", 100, width);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        drawCenteredString(g, "Gameplay Controls", 200, width);
        
        String control = model.isMouseShoot() ? "Left Mouse Click" : "Spacebar";
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        drawCenteredString(g, "Shoot Button: [ " + control + " ]", 300, width);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.ITALIC, 18));
        drawCenteredString(g, "Press 'T' to Toggle Setting - Press ENTER to Go Back", 400, width);
        
        g.setColor(Color.GRAY);
        drawCenteredString(g, "Settings take effect immediately.", height - 100, width);
    }

    private void drawGame(Graphics g, int width, int height) {
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
        g.drawString(timeStr, width - timeWidth - 20, 30);
    }

    private void drawUpgradeScreen(Graphics g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(g, "CHOOSE AN UPGRADE", 150, width);
        
        List<UpgradeManager.UpgradeType> choices = model.getCurrentChoices();
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        for (int i = 0; i < choices.size(); i++) {
            g.setColor(new Color(255, 255, 255, 50));
            g.fillRect(width/2 - 200, 220 + i*80, 400, 60);
            g.setColor(Color.WHITE);
            g.drawRect(width/2 - 200, 220 + i*80, 400, 60);
            
            String choiceStr = (i + 1) + ". " + choices.get(i).description;
            drawCenteredString(g, choiceStr, 258 + i*80, width);
        }
        
        g.setColor(Color.GRAY);
        g.setFont(new Font("Arial", Font.ITALIC, 18));
        drawCenteredString(g, "Press 1, 2, or 3 to select", 500, width);
    }

    private void drawGameOverScreen(Graphics g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, width, height);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        drawCenteredString(g, "GAME OVER", height / 2 - 60, width);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        drawCenteredString(g, "Final Score: " + model.getScore(), height / 2 + 20, width);
        drawCenteredString(g, "Time Survived: " + model.getSurvivalTime() + "s", height / 2 + 60, width);
        
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(g, "Press 'R' to Restart", height / 2 + 130, width);
    }

    private void drawCenteredString(Graphics g, String text, int y, int width) {
        int x = (width - g.getFontMetrics().stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
