import javax.swing.JPanel;
import java.awt.*;
import java.util.List;

/**
 * GameView.java
 */
public class GameView extends JPanel {
    private GameModel model;

    public GameView(GameModel model) {
        this.model = model;
        this.setPreferredSize(new Dimension(model.getWidth(), model.getHeight()));
        this.setBackground(new Color(30, 30, 35)); // Slightly blue-ish dark background
        
        setCursor(getToolkit().createCustomCursor(
            new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB),
            new Point(0, 0), "invisible"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (model == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = model.getWidth();
        int height = model.getHeight();

        switch (model.getState()) {
            case MENU -> {
                setCursor(Cursor.getDefaultCursor());
                drawMenuScreen(g2, width, height);
            }
            case OBJECTIVE -> drawObjectiveScreen(g2, width, height);
            case OPTIONS -> drawOptionsScreen(g2, width, height);
            case PLAYING -> {
                drawGame(g2, width, height);
                drawCrosshair(g2);
            }
            case PAUSED -> {
                setCursor(Cursor.getDefaultCursor());
                drawGame(g2, width, height);
                drawPauseScreen(g2, width, height);
            }
            case UPGRADING -> {
                setCursor(Cursor.getDefaultCursor());
                drawGame(g2, width, height);
                drawUpgradeScreen(g2, width, height);
            }
            case GAME_OVER -> {
                setCursor(Cursor.getDefaultCursor());
                drawGame(g2, width, height);
                drawGameOverScreen(g2, width, height);
            }
        }
    }

    private void drawCrosshair(Graphics2D g) {
        int x = model.getMouseX();
        int y = model.getMouseY();
        g.setColor(new Color(255, 255, 255, 180));
        g.setStroke(new BasicStroke(2));
        g.drawLine(x - 12, y, x + 12, y);
        g.drawLine(x, y - 12, x, y + 12);
        g.drawOval(x - 6, y - 6, 12, 12);
    }

    private void drawMenuScreen(Graphics2D g, int width, int height) {
        g.setColor(new Color(15, 15, 20));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        drawCenteredString(g, "ZOMBIE SURVIVAL", height / 2 - 150, width);
        String[] options = model.getMenuOptions();
        int selected = model.getMenuIndex();
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selected ? Color.YELLOW : Color.WHITE);
            drawCenteredString(g, i == selected ? "> " + options[i] + " <" : options[i], height / 2 + i * 50, width);
        }
    }

    private void drawObjectiveScreen(Graphics2D g, int width, int height) {
        g.setColor(new Color(15, 15, 20));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "OBJECTIVE", 100, width);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 22));
        String[] lines = {
            "Survive as long as possible against the growing zombie horde.",
            "Normal (Green), Sprinter (Light Blue), Brute (Dark Green).",
            "Collect Power-Ups dropped by zombies to stay alive.",
            "Choose powerful upgrades every 30 seconds.",
            "WASD to Move, LMB/Space to Shoot, R to Reload."
        };
        for(int i=0; i<lines.length; i++) drawCenteredString(g, lines[i], 200 + i*40, width);
        drawCenteredString(g, "Press ENTER to return", height - 100, width);
    }

    private void drawOptionsScreen(Graphics2D g, int width, int height) {
        g.setColor(new Color(15, 15, 20));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "OPTIONS", 100, width);

        String status = model.isEnvironmentDetailed() ? "Enabled" : "Disabled";
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        drawCenteredString(g, "Detailed Environment: [ " + status + " ]", 300, width);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.ITALIC, 18));
        drawCenteredString(g, "Press 'T' to Toggle - Press ENTER to Go Back", 400, width);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        drawCenteredString(g, "(Includes Rain, Dynamic Lighting, and Blood Stains)", 340, width);
    }

    private void drawGame(Graphics2D g, int width, int height) {
        boolean detailed = model.isEnvironmentDetailed();

        // Floor Decals (Blood)
        if (detailed) {
            for (BloodDecal d : model.getDecals()) {
                int alpha = Math.min(100, (int)(100 * (Math.min(60, d.getLife()) / 60.0)));
                g.setColor(new Color(d.getColor().getRed(), d.getColor().getGreen(), d.getColor().getBlue(), alpha));
                g.fillOval((int)d.getX() - d.getSize()/2, (int)d.getY() - d.getSize()/2, d.getSize(), d.getSize());
            }
        }

        // PowerUps
        for (PowerUp p : model.getPowerUps()) {
            g.setColor(p.getType().color);
            g.fillOval((int)p.getX() - 12, (int)p.getY() - 12, 24, 24);
            g.setColor(new Color(255, 255, 255, 200));
            g.setStroke(new BasicStroke(2));
            g.drawOval((int)p.getX() - 12, (int)p.getY() - 12, 24, 24);
        }

        // Bullets
        g.setColor(Color.YELLOW);
        for (Bullet b : model.getBullets()) g.fillOval((int)b.getX() - 5, (int)b.getY() - 5, 10, 10);

        // Zombies
        for (Zombie z : model.getZombies()) {
            g.setColor(z.getColor());
            int size = (z.getType() == Zombie.Type.BRUTE) ? 50 : 30;
            g.fillOval((int)z.getX() - size/2, (int)z.getY() - size/2, size, size);
        }

        // Player
        Player p = model.getPlayer();
        g.setColor(model.isFlashing() ? Color.RED : Color.BLUE);
        g.fillRect((int)p.getX() - 15, (int)p.getY() - 15, 30, 30);
        
        // Particles
        for (Particle part : model.getParticles()) {
            g.setColor(new Color(part.getColor().getRed(), part.getColor().getGreen(), part.getColor().getBlue(), Math.min(255, part.getLife() * 10)));
            g.fillRect((int)part.getX(), (int)part.getY(), 3, 3);
        }

        if (detailed) {
            // Lighting Effect (Flashlight/Ambient)
            drawLighting(g, width, height, (int)p.getX(), (int)p.getY());

            // Rain Effect
            g.setColor(new Color(100, 100, 255, 80));
            g.setStroke(new BasicStroke(1));
            for (RainDrop r : model.getRaindrops()) {
                g.drawLine((int)r.getX(), (int)r.getY(), (int)r.getX(), (int)r.getY() + 8);
            }
        }

        // UI
        drawHUD(g, width, height);
    }

    private void drawLighting(Graphics2D g, int width, int height, int px, int py) {
        RadialGradientPaint rgp = new RadialGradientPaint(
            new Point(px, py), 
            400, // Radius of light
            new float[]{0.0f, 1.0f}, 
            new Color[]{new Color(0, 0, 0, 0), new Color(0, 0, 0, 180)}
        );
        g.setPaint(rgp);
        g.fillRect(0, 0, width, height);
    }

    private void drawHUD(Graphics2D g, int width, int height) {
        Player p = model.getPlayer();
        // Health Bar over player icon
        g.setColor(Color.BLACK);
        g.fillRect((int)p.getX() - 20, (int)p.getY() - 30, 40, 6);
        g.setColor(Color.GREEN);
        g.fillRect((int)p.getX() - 20, (int)p.getY() - 30, (int)(40 * (p.getHealth()/100.0)), 6);
        g.setColor(Color.WHITE);
        g.drawRect((int)p.getX() - 20, (int)p.getY() - 30, 40, 6);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + model.getScore(), 20, 30);
        g.drawString("High: " + model.getHighScore(), 20, 60);
        
        String ammoStr = "Ammo: " + model.getCurrentAmmo() + "/" + model.getMagSize();
        if (model.isReloading()) ammoStr = "RELOADING...";
        g.drawString(ammoStr, 20, height - 30);

        String timeStr = "Survived: " + model.getSurvivalTime() + "s";
        g.drawString(timeStr, width - g.getFontMetrics().stringWidth(timeStr) - 20, 30);

        // Notifications
        if (model.getNotificationTimer() > 0) {
            int alpha = Math.min(255, (int)(255 * (model.getNotificationTimer() / 60.0))); 
            g.setColor(new Color(255, 255, 0, alpha));
            g.setFont(new Font("Arial", Font.BOLD, 40));
            drawCenteredString(g, model.getNotificationText(), height / 2 - 50, width);
        }
    }

    private void drawPauseScreen(Graphics2D g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        drawCenteredString(g, "PAUSED", height / 2 - 100, width);
        String[] options = model.getPauseOptions();
        int selected = model.getPauseIndex();
        g.setFont(new Font("Arial", Font.BOLD, 30));
        for (int i = 0; i < options.length; i++) {
            g.setColor(i == selected ? Color.YELLOW : Color.WHITE);
            drawCenteredString(g, i == selected ? "> " + options[i] + " <" : options[i], height / 2 + i * 60, width);
        }
    }

    private void drawUpgradeScreen(Graphics2D g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 220));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        drawCenteredString(g, "UPGRADE GAINED!", 150, width);
        List<UpgradeManager.UpgradeType> choices = model.getCurrentChoices();
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        for (int i = 0; i < choices.size(); i++) {
            g.setColor(new Color(255, 255, 255, 40));
            g.fillRect(width / 2 - 200, 220 + i * 80, 400, 60);
            g.setColor(Color.WHITE);
            g.drawRect(width / 2 - 200, 220 + i * 80, 400, 60);
            drawCenteredString(g, (i + 1) + ". " + choices.get(i).description, 258 + i * 80, width);
        }
    }

    private void drawGameOverScreen(Graphics2D g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 240));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 70));
        drawCenteredString(g, "YOU DIED", height / 2 - 60, width);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 28));
        drawCenteredString(g, "Score: " + model.getScore(), height / 2 + 20, width);
        drawCenteredString(g, "Best: " + model.getHighScore(), height / 2 + 60, width);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        drawCenteredString(g, "Press 'R' to Restart", height / 2 + 130, width);
    }

    private void drawCenteredString(Graphics2D g, String text, int y, int width) {
        int x = (width - g.getFontMetrics().stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }
}
