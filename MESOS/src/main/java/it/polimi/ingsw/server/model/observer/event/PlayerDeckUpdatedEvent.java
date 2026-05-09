package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;

import java.util.UUID;

/**
 * Event emitted when one player's deck changes.
 */
public class PlayerDeckUpdatedEvent implements IGameEvent {

    private final int gameId;
    private final String playerNickname;
    private final PlayerDeckDTO playerDeck;

    /**
     * Constructs a new player deck updated event.
     *
     * @param gameId the unique identifier of the game
     * @param playerNickname the nickname of the player whose deck changed
     * @param playerDeck the DTO representing the player's updated deck
     */
    public PlayerDeckUpdatedEvent(int gameId, String playerNickname, PlayerDeckDTO playerDeck) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (playerNickname == null || playerNickname.isBlank()) {
            throw new IllegalArgumentException("Player nickname cannot be null or blank.");
        }
        if (playerDeck == null) {
            throw new IllegalArgumentException("Player deck DTO cannot be null.");
        }

        this.gameId = gameId;
        this.playerNickname = playerNickname;
        this.playerDeck = playerDeck;
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
     * Returns the nickname of the player whose deck changed.
     *
     * @return the player nickname
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * Returns the updated player deck DTO.
     *
     * @return the player deck DTO
     */
    public PlayerDeckDTO getPlayerDeck() {
        return playerDeck;
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
