package it.polimi.ingsw.server.network.socket.messages.menu;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class LeftGameSuccessMessage extends ServerMessage {
    public LeftGameSuccessMessage(String message) {
        super(message);
    }

    /**
     * @param serverVisitor the visitor that will handle the message
     */
    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }


}
