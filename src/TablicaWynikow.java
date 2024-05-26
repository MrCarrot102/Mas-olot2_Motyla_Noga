import java.io.*;
import java.util.*;

public class TablicaWynikow {
    private static final String SCORE_FILE = "scores.txt";
    private List<ScoreEntry> scores;

    public TablicaWynikow() {
        scores = new ArrayList<>();
        loadScores();
    }

    private void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    scores.add(new ScoreEntry(name, score));
                }
            }
        } catch (IOException e) {
            System.out.println("Brak zapisanych wyników, utworzono nowy plik.");
        }
    }

    public void addScore(String name, int score) {
        scores.add(new ScoreEntry(name, score));
        Collections.sort(scores, Collections.reverseOrder());
        saveScores();
    }

    private void saveScores() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (ScoreEntry entry : scores) {
                writer.write(entry.getName() + ":" + entry.getScore() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getScores() {
        List<Integer> integerScores = new ArrayList<>();
        for (ScoreEntry entry : scores) {
            integerScores.add(entry.getScore());
        }
        return integerScores;
    }

    public static class ScoreEntry implements Comparable<ScoreEntry> {
        private String name;
        private int score;

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(ScoreEntry other) {
            return Integer.compare(score, other.score);
        }
    }
}
