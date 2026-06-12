package it.polimi.ingsw.common.network.socket_messages.server.game;

import it.polimi.ingsw.common.network.socket_messages.visitors.ServerMessageVisitor;
import it.polimi.ingsw.common.network.dto.EventCardDTO;
import it.polimi.ingsw.common.network.socket_messages.server.ServerMessage;

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
