/**
 * Zombie.java
 * 
 * Represents a zombie enemy.
 */
public class Zombie {
    private double x, y;
    private int health;

    private static final double SPEED = 2.0;

    public Zombie(double x, double y) {
        this.x = x;
        this.y = y;
        this.health = 50;
    }

    public void moveToward(double targetX, double targetY) {
        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance > 0) {
            this.x += (dx / distance) * SPEED;
            this.y += (dy / distance) * SPEED;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }

    // TODO: Add AI logic and pathfinding placeholders
}
