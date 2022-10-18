package com.hit.game012.net.server;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Logger class for Server
 */
public class Logger {
    // In actual server I used Path object, it doesn't work in android studio :/

    private final String path = "\\src\\logs";
    private File file;
    private String filename;
    private FileOutputStream fOut;
    private OutputStreamWriter myOutWriter;

    public Logger() {
        filename = path + getUniqueName("\\serverLog");
        file = new File(filename);

        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        myOutWriter = new OutputStreamWriter(fOut);
        this.info("[LOG][Init] Log created at " + filename);
    }

    /**
     * Log string as info.
     *
     * @param toLog string to log
     */
    public void info(String toLog) {
        System.out.println(toLog);
        LocalDateTime now = LocalDateTime.now();
        String message = "[" + now + "] " + toLog + "\n";
        try {
            myOutWriter.append(message);

        } catch (IOException e) {
            System.out.println("Tried to print " + message);
            throw new RuntimeException(e);
        }

    }

    /**
     * Close the logger
     */
    public void close() {
        try {
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            System.out.println("Here");
            throw new RuntimeException(e);
        }
    }

    /**
     * Function to find a unique name for log and create the file
     *
     * @param filename
     * @return Unique filename
     */
    private String getUniqueName(String filename) {
        int i = 1;
        File f = new File(path + filename + '_' + i + ".txt");
        try {
            while (!f.createNewFile())
                f = new File(path + filename + '_' + ++i + ".txt");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filename + '_' + i + ".txt";
    }
}
