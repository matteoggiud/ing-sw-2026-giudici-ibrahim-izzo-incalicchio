package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;

/**
 * This class represents a message sent by the server to all clients when the era changes in the game.
 */
public class EraChangedMessage extends ServerMessage {

    private final int gameId;
    private final String newEra;

    /**
     * Creates a new EraChangedMessage, which indicates that the era has changed in the game.
     * This message is sent to all clients whenever the era changes.
     */
    public EraChangedMessage(int gameId, String newEra) {
        this.gameId = gameId;
        this.newEra = newEra;
    }

    public int getGameId() {
        return gameId;
    }

    public String getNewEra() {
        return newEra;
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
