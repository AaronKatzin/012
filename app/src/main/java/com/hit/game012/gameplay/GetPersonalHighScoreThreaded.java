package com.hit.game012.gameplay;

import com.hit.game012.net.Client;

import java.util.Map;
import java.util.concurrent.Callable;

public class GetPersonalHighScoreThreaded implements Callable<Map<String, Integer>> {
    private String userID;
    private int boardSize, score;


    public GetPersonalHighScoreThreaded(String userID, int boardSize, int score) {
        this.userID = userID;
        this.boardSize = boardSize;
        this.score = score;
    }

    private Map<String, Integer> getPersonalHighScoreFromServer(){
        Client client = new Client(userID);
        client.startConnection();
        Map<String, Integer> highScoreM = client.getMyHighScore();
        client.sentGameResult(boardSize, score);
        System.out.println(client.getHighScoreList());
        client.stopConnection();
        return highScoreM;
    }

    @Override
    public Map<String, Integer> call() {
        return getPersonalHighScoreFromServer();
    }

}
