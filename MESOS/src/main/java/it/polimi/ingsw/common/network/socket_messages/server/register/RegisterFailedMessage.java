package it.polimi.ingsw.common.network.socket_messages.server.register;

import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

public class RegisterFailedMessage extends ServerMessage {
    private final String username;

    public RegisterFailedMessage(String message, String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return username;
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
