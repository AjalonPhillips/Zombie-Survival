/**
 * RainDrop.java
 * 
 * Represents a falling rain particle.
 */
public class RainDrop {
    private double x, y, speed;

    public RainDrop(double x, double y, double speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update(int height) {
        y += speed;
        if (y > height) {
            y = -10;
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
