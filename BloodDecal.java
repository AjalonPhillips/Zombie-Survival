import java.awt.Color;

/**
 * BloodDecal.java
 * 
 * Represents a permanent blood stain on the ground.
 */
public class BloodDecal {
    private double x, y;
    private Color color;
    private int size;

    public BloodDecal(double x, double y, Color color, int size) {
        this.x = x;
        this.y = y;
        this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100); // Faded
        this.size = size;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public Color getColor() { return color; }
    public int getSize() { return size; }
}
