package com.hit.game012.gameplay;

import com.hit.game012.net.Client;

import java.util.TreeMap;
import java.util.concurrent.Callable;

public class GetHighScoreListThreaded implements Callable<TreeMap<String, String>> {
    private final String userID;


    public GetHighScoreListThreaded(String userID) {
        this.userID = userID;
    }

    private TreeMap<String, String> getHighScoreListFromServer(){
        Client client = new Client(userID);
        client.startConnection();
        TreeMap<String, String> highScoreTM = client.getHighScoreList();
        System.out.println(client.getHighScoreList());
        client.stopConnection();
        return highScoreTM;
    }

    @Override
    public TreeMap<String, String> call() {
        return getHighScoreListFromServer();
    }

}
