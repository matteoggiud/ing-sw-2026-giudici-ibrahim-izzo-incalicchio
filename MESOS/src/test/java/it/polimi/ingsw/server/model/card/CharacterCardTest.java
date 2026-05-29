package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {

    private TestCharacterCard characterCard;
    private int nextCardId;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        nextCardId = 1;
        era1 = new Era1();
        characterCard = new TestCharacterCard(nextCardId++, "CharacterA", era1, 2);
    }

    /**
     * Tests the default builder food discount for a standard CharacterCard.
     * Validates that {@link CharacterCard#getBuilderFoodDiscount()} returns 0.
     */
    @Test
    void getBuilderFoodDiscount() {
        assertEquals(0, characterCard.getBuilderFoodDiscount());
    }

    /**
     * Tests the default shaman stars value for a standard CharacterCard.
     * Validates that {@link CharacterCard#getShamanStars()} returns 0.
     */
    @Test
    void getShamanStars() {
        assertEquals(0, characterCard.getShamanStars());
    }

    /**
     * Tests the default inventor type for a standard CharacterCard.
     * Validates that {@link CharacterCard#getInventorType()} returns empty.
     */
    @Test
    void getInventorType() {
        assertEquals(Optional.empty(), characterCard.getInventorType());
    }

    /**
     * Tests the default end game prestige points bonus for a standard CharacterCard.
     * Validates that {@link CharacterCard#getEndGamePPBonus()} returns 0.
     */
    @Test
    void getEndGamePPBonus() {
        assertEquals(0, characterCard.getEndGamePPBonus());
    }

    /**
     * Tests whether a standard CharacterCard has a food icon.
     * Validates that {@link CharacterCard#hasFoodIcon()} returns false by default.
     */
    @Test
    void hasFoodIcon() {
        assertFalse(characterCard.hasFoodIcon());
    }

    /**
     * Tests the behavior when a character card is added to a player.
     * Validates that {@link CharacterCard#onAddedToPlayer(Player)} correctly gives food to the player.
     */
    @Test
    void onAddedToPlayer() {
        Player player = new Player("p1");

        assertEquals(0, player.getFood());
        assertFalse(characterCard.wasAddedToPlayer());

        characterCard.onAddedToPlayer(player);

        assertTrue(characterCard.wasAddedToPlayer());
        assertEquals(2, player.getFood());
    }

    /**
     * Tests adding a character card to a player's deck.
     * Validates that {@link CharacterCard#addTo(PlayerDeck)} correctly places it in the character list.
     */
    @Test
    void addTo() {
        PlayerDeck deck = new PlayerDeck();

        characterCard.addTo(deck);

        assertEquals(1, deck.getCharacters().size());
        assertSame(characterCard, deck.getCharacters().get(0));
        assertEquals(1, characterCard.getOnAddToPlayerDeckCalls());
        assertSame(deck, characterCard.getLastPlayerDeck());
    }

    /**
     * Tests adding a character card directly to a character list.
     * Validates that {@link CharacterCard#addToCharacterList(ArrayList)} works properly.
     */
    @Test
    void addToCharacterList() {
        ArrayList<CharacterCard> characterList = new ArrayList<>();

        characterCard.addToCharacterList(characterList);

        assertEquals(1, characterList.size());
        assertSame(characterCard, characterList.get(0));
    }

    /**
     * Tests if a character card can be selected from a given card row.
     * Validates that {@link CharacterCard#canBeSelectedFrom(CardRow, Player)} checks the presence in the row.
     */
    @Test
    void canBeSelectedFrom() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertFalse(characterCard.canBeSelectedFrom(row, player));

        row.addCard(characterCard);
        assertTrue(characterCard.canBeSelectedFrom(row, player));
    }

    /**
     * Tests validation logic for selecting a character card.
     * Validates that {@link CharacterCard#validateSelectableFrom(CardRow)} throws an exception if not present.
     */
    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> characterCard.validateSelectableFrom(row));

        row.addCard(characterCard);
        assertDoesNotThrow(() -> characterCard.validateSelectableFrom(row));
    }

    /**
     * Tests resolving the acquisition of a character card from the offer row.
     * Validates that it is removed from the row and added to the player's deck, resolving its effects.
     */
    @Test
    void resolveFromOfferRow() {
        CardRow row = new CardRow();
        Player player = new Player("p1");
        row.addCard(characterCard);

        characterCard.resolveFromOfferRow(player, row);

        assertFalse(row.getTribeRow().getCharacterCards().contains(characterCard));
        assertEquals(1, player.getPlayerDeck().getCharacters().size());
        assertSame(characterCard, player.getPlayerDeck().getCharacters().get(0));
        assertTrue(characterCard.wasAddedToPlayer());
        assertEquals(2, player.getFood());
        assertEquals(1, characterCard.getOnAddToPlayerDeckCalls());
    }

    /**
     * Tests the callback method invoked when the card is added to a player deck.
     * Validates that {@link CharacterCard#onAddToPlayerDeck(PlayerDeck)} gets called with the deck.
     */
    @Test
    void onAddToPlayerDeck() {
        PlayerDeck deck = new PlayerDeck();

        assertEquals(0, characterCard.getOnAddToPlayerDeckCalls());
        assertNull(characterCard.getLastPlayerDeck());

        characterCard.onAddToPlayerDeck(deck);

        assertEquals(1, characterCard.getOnAddToPlayerDeckCalls());
        assertSame(deck, characterCard.getLastPlayerDeck());
    }

    private static final class TestCharacterCard extends CharacterCard {

        private boolean addedToPlayer;
        private int onAddToPlayerDeckCalls;
        private PlayerDeck lastPlayerDeck;

        private TestCharacterCard(int cardId, String name, Era era, int minNumPlayers) {
            super(cardId, name, era, minNumPlayers,"");
            this.addedToPlayer = false;
            this.onAddToPlayerDeckCalls = 0;
            this.lastPlayerDeck = null;
        }

        @Override
        public void onAddedToPlayer(Player player) {
            this.addedToPlayer = true;
            player.addFood(2);
        }

        @Override
        public void onAddToPlayerDeck(PlayerDeck playerDeck) {
            this.onAddToPlayerDeckCalls++;
            this.lastPlayerDeck = playerDeck;
        }

        private boolean wasAddedToPlayer() {
            return addedToPlayer;
        }

        private int getOnAddToPlayerDeckCalls() {
            return onAddToPlayerDeckCalls;
        }

        private PlayerDeck getLastPlayerDeck() {
            return lastPlayerDeck;
        }
    }
}