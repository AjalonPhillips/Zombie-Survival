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

    // TODO: Implement methods for game logic (e.g., movePlayer, updateZombies, checkCollisions)

    // --- Getters ---
    public Player getPlayer() { return player; }
    public List<Zombie> getZombies() { return zombies; }
    public List<Bullet> getBullets() { return bullets; }

    // TODO: Implement methods for game logic (e.g., movePlayer, updateZombies, checkCollisions)
}
