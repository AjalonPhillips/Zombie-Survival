import java.awt.Color;

/**
 * PowerUp.java
 * 
 * Represents a temporary buff dropped by zombies.
 */
public class PowerUp {
    public enum Type {
        HEALTH(Color.RED, "Health Restored"),
        AMMO(Color.YELLOW, "Ammo Refilled"),
        RAPID_FIRE(Color.CYAN, "Rapid Fire Mode");

        public final Color color;
        public final String message;

        Type(Color color, String message) {
            this.color = color;
            this.message = message;
        }
    }

    private double x, y;
    private Type type;
    private int life = 600; // 10 seconds before despawn

    public PowerUp(double x, double y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void update() {
        life--;
    }

    public boolean isExpired() { return life <= 0; }
    public double getX() { return x; }
    public double getY() { return y; }
    public Type getType() { return type; }
}
