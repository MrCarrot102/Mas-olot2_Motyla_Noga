import java.awt.*;
import java.util.ArrayList;

public class StatekGracza {
    private int x;
    private int y;
    private String wybranaPostac;
    private final int WIDTH = 40;
    private final int HEIGHT = 40;

    public StatekGracza(int x, int y, String wybranaPostac) {
        this.x = x;
        this.y = y;
        this.wybranaPostac = wybranaPostac;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawString(wybranaPostac, x - WIDTH / 2, y - HEIGHT / 2 - 10);
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void fire(ArrayList<Pocisk> pociski) {
        if (doubleShoot) {
            pociski.add(new Pocisk(x - WIDTH / 4, y - HEIGHT / 2));
            pociski.add(new Pocisk(x + WIDTH / 4, y - HEIGHT / 2));
        } else {
            pociski.add(new Pocisk(x, y - HEIGHT / 2));
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    private boolean doubleShoot = false;
    public void setDoubleShoot(boolean doubleShoot){
        this.doubleShoot=doubleShoot;
    }
}
