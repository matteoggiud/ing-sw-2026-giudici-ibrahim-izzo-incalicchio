package it.polimi.ingsw.model.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the turn order box in the game.
 */
public class TurnOrderBox {
    /** The list of turn order slots in the box. */
    private final ArrayList<TurnOrderSlot> slots;

    /**
     * Constructs a TurnOrderBox with the specified list of turn order slots.
     *
     * @param slots the list of turn order slots in the box
     */
    public TurnOrderBox(List<TurnOrderSlot> slots) throws IllegalArgumentException {
        if (slots == null || slots.size() < 2 || slots.size() > 5)
            throw new IllegalArgumentException("TurnOrderBox must have between 2 and 5 slots and cannot be null");

        this.slots = new ArrayList<>(slots);
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
    public TurnOrderSlot getFirstFreeSlot() throws IllegalArgumentException {
        return slots.stream().filter(TurnOrderSlot::isFree).findFirst().orElseThrow(() -> new IllegalStateException("No free slots available in the TurnOrderBox"));
    }
}
