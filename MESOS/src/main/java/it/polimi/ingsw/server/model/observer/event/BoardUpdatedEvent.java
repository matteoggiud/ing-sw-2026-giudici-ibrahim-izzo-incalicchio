package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.observer.IGameObserver;

import java.util.List;
import java.util.UUID;

public class BoardUpdatedEvent implements IGameEvent {
    private final UUID gameId;
    private final Era currentEra;
    private final List<String> upperRowCards;
    private final List<String> lowerRowCards;
    private final List<String> offerTrackBoxes;
    private final List<String> turnOrderSlots;


    public BoardUpdatedEvent(UUID gameId, Era currentEra, List<String> upperRowCards, List<String> lowerRowCards, List<String> offerTrackBoxes, List<String> turnOrderSlots) {
        this.gameId = gameId;
        this.currentEra = currentEra;
        this.upperRowCards = upperRowCards;
        this.lowerRowCards = lowerRowCards;
        this.offerTrackBoxes = offerTrackBoxes;
        this.turnOrderSlots = turnOrderSlots;
    }


    /**
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
