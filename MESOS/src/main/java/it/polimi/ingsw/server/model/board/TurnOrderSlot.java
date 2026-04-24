package it.polimi.ingsw.server.model.board;
import it.polimi.ingsw.server.model.core.Player;

/**
 * Represents a slot in the turn order track.
 */
public class TurnOrderSlot {
    public static final int DEFAULT_PP_MALUS = -2;

    //private final int index;
    /** The bonus of food provided by this slot, which can be negative or positive */
    private final int foodBonus;

    /** The player currently placed in this slot, or null if the slot is free */
    private Player placedPlayer;

    /**
     * Constructs a TurnOrderSlot with the specified food bonus.
     *
     * @param foodBonus the amount of food bonus provided by this slot (can be zero or positive)
     */
    public TurnOrderSlot(int foodBonus) {
       /*
        if (index < 0) {
            throw new IllegalArgumentException("index cannot be negative");
        }
        this.index = index;
        */
        this.foodBonus = foodBonus;
        this.placedPlayer = null;
    }

    /**
     * Gets the food bonus provided by this slot.
     *
     * @return the amount of food bonus provided by this slot
     */
    public int getFoodBonus() { return foodBonus; }

    /**
     * Gets the player currently placed in this slot, or null if the slot is free.
     *
     * @return the player currently placed in this slot, or null if the slot is free
     */
    public Player getPlacedPlayer() { return placedPlayer; }

    /**
     * Checks if the slot is free.
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
    public void placePlayer(Player player) throws IllegalArgumentException, IllegalStateException {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");
        if (!isFree()) throw new IllegalStateException("TurnOrderSlot occupied");

        this.placedPlayer = player;
    }

    /**
     * Removes the player currently placed in this slot.
     *
     * @return the player removed from this slot (or null if the slot was already free)
     */
    public Player removePlayer() {
        Player removed = placedPlayer;
        placedPlayer = null;
        return removed;
    }
}
