import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * UpgradeManager.java
 * 
 * Handles the logic for generating and applying upgrades.
 */
public class UpgradeManager {
    public enum UpgradeType {
        MOVE_SPEED("Movement Speed +"),
        BULLET_SPEED("Bullet Speed +"),
        FIRE_RATE("Fire Rate +"),
        HEAL("Restore 25 Health");

        public final String description;
        UpgradeType(String desc) { this.description = desc; }
    }

    private static final Random random = new Random();

    public static List<UpgradeType> getRandomUpgrades(int count) {
        List<UpgradeType> all = new ArrayList<>(List.of(UpgradeType.values()));
        Collections.shuffle(all);
        return all.subList(0, Math.min(count, all.size()));
    }

    public static void applyUpgrade(GameModel model, UpgradeType type) {
        switch (type) {
            case MOVE_SPEED -> model.getPlayer().increaseSpeed(1.0);
            case BULLET_SPEED -> model.increaseBulletSpeed(2.0);
            case FIRE_RATE -> model.increaseFireRate(2);
            case HEAL -> model.getPlayer().heal(25);
        }
        System.out.println("Applied Upgrade: " + type.description);
    }
}
