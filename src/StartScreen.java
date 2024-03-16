import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartScreen extends JFrame {

    public StartScreen() {
        setTitle("Prosta Aplikacja - Ekran Startowy");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Witaj w Prostej Grze!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        panel.add(startButton, BorderLayout.CENTER);

        add(panel);
        setLocationRelativeTo(null); // Wyśrodkowanie okna
        setVisible(true);
    }

    private void startGame() {
        // Tworzymy nową instancję gry i wyłączamy ekran startowy
        SimpleApp game = new SimpleApp();
        game.setVisible(true);
        dispose(); // Wyłączamy ekran startowy
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StartScreen(); // Uruchamiamy ekran startowy
            }
        });
    }
}
