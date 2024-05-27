import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Przeciwnik {
    private int x;
    private int y;
    private final int WIDTH = 30;
    private final int HEIGHT = 30;
    private final int SPEED = 4;
    private BufferedImage image;

    public Przeciwnik(int x, int y) {
        this.x = x;
        this.y = y;

        try {
            image = ImageIO.read(getClass().getResource("Grafika/chrabaszcz.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        y += SPEED;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        }
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }
}
