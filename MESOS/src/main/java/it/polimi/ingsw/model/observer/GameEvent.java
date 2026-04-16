package it.polimi.ingsw.model.observer;

import java.util.UUID;

/**
 * Represents a game event that can be observed.
 */
public record GameEvent(GameEventType type, UUID gameId, UUID playerId, String message) {
    /**
     * Constructor for GameEvent.
     *
     * @param type     the type of event
     * @param gameId   the ID of the game
     * @param playerId the ID of the player involved (can be null if not player-specific)
     * @param message  a message describing the event
     */
    public GameEvent(GameEventType type, UUID gameId, UUID playerId, String message) {
        if (type == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }
        if (gameId == null) {
            throw new IllegalArgumentException("Game ID cannot be null");
        }
        this.type = type;
        this.gameId = gameId;
        this.playerId = playerId;
        this.message = message != null ? message : "";
    }

    /**
     * Gets the type of the event.
     */
    @Override
    public GameEventType type() {
        return type;
    }

    /**
     * Gets the ID of the game.
     */
    @Override
    public UUID gameId() {
        return gameId;
    }

    /**
     * Gets the ID of the player involved in the event.
     */
    @Override
    public UUID playerId() {
        return playerId;
    }

    /**
     * Gets the message describing the event.
     */
    @Override
    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return "GameEvent{" +
                "type=" + type +
                ", gameId=" + gameId +
                ", playerId=" + playerId +
                ", message='" + message + '\'' +
                '}';
    }
}
