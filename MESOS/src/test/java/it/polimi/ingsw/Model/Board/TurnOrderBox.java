package it.polimi.ingsw.Model.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the turn order box in the game,
 * which contains slots for players to determine their turn order and food bonus/malus.
 */

public class TurnOrderBox {
    private final List<TurnOrderSlot> slots;
    private final int numPlayers;

    /**
     *
     * @param slots the list of turn order slots in the box
     * @param numPlayers the number of players in the game, which determines how many slots are needed
     */

    public TurnOrderBox(List<TurnOrderSlot> slots, int numPlayers) {
        this.slots = new ArrayList<>(slots);
        this.numPlayers = numPlayers;
    }

    /**
     *
     * @return an unmodifiable list of the turn order slots in the box, to prevent external modification
     */

    public List<TurnOrderSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    /**
     *
     * @return the number of players of the box
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     *
     * @return the first free slot in the box, or null if all slots are occupied
     */

    public TurnOrderSlot getFirstFreeSlot() {
        return slots.stream().filter(TurnOrderSlot::isFree).findFirst().orElse(null);
    }
}
