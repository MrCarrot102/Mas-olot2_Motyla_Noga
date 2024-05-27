import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pocisk {
    private int x;
    private int y;
    private int speed;
    private final int SIZE = 20;
    private static BufferedImage image;

    static {
        try {
            // Load the common image for all bullets
            image = ImageIO.read(Pocisk.class.getResource("/Grafika/red/1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Pocisk(int x, int y, String wybranaPostac) {
        this.x = x;
        this.y = y;

        // Set speed based on the selected character
        switch (wybranaPostac) {
            case "Monarcha":
                this.speed = 9;
                break;
            case "Marek":
                this.speed = 7;
                break;
            case "Bielinek":
                this.speed = 5;
                break;
            case "Wędrowiec":
                this.speed = 3;
                break;
            default:
                this.speed = 5; // Default speed if character is not recognized
                break;
        }
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        }
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }
}
