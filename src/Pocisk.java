import java.awt.*;

public class Pocisk {
    private int x;
    private int y;
    private int speed;
    private final int SIZE = 5;

    public Pocisk(int x, int y, String wybranaPostac) {
        this.x = x;
        this.y = y;
        switch (wybranaPostac) {
            case "Monarcha":
                this.speed = 10;
                break;
            case "Pawica":
                this.speed = 7;
                break;
            case "Bielinek":
                this.speed = 12;
                break;
            case "Wędrowiec":
                this.speed = 9;
                break;
        }
    }

    public void update() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
    }
}
