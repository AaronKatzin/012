package com.hit.game012.net.server;

import com.google.gson.Gson;
import com.hit.game012.gamelogic.game.Board;


import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * A class to receive, execute and send a result to each client.
 */
public class ServerThread extends Thread {
    protected Socket clientSocket;
    private DailyBoardGenerator generator;
    private BufferedReader in;
    private PrintWriter out;
    private Logger logger;
    Map<String, Score> highScoreList;

    public ServerThread(Socket clientSocket, DailyBoardGenerator generator, Map<String, Score> highScoreList, Logger logger) {
        this.clientSocket = clientSocket;
        this.generator = generator;
        this.logger = logger;
        this.highScoreList = highScoreList;
    }

    /**
     * Function to parse and validate string received from client
     *
     * @param msg string sent by client
     * @return A String array containing [command, userData, commandData]
     * @throws IllegalArgumentException if string is not following the server protocol.
     */
    private String[] parseAndValidateMsg(String msg) {
        String splitMsg[] = msg.split("\\|");
        if (splitMsg.length > 3) {
            throw new IllegalArgumentException("Bad command");
        }
        return splitMsg;
    }

    /**
     * Function to handle messages from client in the supported protocol
     *
     * @param msg string received from client.
     * @throws IllegalArgumentException if command is not supported in API.
     */
    private void handleMessage(String msg) {
        String[] splitMsg = parseAndValidateMsg(msg);
        String command = splitMsg[0];
        String userData = splitMsg[1];
        String data = splitMsg[2];

        switch (command) {
            case "GET_BOARD":
                String serialized = getSerializedBoard(Integer.parseInt(data));
                logger.info("[SERVER] sent: " + serialized);
                out.println(serialized);
                break;
            case "SEND_GAME_RESULT":
                addToHighScoreList(userData, data);
                out.println("Saved to list");
                break;
            case "GET_MY_HIGHSCORE":
                Score score = getHighScoreForUser(userData);
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
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                logger.info("[CLIENT][" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + "] Client disconnected ");
                return;

            default:
                throw new IllegalArgumentException("Bad command");
        }

    }

    /**
     * Retrieve and return the user's data in the high score list
     *
     * @param userData UUID of the user
     * @return User's high score
     */
    private Score getHighScoreForUser(String userData) {
        return highScoreList.get(userData);
    }

    /**
     * Parsing top high scores in the list to JSON object and sends back to client.<br />
     * If numberOfEntries > list size -> sends back all the list.
     *
     * @param numberOfEntries number of entries to be sent back to client.
     */
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

    /**
     * Function to add new score to the high score list.<br />
     * If the new score is greater than old score, updates the list.<br />
     * After the operation, sorts the list.
     *
     * @param userData    UUID of the user
     * @param scoreString as supported in the API
     */
    private void addToHighScoreList(String userData, String scoreString) {
        int newScore = Integer.parseInt(scoreString.split("-")[0]);
        int boardSize = Integer.parseInt(scoreString.split("-")[1]);

        if (highScoreList.containsKey(userData)) {
            int currentScore = highScoreList.get(userData).getScore();
            if (newScore <= currentScore) {
                return;
            }
        }
        highScoreList.put(userData, new Score(newScore, boardSize));
        highScoreList = sortMap(highScoreList);
    }

    /**
     * Function to generate a new board from BoardGenerator with size parameter.
     *
     * @param size board size
     * @return String of the serialized board.
     */
    public String getSerializedBoard(int size) {
        Board b = generator.getBoard(size);
        return b.stringFromBoard();
    }

    /**
     * Helper function to sort the high score list map by score.
     * Using stream operations and NaturalOrderComparator Comparator to compare Strings.
     *
     * @param unsortedMap unsorted high score map
     * @return sorted high score map.
     */
    private Map<String, Score> sortMap(Map<String, Score> unsortedMap) {
        System.out.println(unsortedMap);
        Map<String, Score> sortedMap = new LinkedHashMap<>();
        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(new NaturalOrderComparator().reversed())).forEachOrdered(x -> {
            sortedMap.put(((Map.Entry<String, Score>) x).getKey(), ((Map.Entry<String, Score>) x).getValue());
        });
        System.out.println(sortedMap);
        return sortedMap;
    }

    /**
     * Clear the high score list.
     */
    public void resetHighScoreList() {
        highScoreList = new LinkedHashMap<>();
    }

    /**
     * Implementation of the thread execution,
     * Every client will be handled in a different thread.
     * While client is still connected, the socket will listen to messages and handle them.
     */
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            return;
        }
        logger.info("[SERVER] new client connected: " + clientSocket.toString());
        String line;
        while (true) {
            try {
                line = in.readLine();
                logger.info("[CLIENT][" + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + "] sent: " + line);
                handleMessage(line);

            } catch (IOException e) {
                return;
            }
        }
    }
}