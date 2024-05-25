import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

public class StatekGracza {
    private int x;
    private int y;
    private String wybranaPostac;
    private final int WIDTH = 40;
    private final int HEIGHT = 40;
    private boolean isPowerUpActive = false; // Zmienna przechowująca informację o aktywnym power-upie
    private BufferedImage monarchaImage; // Obrazek Monarchy

    public StatekGracza(int x, int y, String wybranaPostac) {
        this.x = x;
        this.y = y;
        this.wybranaPostac = wybranaPostac;

        // Załaduj obrazek Monarchy
        try {
            monarchaImage = ImageIO.read(getClass().getResource("Grafika/monarcha.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (wybranaPostac.equals("Monarcha") && monarchaImage != null) {
            g.drawImage(monarchaImage, x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT, null);
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
                // Monarcha wystrzeliwuje szybkie i proste strzały
                pociski.add(new Pocisk(x, y - HEIGHT / 2, 10)); // Prędkość: 10 pikseli na klatkę
                break;
            case "Pawica":
                // Pawica używa trójnóg, które strzelają w trzech kierunkach równocześnie
                pociski.add(new Pocisk(x - WIDTH / 2, y - HEIGHT / 2, 8)); // Prędkość: 8 pikseli na klatkę
                pociski.add(new Pocisk(x, y - HEIGHT / 2, 8)); // Prędkość: 8 pikseli na klatkę
                pociski.add(new Pocisk(x + WIDTH / 2, y - HEIGHT / 2, 8)); // Prędkość: 8 pikseli na klatkę
                break;
            case "Bielinek":
                // Bielinek wystrzeliwuje szybkie linie kwiatowe
                pociski.add(new Pocisk(x - WIDTH / 2, y - HEIGHT / 2, 12)); // Prędkość: 12 pikseli na klatkę
                pociski.add(new Pocisk(x + WIDTH / 2, y - HEIGHT / 2, 12)); // Prędkość: 12 pikseli na klatkę
                break;
            case "Wędrowiec":
                // Wędrowiec korzysta z wirujących motylków, które przyciągają przeciwników do siebie, a następnie eksplodują
                pociski.add(new Pocisk(x, y - HEIGHT / 2, 6)); // Prędkość: 6 pikseli na klatkę
                break;
        }
    }

    public void activatePowerUp() {
        isPowerUpActive = true; // Aktywuj power-up
        // Ustaw timer lub licznik czasu, po którym power-up zostanie dezaktywowany
        Timer powerUpTimer = new Timer();
        powerUpTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isPowerUpActive = false; // Po 5 sekundach dezaktywuj power-up
            }
        }, 5000);
    }

    public Rectangle getBounds() {
        return new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }
}
