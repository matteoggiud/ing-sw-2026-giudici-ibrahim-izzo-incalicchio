package it.polimi.ingsw.common.network.socket_messages.client.game;

import it.polimi.ingsw.common.network.socket_messages.client.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.visitors.ClientMessageVisitor;

public class ExtraDrawMessage extends ClientMessage {
    public final int selectedCardId;

    public ExtraDrawMessage(int selectedCardId) {
        this.selectedCardId = selectedCardId;
    }

    public int getSelectedCardId() {
        return selectedCardId;
    }

    /**
     * @param clientVisitor
     */
    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);

    }
}
