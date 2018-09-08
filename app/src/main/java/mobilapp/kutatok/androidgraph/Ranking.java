package mobilapp.kutatok.androidgraph;

/**
 * Created by Adam on 2017.08.30..
 */

public class Ranking {
    private int Id;
    private int Score;
    private String Name;

    public int getGameType() {
        return GameType;
    }

    public void setGameType(int gameType) {
        GameType = gameType;
    }

    private int GameType;

    public Ranking(int id, int score, String name, int gameType) {
        Id = id;
        Score = score;
        Name = name;
        GameType = gameType;

    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
