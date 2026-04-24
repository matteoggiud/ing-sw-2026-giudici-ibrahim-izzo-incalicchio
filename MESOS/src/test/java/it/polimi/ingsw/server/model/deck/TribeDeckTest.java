package it.polimi.ingsw.server.model.deck;

import it.polimi.ingsw.server.model.card.Era;
import it.polimi.ingsw.server.model.card.TribeCard;
import it.polimi.ingsw.server.model.card.characters.HunterCharacterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TribeDeckTest {

    private TribeDeck tribeDeck;
    private int nextCardId;

    @BeforeEach
    void setUp() {
        tribeDeck = new TribeDeck();
        nextCardId = 1;
    }

    @Test
    void drawCard() {
        TribeCard first = newHunter();
        TribeCard second = newHunter();

        tribeDeck.addTribesCards(List.of(first, second));

        assertSame(first, tribeDeck.drawCard());
        assertSame(second, tribeDeck.drawCard());
        assertThrows(IllegalStateException.class, () -> tribeDeck.drawCard());
    }

    @Test
    void addTribesCards() {
        TribeCard first = newHunter();
        TribeCard second = newHunter();

        assertTrue(tribeDeck.isEmpty());

        tribeDeck.addTribesCards(List.of(first, second));

        assertFalse(tribeDeck.isEmpty());
        assertSame(first, tribeDeck.drawCard());
        assertSame(second, tribeDeck.drawCard());
    }

    @Test
    void setTribeDeck() {
        TribeCard oldCard = newHunter();
        tribeDeck.addTribesCards(List.of(oldCard));

        TribeCard newFirst = newHunter();
        TribeCard newSecond = newHunter();

        tribeDeck.setTribeDeck(List.of(newFirst, newSecond));

        assertSame(newFirst, tribeDeck.drawCard());
        assertSame(newSecond, tribeDeck.drawCard());
        assertThrows(IllegalStateException.class, () -> tribeDeck.drawCard());

        assertThrows(IllegalArgumentException.class, () -> tribeDeck.setTribeDeck(null));
        assertThrows(IllegalArgumentException.class, () -> tribeDeck.setTribeDeck(List.of()));
    }

    @Test
    void addCardToTop() {
        TribeCard first = newHunter();
        TribeCard top = newHunter();

        tribeDeck.addTribesCards(List.of(first));
        tribeDeck.addCardToTop(top);

        assertSame(top, tribeDeck.drawCard());
        assertSame(first, tribeDeck.drawCard());
    }

    @Test
    void isEmpty() {
        assertTrue(tribeDeck.isEmpty());

        tribeDeck.addCardToTop(newHunter());
        assertFalse(tribeDeck.isEmpty());

        tribeDeck.drawCard();
        assertTrue(tribeDeck.isEmpty());
    }

    private HunterCharacterCard newHunter() {
        return new HunterCharacterCard(nextCardId++, "Hunter" + nextCardId, Era.ERA_1, 2, false);
    }
}