package it.polimi.ingsw.model.observer.event;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.observer.IGameObserver;

import java.util.List;

public class GameStartedEvent implements IGameEvent {

    List<String> playerNicknames;

    /**
     * Constructs a new GameStartedEvent with the given game context, extracting the player nicknames from the game
     *
     * @param game the current game context from which to extract player nicknames
     */
    public GameStartedEvent(Game game) {
        this.playerNicknames = game.getPlayers().getPlayerNicknames();
    }

    /**
     * Constructs a new GameStartedEvent with the given list of player nicknames.
     *
     * @param playerNicknames the list of player nicknames participating in the game, must not be null or empty
     */
    public GameStartedEvent(List<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
    }

    /**
     * Dispatches the GameStartedEvent to the given observer, allowing the observer to react to the event.
     *
     * @param observer the observer to which the event should be dispatched
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
