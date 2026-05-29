package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnOrderSlotTest {

    private TurnOrderSlot slot;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        slot = new TurnOrderSlot(2);
        player1 = new Player("Player1");
        player2 = new Player("Player2");
    }

    /**
     * Tests retrieving the food bonus of the turn order slot.
     * Verifies that the correctly initialized value (both positive and default negative values) is returned.
     */
    @Test
    void getFoodBonus() {
        assertEquals(2, slot.getFoodBonus());

        TurnOrderSlot negativeSlot = new TurnOrderSlot(TurnOrderSlot.DEFAULT_PP_MALUS);
        assertEquals(TurnOrderSlot.DEFAULT_PP_MALUS, negativeSlot.getFoodBonus());
    }

    /**
     * Tests retrieving the player placed in the slot.
     * Asserts that it initially returns null, and returns the correct player once placed.
     */
    @Test
    void getPlacedPlayer() {
        assertNull(slot.getPlacedPlayer());

        slot.placePlayer(player1);

        assertSame(player1, slot.getPlacedPlayer());
    }

    /**
     * Tests the emptiness status of the turn order slot.
     * Verifies that the slot correctly reports true when empty and false when occupied by a player.
     */
    @Test
    void isFree() {
        assertTrue(slot.isFree());

        slot.placePlayer(player1);

        assertFalse(slot.isFree());
    }

    /**
     * Tests placing a player into the slot.
     * Checks that placing null throws an exception, placing a valid player correctly assigns them and 
     * marks the slot as not free, and placing another player in an already occupied slot throws an exception.
     */
    @Test
    void placePlayer() {
        assertThrows(IllegalArgumentException.class, () -> slot.placePlayer(null));

        slot.placePlayer(player1);

        assertSame(player1, slot.getPlacedPlayer());
        assertFalse(slot.isFree());

        assertThrows(IllegalStateException.class, () -> slot.placePlayer(player2));
    }

    /**
     * Tests removing a player from the slot.
     * Verifies that removing from an empty slot returns null, while removing from an occupied slot
     * returns the correct player, frees the slot, and clears the player reference.
     */
    @Test
    void removePlayer() {
        assertNull(slot.removePlayer());

        slot.placePlayer(player1);

        assertSame(player1, slot.removePlayer());
        assertTrue(slot.isFree());
        assertNull(slot.getPlacedPlayer());
    }
}