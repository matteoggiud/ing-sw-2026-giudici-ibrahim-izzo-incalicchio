package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;

import java.util.List;

/**
 * Event emitted when the list of players in the game changes, such as when a player joins or leaves the game.
 */
public class PlayersChangedEvent implements IGameEvent {

    private final List<String> playerNicknames;

    /**
     * Constructs a new PlayersChangedEvent with the given game context, extracting the player nicknames from the game
     * @param playerNicknames the list of player nicknames participating in the game, must not be null or empty
     */
    public PlayersChangedEvent(List<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
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
