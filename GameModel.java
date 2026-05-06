import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * GameModel.java
 * 
 * This class represents the Model in the MVC architecture.
 * It contains all the game logic, state, and data for the Zombie Survival game.
 */
public class GameModel {
    
    public enum GameState {
        START, PLAYING, UPGRADING, GAME_OVER
    }

    // Game Entities
    private Player player;
    private List<Zombie> zombies;
    private List<Bullet> bullets;
    private Random random;
    
    // Spawning and Difficulty
    private int spawnTimer = 0;
    private static final int INITIAL_SPAWN_THRESHOLD = 120;
    private int currentSpawnThreshold = INITIAL_SPAWN_THRESHOLD;
    private double currentZombieSpeed = 2.0;
    
    // Player Stats / Upgrades
    private long totalFrames = 0;
    private double bulletSpeed = 10.0;
    private int fireRateThreshold = 20; 
    private int fireCooldown = 0;
    private int score = 0;
    
    // Facing direction for shooting
    private double lastDx = 0;
    private double lastDy = -1;
    
    private GameState state = GameState.START;
    private List<UpgradeManager.UpgradeType> currentChoices;

    public GameModel() {
        reset();
    }

    public void reset() {
        player = new Player();
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        random = new Random();
        spawnTimer = 0;
        currentSpawnThreshold = INITIAL_SPAWN_THRESHOLD;
        currentZombieSpeed = 2.0;
        totalFrames = 0;
        bulletSpeed = 10.0;
        fireRateThreshold = 20;
        fireCooldown = 0;
        score = 0;
        lastDx = 0;
        lastDy = -1;
        state = GameState.PLAYING;
        System.out.println("Game Reset.");
    }

    public void start() {
        if (state == GameState.START) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Updates the game state. Called every frame.
     */
    public void update(boolean up, boolean down, boolean left, boolean right) {
        if (state != GameState.PLAYING) return;

        totalFrames++;
        updateDifficulty();
        
        // Trigger upgrade every 30 seconds
        if (totalFrames > 0 && totalFrames % 1800 == 0) {
            triggerUpgrade();
        }

        // Handle fire cooldown
        if (fireCooldown > 0) fireCooldown--;

        // Player movement
        double dx = 0;
        double dy = 0;
        
        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        if (dx != 0 || dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length;
            dy /= length;
            lastDx = dx;
            lastDy = dy;
        }

        player.move(dx, dy);

        // Update entities and check collisions
        updateBullets();
        updateZombies();
        handleSpawning();
        checkCollisions();
        
        if (player.isDead()) {
            state = GameState.GAME_OVER;
        }
    }

    private void updateDifficulty() {
        if (totalFrames > 0 && totalFrames % 300 == 0) {
            if (currentSpawnThreshold > 25) {
                currentSpawnThreshold -= 3;
            }
            currentZombieSpeed += 0.1;
        }
    }

    private void checkCollisions() {
        // Bullet vs Zombie
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            for (int j = zombies.size() - 1; j >= 0; j--) {
                Zombie z = zombies.get(j);
                double dist = Math.sqrt(Math.pow(b.getX() - z.getX(), 2) + Math.pow(b.getY() - z.getY(), 2));
                if (dist < 20) {
                    bullets.remove(i);
                    zombies.remove(j);
                    score += 100; // Award points for kills
                    break;
                }
            }
        }
        
        // Zombie vs Player
        for (Zombie z : zombies) {
            double dist = Math.sqrt(Math.pow(player.getX() - z.getX(), 2) + Math.pow(player.getY() - z.getY(), 2));
            if (dist < 25) {
                player.takeDamage(1);
            }
        }
    }

    public void shoot() {
        if (state == GameState.PLAYING && fireCooldown <= 0) {
            bullets.add(new Bullet(player.getX(), player.getY(), lastDx, lastDy, bulletSpeed));
            fireCooldown = fireRateThreshold;
        }
    }

    public void increaseBulletSpeed(double amount) {
        this.bulletSpeed += amount;
    }

    public void increaseFireRate(int amount) {
        this.fireRateThreshold -= amount;
        if (this.fireRateThreshold < 5) this.fireRateThreshold = 5;
    }

    private void updateBullets() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            b.update();
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

    private void spawnZombie() {
        int edge = random.nextInt(4);
        double x = 0, y = 0;
        switch (edge) {
            case 0 -> { x = random.nextDouble() * 800; y = -30; }
            case 1 -> { x = random.nextDouble() * 800; y = 630; }
            case 2 -> { x = -30; y = random.nextDouble() * 600; }
            case 3 -> { x = 830; y = random.nextDouble() * 600; }
        }
        zombies.add(new Zombie(x, y));
    }

    private void triggerUpgrade() {
        state = GameState.UPGRADING;
        currentChoices = UpgradeManager.getRandomUpgrades(3);
    }

    public void selectUpgrade(int index) {
        if (state == GameState.UPGRADING && index >= 0 && index < currentChoices.size()) {
            UpgradeManager.applyUpgrade(this, currentChoices.get(index));
            state = GameState.PLAYING;
            currentChoices = null;
        }
    }

    public int getSurvivalTime() {
        return (int) (totalFrames / 60);
    }

    // Getters
    public Player getPlayer() { return player; }
    public List<Zombie> getZombies() { return zombies; }
    public List<Bullet> getBullets() { return bullets; }
    public GameState getState() { return state; }
    public List<UpgradeManager.UpgradeType> getCurrentChoices() { return currentChoices; }
    public int getScore() { return score; }
}
