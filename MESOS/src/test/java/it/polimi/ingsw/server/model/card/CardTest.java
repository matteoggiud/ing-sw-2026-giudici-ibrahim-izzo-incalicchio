package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private TestCard cardWithId;
    private TestCard cardWithoutId;

    @BeforeEach
    void setUp() {
        cardWithId = new TestCard(10, "CardA", Era.ERA_1, 3);
        cardWithoutId = new TestCard("CardB", Era.ERA_2, 2);
    }

    @Test
    void getCardId() {
        assertEquals(10, cardWithId.getCardId());
        assertEquals(-1, cardWithoutId.getCardId());
    }

    @Test
    void getName() {
        assertEquals("CardA", cardWithId.getName());
        assertEquals("CardB", cardWithoutId.getName());
    }

    @Test
    void getEra() {
        assertEquals(Era.ERA_1, cardWithId.getEra());
        assertEquals(Era.ERA_2, cardWithoutId.getEra());
    }

    @Test
    void getMinNumPlayers() {
        assertEquals(3, cardWithId.getMinNumPlayers());
        assertEquals(2, cardWithoutId.getMinNumPlayers());

        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", Era.ERA_1, 1));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", Era.ERA_1, 6));
    }

    @Test
    void isAvailableForPlayerCount() {
        assertFalse(cardWithId.isAvailableForPlayerCount(2));
        assertTrue(cardWithId.isAvailableForPlayerCount(3));
        assertTrue(cardWithId.isAvailableForPlayerCount(5));
    }

    @Test
    void addTo() {
        PlayerDeck deck = new PlayerDeck();

        assertFalse(cardWithId.wasAdded());
        assertNull(cardWithId.getLastDeck());

        cardWithId.addTo(deck);

        assertTrue(cardWithId.wasAdded());
        assertSame(deck, cardWithId.getLastDeck());
    }

    @Test
    void canBeSelectedFrom() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertFalse(cardWithId.canBeSelectedFrom(row, player));
    }

    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> cardWithId.validateSelectableFrom(row));
    }

    @Test
    void resolveFromOfferRow() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertThrows(IllegalArgumentException.class, () -> cardWithId.resolveFromOfferRow(player, row));
    }

    @Test
    void constructorValidation() {
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, null, Era.ERA_1, 2));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", null, 2));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(null, Era.ERA_1, 2));
        assertThrows(IllegalArgumentException.class, () -> new TestCard("X", null, 2));
    }

    private static final class TestCard extends Card {

        private boolean added;
        private PlayerDeck lastDeck;

        private TestCard(int cardId, String name, Era era, int minNumPlayers) {
            super(cardId, name, era, minNumPlayers, "");
            this.added = false;
            this.lastDeck = null;
        }

        private TestCard(String name, Era era, int minNumPlayers) {
            super(1 ,name, era, minNumPlayers,"");
            this.added = false;
            this.lastDeck = null;
        }

        @Override
        public void addTo(PlayerDeck playerDeck) {
            this.added = true;
            this.lastDeck = playerDeck;
        }

        private boolean wasAdded() {
            return added;
        }

        private PlayerDeck getLastDeck() {
            return lastDeck;
        }
    }
}