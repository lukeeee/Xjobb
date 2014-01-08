package se.xjobb.scardfeud;

/**
 * Created by Svempa on 2014-01-08.
 */
public class Game {

    // This object represents the current ongoing game

    private int userId;
    private int gameId;
    private int choice;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }
}
