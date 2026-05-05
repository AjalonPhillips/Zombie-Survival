import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameModel.java
 * 
 * This class represents the Model in the MVC architecture.
 * It contains all the game logic, state, and data for the Zombie Survival game.
 * Per requirements, it does not import any Swing or AWT classes.
 */
public class GameModel {
    
    // Game Entities
    private Player player;
    private List<Zombie> zombies;
    private List<Bullet> bullets;
    
    private Random random;
    private int spawnTimer = 0;
    private static final int SPAWN_THRESHOLD = 120; // ~2 seconds at 60 FPS

    public GameModel() {
        player = new Player();
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        random = new Random();
        System.out.println("Model initialized.");
    }

    /**
     * Updates the game state. Called every frame.
     */
    public void update(boolean up, boolean down, boolean left, boolean right) {
        // Player movement
        double dx = 0;
        double dy = 0;

        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
        }

        player.move(dx, dy);

        // Zombie logic
        updateZombies();
        handleSpawning();
    }

    private void updateZombies() {
        for (Zombie zombie : zombies) {
            zombie.moveToward(player.getX(), player.getY());
        }
    }

    private void handleSpawning() {
        spawnTimer++;
        if (spawnTimer >= SPAWN_THRESHOLD) {
            spawnZombie();
            spawnTimer = 0;
        }
    }

    private void spawnZombie() {
        int edge = random.nextInt(4);
        double x = 0, y = 0;
        int width = 800;
        int height = 600;

        switch (edge) {
            case 0 -> { // Top
                x = random.nextDouble() * width;
                y = -30;
            }
            case 1 -> { // Bottom
                x = random.nextDouble() * width;
                y = height + 30;
            }
            case 2 -> { // Left
                x = -30;
                y = random.nextDouble() * height;
            }
            case 3 -> { // Right
                x = width + 30;
                y = random.nextDouble() * height;
            }
        }
        zombies.add(new Zombie(x, y));
    }

    // TODO: Implement methods for game logic (e.g., movePlayer, updateZombies, checkCollisions)

    // --- Getters ---
    public Player getPlayer() { return player; }
    public List<Zombie> getZombies() { return zombies; }
    public List<Bullet> getBullets() { return bullets; }

    // TODO: Implement methods for game logic (e.g., movePlayer, updateZombies, checkCollisions)
}
