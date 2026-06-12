package it.polimi.ingsw.common.network.socket_messages.client.menu;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.visitors.ClientMessageVisitor;

public class GetAvailableGamesMessage extends ClientMessage {
    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);

    }
}
