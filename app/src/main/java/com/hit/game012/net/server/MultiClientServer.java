package com.hit.game012.net.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;

/**
 * Class to run as the main server instance
 * The server supports multiple clients in parallel as every new connection starts a new thread.
 * The server maintains a high score Map with userData as the key, and score as the value.
 * The server will generate new daily boards of all sizes every 00:00 Israel Local time.
 * The server logs all requests.
 */
public class MultiClientServer {
    private static final int PORT = 5234;
    private static LinkedHashMap<String, Score> highScoreList;

    // In the actual server I used Path object, it doesn't work in android studio :/
    private static String path = "\\src\\net\\highScoreList.ser";

    public static void main(String args[]) {
        System.out.println("[SERVER][Init] Starting server");

        ServerSocket serverSocket = null;
        Socket socket = null;
        Logger logger = new Logger();
        DailyBoardGenerator generator = new DailyBoardGenerator(logger);
        GeneratorScheduler executor = new GeneratorScheduler(generator, 0, 0, 0);

        // Loads high score list from file, if not succeeded creates an empty Map.
        loadHighScoreList();
        if (highScoreList == null) {
            highScoreList = new LinkedHashMap<>();
        }

        // Adding shutdown hook to save the high score list and close the logger when program exists.
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                logger.info("[SERVER][SHUTDOWN] Saving high score list ");
                saveHighScoreList();
                logger.info("[SERVER][SHUTDOWN] Shutting down ");
                logger.close();
            }
        }));

        try {
            serverSocket = new ServerSocket(PORT);
            logger.info("[SERVER][Init] Running on " + serverSocket + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            // Running in loop and receive connection requests
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);

                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    executor.stop();
                }
            }

            // When connection established, creates a new thread.
            new ServerThread(socket, generator, highScoreList, logger).start();
        }
    }

    /**
     * Function to save current high score list as a file.
     */
    public static void saveHighScoreList() {
        try {
            // Saving of object in a file
            FileOutputStream file = new FileOutputStream(path.toString());
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(highScoreList);
            out.close();
            file.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Function to load high score list from file
     */
    public static void loadHighScoreList() {
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(path.toString());
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            highScoreList = (LinkedHashMap<String, Score>) in.readObject();

            in.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

    }

}
