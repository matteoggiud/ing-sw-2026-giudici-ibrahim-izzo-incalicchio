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

    @Test
    void getFoodBonus() {
        assertEquals(2, slot.getFoodBonus());

        TurnOrderSlot negativeSlot = new TurnOrderSlot(TurnOrderSlot.DEFAULT_PP_MALUS);
        assertEquals(TurnOrderSlot.DEFAULT_PP_MALUS, negativeSlot.getFoodBonus());
    }

    @Test
    void getPlacedPlayer() {
        assertNull(slot.getPlacedPlayer());

        slot.placePlayer(player1);

        assertSame(player1, slot.getPlacedPlayer());
    }

    @Test
    void isFree() {
        assertTrue(slot.isFree());

        slot.placePlayer(player1);

        assertFalse(slot.isFree());
    }

    @Test
    void placePlayer() {
        assertThrows(IllegalArgumentException.class, () -> slot.placePlayer(null));

        slot.placePlayer(player1);

        assertSame(player1, slot.getPlacedPlayer());
        assertFalse(slot.isFree());

        assertThrows(IllegalStateException.class, () -> slot.placePlayer(player2));
    }

    @Test
    void removePlayer() {
        assertNull(slot.removePlayer());

        slot.placePlayer(player1);

        assertSame(player1, slot.removePlayer());
        assertTrue(slot.isFree());
        assertNull(slot.getPlacedPlayer());
    }
}