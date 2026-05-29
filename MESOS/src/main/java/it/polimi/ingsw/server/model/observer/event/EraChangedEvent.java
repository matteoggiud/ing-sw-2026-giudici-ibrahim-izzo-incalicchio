package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;


/**
 * Event emitted when the game transitions to a new era.
 */
public class EraChangedEvent implements IGameEvent {

    private final int gameId;
    private final String newEra;

    /**
     * Constructs a new era changed event.
     *
     * @param gameId the unique identifier of the game
     * @param newEra the new era reached by the game
     */
    public EraChangedEvent(int gameId, String newEra) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (newEra == null || newEra.isBlank()) {
            throw new IllegalArgumentException("New era cannot be null or blank.");
        }

        this.gameId = gameId;
        this.newEra = newEra;
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
     * Returns the new era reached by the game.
     *
     * @return the new era
     */
    public String getNewEra() {
        return newEra;
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
