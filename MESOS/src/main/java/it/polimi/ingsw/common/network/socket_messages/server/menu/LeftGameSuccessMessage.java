package it.polimi.ingsw.common.network.socket_messages.server.menu;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

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
