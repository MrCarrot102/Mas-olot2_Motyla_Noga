import java.awt.*;

public class PowerUp {
    private int x;
    private int y;
    private int points; // Ilość punktów, jakie dodaje power-up
    private final int SIZE = 20;

    public PowerUp(int x, int y, int points) {
        this.x = x;
        this.y = y;
        this.points = points;
    }

    public void update() {
        y += 2; // Power-upy opadają w dół ekranu
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        g.setColor(Color.BLACK);
        g.drawString("+" + points, x - SIZE / 2, y);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    public int getPoints() {
        return points;
    }

    public int getY() {
        return y;
    }
}