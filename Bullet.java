/**
 * Bullet.java
 * 
 * Represents a projectile fired by the player.
 */
public class Bullet {
    private double x, y;
    private double angle;

    public Bullet(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getAngle() { return angle; }

    // TODO: Add collision detection and movement placeholders
}
