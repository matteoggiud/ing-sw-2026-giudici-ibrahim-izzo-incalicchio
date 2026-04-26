package it.polimi.ingsw.server.network.socket.messages.login;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class LoginNeededMessage extends ServerMessage {


    public LoginNeededMessage(String message) {
        super(message);
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
