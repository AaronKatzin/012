package com.hit.game012.gameplay;

import com.hit.game012.net.Client;

import java.util.Map;
import java.util.concurrent.Callable;

public class GetHighScoreListThreaded implements Callable<Map<String, String>> {
    private final String userID;


    public GetHighScoreListThreaded(String userID) {
        this.userID = userID;
    }

    private Map<String, String> getHighScoreListFromServer(){
        Client client = new Client(userID);
        client.startConnection();
        Map<String, String> highScoreTM = client.getHighScoreList();
        System.out.println(client.getHighScoreList());
        client.stopConnection();
        return highScoreTM;
    }

    @Override
    public Map<String, String> call() {
        return getHighScoreListFromServer();
    }

}
