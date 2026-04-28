package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.board.CardRow;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.server.model.effect.event.IEventEffect;
import it.polimi.ingsw.server.model.era.Era;
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

    @BeforeEach
    void setUp() {
        nextCardId = 1;
        effect = new DummyEventEffect();
        eventCard = new EventCard(nextCardId++, "EventA", Era.ERA_1, 2, false, effect, 3, 1, 2, 1, 4, "");
        finalEventCard = new EventCard(nextCardId++, "FinalEvent", Era.ERA_2, 3, true, effect, 0, 2, 1, 3, 5, "");
    }

    @Test
    void isFinalEvent() {
        assertFalse(eventCard.isFinalEvent());
        assertTrue(finalEventCard.isFinalEvent());
    }

    @Test
    void goesInUpperRow() {
        assertTrue(eventCard.goesInUpperRow());
        assertTrue(finalEventCard.goesInUpperRow());
    }

    @Test
    void getEventEffect() {
        assertSame(effect, eventCard.getEventEffect());
    }

    @Test
    void getGainedFood() {
        assertEquals(3, eventCard.getGainedFood());
        assertEquals(0, finalEventCard.getGainedFood());
    }

    @Test
    void getGainedPP() {
        assertEquals(2, eventCard.getGainedPP());
        assertEquals(1, finalEventCard.getGainedPP());
    }

    @Test
    void getMalusFood() {
        assertEquals(1, eventCard.getMalusFood());
        assertEquals(2, finalEventCard.getMalusFood());
    }

    @Test
    void getMalusPP() {
        assertEquals(1, eventCard.getMalusPP());
        assertEquals(3, finalEventCard.getMalusPP());
    }

    @Test
    void getThreshold() {
        assertEquals(4, eventCard.getThreshold());
        assertEquals(5, finalEventCard.getThreshold());
    }

    @Test
    void addTo() {
        PlayerDeck playerDeck = new PlayerDeck();

        assertThrows(UnsupportedOperationException.class, () -> eventCard.addTo(playerDeck));
    }

    @Test
    void addToEventList() {
        ArrayList<EventCard> eventCards = new ArrayList<>();

        eventCard.addToEventList(eventCards);

        assertEquals(1, eventCards.size());
        assertSame(eventCard, eventCards.get(0));
    }

    @Test
    void validateSelectableFrom() {
        CardRow row = new CardRow();

        assertThrows(IllegalArgumentException.class, () -> eventCard.validateSelectableFrom(row));
    }

    @Test
    void resolveFromOfferRow() {
        CardRow row = new CardRow();
        Player player = new Player("p1");

        assertThrows(IllegalArgumentException.class, () -> eventCard.resolveFromOfferRow(player, row));
    }

    @Test
    void constructorValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", Era.ERA_1, 2, false, null, 0, 0, 0, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", Era.ERA_1, 2, false, effect, -1, 0, 0, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", Era.ERA_1, 2, false, effect, 0, -1, 0, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", Era.ERA_1, 2, false, effect, 0, 0, -1, 0, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", Era.ERA_1, 2, false, effect, 0, 0, 0, -1, 0,""));

        assertThrows(IllegalArgumentException.class,
                () -> new EventCard(99, "E", Era.ERA_1, 2, false, effect, 0, 0, 0, 0, -1,""));
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