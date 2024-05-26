import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class Maslolot extends JFrame {
    private final int WIDTH = 400;
    private final int HEIGHT = 600;
    private JPanel mainPanel;
    private ArrayList<Pocisk> pociski = new ArrayList<>();
    private ArrayList<Przeciwnik> przeciwnicy = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private StatekGracza statek;
    private int score = 0;
    private boolean isGameOver = false;
    private String wybranaPostac = "Monarcha";
    private Timer gameTimer;
    private Timer fireTimer;
    private Timer enemyTimer;
    private BufferedImage backgroundImage;
    private BufferedImage menuBackgroundImage;
    private TablicaWynikow TablicaWynikow;

    public Maslolot() {
        TablicaWynikow = new TablicaWynikow();
        setSize(WIDTH, HEIGHT);
        setTitle("Masłolot 2. Motyla Noga.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            menuBackgroundImage = ImageIO.read(getClass().getResource("Grafika/tło_menu.jpg"));
            backgroundImage = ImageIO.read(getClass().getResource("Grafika/niebo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        showStartScreen();
    }

    private void showStartScreen() {
        JPanel startPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (menuBackgroundImage != null) {
                    g.drawImage(menuBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Masłolot 2. Motyla Noga.");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startPanel.add(titleLabel);
        startPanel.add(Box.createRigidArea(new Dimension(20, 100)));

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(150, 40));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                initializeGame();
            }
        });
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(startButton);

        JButton characterButton = new JButton("Wybierz postać");
        characterButton.setFont(new Font("Arial", Font.PLAIN, 20));
        characterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        characterButton.setMaximumSize(new Dimension(200, 40));
        characterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wybierzPostac();
            }
        });
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(characterButton);

        JButton highScoresButton = new JButton("Tablica wyników");
        highScoresButton.setFont(new Font("Arial", Font.PLAIN, 20));
        highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoresButton.setMaximumSize(new Dimension(200, 40));
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHighScores();
            }
        });
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(highScoresButton);

        JButton exitButton = new JButton("Wyjście");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(150, 40));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        startPanel.add(Box.createVerticalGlue());
        startPanel.add(exitButton);

        startPanel.setPreferredSize(new Dimension(400, 600));
        setContentPane(startPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
        powerUps.clear();
        showStartScreen();
    }

    private void initializeGame() {
        getContentPane().removeAll();
        revalidate();
        repaint();

        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

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
        getContentPane().add(mainPanel);
        revalidate();
        repaint();

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

        startGame();
    }

    private void startGame() {
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

        fireTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    statek.fire(pociski);
                }
            }
        });
        fireTimer.start();

        generujPrzeciwnikow();

        mainPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                statek.moveTo(e.getX(), e.getY());
            }
        });
    }

    private void generujPrzeciwnikow() {
        enemyTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isGameOver) {
                    int x = (int) (Math.random() * WIDTH);
                    int y = -50;
                    przeciwnicy.add(new Przeciwnik(x, y));
                    if (Math.random() < 0.3) {
                        powerUps.add(new PowerUp((int) (Math.random() * WIDTH), -50, (int) (Math.random() * 100) + 50));
                    }
                }
            }
        });
        enemyTimer.start();
    }

    private void update() {
        for (Pocisk p : new ArrayList<>(pociski)) {
            p.update();
            if (p.getY() < 0) {
                pociski.remove(p);
            }
        }

        for (Przeciwnik przeciwnik : new ArrayList<>(przeciwnicy)) {
            przeciwnik.update();
            if (przeciwnik.getY() > HEIGHT) {
                przeciwnicy.remove(przeciwnik);
            }
        }

        for (PowerUp powerUp : new ArrayList<>(powerUps)) {
            powerUp.update();
            if (powerUp.getY() > HEIGHT) {
                powerUps.remove(powerUp);
            }
        }

        checkCollisions();
    }

    private void checkCollisions() {
        for (Pocisk p : new ArrayList<>(pociski)) {
            for (Przeciwnik przeciwnik : new ArrayList<>(przeciwnicy)) {
                if (p.getBounds().intersects(przeciwnik.getBounds())) {
                    pociski.remove(p);
                    przeciwnicy.remove(przeciwnik);
                    score += 10;
                    break;
                }
            }
        }

        for (PowerUp powerUp : new ArrayList<>(powerUps)) {
            if (statek.getBounds().intersects(powerUp.getBounds())) {
                powerUps.remove(powerUp);
                score += powerUp.getPoints();
            }
        }

        for (Przeciwnik przeciwnik : new ArrayList<>(przeciwnicy)) {
            if (statek.getBounds().intersects(przeciwnik.getBounds())) {
                isGameOver = true;
                TablicaWynikow.addScore("nick", score);
            }
        }
    }

    private void showHighScores() {
        JPanel highScoresPanel = new JPanel();
        highScoresPanel.setLayout(new BoxLayout(highScoresPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Tablica wyników");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        highScoresPanel.add(titleLabel);
        highScoresPanel.add(Box.createRigidArea(new Dimension(20, 50)));

        List<Integer> highScores = TablicaWynikow.getScores();
        for (int i = 0; i < highScores.size() && i < 10; i++) {
            JLabel scoreLabel = new JLabel((i + 1) + ". " + highScores.get(i));
            scoreLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            highScoresPanel.add(scoreLabel);
        }

        JButton backButton = new JButton("Powrót");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(150, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStartScreen();
            }
        });
        highScoresPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        highScoresPanel.add(backButton);

        highScoresPanel.setPreferredSize(new Dimension(400, 600));
        setContentPane(highScoresPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Maslolot();
    }
}
