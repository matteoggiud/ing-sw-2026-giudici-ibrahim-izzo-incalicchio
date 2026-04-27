package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;


public class ExtraActionTurnChangedMessage extends ServerMessage {
    private final int gameId;
    private final String currentPlayerNickname;

    public ExtraActionTurnChangedMessage(int gameId, String currentPlayerNickname) {
        this.gameId = gameId;
        this.currentPlayerNickname = currentPlayerNickname;
    }
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }

    public int getGameId() {
        return gameId;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }
}
