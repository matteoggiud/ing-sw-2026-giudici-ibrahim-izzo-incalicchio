package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.server.model.effect.event.IEventEffect;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.server.model.era.Era2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventCardTest {

    private int nextCardId;
    private IEventEffect effect;
    private EventCard eventCard;
    private EventCard finalEventCard;
    private Era1 era1;
    private Era2 era2;

    @BeforeEach
    void setUp() {
        nextCardId = 1;
        effect = new DummyEventEffect();
        era1 = new Era1();
        era2 = new Era2();
        eventCard = new EventCard(nextCardId++, "EventA", era1, 2, false, effect, 3, 1, 2, 1, 4, "");
        finalEventCard = new EventCard(nextCardId++, "FinalEvent", era2, 3, true, effect, 0, 2, 1, 3, 5, "");
    }

    /**
     * Tests checking whether an event card is the final event.
     * Validates that {@link EventCard#isFinalEvent()} returns the correct boolean value based on the constructor.
     */
    @Test
    void isFinalEvent() {
        assertFalse(eventCard.isFinalEvent());
        assertTrue(finalEventCard.isFinalEvent());
    }

    /**
     * Tests whether an event card belongs in the upper row of the board.
     * Validates that {@link EventCard#goesInUpperRow()} returns true for event cards.
     */
    @Test
    void goesInUpperRow() {
        assertTrue(eventCard.goesInUpperRow());
        assertTrue(finalEventCard.goesInUpperRow());
    }

    /**
     * Tests retrieving the event's effect.
     * Validates that {@link EventCard#getEventEffect()} returns the assigned effect.
     */
    @Test
    void getEventEffect() {
        assertSame(effect, eventCard.getEventEffect());
    }

    /**
     * Tests getting the food gained from the event.
     * Validates that {@link EventCard#getGainedFood()} returns the correct amount.
     */
    @Test
    void getGainedFood() {
        assertEquals(3, eventCard.getGainedFood());
        assertEquals(0, finalEventCard.getGainedFood());
    }

    /**
     * Tests getting the prestige points gained from the event.
     * Validates that {@link EventCard#getGainedPP()} returns the correct amount.
     */
    @Test
    void getGainedPP() {
        assertEquals(2, eventCard.getGainedPP());
        assertEquals(1, finalEventCard.getGainedPP());
    }

    /**
     * Tests getting the food penalty from the event.
     * Validates that {@link EventCard#getMalusFood()} returns the correct penalty amount.
     */
    @Test
    void getMalusFood() {
        assertEquals(1, eventCard.getMalusFood());
        assertEquals(2, finalEventCard.getMalusFood());
    }

    /**
     * Tests getting the prestige points penalty from the event.
     * Validates that {@link EventCard#getMalusPP()} returns the correct penalty amount.
     */
    @Test
    void getMalusPP() {
        assertEquals(1, eventCard.getMalusPP());
        assertEquals(3, finalEventCard.getMalusPP());
    }

    /**
     * Tests getting the threshold value required for the event's penalty.
     * Validates that {@link EventCard#getThreshold()} returns the correct threshold amount.
     */
    @Test
    void getThreshold() {
        assertEquals(4, eventCard.getThreshold());
        assertEquals(5, finalEventCard.getThreshold());
    }

    /**
     * Tests adding an event card to a player's deck.
     * Validates that {@link EventCard#addTo(PlayerDeck)} throws an UnsupportedOperationException since events aren't added to player decks.
     */
    @Test
    void addTo() {
        PlayerDeck playerDeck = new PlayerDeck();

        assertThrows(UnsupportedOperationException.class, () -> eventCard.addTo(playerDeck));
    }

    /**
     * Tests adding an event card to a list of events.
     * Validates that {@link EventCard#addToEventList(ArrayList)} successfully appends the card to the list.
     */
    @Test
    void addToEventList() {
        ArrayList<EventCard> eventCards = new ArrayList<>();

        eventCard.addToEventList(eventCards);

        assertEquals(1, eventCards.size());
        assertSame(eventCard, eventCards.get(0));
    }

    /**
     * Tests the validation for selecting an event card.
     * Validates that {@link EventCard#validateSelectableFrom(CardRow)} throws an exception because event cards cannot be directly selected.
     */
    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> eventCard.validateSelectableFrom(row));
    }

    /**
     * Tests resolving the acquisition of an event card from an offer row.
     * Validates that {@link EventCard#resolveFromOfferRow(Player, CardRow)} throws an exception because event cards are not resolved this way.
     */
    @Test
    void resolveFromOfferRow() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertThrows(IllegalArgumentException.class, () -> eventCard.resolveFromOfferRow(player, row));
    }

    /**
     * Tests constructor validation for invalid parameters.
     * Validates that {@link EventCard} construction throws an exception for negative values.
     */
    @Test
    void constructorValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", era1, 2, false, null, 0, 0, 0, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", era1, 2, false, effect, -1, 0, 0, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", era1, 2, false, effect, 0, -1, 0, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", era1, 2, false, effect, 0, 0, -1, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", era1, 2, false, effect, 0, 0, 0, -1, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", era1, 2, false, effect, 0, 0, 0, 0, -1,""));
    }

    private static final class DummyEventEffect implements IEventEffect {
        @Override
        public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
            return List.of();
        }

        @Override
        public void acceptModifier(IEventModifier eventModifier, EffectEventResult effectEventResult) {
            // no-op for tests
        }
    }
}