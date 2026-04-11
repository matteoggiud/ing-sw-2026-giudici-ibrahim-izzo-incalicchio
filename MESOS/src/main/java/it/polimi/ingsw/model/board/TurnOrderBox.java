package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.core.Player;

import java.util.ArrayList;

/**
 * Represents the turn order box in the game.
 */
public class TurnOrderBox {
    /**
     * The list of turn order slots in the box.
     */
    private final ArrayList<TurnOrderSlot> slots;


    /**
     * Constructs an empty TurnOrderBox.
     */
    public TurnOrderBox() {
        this.slots = new ArrayList<>();
    }

    /**
     * Initializes the turn order box with a given list of turn order slots.
     *
     * @param slots the list of turn order slots in the box
     */
    public void initializeSlots(ArrayList<TurnOrderSlot> slots) throws IllegalArgumentException {
        if (slots == null || slots.contains(null) || slots.size() < 2 || slots.size() > 5)
            throw new IllegalArgumentException("TurnOrderBox must have between 2 and 5 slots and cannot be null");
        if (!this.slots.isEmpty())
            throw new IllegalStateException("TurnOrderBox slots have already been initialized");

        this.slots.addAll(slots);
    }


    /**
     * Gets the list of turn order slots in the box.
     *
     * @return the list of the turn order slots in the box
     */
    public ArrayList<TurnOrderSlot> getSlots() {
        return new ArrayList<>(slots);
    }

    /**
     * Gets the first free slot in the box.
     *
     * @return the first free slot in the box
     * @throws IllegalStateException if there are no free slots available
     */
    public TurnOrderSlot getFirstFreeSlot() throws IllegalStateException {
        return slots.stream().filter(TurnOrderSlot::isFree).findFirst().orElseThrow(() -> new IllegalStateException("No free slots available in the TurnOrderBox"));
    }

    /**
     * Removes the specified player from the box.
     *
     * @param player the player to be removed from the box
     * @throws IllegalStateException    if the player is not found in any slot of the box
     * @throws IllegalArgumentException if the player is null
     */
    public Player removePlayer(Player player) throws IllegalStateException, IllegalArgumentException {
        if (player == null)
            throw new IllegalArgumentException("Player cannot be null");
        for (TurnOrderSlot slot : slots)
            if (!slot.isFree() && slot.getPlacedPlayer().equals(player)) {
                return slot.removePlayer();
            }

        throw new IllegalStateException("Player not found in any slot of the TurnOrderBox");
    }

    /**
     * Checks if all the slots are free
     *
     * @return true if all the slots are free, false otherwise
     */
    public boolean isCompletelyEmpty() {
        return slots.stream().allMatch(TurnOrderSlot::isFree);
    }


    public TurnOrderSlot getSlotByPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException("Player cannot be null");

        for (TurnOrderSlot slot : slots) {
            if (!slot.isFree() && slot.getPlacedPlayer().equals(player)) {
                return slot;
            }
        }
        throw new IllegalStateException("Player not found in any slot of the TurnOrderBox");
    }
}
