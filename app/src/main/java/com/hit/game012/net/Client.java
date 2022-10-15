package com.hit.game012.net;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.hit.game012.gamelogic.game.Board;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private String userID;
    private final String SERVERIP = "172.105.68.139";
    //    private final String SERVERIP = "192.168.1.42";
    private final int PORT = 5234;


    public Client(String userID) {
        this.userID = userID;
    }

    public void startConnection() {
        try {
            client = new Socket(SERVERIP, PORT);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startConnection(String ip, int port) {
        try {
            client = new Socket(ip, port);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseSendReceiveMessage(String command, String data) {
        String message = command + "|" + userID + "|" + data;
        out.println(message);
        String response = null;
        try {
            response = in.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public Board getBoard(int size) {
        String result = parseSendReceiveMessage("GET_BOARD", Integer.toString(size));
        System.out.println("Received " + result);
        Board b = Board.boardFromString(size, result);
        return b;
    }

    public Map<String, Integer> getMyHighScore() {
        String scoreString = parseSendReceiveMessage("GET_MY_HIGHSCORE", " ");
        return getScoreResult(scoreString);
    }

    public void sentGameResult(int boardSize, int score) {
        parseSendReceiveMessage("SEND_GAME_RESULT", buildScoreString(boardSize, score));
    }

    public TreeMap<String, String> getHighScoreList() {
        String jsonList = parseSendReceiveMessage("GET_HIGHSCORE_LIST", " ");
        Gson gson = new Gson();
        TreeMap<String, String> deserializedHighScoreList = gson.fromJson(jsonList, TreeMap.class);
        return deserializedHighScoreList;
    }

    public static String buildScoreString(int boardSize, int score) {
        return score + "-" + boardSize;
    }
    public static Map<String, Integer> getScoreResult(String scoreString){
        Map<String, Integer> scoreResult = new HashMap<>();
        int score = Integer.parseInt(scoreString.split("-")[0]);
        int boardSize = Integer.parseInt(scoreString.split("-")[1]);
        scoreResult.put("boardSize", boardSize);
        scoreResult.put("score", score);
        return scoreResult;
    }

    public void stopConnection() {
        try {
            parseSendReceiveMessage("QUIT", " ");
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    public static void main(String[] args) {
//        Client c = new Client();
//        // "172.105.68.139"
//        c.startConnection("localhost", 5234);
//        Scanner s = new Scanner(System.in);
//        System.out.println("Enter grid size");
//        int size = s.nextInt();
//        Board b = c.getBoard(size);
//        System.out.println(b);
//
//        c.sentGameResult(8, 300);
//        System.out.println(c.getMyHighScore().toString());
//        c.sentGameResult(4,200);
//        System.out.println(c.getMyHighScore().toString());
//        System.out.println(c.getHighScoreList());
//        c.stopConnection();
//
//    }
}

