package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

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
