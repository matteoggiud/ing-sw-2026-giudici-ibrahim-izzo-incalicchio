package it.polimi.ingsw.common.network.socket_messages.client.menu;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

public class LeftLobbyMessage extends ClientMessage {

    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
