package it.polimi.ingsw.model.board;
import it.polimi.ingsw.model.core.Player;

/**
 * Represents a slot in the turn order track.
 * Each slot has an index, a food bonus, and can hold at most one player.
 */

public class TurnOrderSlot {
    private final int index;
    private final int foodBonus;
    private Player placedPlayer;

    /**
     *
     * @param index the position of the slot in the turn order track (must be non-negative)
     * @param foodBonus the amount of food bonus provided by this slot (can be zero or positive)
     */

    public TurnOrderSlot(int index, int foodBonus) {
        if (index < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        this.index = index;
        this.foodBonus = foodBonus;
        this.placedPlayer = null;
    }

    /**
     *
     * @return the index of the slot, the food bonus provided by this slot,
     * and the player currently placed in this slot (or null if the slot is free)
     */

    public int getIndex() { return index; }
    public int getFoodBonus() { return foodBonus; }
    public Player getPlacedPlayer() { return placedPlayer; }

    /**
     *
     * @return true if the slot is free, false otherwise
     */

    public boolean isFree() {
        return placedPlayer == null;
    }

    /**
     *
     * @param player the player to place in this slot (must not be null)
     */

    public void placePlayer(Player player) {
        if (!isFree()) throw new IllegalStateException("TurnOrderSlot occupato");
        this.placedPlayer = player;
    }

    /**
     *
     * @return the player removed from this slot (or null if the slot was already free)
     */

    public Player removePlayer() {
        Player removed = placedPlayer;
        placedPlayer = null;
        return removed;
    }
}
