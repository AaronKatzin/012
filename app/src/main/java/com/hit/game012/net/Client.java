package com.hit.game012.net;

import com.hit.game012.gamelogic.game.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;

    public Client(){}

    public void startConnection(String ip, int port){
        try {
            client = new Socket(ip, port);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String sendMessage(String message){
        out.println(message);
        String response = null;
        try {
            response = in.readLine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
    public Board getBoard(int size){
        String result = sendMessage("board" + size);
        System.out.println("Received " + result);
        Board b = Board.boardFromString(size, result);
        return b;
    }
    public void stopConnection(){
        try {
            sendMessage("quit");
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
//        c.startConnection("172.105.68.139", 5234);
//        Scanner s = new Scanner(System.in);
//        System.out.println("Enter grid size");
//        int size = s.nextInt();
//        Board b = c.getBoard(size);
//        c.stopConnection();
//
//        System.out.println(b);
//    }
}
