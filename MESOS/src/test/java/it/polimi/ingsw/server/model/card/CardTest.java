package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    private TestCard cardWithId;
    private TestCard cardWithoutId;
    private Era1 era1;
    private Era2 era2;

    @BeforeEach
    void setUp() {
        era1 = new Era1();
        era2 = new Era2();
        cardWithId = new TestCard(10, "CardA", era1, 3);
        cardWithoutId = new TestCard("CardB", era2, 2);
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
        assertSame(era1, cardWithId.getEra());
        assertSame(era2, cardWithoutId.getEra());
    }

    @Test
    void getMinNumPlayers() {
        assertEquals(3, cardWithId.getMinNumPlayers());
        assertEquals(2, cardWithoutId.getMinNumPlayers());

        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", era1, 1));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", era1, 6));
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
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, null, era1, 2));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", null, 2));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(null, era1, 2));
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