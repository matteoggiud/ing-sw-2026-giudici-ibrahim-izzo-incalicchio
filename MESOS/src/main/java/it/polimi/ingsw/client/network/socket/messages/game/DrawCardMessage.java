package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

public class DrawCardMessage extends ClientMessage {
    private final int selectedCardId;
    private final boolean fromUpperRow;

    public DrawCardMessage(int selectedCardId, boolean fromUpperRow) {
        if (selectedCardId <= 0) {
            throw new IllegalArgumentException("Card id must be positive.");
        }

        this.selectedCardId = selectedCardId;
        this.fromUpperRow = fromUpperRow;
    }

    public int getSelectedCardId() {
        return selectedCardId;
    }

    public boolean isFromUpperRow() {
        return fromUpperRow;
    }

    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
