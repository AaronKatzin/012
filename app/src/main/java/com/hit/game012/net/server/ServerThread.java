package com.hit.game012.net.server;

import com.google.gson.Gson;
import com.hit.game012.gamelogic.game.Board;


import java.io.*;
import java.net.Socket;
import java.util.*;

public class ServerThread extends Thread {
    protected Socket socket;
    private DailyBoardGenerator generator;
    private BufferedReader in;
    private PrintWriter out;
    private Logger logger;
    Map<String, Score> highScoreList;

    public ServerThread(Socket clientSocket, DailyBoardGenerator generator, Map<String, Score> highScoreList, Logger logger) {
        this.socket = clientSocket;
        this.generator = generator;
        this.logger = logger;
        this.highScoreList = highScoreList;
    }

    private String[] parseMsg(String msg) {
        String splittedMsg[] = msg.split("\\|");
        if (splittedMsg.length > 3) {
            throw new IllegalArgumentException("Bad command");
        }
        return splittedMsg;

    }

    private void handleMessage(String msg) {
        String[] splittedMsg = parseMsg(msg);
        String command = splittedMsg[0];
        String userID = splittedMsg[1];
        String data = splittedMsg[2];
        switch (command) {
            case "GET_BOARD":
                String serialized = getSerializedBoard(Integer.parseInt(data));
                logger.info("[SERVER] sent: " + serialized);
                out.println(serialized);
                break;
            case "SEND_GAME_RESULT":
                addToHighScoreList(userID, data);
                out.println("Saved to list");
                break;
            case "GET_MY_HIGHSCORE":
                Score score = getHighScoreForUser(userID);
                out.println(score);
                break;
            case "GET_HIGHSCORE_LIST":
                serializeAndSendHighScoreList(5);

                break;
            case "RESET_SCORE_BOARD":
                resetHighScoreList();
                out.println("reset completed");
                break;
            case "QUIT":
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                logger.info("[CLIENT][" + socket.getInetAddress() + ":" + socket.getPort() + "] Client disconnected ");
                return;

            default:
                throw new IllegalArgumentException("Bad command");
        }

    }

    private Score getHighScoreForUser(String userID) {
        return highScoreList.get(userID);
    }

    public void serializeAndSendHighScoreList(int numberOfEntries) {
        if (highScoreList.isEmpty()) {
            return;
        }
        int limit = Math.min(numberOfEntries, highScoreList.size());
        Map<String, String> list = highScoreList.entrySet().stream().limit(limit).collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue().toString()), Map::putAll);
        Gson gson = new Gson();
        String highScoreJson = gson.toJson(list);
        out.println(highScoreJson);
    }

    private void addToHighScoreList(String userID, String scoreString) {
        int newScore = Integer.parseInt(scoreString.split("-")[0]);
        int boardSize = Integer.parseInt(scoreString.split("-")[1]);

        if (highScoreList.containsKey(userID)) {
            int currentScore = highScoreList.get(userID).getScore();
            if (newScore <= currentScore) {
                return;
            }
        }
        highScoreList.put(userID, new Score(newScore, boardSize));
        highScoreList = sortMap(highScoreList);
    }

    public String getSerializedBoard(int size) {
        Board b = generator.getBoard(size);
        return b.stringFromBoard();
    }

    private Map<String, Score> sortMap(Map<String, Score> unsortedMap) {
        System.out.println(unsortedMap);
        Map<String, Score> sortedMap = new LinkedHashMap<>();
        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(new NaturalOrderComparator().reversed())).forEachOrdered(x -> {
            sortedMap.put(((Map.Entry<String, Score>) x).getKey(), ((Map.Entry<String, Score>) x).getValue());
        });
        System.out.println(sortedMap);
        return sortedMap;
    }

    public void resetHighScoreList() {
        highScoreList = new LinkedHashMap<>();
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            return;
        }
        logger.info("[SERVER] new client connected: " + socket.toString());
        String line;
        while (true) {
            try {
                line = in.readLine();
                logger.info("[CLIENT][" + socket.getInetAddress() + ":" + socket.getPort() + "] sent: " + line);
                handleMessage(line);

            } catch (IOException e) {
                return;
            }
        }
    }
}