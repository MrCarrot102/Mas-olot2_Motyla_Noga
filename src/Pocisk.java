import java.awt.*;

public class Pocisk {
    private int x;
    private int y;
    private final int WIDTH = 5;
    private final int HEIGHT = 10;
    private final int SPEED = 10;

    public Pocisk(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y -= SPEED;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }
}
