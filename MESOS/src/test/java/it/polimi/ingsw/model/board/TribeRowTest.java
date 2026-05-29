package it.polimi.ingsw.model.board;

import it.polimi.ingsw.server.model.board.TribeRow;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import it.polimi.ingsw.server.model.effect.event.SustenanceEventEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TribeRowTest {

    private TribeRow tribeRow;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        tribeRow = new TribeRow();
        nextCardId = 1;
        era1 = new Era1();
    }

    /**
     * Tests adding cards to the tribe row.
     * Verifies that both character and event cards are successfully added and can be correctly retrieved by type.
     */
    @Test
    void add() {
        HunterCharacterCard hunter = newHunter();
        EventCard event = newEvent(false);

        tribeRow.add(hunter);
        tribeRow.add(event);

        assertEquals(2, tribeRow.count());
        assertEquals(1, tribeRow.getCharacterCards().size());
        assertEquals(1, tribeRow.getEventCards().size());
        assertTrue(tribeRow.getCharacterCards().contains(hunter));
        assertTrue(tribeRow.getEventCards().contains(event));
    }

    /**
     * Tests removing cards from the tribe row.
     * Adds multiple cards, then removes an event and a character card, verifying that the count
     * and the specific type lists are properly updated after each removal.
     */
    @Test
    void remove() {
        HunterCharacterCard hunter1 = newHunter();
        HunterCharacterCard hunter2 = newHunter();
        EventCard event = newEvent(false);

        tribeRow.add(hunter1);
        tribeRow.add(event);
        tribeRow.add(hunter2);
        assertEquals(3, tribeRow.count());

        tribeRow.remove(event);
        assertEquals(2, tribeRow.count());
        assertTrue(tribeRow.getCharacterCards().contains(hunter1));
        assertTrue(tribeRow.getCharacterCards().contains(hunter2));
        assertTrue(tribeRow.getEventCards().isEmpty());

        tribeRow.remove(hunter1);
        assertEquals(1, tribeRow.count());
        assertEquals(1, tribeRow.getCharacterCards().size());
        assertTrue(tribeRow.getCharacterCards().contains(hunter2));
    }

    /**
     * Tests clearing the tribe row.
     * Adds cards and then calls clear, verifying that the row becomes completely empty.
     */
    @Test
    void clear() {
        tribeRow.add(newHunter());
        tribeRow.add(newEvent(true));
        assertEquals(2, tribeRow.count());

        tribeRow.clear();

        assertTrue(tribeRow.isEmpty());
        assertEquals(0, tribeRow.count());
        assertTrue(tribeRow.getCharacterCards().isEmpty());
        assertTrue(tribeRow.getEventCards().isEmpty());
    }

    /**
     * Tests retrieving character cards from the tribe row.
     * Ensures that only character cards are returned and verifies that the returned list
     * is a defensive copy, so modifying it does not alter the tribe row's state.
     */
    @Test
    void getCharacterCards() {
        HunterCharacterCard hunter1 = newHunter();
        HunterCharacterCard hunter2 = newHunter();
        EventCard event = newEvent(false);

        tribeRow.add(hunter1);
        tribeRow.add(event);
        tribeRow.add(hunter2);

        List<CharacterCard> characters = tribeRow.getCharacterCards();
        assertEquals(2, characters.size());
        assertTrue(characters.contains(hunter1));
        assertTrue(characters.contains(hunter2));

        // Verifica copia difensiva della lista restituita
        characters.clear();
        assertEquals(3, tribeRow.count());
        assertEquals(2, tribeRow.getCharacterCards().size());
    }

    /**
     * Tests retrieving event cards from the tribe row.
     * Ensures that only event cards are returned and verifies that the returned list
     * is a defensive copy, so modifying it does not alter the tribe row's state.
     */
    @Test
    void getEventCards() {
        HunterCharacterCard hunter = newHunter();
        EventCard event1 = newEvent(false);
        EventCard event2 = newEvent(true);

        tribeRow.add(event1);
        tribeRow.add(hunter);
        tribeRow.add(event2);

        List<EventCard> events = tribeRow.getEventCards();
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));

        // Verifica copia difensiva della lista restituita
        events.clear();
        assertEquals(3, tribeRow.count());
        assertEquals(2, tribeRow.getEventCards().size());
    }

    /**
     * Tests transferring cards from another tribe row.
     * Verifies that cards are moved from the source row to this row,
     * leaving the source row empty while increasing the count of this row appropriately.
     */
    @Test
    void transferTribeCardsFrom() {
        TribeRow source = new TribeRow();

        HunterCharacterCard hunter1 = newHunter();
        EventCard event = newEvent(false);
        HunterCharacterCard hunter2 = newHunter();

        source.add(hunter1);
        source.add(event);
        tribeRow.add(hunter2);

        tribeRow.transferTribeCardsFrom(source);

        assertEquals(3, tribeRow.count());
        assertTrue(tribeRow.getCharacterCards().contains(hunter1));
        assertTrue(tribeRow.getCharacterCards().contains(hunter2));
        assertTrue(tribeRow.getEventCards().contains(event));

        assertTrue(source.isEmpty());
        assertEquals(0, source.count());
    }

    /**
     * Tests copying cards to another tribe row.
     * Verifies that cards from this row are added to the target row without removing them
     * from this row, effectively duplicating references in the target.
     */
    @Test
    void copyTribeCardsTo() {
        TribeRow target = new TribeRow();

        HunterCharacterCard hunter1 = newHunter();
        EventCard event = newEvent(true);
        HunterCharacterCard hunter2 = newHunter();

        tribeRow.add(hunter1);
        tribeRow.add(event);
        target.add(hunter2);

        tribeRow.copyTribeCardsTo(target);

        assertEquals(2, tribeRow.count());
        assertEquals(3, target.count());
        assertTrue(target.getCharacterCards().contains(hunter1));
        assertTrue(target.getCharacterCards().contains(hunter2));
        assertTrue(target.getEventCards().contains(event));
    }

    /**
     * Tests the emptiness check of the tribe row.
     * Verifies that it returns true when empty, false when cards are added, and true again after being cleared.
     */
    @Test
    void isEmpty() {
        assertTrue(tribeRow.isEmpty());

        tribeRow.add(newHunter());
        assertFalse(tribeRow.isEmpty());

        tribeRow.clear();
        assertTrue(tribeRow.isEmpty());
    }

    /**
     * Tests counting the total number of cards in the tribe row.
     * Verifies that the count increases as cards are added, decreases as they are removed,
     * and returns to zero when the row is cleared.
     */
    @Test
    void count() {
        assertEquals(0, tribeRow.count());

        tribeRow.add(newHunter());
        assertEquals(1, tribeRow.count());

        tribeRow.add(newEvent(false));
        tribeRow.add(newHunter());
        assertEquals(3, tribeRow.count());

        tribeRow.remove(tribeRow.getEventCards().get(0));
        assertEquals(2, tribeRow.count());

        tribeRow.clear();
        assertEquals(0, tribeRow.count());
    }

    private HunterCharacterCard newHunter() {
        return new HunterCharacterCard(nextCardId++, "Hunter" + nextCardId, era1, 2, false, "");
    }

    private EventCard newEvent(boolean finalEvent) {
        return new EventCard(
                nextCardId++,
                "Event" + nextCardId,
                era1,
                2,
                finalEvent,
                new SustenanceEventEffect(),
                1,
                1,
                0,
                1,
                0,
                ""
        );
    }
}