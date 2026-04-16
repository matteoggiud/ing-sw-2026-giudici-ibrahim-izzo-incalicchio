package it.polimi.ingsw.model.observer;

import java.util.UUID;

/**
 * Represents a game event that can be observed.
 */
public class GameEvent {
    private final GameEventType type;
    private final UUID gameId;
    private final UUID playerId;
    private final String message;

    /**
     * Constructor for GameEvent.
     *
     * @param type the type of event
     * @param gameId the ID of the game
     * @param playerId the ID of the player involved (can be null if not player-specific)
     * @param message a message describing the event
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
    public GameEventType getType() {
        return type;
    }

    /**
     * Gets the ID of the game.
     */
    public UUID getGameId() {
        return gameId;
    }

    /**
     * Gets the ID of the player involved in the event.
     */
    public UUID getPlayerId() {
        return playerId;
    }

    /**
     * Gets the message describing the event.
     */
    public String getMessage() {
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
