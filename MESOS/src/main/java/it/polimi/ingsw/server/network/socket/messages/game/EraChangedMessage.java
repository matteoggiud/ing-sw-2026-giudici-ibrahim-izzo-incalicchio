package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.network.socket.messages.ServerMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;

/**
 * This class represents a message sent by the server to all clients when the era changes in the game.
 */
public class EraChangedMessage extends ServerMessage {

    /**
     * Creates a new EraChangedMessage, which indicates that the era has changed in the game.
     * This message is sent to all clients whenever the era changes.
     */
    public EraChangedMessage() {

    }

    /**
     * Accepts a visitor that will handle the message
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
