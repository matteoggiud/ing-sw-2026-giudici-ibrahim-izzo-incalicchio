package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;

import java.util.UUID;

/**
 * Event emitted whenever the visible board state changes.
 */
public class BoardUpdatedEvent implements IGameEvent {

    private final UUID gameId;
    private final BoardDTO board;

    /**
     * Constructs a new board updated event.
     *
     * @param gameId the unique identifier of the game
     * @param board the DTO representing the current visible board state
     */
    public BoardUpdatedEvent(UUID gameId, BoardDTO board) {
        if (gameId == null) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (board == null) {
            throw new IllegalArgumentException("Board DTO cannot be null.");
        }

        this.gameId = gameId;
        this.board = board;
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
     * Returns the DTO representing the current visible board state.
     *
     * @return the board DTO
     */
    public BoardDTO getBoard() {
        return board;
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
