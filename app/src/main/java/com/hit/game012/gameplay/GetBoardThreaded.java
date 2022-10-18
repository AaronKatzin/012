package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.net.Client;

import java.util.concurrent.Callable;

/**
 * Class implementing Callable to get a board asynchronously.
 * If fromServer boolean is true request a board from server.
 *
 */
public class GetBoardThreaded implements Callable<Board> {
    private int size;
    private boolean fromServer;
    private BoardGenerator boardGenerator;
    private String userData;


    public GetBoardThreaded(int size, boolean fromServer, String userID) {
        this.size = size;
        this.fromServer = fromServer;
        boardGenerator = new BoardGenerator();
        this.userData = userID;
    }

    /**
     * Request a board from server.
     * @return Board received from server.
     */
    private Board getBoardFromServer(){
        Client client = new Client(userData);
        client.startConnection();
        Board board = client.getBoard(size);
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
