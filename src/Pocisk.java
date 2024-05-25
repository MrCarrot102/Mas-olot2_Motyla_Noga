import java.awt.*;

public class Pocisk {
    private int x;
    private int y;
    private int speed; // Prędkość pocisku
    private final int WIDTH = 5;
    private final int HEIGHT = 10;

    public Pocisk(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void update() {
        y -= speed; // Aktualizuj pozycję pocisku na podstawie jego prędkości
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public int getY() {
        return y;
    }
}
