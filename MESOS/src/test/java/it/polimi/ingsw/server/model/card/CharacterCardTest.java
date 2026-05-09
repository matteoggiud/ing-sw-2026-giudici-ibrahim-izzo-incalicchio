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

    @Test
    void getBuilderFoodDiscount() {
        assertEquals(0, characterCard.getBuilderFoodDiscount());
    }

    @Test
    void getShamanStars() {
        assertEquals(0, characterCard.getShamanStars());
    }

    @Test
    void getInventorType() {
        assertEquals(Optional.empty(), characterCard.getInventorType());
    }

    @Test
    void getEndGamePPBonus() {
        assertEquals(0, characterCard.getEndGamePPBonus());
    }

    @Test
    void hasFoodIcon() {
        assertFalse(characterCard.hasFoodIcon());
    }

    @Test
    void onAddedToPlayer() {
        Player player = new Player("p1");

        assertEquals(0, player.getFood());
        assertFalse(characterCard.wasAddedToPlayer());

        characterCard.onAddedToPlayer(player);

        assertTrue(characterCard.wasAddedToPlayer());
        assertEquals(2, player.getFood());
    }

    @Test
    void addTo() {
        PlayerDeck deck = new PlayerDeck();

        characterCard.addTo(deck);

        assertEquals(1, deck.getCharacters().size());
        assertSame(characterCard, deck.getCharacters().get(0));
        assertEquals(1, characterCard.getOnAddToPlayerDeckCalls());
        assertSame(deck, characterCard.getLastPlayerDeck());
    }

    @Test
    void addToCharacterList() {
        ArrayList<CharacterCard> characterList = new ArrayList<>();

        characterCard.addToCharacterList(characterList);

        assertEquals(1, characterList.size());
        assertSame(characterCard, characterList.get(0));
    }

    @Test
    void canBeSelectedFrom() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertFalse(characterCard.canBeSelectedFrom(row, player));

        row.addCard(characterCard);
        assertTrue(characterCard.canBeSelectedFrom(row, player));
    }

    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> characterCard.validateSelectableFrom(row));

        row.addCard(characterCard);
        assertDoesNotThrow(() -> characterCard.validateSelectableFrom(row));
    }

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