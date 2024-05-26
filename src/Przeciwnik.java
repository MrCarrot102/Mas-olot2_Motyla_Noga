import java.awt.*;

public class Przeciwnik {
    private int x;
    private int y;
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    private final int SPEED = 5;

    public Przeciwnik(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += SPEED;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

}