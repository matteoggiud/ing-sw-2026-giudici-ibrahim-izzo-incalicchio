package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.BoardDTO;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class BoardUpdatedMessage extends ServerMessage {

    int gameId;
    BoardDTO board;

    public BoardUpdatedMessage(int gameId, BoardDTO board) {
        this.gameId = gameId;
        this.board = board;
    }

    public int getGameId() {
        return gameId;
    }

    public BoardDTO getBoard() {
        return board;
    }

    /**
     * Accepts a visitor that will handle the message
     *
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
