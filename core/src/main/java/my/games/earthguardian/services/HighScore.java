package my.games.earthguardian.services;

public class HighScore {
    private int id; // Unique identifier for the high score
    private String name; // Name of the player
    private int score; // Player's score

    /**
     * Constructor to initialize the HighScore object with the given id, name, and score.
     * @param id The unique identifier for the high score.
     * @param name The name of the player.
     * @param score The player's score.
     */
    public HighScore(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    /**
     * Gets the unique identifier for the high score.
     * @return The id of the high score.
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the unique identifier for the high score.
     * @param id The id to set.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the player.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the player's score.
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }
    /**
     * Sets the player's score.
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }
    /**
     * Returns a string representation of the HighScore object.
     * @return A string containing the id, name, and score of the high score.
     */
    @Override
    public String toString() {
        return "HighScore{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", score=" + score +
            '}';
    }
}
