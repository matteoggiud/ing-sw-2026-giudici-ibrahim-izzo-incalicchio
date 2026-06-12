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

    /**
     * Tests getting the ID of a card.
     * Validates that {@link Card#getCardId()} returns the correct ID, or -1 if constructed without one.
     */
    @Test
    void getCardId() {
        assertEquals(10, cardWithId.getCardId());
        assertEquals(-1, cardWithoutId.getCardId());
    }

    /**
     * Tests getting the name of a card.
     * Validates that {@link Card#getName()} returns the correct string.
     */
    @Test
    void getName() {
        assertEquals("CardA", cardWithId.getName());
        assertEquals("CardB", cardWithoutId.getName());
    }

    /**
     * Tests getting the era of a card.
     * Validates that {@link Card#getEra()} returns the correct era object.
     */
    @Test
    void getEra() {
        assertSame(era1, cardWithId.getEra());
        assertSame(era2, cardWithoutId.getEra());
    }

    /**
     * Tests getting the minimum number of players required for a card.
     * Validates that {@link Card#getMinNumPlayers()} returns the correct count and throws for invalid counts.
     */
    @Test
    void getMinNumPlayers() {
        assertEquals(3, cardWithId.getMinNumPlayers());
        assertEquals(2, cardWithoutId.getMinNumPlayers());

        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", era1, 1));
        assertThrows(IllegalArgumentException.class, () -> new TestCard(1, "X", era1, 6));
    }

    /**
     * Tests checking if a card is available for a given player count.
     * Validates that {@link Card#isAvailableForPlayerCount(int)} returns true if the player count meets or exceeds the minimum.
     */
    @Test
    void isAvailableForPlayerCount() {
        assertFalse(cardWithId.isAvailableForPlayerCount(2));
        assertTrue(cardWithId.isAvailableForPlayerCount(3));
        assertTrue(cardWithId.isAvailableForPlayerCount(5));
    }

    /**
     * Tests adding a card to a player deck.
     * Validates that {@link Card#addTo(PlayerDeck)} invokes the appropriate subclass implementation.
     */
    @Test
    void addTo() {
        PlayerDeck deck = new PlayerDeck();

        assertFalse(cardWithId.wasAdded());
        assertNull(cardWithId.getLastDeck());

        cardWithId.addTo(deck);

        assertTrue(cardWithId.wasAdded());
        assertSame(deck, cardWithId.getLastDeck());
    }

    /**
     * Tests whether a generic card can be selected from a card row.
     * Validates that {@link Card#canBeSelectedFrom(CardRow, Player)} returns false by default for generic cards.
     */
    @Test
    void canBeSelectedFrom() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertFalse(cardWithId.canBeSelectedFrom(row, player));
    }

    /**
     * Tests validation logic for selecting a generic card.
     * Validates that {@link Card#validateSelectableFrom(CardRow)} throws an exception.
     */
    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> cardWithId.validateSelectableFrom(row));
    }

    /**
     * Tests resolving the acquisition of a generic card from the offer row.
     * Validates that {@link Card#resolveFromOfferRow(Player, CardRow)} throws an exception by default.
     */
    @Test
    void resolveFromOfferRow() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertThrows(IllegalArgumentException.class, () -> cardWithId.resolveFromOfferRow(player, row));
    }

    /**
     * Tests constructor parameter validation for the Card class.
     * Validates that constructing a Card with null name or null era throws an exception.
     */
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
            super(-1, name, era, minNumPlayers, "");
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
