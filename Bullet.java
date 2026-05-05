/**
 * Bullet.java
 * 
 * Represents a projectile fired by the player.
 */
public class Bullet {
    private double x, y;
    private double dx, dy;
    private static final double SPEED = 10.0;

    public Bullet(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        
        // Normalize direction vector
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            this.dx = (dx / length) * SPEED;
            this.dy = (dy / length) * SPEED;
        } else {
            // Default to shooting upward if no direction
            this.dx = 0;
            this.dy = -SPEED;
        }
    }

    public void update() {
        this.x += dx;
        this.y += dy;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
