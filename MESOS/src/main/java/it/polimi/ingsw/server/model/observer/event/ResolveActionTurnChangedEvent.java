package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;

import java.util.UUID;

/**
 * Event emitted when the current player for the action resolution phase changes.
 */
public class ResolveActionTurnChangedEvent implements IGameEvent {

    private final UUID gameId;
    private final String currentPlayerNickname;

    /**
     * Constructs a new resolve action turn changed event.
     *
     * @param gameId the unique identifier of the game
     * @param currentPlayerNickname the nickname of the player whose turn it is
     */
    public ResolveActionTurnChangedEvent(UUID gameId, String currentPlayerNickname) {
        if (gameId == null) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (currentPlayerNickname == null || currentPlayerNickname.isBlank()) {
            throw new IllegalArgumentException("Current player nickname cannot be null or blank.");
        }

        this.gameId = gameId;
        this.currentPlayerNickname = currentPlayerNickname;
    }

    /**
     * Returns the unique identifier of the game.
     *
     * @return the game id
     */
    public UUID getGameId() {
        return gameId;
    }

    /**
     * Returns the nickname of the player whose turn it is.
     *
     * @return the current player nickname
     */
    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
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
