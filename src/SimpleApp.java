import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimpleApp extends JFrame {
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private JPanel mainPanel; // Panel do rysowania gry
    private ArrayList<Pocisk> pociski = new ArrayList<>();
    private ArrayList<Przeciwnik> przeciwnicy = new ArrayList<>();
    private StatekGracza statek;
    private int score = 0;
    private boolean isGameOver = false;

    public SimpleApp() {
        setSize(WIDTH, HEIGHT);
        setTitle("Gra 1942");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Tworzenie ekranu startowego
        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Masłolot 2. Motyla Noga.");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startPanel.add(titleLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(startPanel); // Usunięcie ekranu startowego
                initializeGame(); // Inicjalizacja gry
            }
        });
        startPanel.add(startButton, BorderLayout.CENTER);

        // Dodaj przycisk do wyboru postaci
        JButton characterButton = new JButton("Wybierz postać");
        characterButton.setFont(new Font("Arial", Font.PLAIN, 20));
        characterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Tutaj obsłuż wybór postaci, np. pokazując okno dialogowe z listą postaci do wyboru
                // Po wybraniu postaci wykonaj odpowiednie działania, np. przekazanie informacji o wybranej postaci do gry
            }
        });
        startPanel.add(characterButton, BorderLayout.SOUTH);

        add(startPanel);
    }

    private void restartGame() {
        isGameOver = false;
        score = 0;
        pociski.clear();
        przeciwnicy.clear();
        statek.resetPosition(WIDTH / 2, HEIGHT / 2);
        generujPrzeciwnikow();
    }

    private void initializeGame() {
        // Usunięcie ekranu startowego
        getContentPane().removeAll();
        revalidate();
        repaint();

        // Inicjalizacja gry
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Rysowanie zawartości gry
                for (Pocisk p : pociski) {
                    p.draw(g);
                }
                for (Przeciwnik przeciwnik : przeciwnicy) {
                    przeciwnik.draw(g);
                }
                statek.draw(g);
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 20, 20);

                if (isGameOver) {
                    String gameOverMsg = "Game Over. Twój wynik: " + score + ". Naciśnij R, aby zrestartować.";
                    FontMetrics fontMetrics = g.getFontMetrics();
                    int textWidth = fontMetrics.stringWidth(gameOverMsg);
                    int x = 50;
                    int y = 300;
                    g.drawString(gameOverMsg, x, y);
                }
            }
        };
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setFocusable(true);
        mainPanel.requestFocusInWindow();

        // Dodanie głównego panelu do ramki
        getContentPane().add(mainPanel);

        // Ponowne wymalowanie ramki
        revalidate();
        repaint();

        // Dodanie akcji przycisku R do resetowania gry
        Action restartAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isGameOver) {
                    restartGame();
                }
            }
        };
        mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "restart");
        mainPanel.getActionMap().put("restart", restartAction);

        // Rozpoczęcie gry
        startGame();
    }

    private void startGame() {
        // Inicjalizacja początkowych wartości gry, wątków, nasłuchiwania itp.
        statek = new StatekGracza(WIDTH / 2, HEIGHT / 2);
        Timer timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    update();
                    mainPanel.repaint();
                }
            }
        });
        timer.start();
        generujPrzeciwnikow();


        // Automatyczne strzelanie co 500 milisekund
        Timer fireTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    int x = statek.getX() + statek.getWidth() / 2;
                    int y = statek.getY();
                    pociski.add(new Pocisk(x, y));
                }
            }
        });
        fireTimer.start();

        // Dodanie nasłuchiwania ruchu myszy
        mainPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                statek.moveTo(e.getX(), e.getY());
            }
        });
    }

    private void update() {
        for (Pocisk p : pociski) {
            p.update();
        }
        for (int i=0; i<przeciwnicy.size();i++){
            Przeciwnik przeciwnik = przeciwnicy.get(i);
            przeciwnik.update();
            if(przeciwnik.getY() > HEIGHT){
                przeciwnicy.remove(i);
                score -=5;
                i--;
            }
        }

        for(int i=0;i<pociski.size();i++){
            Pocisk p= pociski.get(i);
            Rectangle pRect= p.getBounds();
            for(int j=0;j< przeciwnicy.size(); j++){
                Przeciwnik przeciwnik = przeciwnicy.get(j);
                Rectangle przeciwnikRect=przeciwnik.getBounds();
                if(pRect.intersects(przeciwnikRect)){
                    pociski.remove(i);
                    przeciwnicy.remove(j);
                    score+=10;
                    i--;
                    break;
                }
            }
        }

        // Sprawdzanie kolizji z graczem
        Rectangle statekRect = statek.getBounds();
        for (Przeciwnik przeciwnik : przeciwnicy) {
            Rectangle przeciwnikRect = przeciwnik.getBounds();
            if (statekRect.intersects(przeciwnikRect)) {
                isGameOver = true;
                break;
            }
        }
    }

    private void generujPrzeciwnikow() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    int x = (int) (Math.random() * WIDTH);
                    int y = 0;
                    przeciwnicy.add(new Przeciwnik(x, y));
                }
            }
        });
        timer.start();
    }


    public void keyTyped(KeyEvent e) {
    }


    public void keyReleased(KeyEvent e) {
    }


    public void keyPressed(KeyEvent e) {
        if (isGameOver && e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SimpleApp app = new SimpleApp();
                app.setVisible(true);
            }
        });
    }
}

class StatekGracza {
    private int x, y;
    private int width = 20;
    private int height = 30;
    private int speed = 3; // Prędkość ruchu gracza

    public StatekGracza(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public void moveTo(int mouseX, int mouseY) {
        x = mouseX - width / 2;
        y = mouseY - height / 2;
    }

    public void resetPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
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

class Pocisk {
    private int x, y;
    private int width = 3;
    private int height = 10;
    private int speed = 5;

    public Pocisk(int x, int y) {
        this.x = x;
        this.y = y - height / 2;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, width, height);
    }

    public void update() {
        y -= speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}

class Przeciwnik {
    private int x, y;
    private int width = 20;
    private int height = 20;
    private int speed = 3;

    public Przeciwnik(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public void update() {
        y += speed;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getY() {
        return y;
    }
}
