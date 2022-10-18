package com.hit.game012.net.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class MulticlientServer {
    private static final int PORT = 5234;
    private static LinkedHashMap<String, Score> highScoreList;

    // In actual server I used Path object, it doesn't work in android studio
    private static String path = "\\src\\net\\highScoreList.ser";

    public static void main(String args[]) {
        System.out.println("[SERVER][Init] Starting server");

        ServerSocket serverSocket = null;
        Socket socket = null;
        Logger logger = new Logger();
        DailyBoardGenerator generator = new DailyBoardGenerator(logger);
        GeneratorScheduler executor = new GeneratorScheduler(generator, 21, 04, 0);

        loadHighScoreList();
        if (highScoreList == null) {
            highScoreList = new LinkedHashMap<>();
        }

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

            // new thread for a client
            new ServerThread(socket, generator, highScoreList, logger).start();
        }


    }

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
