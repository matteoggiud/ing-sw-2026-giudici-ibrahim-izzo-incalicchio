package it.polimi.ingsw.common.network.socket_messages.server;

import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;

import java.io.Serializable;

/**
 * Message that the server sends to the client
 */

public abstract class ServerMessage implements Serializable {
    private final String message;

    protected ServerMessage() {
        this.message = "";
    }

    protected ServerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Accepts a visitor that will handle the message
     *
     * @param serverVisitor the visitor that will handle the message
     */
    public abstract void dispatch(ServerMessageVisitor serverVisitor);


}
