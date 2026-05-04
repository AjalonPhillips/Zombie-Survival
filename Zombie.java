/**
 * Zombie.java
 * 
 * Represents a zombie enemy.
 */
public class Zombie {
    private double x, y;
    private int health;

    public Zombie(double x, double y) {
        this.x = x;
        this.y = y;
        this.health = 50;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }

    // TODO: Add AI logic and pathfinding placeholders
}
