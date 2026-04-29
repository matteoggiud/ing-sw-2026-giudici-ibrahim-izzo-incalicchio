package it.polimi.ingsw.server.model.card;

import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.server.model.era.Era;
import it.polimi.ingsw.server.model.era.Era1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TribeCardTest {

    private int nextCardId;
    private TestTribeCard tribeCard;
    private Era1 era1;

    @BeforeEach
    void setUp() {
        nextCardId = 1;
        era1 = new Era1();
        tribeCard = new TestTribeCard(nextCardId++, "TribeA", era1, 2);
    }

    @Test
    void isFinalEvent() {
        assertFalse(tribeCard.isFinalEvent());
    }

    @Test
    void goesInUpperRow() {
        assertFalse(tribeCard.goesInUpperRow());
    }

    @Test
    void addToCharacterList() {
        ArrayList<CharacterCard> characters = new ArrayList<>();
        characters.add(new TestCharacterCard(nextCardId++, "CharacterX", era1, 2));

        tribeCard.addToCharacterList(characters);

        assertEquals(1, characters.size());
        assertEquals("CharacterX", characters.get(0).getName());
    }

    @Test
    void addToEventList() {
        ArrayList<EventCard> events = new ArrayList<>();
        events.add(new EventCard(nextCardId++, "EventX", era1, 2, false, new DummyEventEffect(), 1, 0, 1, 0, 1,""));

        tribeCard.addToEventList(events);

        assertEquals(1, events.size());
        assertEquals("EventX", events.get(0).getName());
    }

    private static final class TestTribeCard extends TribeCard {

        private TestTribeCard(int cardId, String name, Era era, int minNumPlayers) {
            super(cardId, name, era, minNumPlayers,"");
        }

        @Override
        public void addTo(PlayerDeck playerDeck) {
            // no-op for default TribeCard tests
        }
    }

    private static final class TestCharacterCard extends CharacterCard {

        private TestCharacterCard(int cardId, String name, Era era, int minNumPlayers) {
            super(cardId, name, era, minNumPlayers,"");
        }

        @Override
        public void onAddToPlayerDeck(PlayerDeck playerDeck) {
            // no-op for list setup in TribeCard tests
        }
    }

    private static final class DummyEventEffect implements it.polimi.ingsw.server.model.effect.event.IEventEffect {
        @Override
        public java.util.List<it.polimi.ingsw.server.model.effect.EffectEventResult> calculateBaseEffect(
                it.polimi.ingsw.server.model.core.Game game,
                EventCard eventCard
        ) {
            return java.util.List.of();
        }

        @Override
        public void acceptModifier(
                it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier eventModifier,
                it.polimi.ingsw.server.model.effect.EffectEventResult effectEventResult
        ) {
            // no-op for list setup in TribeCard tests
        }
    }
}