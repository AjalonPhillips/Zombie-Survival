/**
 * Player.java
 * 
 * Represents the player character in the game.
 */
public class Player {
    private double x, y;
    private int health;

    public Player() {
        this.x = 400; // Center screen
        this.y = 300;
        this.health = 100;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }

    // TODO: Add movement and stat methods
}
