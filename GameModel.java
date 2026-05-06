import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;

/**
 * GameModel.java
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
    
    private boolean isMouseShoot = false; 

    // Game Entities
    private Player player;
    private List<Zombie> zombies;
    private List<Bullet> bullets;
    private List<Particle> particles;
    private List<PowerUp> powerups;
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
    private int highScore = 0;
    
    // Reload Mechanics
    private int magSize = 12;
    private int currentAmmo = 12;
    private int reloadTimer = 0;
    private final int RELOAD_TIME = 90; // 1.5 seconds

    // Damage Flash
    private int flashFrames = 0;
    
    // Facing direction for shooting
    private double lastDx = 0;
    private double lastDy = -1;
    
    private List<UpgradeManager.UpgradeType> currentChoices;
    
    private int worldWidth;
    private int worldHeight;
    
    // Mouse Pos for Crosshair
    private int mouseX, mouseY;

    public GameModel(int width, int height) {
        this.worldWidth = width;
        this.worldHeight = height;
        loadHighScore();
        reset();
        this.state = GameState.MENU;
    }

    public void reset() {
        player = new Player(); 
        player.setPosition(worldWidth / 2.0, worldHeight / 2.0);
        
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        particles = new ArrayList<>();
        powerups = new ArrayList<>();
        random = new Random();
        spawnTimer = 0;
        currentSpawnThreshold = INITIAL_SPAWN_THRESHOLD;
        currentZombieSpeed = 2.0;
        totalFrames = 0;
        bulletSpeed = 10.0;
        fireRateThreshold = 20;
        fireCooldown = 0;
        score = 0;
        currentAmmo = magSize;
        reloadTimer = 0;
        flashFrames = 0;
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
                case 0 -> reset(); 
                case 1 -> state = GameState.OBJECTIVE; 
                case 2 -> state = GameState.OPTIONS; 
                case 3 -> System.exit(0); 
            }
        } else if (state == GameState.OBJECTIVE || state == GameState.OPTIONS) {
            state = GameState.MENU;
        }
    }

    public void toggleShootControl() {
        if (state == GameState.OPTIONS) {
            isMouseShoot = !isMouseShoot;
        }
    }

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
                case 0 -> state = GameState.PLAYING; 
                case 1 -> state = GameState.MENU; 
            }
        }
    }

    public void update(boolean up, boolean down, boolean left, boolean right) {
        if (state != GameState.PLAYING) return;

        totalFrames++;
        updateDifficulty();
        
        if (totalFrames > 0 && totalFrames % 1800 == 0) {
            triggerUpgrade();
        }

        if (fireCooldown > 0) fireCooldown--;
        if (reloadTimer > 0) {
            reloadTimer--;
            if (reloadTimer == 0) currentAmmo = magSize;
        }
        if (flashFrames > 0) flashFrames--;

        double dx = 0, dy = 0;
        if (up) dy -= 1;
        if (down) dy += 1;
        if (left) dx -= 1;
        if (right) dx += 1;

        if (dx != 0 || dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx /= length; dy /= length;
            lastDx = dx; lastDy = dy;
        }

        player.move(dx, dy);

        // Clamp player
        double px = player.getX(), py = player.getY(), hs = 15.0;
        if (px < hs) px = hs; if (px > worldWidth - hs) px = worldWidth - hs;
        if (py < hs) py = hs; if (py > worldHeight - hs) py = worldHeight - hs;
        player.setPosition(px, py);

        updateBullets();
        updateZombies();
        updateParticles();
        updatePowerUps();
        handleSpawning();
        checkCollisions();
        
        if (player.isDead()) {
            state = GameState.GAME_OVER;
            if (score > highScore) {
                highScore = score;
                saveHighScore();
            }
        }
    }

    private void updateDifficulty() {
        if (totalFrames > 0 && totalFrames % 300 == 0) {
            if (currentSpawnThreshold > 25) currentSpawnThreshold -= 3;
            currentZombieSpeed += 0.1;
        }
    }

    private void updateParticles() {
        for (int i = particles.size() - 1; i >= 0; i--) {
            particles.get(i).update();
            if (particles.get(i).isDead()) particles.remove(i);
        }
    }

    private void updatePowerUps() {
        for (int i = powerups.size() - 1; i >= 0; i--) {
            powerups.get(i).update();
            if (powerups.get(i).isExpired()) powerups.remove(i);
        }
    }

    private void checkCollisions() {
        // Bullets vs Zombies
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet b = bullets.get(i);
            for (int j = zombies.size() - 1; j >= 0; j--) {
                Zombie z = zombies.get(j);
                double dist = Math.sqrt(Math.pow(b.getX() - z.getX(), 2) + Math.pow(b.getY() - z.getY(), 2));
                if (dist < 20) {
                    bullets.remove(i);
                    z.takeDamage(1);
                    spawnBlood(z.getX(), z.getY(), z.getColor());
                    if (z.isDead()) {
                        zombies.remove(j);
                        score += (z.getType() == Zombie.Type.BRUTE) ? 500 : 100;
                        if (random.nextDouble() < 0.1) spawnPowerUp(z.getX(), z.getY());
                    }
                    break;
                }
            }
        }
        
        // Zombies vs Player
        if (flashFrames <= 0) {
            for (Zombie z : zombies) {
                double dist = Math.sqrt(Math.pow(player.getX() - z.getX(), 2) + Math.pow(player.getY() - z.getY(), 2));
                if (dist < 25) {
                    player.takeDamage(1);
                    flashFrames = 30; // Half second flash
                    break;
                }
            }
        }

        // Player vs PowerUps
        for (int i = powerups.size() - 1; i >= 0; i--) {
            PowerUp p = powerups.get(i);
            double dist = Math.sqrt(Math.pow(player.getX() - p.getX(), 2) + Math.pow(player.getY() - p.getY(), 2));
            if (dist < 30) {
                applyPowerUp(p.getType());
                powerups.remove(i);
            }
        }
    }

    private void applyPowerUp(PowerUp.Type type) {
        switch (type) {
            case HEALTH -> player.heal(20);
            case AMMO -> currentAmmo = magSize;
            case RAPID_FIRE -> fireCooldown = 0; // Not persistent, just a burst
        }
    }

    private void spawnBlood(double x, double y, java.awt.Color color) {
        for (int i = 0; i < 5; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = random.nextDouble() * 3 + 1;
            particles.add(new Particle(x, y, Math.cos(angle)*speed, Math.sin(angle)*speed, 20 + random.nextInt(20), color));
        }
    }

    private void spawnPowerUp(double x, double y) {
        PowerUp.Type type = PowerUp.Type.values()[random.nextInt(PowerUp.Type.values().length)];
        powerups.add(new PowerUp(x, y, type));
    }

    public void shoot() {
        if (state == GameState.PLAYING && fireCooldown <= 0 && currentAmmo > 0 && reloadTimer <= 0) {
            bullets.add(new Bullet(player.getX(), player.getY(), lastDx, lastDy, bulletSpeed));
            fireCooldown = fireRateThreshold;
            currentAmmo--;
            if (currentAmmo <= 0) reloadTimer = RELOAD_TIME;
        }
    }

    public void shoot(double targetX, double targetY) {
        if (state == GameState.PLAYING && fireCooldown <= 0 && currentAmmo > 0 && reloadTimer <= 0) {
            double dx = targetX - player.getX(), dy = targetY - player.getY();
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length > 0) {
                bullets.add(new Bullet(player.getX(), player.getY(), dx/length, dy/length, bulletSpeed));
                fireCooldown = fireRateThreshold;
                currentAmmo--;
                if (currentAmmo <= 0) reloadTimer = RELOAD_TIME;
            }
        }
    }

    public void reload() {
        if (currentAmmo < magSize && reloadTimer <= 0) reloadTimer = RELOAD_TIME;
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
        
        Zombie.Type type = Zombie.Type.NORMAL;
        double r = random.nextDouble();
        if (r < 0.15) type = Zombie.Type.SPRINTER;
        else if (r < 0.25) type = Zombie.Type.BRUTE;
        
        zombies.add(new Zombie(x, y, type));
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

    private void saveHighScore() {
        try (PrintWriter out = new PrintWriter(new FileWriter("highscore.txt"))) {
            out.println(highScore);
        } catch (IOException e) {}
    }

    private void loadHighScore() {
        try (BufferedReader in = new BufferedReader(new FileReader("highscore.txt"))) {
            highScore = Integer.parseInt(in.readLine());
        } catch (Exception e) { highScore = 0; }
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

    public int getSurvivalTime() {
        return (int) (totalFrames / 60);
    }

    // Getters
    public Player getPlayer() { return player; }
    public List<Zombie> getZombies() { return zombies; }
    public List<Bullet> getBullets() { return bullets; }
    public List<Particle> getParticles() { return particles; }
    public List<PowerUp> getPowerUps() { return powerups; }
    public GameState getState() { return state; }
    public List<UpgradeManager.UpgradeType> getCurrentChoices() { return currentChoices; }
    public String[] getMenuOptions() { return menuOptions; }
    public int getMenuIndex() { return menuIndex; }
    public String[] getPauseOptions() { return pauseOptions; }
    public int getPauseIndex() { return pauseIndex; }
    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
    public int getWidth() { return worldWidth; }
    public int getHeight() { return worldHeight; }
    public boolean isMouseShoot() { return isMouseShoot; }
    public int getCurrentAmmo() { return currentAmmo; }
    public int getMagSize() { return magSize; }
    public boolean isReloading() { return reloadTimer > 0; }
    public boolean isFlashing() { return flashFrames > 0; }
    public void setMousePos(int x, int y) { this.mouseX = x; this.mouseY = y; }
    public int getMouseX() { return mouseX; }
    public int getMouseY() { return mouseY; }
}
