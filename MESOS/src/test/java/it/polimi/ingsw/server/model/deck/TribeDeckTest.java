package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TribeDeckTest {

    private TribeDeck tribeDeck;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        tribeDeck = new TribeDeck();
        nextCardId = 1;
        era1 = new Era1();
    }

    /**
     * Tests that drawing from the deck correctly returns the cards in the order they were added,
     * and throws an IllegalStateException when attempting to draw from an empty deck.
     */
    @Test
    void drawCard() {
        TribeCard first = newHunter();
        TribeCard second = newHunter();

        tribeDeck.getTribeDeck().addAll(List.of(first, second));

        assertSame(first, tribeDeck.drawCard());
        assertSame(second, tribeDeck.drawCard());
        assertThrows(IllegalStateException.class, () -> tribeDeck.drawCard());
    }

    /**
     * Tests adding multiple tribe cards to an empty deck, verifying that the deck is updated
     * correctly and cards are available to be drawn in the expected order.
     */
    @Test
    void addTribesCards() {
        TribeCard first = newHunter();
        TribeCard second = newHunter();

        assertTrue(tribeDeck.isEmpty());

        tribeDeck.getTribeDeck().addAll(List.of(first, second));

        assertFalse(tribeDeck.isEmpty());
        assertSame(first, tribeDeck.drawCard());
        assertSame(second, tribeDeck.drawCard());
    }

    /**
     * Tests setting the entire tribe deck to a new list of cards, verifying the old cards
     * are discarded, the new ones can be drawn, and null or empty lists throw exceptions.
     */
    @Test
    void setTribeDeck() {
        TribeCard oldCard = newHunter();
        tribeDeck.getTribeDeck().addAll(List.of(oldCard));

        TribeCard newFirst = newHunter();
        TribeCard newSecond = newHunter();

        tribeDeck.setTribeDeck(List.of(newFirst, newSecond));

        assertSame(newFirst, tribeDeck.drawCard());
        assertSame(newSecond, tribeDeck.drawCard());
        assertThrows(IllegalStateException.class, () -> tribeDeck.drawCard());

        assertThrows(IllegalArgumentException.class, () -> tribeDeck.setTribeDeck(null));
        assertThrows(IllegalArgumentException.class, () -> tribeDeck.setTribeDeck(List.of()));
    }

    /**
     * Tests adding a single tribe card directly to the top of the deck, ensuring it is drawn
     * before any previously existing cards.
     */
    @Test
    void addCardToTop() {
        TribeCard first = newHunter();
        TribeCard top = newHunter();

        tribeDeck.getTribeDeck().addAll(List.of(first));
        tribeDeck.getTribeDeck().addFirst(top);

        assertSame(top, tribeDeck.drawCard());
        assertSame(first, tribeDeck.drawCard());
    }

    /**
     * Tests the isEmpty condition of the tribe deck across different states: initially empty,
     * after adding a card, and after drawing the only available card.
     */
    @Test
    void isEmpty() {
        assertTrue(tribeDeck.isEmpty());

        tribeDeck.getTribeDeck().addFirst(newHunter());
        assertFalse(tribeDeck.isEmpty());

        tribeDeck.drawCard();
        assertTrue(tribeDeck.isEmpty());
    }

    private HunterCharacterCard newHunter() {
        return new HunterCharacterCard(nextCardId++, "Hunter" + nextCardId, era1, 2, false, "");
    }
}