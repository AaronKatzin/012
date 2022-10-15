package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.net.Client;

import java.util.concurrent.Callable;

public class GetBoardThreaded implements Callable<Board> {
    private int size;
    private boolean fromServer;
    private BoardGenerator boardGenerator;
    private String userID;


    public GetBoardThreaded(int size, boolean fromServer, String userID) {
        this.size = size;
        this.fromServer = fromServer;
        boardGenerator = new BoardGenerator();
        this.userID = userID;
    }

    private Board getBoardFromServer(){
        Client client = new Client(userID);
        client.startConnection();
        Board board = client.getBoard(size);
        System.out.println(client.getHighScoreList());
        client.stopConnection();
        return board;
    }
    private Board getBoardLocally(){
        return boardGenerator.generate(size);
    }

    @Override
    public Board call() {
        if (fromServer) return getBoardFromServer();
        else return getBoardLocally();
    }

}
