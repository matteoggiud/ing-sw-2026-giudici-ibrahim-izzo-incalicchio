package it.polimi.ingsw.client.network.socket.messages.menu;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class LeftGameMessage extends ClientMessage {

    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
