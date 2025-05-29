package my.games.earthguardian.services;

import java.util.List;

public interface HighScoreService {
/**
 * Saves a high score for a player.
 * * @param playerName The name of the player.
 * * @param score The score to be saved.
 * */

    void saveHighScore(String playerName, int score);
  /**
  * * Loads all high scores.
  * * @return A list of all high scores.
  * */


    List<HighScore> loadAllScores();
  /**
  * * Loads the top ten high scores in descending order.
  * * @return A list of the top ten high scores.
  * */


    List<HighScore> loadTopTenScores();
 /**
  * * Loads the top ten high scores in ascending order.
  * * @return A list of the top ten high scores in ascending order.
  * */


    List<HighScore> loadTopTenScoresAscending(); // New method
 /**
  * * Deletes a high score by its ID.
  * * @param id The ID of the high score to be deleted.
  * */


    void deleteHighScore(int id);
    /**
     * Resets all high scores.
     */
    void resetHighScores();
    /**
     * Checks if a given score is in the top ten high scores.
     * @param score The score to check.
     * @return True if the score is in the top ten, false otherwise.
     */
    boolean isScoreInTopTen(int score);
}
