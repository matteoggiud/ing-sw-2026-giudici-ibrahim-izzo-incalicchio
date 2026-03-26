package it.polimi.ingsw.Model.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TurnOrderBox {
    private final List<TurnOrderSlot> slots;
    private final int numPlayers;

    public TurnOrderBox(List<TurnOrderSlot> slots, int numPlayers) {
        this.slots = new ArrayList<>(slots);
        this.numPlayers = numPlayers;
    }

    public List<TurnOrderSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public TurnOrderSlot getFirstFreeSlot() {
        return slots.stream().filter(TurnOrderSlot::isFree).findFirst().orElse(null);
    }
}
