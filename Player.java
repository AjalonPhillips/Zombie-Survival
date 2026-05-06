/**
 * Player.java
 * 
 * Represents the player character in the game.
 */
public class Player {
    private double x, y;
    private int health;
    private double speed = 5.0;

    public Player() {
        this.x = 400; // Center screen
        this.y = 300;
        this.health = 100;
    }

    public void move(double dx, double dy) {
        this.x += dx * speed;
        this.y += dy * speed;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > 100) this.health = 100;
    }

    public void increaseSpeed(double amount) {
        this.speed += amount;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) this.health = 0;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }

    // TODO: Add movement and stat methods
}
