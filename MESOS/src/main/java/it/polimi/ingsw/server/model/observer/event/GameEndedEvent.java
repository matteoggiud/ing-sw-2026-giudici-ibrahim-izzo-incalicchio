package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Event emitted when the game reaches the final scoring phase
 * and the final ranking becomes available.
 */
public class GameEndedEvent implements IGameEvent {

    private final int gameId;
    private final Map<String, Integer> finalRanking;

    /**
     * Constructs a new game ended event.
     *
     * @param gameId the unique identifier of the game
     * @param finalRanking the final ranking using player nicknames as keys
     */
    public GameEndedEvent(int gameId, Map<String, Integer> finalRanking) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (finalRanking == null) {
            throw new IllegalArgumentException("Final ranking cannot be null.");
        }

        this.gameId = gameId;
        this.finalRanking = new LinkedHashMap<>(finalRanking);
    }

    /**
     * Returns the unique identifier of the game.
     *
     * @return the game id
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Returns the final ranking using player nicknames as keys.
     *
     * @return the final ranking
     */
    public Map<String, Integer> getFinalRanking() {
        return new LinkedHashMap<>(finalRanking);
    }

    /**
     * Dispatches this event to the observer.
     *
     * @param observer the observer to notify
     */
    @Override
    public void dispatch(IGameObserver observer) {
        observer.update(this);
    }
}
