import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class StatekGracza {
    private int x;
    private int y;
    private String wybranaPostac;
    private final int WIDTH = 40;
    private final int HEIGHT = 40;
    private BufferedImage image;

    public StatekGracza(int x, int y, String wybranaPostac) {
        this.x = x;
        this.y = y;
        this.wybranaPostac = wybranaPostac;

        try {
            switch (wybranaPostac) {
                case "Monarcha":
                    image = ImageIO.read(getClass().getResource("Grafika/monarcha.png"));
                    break;
                /*case "Pawica":
                    image = ImageIO.read(getClass().getResource("pawica.png"));
                    break;
                case "Bielinek":
                    image = ImageIO.read(getClass().getResource("bielinek.png"));
                    break;
                case "Wędrowiec":
                    image = ImageIO.read(getClass().getResource("wedrowiec.png"));
                    break;*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            g.drawString(wybranaPostac, x - WIDTH / 2, y - HEIGHT / 2 - 10);
        }
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public void fire(ArrayList<Pocisk> pociski) {
        switch (wybranaPostac) {
            case "Monarcha":
                // Monarcha fires normally
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                break;
            case "Pawica":
                // Pawica fires a series of 3 projectiles
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                break;
            case "Bielinek":
                // Bielinek fires slowly
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                break;
            case "Wędrowiec":
                // Wędrowiec fires quickly
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                pociski.add(new Pocisk(x, y - HEIGHT / 2, wybranaPostac));
                break;
        }
    }



    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }
}
