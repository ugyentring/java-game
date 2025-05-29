package my.games.earthguardian.services;
import com.badlogic.gdx.Gdx; // Importing Gdx class from libGDX
import com.badlogic.gdx.Preferences; // Importing Preferences class from libGDX
import java.util.ArrayList; // Importing ArrayList class from java.util package
import java.util.List; // Importing List interface from java.util package
import java.util.Map; // Importing Map interface from java.util package
/**
 * The LocalHighScoreServiceImpl class implements the HighScoreService interface
 * to manage high scores using libGDX Preferences for local storage.
 */
public class LocalHighScoreServiceImpl implements  HighScoreService{
    private Preferences prefs; // Preferences object to store high scores
    /**
     * Constructor initializes the Preferences object for high scores.
     */
    public LocalHighScoreServiceImpl() {
        prefs = Gdx.app.getPreferences("HighScores");
    }
    /**
     * Saves a high score for a player.
     * @param playerName The name of the player.
     * @param score The score to be saved.
     */
    @Override
    public void saveHighScore(String playerName, int score) {
        int currentHighScore = prefs.getInteger(playerName, 0); // Get current high score for the player
        prefs.putInteger(playerName, score); // Save the new score
        prefs.flush();
    }
    /**
     * Loads all high scores.
     * @return A list of all high scores.
     */
    @Override
    public List<HighScore> loadAllScores() {
        List<HighScore> highScores = new ArrayList<>();
        int id = 1;
        for (Map.Entry<String, ?> entry : prefs.get().entrySet()) {
            String playerName = entry.getKey();
            int score = prefs.getInteger(playerName, 0);
            highScores.add(new HighScore(id++, playerName, score));
        }
        highScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        return highScores;
    }
    /**
     * Loads the top ten high scores in descending order.
     * @return A list of the top ten high scores.
     */
    @Override
    public List<HighScore> loadTopTenScores() {
        List<HighScore> highScores = loadAllScores(); // Load all high scores
        return highScores.subList(0, Math.min(highScores.size(), 10)); // Return the top ten high scores
    }
/**
 * Loads the top ten high scores in ascending order.
 * @return A list of the top ten high scores in ascending order.
 */
@Override
public List<HighScore> loadTopTenScoresAscending() {
    List<HighScore> highScores = loadAllScores();
    highScores.sort((a, b) -> Integer.compare(a.getScore(), b.getScore())); // Sort high scores in ascending order
    return highScores.subList(0, Math.min(highScores.size(), 10)); // Return the top ten high scores
}
    /**
     * Deletes a high score by its ID.
     * Note: Preferences does not support deleting by ID, so this method is not applicable.
     * @param id The ID of the high score to be deleted.
     */
    @Override
    public void deleteHighScore(int id) {
    }
    /**
     * Resets all high scores.
     */
    @Override
    public void resetHighScores() {
        prefs.clear(); // Clear all high scores
        prefs.flush(); // Persist the changes
    }
/**
 * Checks if a given score is in the top ten high scores.
 * @param score The score to check.
 * @return True if the score is in the top ten, false otherwise.
 */
    @Override
    public boolean isScoreInTopTen(int score) {
        List<HighScore> topTenScores = loadTopTenScores(); // Load the top ten high scores
        if (topTenScores.size() < 10) { // If there are less than ten scores
            return true; // The score is in the top ten
        }
        return score > topTenScores.get(topTenScores.size() - 1).getScore(); // Check if the score is higher than the lowest top ten score
    }
}
