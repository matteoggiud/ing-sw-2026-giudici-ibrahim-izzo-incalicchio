package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.BoardDTO;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

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
