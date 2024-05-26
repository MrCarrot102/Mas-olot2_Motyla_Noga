import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Pocisk {
    private int x;
    private int y;
    private int speed;
    private final int SIZE = 10;
    private BufferedImage[] images;
    private int currentFrame = 0;
    private int frameCount = 6;
    private int frameDelay = 5;
    private int frameDelayCounter = 0;

    public Pocisk(int x, int y, String wybranaPostac) {
        this.x = x;
        this.y = y;

        // Load images based on the character selected
        switch (wybranaPostac) {
            case "Monarcha":
                this.speed = 9;
                loadImages("Grafika/red");
                break;
            case "Pawica":
                this.speed = 7;
                loadImages("Grafika/red");
                break;
            case "Bielinek":
                this.speed = 5;
                loadImages("Grafika/red");
                break;
            case "Wędrowiec":
                this.speed = 3;
                loadImages("Grafika/red");
                break;
        }
    }

    private void loadImages(String folderPath) {
        images = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            try {
                images[i] = ImageIO.read(getClass().getResource(folderPath + "/" + (i + 1) + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        y -= speed;
        // Update the frame delay counter and switch to the next frame if necessary
        frameDelayCounter++;
        if (frameDelayCounter >= frameDelay) {
            frameDelayCounter = 0;
            currentFrame = (currentFrame + 1) % frameCount;
        }
    }

    public void draw(Graphics g) {
        if (images != null && images[currentFrame] != null) {
            g.drawImage(images[currentFrame], x - SIZE / 2, y - SIZE / 2, SIZE * 2, SIZE * 2, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
        }
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x - SIZE / 2, y - SIZE / 2, SIZE * 2, SIZE * 2);
    }
}
