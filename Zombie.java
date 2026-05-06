import java.awt.Color;

/**
 * Zombie.java
 * 
 * Represents an enemy in the game.
 */
public class Zombie {
    public enum Type {
        NORMAL(2.0, 1, Color.GREEN),
        SPRINTER(3.5, 1, new Color(173, 216, 230)), // Light Blue
        BRUTE(1.2, 3, new Color(0, 100, 0));       // Dark Green

        public final double baseSpeed;
        public final int health;
        public final Color color;

        Type(double baseSpeed, int health, Color color) {
            this.baseSpeed = baseSpeed;
            this.health = health;
            this.color = color;
        }
    }

    private double x, y;
    private int health;
    private Type type;

    public Zombie(double x, double y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.health = type.health;
    }

    public void moveToward(double targetX, double targetY, double difficultyMultiplier) {
        double dx = targetX - x;
        double dy = targetY - y;
        double length = Math.sqrt(dx * dx + dy * dy);
        
        if (length > 0) {
            // Speed scales with difficulty but type base speed is the foundation
            double speed = type.baseSpeed * (difficultyMultiplier / 2.0);
            x += (dx / length) * speed;
            y += (dy / length) * speed;
        }
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public Type getType() { return type; }
    public Color getColor() { return type.color; }
}
