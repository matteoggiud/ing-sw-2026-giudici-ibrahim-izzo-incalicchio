package it.polimi.ingsw.common.network.socket_messages.server.menu;

import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

public class DisconnectSuccessMessage extends ServerMessage {


    public DisconnectSuccessMessage(String info) {
        super(info);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
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
