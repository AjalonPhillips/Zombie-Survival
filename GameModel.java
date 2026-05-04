import java.util.ArrayList;
import java.util.List;

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
    
    public GameModel() {
        player = new Player();
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        
        // TODO: Initialize game world state
        System.out.println("Model initialized with Player, Zombies, and Bullets.");
    }

    // --- Entity Placeholders ---

    /**
     * Placeholder class for the Player.
     */
    public class Player {
        private double x, y;
        private int health;
        
        public Player() {
            this.x = 400; // Center screen
            this.y = 300;
            this.health = 100;
        }
        // TODO: Add movement and stat methods
    }

    /**
     * Placeholder class for Zombies.
     */
    public class Zombie {
        private double x, y;
        private int health;
        
        public Zombie(double x, double y) {
            this.x = x;
            this.y = y;
            this.health = 50;
        }
        // TODO: Add AI logic and pathfinding placeholders
    }

    /**
     * Placeholder class for Bullets.
     */
    public class Bullet {
        private double x, y;
        private double angle;
        
        public Bullet(double x, double y, double angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
        // TODO: Add collision detection and movement placeholders
    }

    // --- Getters ---
    public Player getPlayer() { return player; }
    public List<Zombie> getZombies() { return zombies; }
    public List<Bullet> getBullets() { return bullets; }

    // TODO: Implement methods for game logic (e.g., movePlayer, updateZombies, checkCollisions)
}
