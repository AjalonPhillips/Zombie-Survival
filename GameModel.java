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
        MENU, PLAYING, UPGRADING, GAME_OVER, OBJECTIVE, OPTIONS, PAUSED
    }

    private GameState state = GameState.MENU;
    private int menuIndex = 0;
    private final String[] menuOptions = {"Start Game", "Objective", "Options", "Quit"};
    
    private int pauseIndex = 0;
    private final String[] pauseOptions = {"Resume", "Quit Attempt"};
    
    private boolean isMouseShoot = false; // Default to Spacebar

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
    
    private List<UpgradeManager.UpgradeType> currentChoices;
    
    private int worldWidth;
    private int worldHeight;

    public GameModel(int width, int height) {
        this.worldWidth = width;
        this.worldHeight = height;
        reset();
        this.state = GameState.MENU;
    }

    public void reset() {
        player = new Player(); 
        player.setPosition(worldWidth / 2.0, worldHeight / 2.0);
        
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
    }

    public void navigateMenu(int dir) {
        if (state == GameState.MENU) {
            menuIndex += dir;
            if (menuIndex < 0) menuIndex = menuOptions.length - 1;
            if (menuIndex >= menuOptions.length) menuIndex = 0;
        }
    }

    public void selectMenuOption() {
        if (state == GameState.MENU) {
            switch (menuIndex) {
                case 0 -> reset(); // Start Game
                case 1 -> state = GameState.OBJECTIVE; // Objective
                case 2 -> state = GameState.OPTIONS; // Options
                case 3 -> System.exit(0); // Quit
            }
        } else if (state == GameState.OBJECTIVE || state == GameState.OPTIONS) {
            state = GameState.MENU;
        }
    }

    public void toggleShootControl() {
        if (state == GameState.OPTIONS) {
            isMouseShoot = !isMouseShoot;
            System.out.println("Shoot control set to: " + (isMouseShoot ? "Mouse Click" : "Spacebar"));
        }
    }

    public boolean isMouseShoot() { return isMouseShoot; }

    public void togglePause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
            pauseIndex = 0;
        } else if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
        }
    }

    public void navigatePauseMenu(int dir) {
        if (state == GameState.PAUSED) {
            pauseIndex += dir;
            if (pauseIndex < 0) pauseIndex = pauseOptions.length - 1;
            if (pauseIndex >= pauseOptions.length) pauseIndex = 0;
        }
    }

    public void selectPauseOption() {
        if (state == GameState.PAUSED) {
            switch (pauseIndex) {
                case 0 -> state = GameState.PLAYING; // Resume
                case 1 -> state = GameState.MENU; // Quit Attempt
            }
        }
    }

    /**
     * Updates the game state. Called every frame.
     */
    public void update(boolean up, boolean down, boolean left, boolean right) {
        if (state != GameState.PLAYING) return;

        totalFrames++;
        updateDifficulty();
        
        if (totalFrames > 0 && totalFrames % 1800 == 0) {
            triggerUpgrade();
        }

        if (fireCooldown > 0) fireCooldown--;

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

        // Clamp player position to world boundaries
        double px = player.getX();
        double py = player.getY();
        double halfSize = 15.0; // Player is 30x30
        
        if (px < halfSize) px = halfSize;
        if (px > worldWidth - halfSize) px = worldWidth - halfSize;
        if (py < halfSize) py = halfSize;
        if (py > worldHeight - halfSize) py = worldHeight - halfSize;
        
        player.setPosition(px, py);

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
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            for (int j = zombies.size() - 1; j >= 0; j--) {
                Zombie z = zombies.get(j);
                double dist = Math.sqrt(Math.pow(b.getX() - z.getX(), 2) + Math.pow(b.getY() - z.getY(), 2));
                if (dist < 20) {
                    bullets.remove(i);
                    zombies.remove(j);
                    score += 100;
                    break;
                }
            }
        }
        
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

    public void shoot(double targetX, double targetY) {
        if (state == GameState.PLAYING && fireCooldown <= 0) {
            double dx = targetX - player.getX();
            double dy = targetY - player.getY();
            double length = Math.sqrt(dx * dx + dy * dy);
            
            if (length > 0) {
                dx /= length;
                dy /= length;
                bullets.add(new Bullet(player.getX(), player.getY(), dx, dy, bulletSpeed));
                fireCooldown = fireRateThreshold;
            }
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
            if (b.getX() < -50 || b.getX() > worldWidth + 50 || b.getY() < -50 || b.getY() > worldHeight + 50) {
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
            case 0 -> { x = random.nextDouble() * worldWidth; y = -30; }
            case 1 -> { x = random.nextDouble() * worldWidth; y = worldHeight + 30; }
            case 2 -> { x = -30; y = random.nextDouble() * worldHeight; }
            case 3 -> { x = worldWidth + 30; y = random.nextDouble() * worldHeight; }
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
    public String[] getMenuOptions() { return menuOptions; }
    public int getMenuIndex() { return menuIndex; }
    public String[] getPauseOptions() { return pauseOptions; }
    public int getPauseIndex() { return pauseIndex; }
    public int getScore() { return score; }
    public int getWidth() { return worldWidth; }
    public int getHeight() { return worldHeight; }
}
