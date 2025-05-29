package my.games.earthguardian.services;

import java.util.List;

public class GlobalHighScoreServiceProxy implements HighScoreService {
    private HighScoreService globalService;

    public GlobalHighScoreServiceProxy() {
        this.globalService = new GlobalHighScoreServiceImpl();
    }

    @Override
    public void saveHighScore(String playerName, int score) {
        globalService.saveHighScore(playerName, score);
    }

    @Override
    public List<HighScore> loadAllScores() {
        return globalService.loadAllScores();
    }

    @Override
    public List<HighScore> loadTopTenScores() {
        return globalService.loadTopTenScores();
    }

    @Override
    public List<HighScore> loadTopTenScoresAscending() {
        return globalService.loadTopTenScoresAscending();
    }

    @Override
    public void deleteHighScore(int id) {
        globalService.deleteHighScore(id);
    }

    @Override
    public void resetHighScores() {
        globalService.resetHighScores();
    }

    @Override
    public boolean isScoreInTopTen(int score) {
        return globalService.isScoreInTopTen(score);
    }
}
