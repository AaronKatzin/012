package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.net.Client;

import java.util.concurrent.Callable;

public class GetBoardThreaded implements Callable<Board> {
    private int size;
    private boolean fromServer;
    private BoardGenerator boardGenerator;
    private String SERVERIP = "172.105.68.139";
    private int PORT = 5234;

    public GetBoardThreaded(int size, boolean fromServer) {
        this.size = size;
        this.fromServer = fromServer;
        boardGenerator = new BoardGenerator();
    }

    private Board getBoardFromServer(){
        Client client = new Client();
        client.startConnection(SERVERIP, PORT);
        Board board = client.getBoard(size);
        return board;
    }

    @Override
    public Board call() throws Exception {
        if (fromServer) return getBoardFromServer();
        else return boardGenerator.generate(size);
    }

}
