package com.hit.game012.gameplay;

import com.hit.game012.net.Client;

import java.util.Map;
import java.util.concurrent.Callable;

public class GetPersonalHighScoreThreaded implements Callable<Map<String, Integer>> {
    private final String userID;
    private final int boardSize;
    private final int score;


    public GetPersonalHighScoreThreaded(String userID, int boardSize, int score) {
        this.userID = userID;
        this.boardSize = boardSize;
        this.score = score;
    }

    private Map<String, Integer> getPersonalHighScoreFromServer(){
        Client client = new Client(userID);
        client.startConnection();
        client.sentGameResult(boardSize, score);
        Map<String, Integer> highScoreM = client.getMyHighScore();
        client.stopConnection();
        return highScoreM;
    }

    @Override
    public Map<String, Integer> call() {
        return getPersonalHighScoreFromServer();
    }

}
