package it.polimi.ingsw.client.network.socket.messages.menu;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

public class LeftLobbyMessage extends ClientMessage {

    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
