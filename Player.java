/**
 * Player.java
 * 
 * Represents the player character in the game.
 */
public class Player {
    private double x, y;
    private int health;

    private static final double SPEED = 5.0;

    public Player() {
        this.x = 400; // Center screen
        this.y = 300;
        this.health = 100;
    }

    public void move(double dx, double dy) {
        this.x += dx * SPEED;
        this.y += dy * SPEED;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }

    // TODO: Add movement and stat methods
}
