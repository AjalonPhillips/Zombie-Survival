import java.util.Random;

/**
 * UpgradeManager.java
 * 
 * Handles the logic for applying random upgrades to the player.
 */
public class UpgradeManager {
    private static final Random random = new Random();

    public static void applyRandomUpgrade(GameModel model) {
        int choice = random.nextInt(4);
        String message = "";

        switch (choice) {
            case 0 -> {
                model.getPlayer().increaseSpeed(1.0);
                message = "Speed Increased!";
            }
            case 1 -> {
                model.increaseBulletSpeed(2.0);
                message = "Bullet Speed Increased!";
            }
            case 2 -> {
                model.increaseFireRate(2); // Decrease cooldown frames
                message = "Fire Rate Increased!";
            }
            case 3 -> {
                model.getPlayer().heal(25);
                message = "Health Restored!";
            }
        }
        
        System.out.println("UPGRADE: " + message);
    }
}
