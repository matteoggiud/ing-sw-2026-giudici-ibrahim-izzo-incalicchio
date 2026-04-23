package it.polimi.ingsw.model.observer.event;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.observer.IGameObserver;

import java.util.List;

public class PlayersChangedEvent implements IGameEvent {

    private final List<String> playerNicknames;

    public PlayersChangedEvent(List<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
    }

    public PlayersChangedEvent(Game game) {
        this.playerNicknames = game.getPlayers().getPlayerNicknames();
    }

    /**
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
