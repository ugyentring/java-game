package my.games.earthguardian.services;
import java.util.List; // Importing the List interface from the java.util package
/**
 * The LocalHighScoreServiceProxy class implements the HighScoreService interface
 * and acts as a proxy to the LocalHighScoreServiceImpl class.
 */
public class LocalHighScoreServiceProxy implements  HighScoreService{
    private HighScoreService localService;
    /**
     * Constructor initializes the proxy with an instance of LocalHighScoreServiceImpl.
     */
    public LocalHighScoreServiceProxy() {
        this.localService = new LocalHighScoreServiceImpl();
    }
    /**
     * Saves a high score for a player by delegating to the local service.
     * @param playerName The name of the player.
     * @param score The score to be saved.
     */
    @Override
    public void saveHighScore(String playerName, int score) {
        localService.saveHighScore(playerName, score);
    }
/**
 * Loads all high scores by delegating to the local service.
 * @return A list of all high scores.
 */
@Override
public List<HighScore> loadAllScores() {
    return localService.loadAllScores();
}
    /**
     * Loads the top ten high scores in descending order by delegating to the local service.
     * @return A list of the top ten high scores.
     */
    @Override
    public List<HighScore> loadTopTenScores() {
        return localService.loadTopTenScores();
    }
    /**
     * Loads the top ten high scores in ascending order by delegating to the local service.
     * @return A list of the top ten high scores in ascending order.
     */
    @Override
    public List<HighScore> loadTopTenScoresAscending() {
        return localService.loadTopTenScoresAscending();
    }
    /**
     * Deletes a high score by its ID by delegating to the local service.
     * @param id The ID of the high score to be deleted.
     */
    @Override
    public void deleteHighScore(int id) {
        localService.deleteHighScore(id);
    }
    /**
     * Resets all high scores by delegating to the local service.
     */
    @Override
    public void resetHighScores() {
        localService.resetHighScores();
    }
    /**
     * Checks if a given score is in the top ten high scores by delegating to the local service.
     * @param score The score to check.
     * @return True if the score is in the top ten, false otherwise.
     */
    @Override
    public boolean isScoreInTopTen(int score) {
        return localService.isScoreInTopTen(score);
    }
}
