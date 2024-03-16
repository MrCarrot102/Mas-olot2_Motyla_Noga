import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SimpleApp extends JFrame implements MouseMotionListener {
    private JPanel panel;
    private Color squareColor = Color.BLACK;
    private int squareX = 175;
    private int squareY = 500;
    private final int squareSize = 50;
    private final int moveSpeed = 5;
    private final int bulletSpeed = 10;
    private int score = 0;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Obstacle> obstacles = new ArrayList<>();
    private boolean gameOver = false;

    public SimpleApp() {
        setTitle("Prosta Aplikacja");
        setSize(400, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(squareColor);
                g.fillRect(squareX, squareY, squareSize, squareSize);
                for (Bullet bullet : bullets) {
                    bullet.draw(g);
                }
                for (Obstacle obstacle : obstacles) {
                    obstacle.draw(g);
                }
                g.setColor(Color.BLACK);
                g.drawString("Score: " + score, 10, 20);
                if (gameOver) {
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.drawString("Game Over!", 130, 300);
                    g.setFont(new Font("Arial", Font.PLAIN, 12));
                    g.drawString("Press 'R' to restart", 135, 320);
                }
            }
        };
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameOver && e.getButton() == MouseEvent.BUTTON1) {
                    shoot();
                }
            }
        });
        panel.addMouseMotionListener(this);
        add(panel);

        Timer timer = new Timer(1000 / 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    moveBullets();
                    moveObstacles();
                    checkCollisions();
                    panel.repaint();
                }
            }
        });
        timer.start();
        generateObstacles();

        // Dodanie obsługi restartu gry po naciśnięciu klawisza 'R'
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
                    restartGame();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        squareX = e.getX() - squareSize / 2;
        squareY = e.getY() - squareSize / 2;

        if (squareX < 0) {
            squareX = 0;
        } else if (squareX > panel.getWidth() - squareSize) {
            squareX = panel.getWidth() - squareSize;
        }
        if (squareY < 0) {
            squareY = 0;
        } else if (squareY > panel.getHeight() - squareSize) {
            squareY = panel.getHeight() - squareSize;
        }

        panel.repaint();
    }

    private void shoot() {
        int bulletX = squareX + squareSize / 2;
        int bulletY = squareY;
        bullets.add(new Bullet(bulletX, bulletY));
    }

    private void moveBullets() {
        for (Bullet bullet : bullets) {
            bullet.move();
        }
    }

    private void generateObstacles() {
        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random rand = new Random();
                int x = rand.nextInt(panel.getWidth() - 20);
                int y = 0;
                int width = rand.nextInt(20) + 10; // losowa szerokość z zakresu 10-29
                int height = rand.nextInt(20) + 10; // losowa wysokość z zakresu 10-29
                obstacles.add(new Obstacle(x, y, width, height));
            }
        });
        timer.start();
    }

    private void moveObstacles() {
        for (Obstacle obstacle : obstacles) {
            obstacle.move();
        }
    }

    private void checkCollisions() {
        Rectangle playerBounds = new Rectangle(squareX, squareY, squareSize, squareSize);
        for (Obstacle obstacle : obstacles) {
            Rectangle obstacleBounds = new Rectangle(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            if (playerBounds.intersects(obstacleBounds)) {
                gameOver = true;
                return;
            }
            for (Bullet bullet : bullets) {
                Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
                if (bulletBounds.intersects(obstacleBounds)) {
                    bullets.remove(bullet);
                    obstacles.remove(obstacle);
                    score += 10;
                    return;
                }
            }
        }
    }

    private class Bullet {
        private int x;
        private int y;
        private final int width = 5;
        private final int height = 10;

        public Bullet(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }

        public void move() {
            y -= bulletSpeed;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private class Obstacle {
        private int x;
        private int y;
        private int width;
        private int height;
        private final int moveSpeed = 5;

        public Obstacle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillOval(x, y, width, height);
        }

        public void move() {
            y += moveSpeed;
            if (y > panel.getHeight()) {
                obstacles.remove(this);
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    private void restartGame() {
        bullets.clear();
        obstacles.clear();
        score = 0;
        squareX = 175;
        squareY = 500;
        gameOver = false;
        generateObstacles();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartScreen(); // Uruchamiamy ekran startowy
        });
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Unused
    }
}
