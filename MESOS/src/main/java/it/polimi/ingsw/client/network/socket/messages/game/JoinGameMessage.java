package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

public class JoinGameMessage extends ClientMessage {
    private final int gameId;

    public JoinGameMessage(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);

    }
}
