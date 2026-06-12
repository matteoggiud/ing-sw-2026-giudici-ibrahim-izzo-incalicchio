package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.observer.IGameObserver;

import java.util.List;
import java.util.UUID;

/**
 * Event emitted when a new game starts, containing the list of player nicknames participating in the game and the unique identifier of the game.
 */
public class GameStartedEvent implements IGameEvent {

    private final List<String> playerNicknames;
    private final int gameId;

    /**
     * Constructs a new GameStartedEvent with the given game context, extracting the player nicknames from the game
     *
     * @param game the current game context from which to extract player nicknames
     */
    public GameStartedEvent(Game game) {
        this.playerNicknames = game.getPlayers().getPlayerNicknames();
        this.gameId = game.getGameId();
    }

    /**
     * Constructs a new GameStartedEvent with the given list of player nicknames.
     *
     * @param playerNicknames the list of player nicknames participating in the game, must not be null or empty
     */
    public GameStartedEvent(List<String> playerNicknames, int gameId) {
        this.playerNicknames = playerNicknames;
        this.gameId = gameId;
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
     * Returns the unique identifier of the game that has started.
     *
     * @return the UUID of the game
     */
    public int getGameId() {
        return gameId;
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
