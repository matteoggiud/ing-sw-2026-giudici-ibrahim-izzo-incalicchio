package it.polimi.ingsw.server.network.socket.messages.game;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;
import it.polimi.ingsw.server.model.observer.event.IGameEvent;

public class ResolvingEventUpdate implements IGameEvent{
    int gameId;
    EventCardDTO eventCard;

    public ResolvingEventUpdate(int gameId, EventCardDTO eventCard) {
        this.gameId = gameId;
        this.eventCard = eventCard;
    }

    public int getGameId() {
        return gameId;
    }

    public EventCardDTO getEventCard() {
        return eventCard;
    }

    /**
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(new ResolvingEventUpdate(gameId, eventCard));

    }
}
