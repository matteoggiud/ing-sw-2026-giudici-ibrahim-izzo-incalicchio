package it.polimi.ingsw.client.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ClientMessage;
import it.polimi.ingsw.server.network.socket.messages.ClientMessageVisitor;

public class PlaceTotemMessage extends ClientMessage {
    private final String offerBoxId;

    public PlaceTotemMessage(String offerBoxId) {
        if (offerBoxId == null || offerBoxId.isBlank()) {
            throw new IllegalArgumentException("Offer box id cannot be null or blank.");
        }

        this.offerBoxId = offerBoxId.trim().toUpperCase();
    }

    public String getOfferBoxId() {
        return offerBoxId;
    }

    @Override
    public void dispatch(ClientMessageVisitor clientVisitor) {
        clientVisitor.visit(this);
    }
}
