package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.common.network.socket_messages.server.ClientMessageVisitor;

public class PlaceTotemMessage extends ClientMessage {
    private final int offerBoxPosition;

    public PlaceTotemMessage(int offerBoxPosition) {
        if (offerBoxPosition < 1) {
            throw new IllegalArgumentException("Offer box position must be positive.");
        }

        this.offerBoxPosition = offerBoxPosition;
    }

    public int getOfferBoxPosition() {
        return offerBoxPosition;
    }

    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
