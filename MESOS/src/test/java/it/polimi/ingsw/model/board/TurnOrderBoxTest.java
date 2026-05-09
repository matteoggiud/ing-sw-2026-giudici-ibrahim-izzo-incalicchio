package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.TurnOrderBox;
import it.polimi.ingsw.server.model.board.TurnOrderSlot;
import it.polimi.ingsw.server.model.core.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TurnOrderBoxTest {

    private TurnOrderBox turnOrderBox;
    private TurnOrderSlot slot1;
    private TurnOrderSlot slot2;
    private TurnOrderSlot slot3;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        turnOrderBox = new TurnOrderBox();
        slot1 = new TurnOrderSlot(2);
        slot2 = new TurnOrderSlot(0);
        slot3 = new TurnOrderSlot(-1);
        turnOrderBox.initializeTurnOrderBox(Arrays.asList(slot1, slot2, slot3));

        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player3 = new Player("Player3");

        slot1.placePlayer(player1);
        slot2.placePlayer(player2);
    }

    @Test
    void initializeTurnOrderBox() {
        TurnOrderBox box = new TurnOrderBox();
        TurnOrderSlot first = new TurnOrderSlot(1);
        TurnOrderSlot second = new TurnOrderSlot(3);

        box.initializeTurnOrderBox(Arrays.asList(first, second));

        assertEquals(2, box.getSlots().size());
        assertSame(first, box.getSlots().get(0));
        assertSame(second, box.getSlots().get(1));

        assertThrows(IllegalArgumentException.class, () -> box.initializeTurnOrderBox(null));
        assertThrows(IllegalArgumentException.class, () -> box.initializeTurnOrderBox(new java.util.ArrayList<>()));
        assertThrows(IllegalArgumentException.class, () -> box.initializeTurnOrderBox(Arrays.asList(first, null)));
    }

    @Test
    void getSlots() {
        List<TurnOrderSlot> slots = turnOrderBox.getSlots();

        assertEquals(3, slots.size());
        assertSame(slot1, slots.get(0));
        assertSame(slot2, slots.get(1));
        assertSame(slot3, slots.get(2));

        slots.clear();

        assertEquals(3, turnOrderBox.getSlots().size());
        assertSame(slot1, turnOrderBox.getSlots().getFirst());
    }

    @Test
    void getFirstFreeSlot() {
        assertSame(slot3, turnOrderBox.getFirstFreeSlot());

        slot3.placePlayer(player3);

        assertThrows(IllegalStateException.class, () -> turnOrderBox.getFirstFreeSlot());
    }

    @Test
    void removePlayer() {
        assertSame(player1, turnOrderBox.removePlayer(player1));
        assertTrue(slot1.isFree());
        assertSame(slot2, turnOrderBox.getSlotByPlayer(player2));

        assertThrows(IllegalArgumentException.class, () -> turnOrderBox.removePlayer(null));
        assertThrows(IllegalStateException.class, () -> turnOrderBox.removePlayer(player3));
    }

    @Test
    void isCompletelyEmpty() {
        assertFalse(turnOrderBox.isCompletelyEmpty());

        TurnOrderBox emptyBox = new TurnOrderBox();
        TurnOrderSlot emptySlot1 = new TurnOrderSlot(1);
        TurnOrderSlot emptySlot2 = new TurnOrderSlot(0);
        emptyBox.initializeTurnOrderBox(Arrays.asList(emptySlot1, emptySlot2));

        assertTrue(emptyBox.isCompletelyEmpty());

        emptySlot1.placePlayer(new Player("Tmp"));
        assertFalse(emptyBox.isCompletelyEmpty());
    }

    @Test
    void getSlotByPlayer() {
        assertSame(slot1, turnOrderBox.getSlotByPlayer(player1));
        assertSame(slot2, turnOrderBox.getSlotByPlayer(player2));

        assertThrows(IllegalArgumentException.class, () -> turnOrderBox.getSlotByPlayer(null));
        assertThrows(IllegalStateException.class, () -> turnOrderBox.getSlotByPlayer(player3));
    }

    @Test
    void getIndexBySlot() {
        assertEquals(0, turnOrderBox.getIndexBySlot(slot1));
        assertEquals(1, turnOrderBox.getIndexBySlot(slot2));
        assertEquals(2, turnOrderBox.getIndexBySlot(slot3));

        assertEquals(-1, turnOrderBox.getIndexBySlot(new TurnOrderSlot(99)));
    }
}