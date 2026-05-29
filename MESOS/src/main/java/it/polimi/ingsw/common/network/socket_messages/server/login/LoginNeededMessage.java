package it.polimi.ingsw.common.network.socket_messages.server.login;

import it.polimi.ingsw.common.network.socket_messages.client.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

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
