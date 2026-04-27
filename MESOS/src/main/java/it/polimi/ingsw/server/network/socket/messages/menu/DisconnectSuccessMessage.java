package it.polimi.ingsw.server.network.socket.messages.menu;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

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
