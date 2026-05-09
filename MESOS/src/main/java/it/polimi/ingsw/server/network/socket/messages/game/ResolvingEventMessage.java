package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.client.network.socket.messages.ServerMessageVisitor;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;
import it.polimi.ingsw.server.network.socket.messages.ServerMessage;

public class ResolvingEventMessage extends ServerMessage {
    private final int gameId;
    private final EventCardDTO eventCard;

    public ResolvingEventMessage(int gameId, EventCardDTO eventCard) {
        this.gameId = gameId;
        this.eventCard = eventCard;
    }

    public int getGameId() {
        return gameId;
    }

    public EventCardDTO getEventCard() {
        return eventCard;
    }

    @Override
    public void dispatch(ServerMessageVisitor serverVisitor) {
        serverVisitor.visit(this);
    }
}
