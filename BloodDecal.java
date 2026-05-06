import java.awt.Color;

/**
 * BloodDecal.java
 * 
 * Represents a temporary blood stain on the ground.
 */
public class BloodDecal {
    private double x, y;
    private Color color;
    private int size;
    private int life; // Frames until it disappears

    public BloodDecal(double x, double y, Color color, int size, int life) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.size = size;
        this.life = life;
    }

    public void update() {
        if (life > 0) life--;
    }

    public boolean isExpired() { return life <= 0; }
    public double getX() { return x; }
    public double getY() { return y; }
    public Color getColor() { return color; }
    public int getSize() { return size; }
    public int getLife() { return life; }
}
