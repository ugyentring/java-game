package my.games.earthguardian.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
// Implementation of the HighScoreService interface

public class GlobalHighScoreServiceImpl implements HighScoreService {
    // Base URL for the high score service
    private static final String BASE_URL = "https://highscore.ronniepeh.serv00.net/highscores/";
    // Save a high score to the server
    @Override
    public void saveHighScore(String playerName, int score) {
        try {
// Create a URL object with the base URL
            URL url = new URL(BASE_URL);
// Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
// Set the request method to POST
            conn.setRequestMethod("POST");
// Set the request headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
// Enable output for the connection
            conn.setDoOutput(true);
// Convert the high score data to JSON
            String jsonInputString = new Gson().toJson(new HighScoreRequest(playerName, score));
// Write the JSON data to the output stream
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
// Get the response code from the server
            int responseCode = conn.getResponseCode();
// Check if the response code is not 201 (Created)
            if (responseCode != 201) {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Load all high scores from the server
    @Override
    public List<HighScore> loadAllScores() {
        return fetchScores("");
    }
    // Load the top ten high scores in descending order
    @Override
    public List<HighScore> loadTopTenScores() {
        return fetchScores("descending");
    }
    // Load the top ten high scores in ascending order
    @Override
    public List<HighScore> loadTopTenScoresAscending() {
        return fetchScores("ascending");
    }
    // Delete a high score by its ID
    @Override
    public void deleteHighScore(int id) {
        try {
// Create a URL object with the base URL and the high score ID
            URL url = new URL(BASE_URL + id);
// Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
// Set the request method to DELETE
            conn.setRequestMethod("DELETE");
// Get the response code from the server
            int responseCode = conn.getResponseCode();
// Check if the response code is not 200 (OK)
            if (responseCode != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Reset all high scores on the server
    @Override
    public void resetHighScores() {
        try {
// Create a URL object with the base URL
            URL url = new URL(BASE_URL);
// Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
// Set the request method to DELETE
            conn.setRequestMethod("DELETE");
// Get the response code from the server
            int responseCode = conn.getResponseCode();
// Check if the response code is not 200 (OK)
            if (responseCode != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Check if a score is in the top ten high scores
    @Override
    public boolean isScoreInTopTen(int score) {
// Load the top ten high scores
        List<HighScore> topTenScores = loadTopTenScores();
// If there are less than ten scores, the score is in the top ten
        if (topTenScores.size() < 10) {
            return true;
        }
// Check if the score is higher than the lowest score in the top ten
        return score > topTenScores.get(topTenScores.size() - 1).getScore();
    }
    // Fetch high scores from the server with an optional endpoint
    private List<HighScore> fetchScores(String endpoint) {
        List<HighScore> highScores = new ArrayList<>();
        try {
// Create a URL object with the base URL and the endpoint
            URL url = new URL(BASE_URL + endpoint);
// Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
// Set the request method to GET
            conn.setRequestMethod("GET");
// Read the response from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            conn.disconnect();
// Convert the response JSON to a list of HighScore objects
            Gson gson = new Gson();
            highScores = gson.fromJson(content.toString(), new TypeToken<List<HighScore>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return highScores;
    }
    // Inner class to represent a high score request
    private static class HighScoreRequest {
        private String name;
        private int score;
        public HighScoreRequest(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
