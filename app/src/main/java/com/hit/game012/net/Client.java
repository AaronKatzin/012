package com.hit.game012.net;

import com.google.gson.Gson;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.net.server.NaturalOrderComparator;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * Client class to implement connection with server.<br/>
 * The protocol supported: command|userData|data <br/>
 *
 * The server commands and data:<br/>
 * GET_BOARD - data contains board size, returns a serialized board of the size.<br/>
 * GET_MY_HIGHSCORE - data is empty, returns a String with the highest user score.<br/>
 * SEND_GAME_RESULT - data contains the score and board size concatenated with char '-' as a delimiter.<br/>
 * GET_HIGHSCORE_LIST - data contains number of entries to be sent from the server - default 10,<br/>
 * returns a JSON object of the highest scores in the list.<br/>
 * RESET_SCORE_BOARD - data is empty, helper function to clear score board in the server.
 *
 */
public class Client {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private String userData;
    private final String SERVERIP = "172.105.68.139";
    private final int PORT = 5234;


    public Client(String userID) {
        this.userData = userID;
    }

    /**
     * Create a socket with default ip and port arguments.
     */
    public void startConnection() {
        startConnection(SERVERIP, PORT);
    }

    /**
     * Create a socket with the ip and port given parameters.
     *
     * @param ip   Server IP
     * @param port Server port
     */
    public void startConnection(String ip, int port) {
        try {
            client = new Socket(ip, port);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper function to parse a valid message to server, send it, and wait for result.
     *
     * @param command As defined in the API
     * @param data    As defined in the API
     * @return Result from server.
     */
    public String parseSendReceiveMessage(String command, String data) {
        String message = command + "|" + userData + "|" + data;
        out.println(message);
        String response = null;
        try {
            response = in.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    /**
     * Request a new board from server, and build the board instance.
     *
     * @param size Size of board
     * @return New board instance
     */
    public Board getBoard(int size) {
        String result = parseSendReceiveMessage("GET_BOARD", Integer.toString(size));
        System.out.println("Received " + result);
        Board b = Board.boardFromString(size, result);
        return b;
    }

    /**
     * Requests user's high score from the server
     *
     * @return User's high score if exists in server, null otherwise.
     */
    public Map<String, Integer> getMyHighScore() {
        String scoreString = parseSendReceiveMessage("GET_MY_HIGHSCORE", " ");
        if (!scoreString.equals("null"))
            return getScoreResult(scoreString);
        return null;
    }

    /**
     * Sends new game result to server
     *
     * @param boardSize
     * @param score
     */
    public void sendGameResult(int boardSize, int score) {
        parseSendReceiveMessage("SEND_GAME_RESULT", buildScoreString(boardSize, score));
    }

    /**
     * Requests the highest scores from the server, parse it to Map and sort it by score.
     *
     * @return sorted Map of 10 highest scores in the server
     */
    public Map<String, String> getHighScoreList() {
        // Default 10 entries
        return getHighScoreList(10);
    }

    /**
     * Requests the highest scores from the server, parse it to Map and sort it by score.
     *
     * @param numberOfEntries to be sent from the server
     * @return sorted Map of highest scores in the server
     */
    public Map<String, String> getHighScoreList(int numberOfEntries) {
        String jsonList = parseSendReceiveMessage("GET_HIGHSCORE_LIST", String.valueOf(numberOfEntries));
        if (Objects.equals(jsonList, "Empty list")) {
            return null;
        }
        Gson gson = new Gson();
        Map<String, String> deserializedHighScoreList = gson.fromJson(jsonList, Map.class);
        return sortMap(deserializedHighScoreList);
    }

    /**
     * Function to sort a Map object by the score value
     * Using stream operations and a NaturalOrderComparator Comparator to compare strings.
     *
     * @param unsortedMap Map object to be sorted
     * @return Sorted Map
     */
    private Map<String, String> sortMap(Map<String, String> unsortedMap) {
        Map<String, String> sortedMap = new LinkedHashMap<>();
        unsortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(new NaturalOrderComparator().reversed())).forEachOrdered(x -> {
            sortedMap.put(((Map.Entry<String, String>) x).getKey(), ((Map.Entry<String, String>) x).getValue());
        });
        return sortedMap;
    }

    /**
     * Parse the string of the score to be sent to server.
     *
     * @param boardSize
     * @param score
     * @return string ready to be sent to server
     */
    public static String buildScoreString(int boardSize, int score) {
        return score + "-" + boardSize;
    }

    /**
     * Parse the score string received from server into a Map object containing score and board size.
     * @param scoreString received from server
     * @return parsed score
     */
    public static Map<String, Integer> getScoreResult(String scoreString) {
        Map<String, Integer> scoreResult = new HashMap<>();
        int score = Integer.parseInt(scoreString.split("-")[0]);
        int boardSize = Integer.parseInt(scoreString.split("-")[1]);
        scoreResult.put("boardSize", boardSize);
        scoreResult.put("score", score);
        return scoreResult;
    }

    /**
     * Send a request to the server to clear the high score list
     */
    public void resetScoreBoard() {
        parseSendReceiveMessage("RESET_SCORE_BOARD", " ");
    }

    /**
     * Close the connection with the server.
     */
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

}

