package it.polimi.ingsw.server.network.socket.messages.register;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

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
