import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Maslolot extends JFrame {
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private JPanel mainPanel; // Panel do rysowania gry
    private ArrayList<Pocisk> pociski = new ArrayList<>();
    private ArrayList<Przeciwnik> przeciwnicy = new ArrayList<>();

    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private StatekGracza statek;
    private int score = 0;
    private boolean isGameOver = false;
    private String wybranaPostac = "Monarcha"; // Domyślnie wybrana postać

    private Timer gameTimer;
    private Timer fireTimer;
    private Timer enemyTimer;

    private BufferedImage backgroundImage;
    private BufferedImage menuBackgroundImage;

    public Maslolot() {
        setSize(WIDTH, HEIGHT);
        setTitle("Masłolot 2. Motyla Noga.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Załaduj obraz tła
        try {
            menuBackgroundImage = ImageIO.read(getClass().getResource("Grafika/tło_menu.jpg"));
            backgroundImage = ImageIO.read(getClass().getResource("Grafika/niebo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Pokaż ekran startowy
        showStartScreen();
    }

    private void showStartScreen() {
        // Tworzenie ekranu startowego z niestandardowym tłem
        JPanel startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Rysowanie tła menu
                if (menuBackgroundImage != null) {
                    g.drawImage(menuBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        startPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Dodanie tytułu
        JLabel titleLabel = new JLabel("Masłolot 2. Motyla Noga.");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        startPanel.add(titleLabel, gbc);

        // Dodanie przycisku wyboru postaci
        JButton characterButton = new JButton("Wybierz postać");
        characterButton.setFont(new Font("Arial", Font.PLAIN, 20));
        characterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wybierzPostac(); // Obsługa wyboru postaci
            }
        });
        gbc.gridy = 1;
        startPanel.add(characterButton, gbc);

        // Dodanie przycisku startowego
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.setPreferredSize(new Dimension(100, 40)); // Ustawienie preferowanego rozmiaru
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(startPanel); // Usunięcie ekranu startowego
                initializeGame(); // Inicjalizacja gry
            }
        });
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.SOUTH;
        startPanel.add(startButton, gbc);

        // Wyświetlenie ekranu startowego
        setContentPane(startPanel);
        revalidate();
        repaint();
    }


    private void wybierzPostac() {
        String[] postacie = {"Monarcha", "Pawica", "Bielinek", "Wędrowiec"};
        String nowaPostac = (String) JOptionPane.showInputDialog(
                this,
                "Wybierz swoją postać",
                "Wybór postaci",
                JOptionPane.PLAIN_MESSAGE,
                null,
                postacie,
                wybranaPostac);

        if (nowaPostac != null) {
            wybranaPostac = nowaPostac;
        }
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (fireTimer != null) {
            fireTimer.stop();
        }
        if (enemyTimer != null) {
            enemyTimer.stop();
        }

        isGameOver = false;
        score = 0;
        pociski.clear();
        przeciwnicy.clear();
        showStartScreen();
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
                // Rysowanie tła
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

                // Rysowanie zawartości gry
                for (Pocisk p : pociski) {
                    p.draw(g);
                }
                for (Przeciwnik przeciwnik : przeciwnicy) {
                    przeciwnik.draw(g);
                }
                for (PowerUp powerUp : powerUps) {
                    powerUp.draw(g);
                }
                statek.draw(g);
                g.setColor(Color.WHITE);
                g.drawString("Score: " + score, 20, 20);

                if (isGameOver) {
                    String gameOverMsg = "Game Over. Twój wynik: " + score + ". Naciśnij R, aby zrestartować.";
                    FontMetrics fontMetrics = g.getFontMetrics();
                    int textWidth = fontMetrics.stringWidth(gameOverMsg);
                    int x = (getWidth() - textWidth) / 2;
                    int y = getHeight() / 2;
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
        statek = new StatekGracza(WIDTH / 2, HEIGHT / 2, wybranaPostac);

        gameTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    update();
                    mainPanel.repaint();
                }
            }
        });
        gameTimer.start();
        generujPrzeciwnikow();

        // Automatyczne strzelanie co 200 milisekund
        fireTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    statek.fire(pociski);
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
        // Aktualizacja przeciwników
        for (int i = 0; i < przeciwnicy.size(); i++) {
            Przeciwnik przeciwnik = przeciwnicy.get(i);
            przeciwnik.update();
            if (przeciwnik.getY() > HEIGHT) {
                przeciwnicy.remove(i);
                score -= 5;
                i--;
            }
        }
        // Sprawdzanie kolizji pocisków z przeciwnikami
        for (int i = 0; i < pociski.size(); i++) {
            Pocisk p = pociski.get(i);
            Rectangle pRect = p.getBounds();
            for (int j = 0; j < przeciwnicy.size(); j++) {
                Przeciwnik przeciwnik = przeciwnicy.get(j);
                Rectangle przeciwnikRect = przeciwnik.getBounds();
                if (pRect.intersects(przeciwnikRect)) {
                    pociski.remove(i);
                    przeciwnicy.remove(j);
                    score += 10;
                    i--;
                    // Losowe generowanie power-upa
                    if (Math.random() < 0.2) { // 20% szans na power-up
                        String[] types = {"extra_points", "double_shoot"};
                        String type = types[(int) (Math.random() * types.length)];
                        powerUps.add(new PowerUp(przeciwnik.getBounds().x, przeciwnik.getY(), type));
                    }
                    break;
                }
            }
        }
        // Aktualizacja power-upów
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update();
            if (powerUp.getBounds().intersects(statek.getBounds())) {
                powerUps.remove(i);
                applyPowerUp(powerUp);
                i--;
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
    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case "extra_points":
                score += 100;
                break;
            case "double_shoot":
                statek.setDoubleShoot(true);
                Timer timer = new Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statek.setDoubleShoot(false);
                    }
                });
                timer.setRepeats(false);
                timer.start();
                break;
        }
    }

    private void generujPrzeciwnikow() {
        enemyTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    int x = (int) (Math.random() * WIDTH);
                    int y = 0;
                    przeciwnicy.add(new Przeciwnik(x, y));
                }
            }
        });
        enemyTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Maslolot().setVisible(true);
            }
        });
    }
}
