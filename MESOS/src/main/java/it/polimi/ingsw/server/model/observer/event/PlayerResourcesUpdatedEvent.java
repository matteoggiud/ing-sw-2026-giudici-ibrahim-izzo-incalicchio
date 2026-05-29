package it.polimi.ingsw.server.model.observer.event;

import it.polimi.ingsw.server.model.observer.IGameObserver;

/**
 * Event emitted when one player's resources change.
 */
public class PlayerResourcesUpdatedEvent implements IGameEvent {

    private final int gameId;
    private final String playerNickname;
    private final int food;
    private final int prestigePoints;

    /**
     * Constructs a new player resources updated event.
     *
     * @param gameId the unique identifier of the game
     * @param playerNickname the nickname of the player whose resources changed
     * @param food the player's current food amount
     * @param prestigePoints the player's current prestige points amount
     */
    public PlayerResourcesUpdatedEvent(int gameId, String playerNickname, int food, int prestigePoints) {
        if (gameId < 0) {
            throw new IllegalArgumentException("Game id cannot be null.");
        }
        if (playerNickname == null || playerNickname.isBlank()) {
            throw new IllegalArgumentException("Player nickname cannot be null or blank.");
        }
        if (food < 0) {
            throw new IllegalArgumentException("Food cannot be negative.");
        }

        this.gameId = gameId;
        this.playerNickname = playerNickname;
        this.food = food;
        this.prestigePoints = prestigePoints;
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
     * Returns the nickname of the player whose resources changed.
     *
     * @return the player nickname
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * Returns the player's current food amount.
     *
     * @return the food amount
     */
    public int getFood() {
        return food;
    }

    /**
     * Returns the player's current prestige points amount.
     *
     * @return the prestige points amount
     */
    public int getPrestigePoints() {
        return prestigePoints;
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
