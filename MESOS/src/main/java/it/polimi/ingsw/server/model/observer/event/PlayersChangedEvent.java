package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.observer.IGameObserver;

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
     * Returns the list of player nicknames participating in the game.
     *
     * @return the list of player nicknames, never null or empty
     */
    public List<String> getPlayerNicknames() {
        return playerNicknames;
    }

    /**
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
