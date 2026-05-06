/**
 * Particle.java
 * 
 * Simple visual effect particle.
 */
public class Particle {
    private double x, y, dx, dy;
    private int life; // Frames until it disappears
    private java.awt.Color color;

    public Particle(double x, double y, double dx, double dy, int life, java.awt.Color color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.life = life;
        this.color = color;
    }

    public void update() {
        x += dx;
        y += dy;
        life--;
    }

    public boolean isDead() { return life <= 0; }
    public double getX() { return x; }
    public double getY() { return y; }
    public java.awt.Color getColor() { return color; }
    public int getLife() { return life; }
}
