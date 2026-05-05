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
    private static final int INITIAL_SPAWN_THRESHOLD = 120; // 2 seconds
    private int currentSpawnThreshold = INITIAL_SPAWN_THRESHOLD;
    
    private long totalFrames = 0;
    private double currentZombieSpeed = 2.0;
    
    // Facing direction for shooting
    private double lastDx = 0;
    private double lastDy = -1;
    
    private boolean gameOver = false;

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
        if (gameOver) return;

        totalFrames++;
        updateDifficulty();

        // Player movement
        double dx = 0;
        double dy = 0;
        // ... (existing logic)
        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        if (dx != 0 || dy != 0) {
            // Normalize for movement and facing
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
            
            // Update facing direction
            lastDx = dx;
            lastDy = dy;
        }

        player.move(dx, dy);

        // Update Bullets
        updateBullets();

        // Zombie logic
        updateZombies();
        handleSpawning();
        
        // Collision detection
        checkCollisions();
        
        if (player.isDead()) {
            gameOver = true;
        }
    }

    private void updateDifficulty() {
        // Every 5 seconds (300 frames), slightly increase difficulty
        if (totalFrames > 0 && totalFrames % 300 == 0) {
            if (currentSpawnThreshold > 25) {
                currentSpawnThreshold -= 3;
            }
            currentZombieSpeed += 0.1;
            System.out.println("Difficulty increased! Speed: " + currentZombieSpeed + ", Spawn Rate: " + currentSpawnThreshold);
        }
    }

    private void checkCollisions() {
        // Bullet vs Zombie
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            for (int j = zombies.size() - 1; j >= 0; j--) {
                Zombie z = zombies.get(j);
                
                double dist = Math.sqrt(Math.pow(b.getX() - z.getX(), 2) + Math.pow(b.getY() - z.getY(), 2));
                if (dist < 20) { // Collision radius
                    bullets.remove(i);
                    zombies.remove(j);
                    break; // Bullet is gone, move to next
                }
            }
        }
        
        // Zombie vs Player
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Zombie z = zombies.get(i);
            double dist = Math.sqrt(Math.pow(player.getX() - z.getX(), 2) + Math.pow(player.getY() - z.getY(), 2));
            if (dist < 25) { // Collision radius
                player.takeDamage(1); // Small continuous damage on contact
            }
        }
    }

    public void shoot() {
        // Spawn bullet at player position in the direction they are facing
        bullets.add(new Bullet(player.getX(), player.getY(), lastDx, lastDy));
    }

    private void updateBullets() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
            
            // Remove if off screen
            if (b.getX() < -50 || b.getX() > 850 || b.getY() < -50 || b.getY() > 650) {
                bullets.remove(i);
            }
        }
    }

    private void updateZombies() {
        for (Zombie zombie : zombies) {
            zombie.moveToward(player.getX(), player.getY(), currentZombieSpeed);
        }
    }

    private void handleSpawning() {
        spawnTimer++;
        if (spawnTimer >= currentSpawnThreshold) {
            spawnZombie();
            spawnTimer = 0;
        }
    }

    public int getSurvivalTime() {
        return (int) (totalFrames / 60);
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
    public boolean isGameOver() { return gameOver; }

    // TODO: Implement methods for game logic (e.g., movePlayer, updateZombies, checkCollisions)
}
