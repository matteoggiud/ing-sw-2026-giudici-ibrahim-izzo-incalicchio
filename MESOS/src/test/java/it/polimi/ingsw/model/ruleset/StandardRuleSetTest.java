package it.polimi.ingsw.model.ruleset;

import it.polimi.ingsw.model.board.OFFER_BOX_ID;
import it.polimi.ingsw.model.board.OfferBox;
import it.polimi.ingsw.model.board.TurnOrderSlot;
import it.polimi.ingsw.model.card.Era;
import it.polimi.ingsw.model.card.EventCard;
import it.polimi.ingsw.model.card.EventType;
import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.effect.EffectEventResult;
import it.polimi.ingsw.model.effect.event.IEventEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class StandardRuleSetTest {

    private StandardRuleSet ruleSet;

    @BeforeEach
    void setUp() {
        ruleSet = new StandardRuleSet();
    }

    @Test
    void getUpperRowSize() {
        // valid values
        assertEquals(6, ruleSet.getUpperRowSize(2));
        assertEquals(7, ruleSet.getUpperRowSize(3));
        assertEquals(8, ruleSet.getUpperRowSize(4));
        assertEquals(9, ruleSet.getUpperRowSize(5));

        // not valid values
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(1));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(6));
    }

    @Test
    void getLowerRowSize() {
        // valid values
        assertEquals(3, ruleSet.getLowerRowSize(2));
        assertEquals(4, ruleSet.getLowerRowSize(3));
        assertEquals(5, ruleSet.getLowerRowSize(4));
        assertEquals(6, ruleSet.getLowerRowSize(5));

        // not valid values
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getLowerRowSize(1));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getLowerRowSize(6));
    }

    @Test
    void getInitialFood() {
        // valid values
        assertEquals(2, ruleSet.getInitialFood(0));
        assertEquals(3, ruleSet.getInitialFood(1));
        assertEquals(3, ruleSet.getInitialFood(2));
        assertEquals(4, ruleSet.getInitialFood(3));
        assertEquals(4, ruleSet.getInitialFood(4));

        // not valid values
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getInitialFood(-1));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getInitialFood(5));
    }

    @Test
    void createTurnOrderSlots() {
        // Test 2 players
        List<TurnOrderSlot> slots2 = ruleSet.createTurnOrderSlots(2);
        assertEquals(2, slots2.size());
        assertEquals(1, slots2.get(0).getFoodBonus());
        assertEquals(-1, slots2.get(1).getFoodBonus());

        // Test 3 players
        List<TurnOrderSlot> slots3 = ruleSet.createTurnOrderSlots(3);
        assertEquals(3, slots3.size());
        assertEquals(2, slots3.get(0).getFoodBonus());
        assertEquals(0, slots3.get(1).getFoodBonus());
        assertEquals(-1, slots3.get(2).getFoodBonus());

        // Test 4 players
        List<TurnOrderSlot> slots4 = ruleSet.createTurnOrderSlots(4);
        assertEquals(4, slots4.size());
        assertEquals(2, slots4.get(0).getFoodBonus());
        assertEquals(1, slots4.get(1).getFoodBonus());
        assertEquals(0, slots4.get(2).getFoodBonus());
        assertEquals(-1, slots4.get(3).getFoodBonus());

        // Test 5 players
        List<TurnOrderSlot> slots5 = ruleSet.createTurnOrderSlots(5);
        assertEquals(5, slots5.size());
        assertEquals(3, slots5.get(0).getFoodBonus());
        assertEquals(1, slots5.get(1).getFoodBonus());
        assertEquals(0, slots5.get(2).getFoodBonus());
        assertEquals(0, slots5.get(3).getFoodBonus());
        assertEquals(-1, slots5.get(4).getFoodBonus());

        // not valid values
        assertThrows(IllegalArgumentException.class, () -> ruleSet.createTurnOrderSlots(1));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.createTurnOrderSlots(6));
    }

    @Test
    void getBuildingLimit() {
        // Test numPlayers == 2
        assertEquals(1, ruleSet.getBuildingLimit(Era.ERA_1, 2));
        assertEquals(2, ruleSet.getBuildingLimit(Era.ERA_2, 2));
        assertEquals(3, ruleSet.getBuildingLimit(Era.ERA_3, 2));

        // Test numPlayers == 3
        assertEquals(2, ruleSet.getBuildingLimit(Era.ERA_1, 3));
        assertEquals(2, ruleSet.getBuildingLimit(Era.ERA_2, 3));
        assertEquals(4, ruleSet.getBuildingLimit(Era.ERA_3, 3));

        // Test numPlayers == 4
        assertEquals(2, ruleSet.getBuildingLimit(Era.ERA_1, 4));
        assertEquals(3, ruleSet.getBuildingLimit(Era.ERA_2, 4));
        assertEquals(4, ruleSet.getBuildingLimit(Era.ERA_3, 4));

        // Test numPlayers == 5
        assertEquals(2, ruleSet.getBuildingLimit(Era.ERA_1, 5));
        assertEquals(3, ruleSet.getBuildingLimit(Era.ERA_2, 5));
        assertEquals(5, ruleSet.getBuildingLimit(Era.ERA_3, 5));

        // Test era null
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getBuildingLimit(null, 2));

        // Test numPlayers not valid
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getBuildingLimit(Era.ERA_1, 1));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getBuildingLimit(Era.ERA_1, 6));
    }

    @Test
    void sortEventCards() {
        // Create effect dummy
        IEventEffect dummyEffect = new IEventEffect() {
            @Override
            public EventType getEventEffectType() {
                return EventType.SUSTENANCE;
            }

            @Override
            public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
                return List.of();
            }
        };

        // Create cards
        EventCard sustenance1 = new EventCard("Sustenance1", Era.ERA_1, 2, false, EventType.SUSTENANCE, dummyEffect, 0, 0, 0, 0, 0);
        EventCard sustenance2 = new EventCard("Sustenance2", Era.ERA_1, 2, false, EventType.SUSTENANCE, dummyEffect, 0, 0, 0, 0, 0);
        EventCard other1 = new EventCard("Hunt", Era.ERA_1, 2, false, EventType.HUNT, dummyEffect, 0, 0, 0, 0, 0);
        EventCard other2 = new EventCard("Ritual", Era.ERA_1, 2, false, EventType.SHAMANIC_RITUAL, dummyEffect, 0, 0, 0, 0, 0);

        List<EventCard> eventCards = List.of(sustenance1, other1, sustenance2, other2);

        Stack<EventCard> stack = ruleSet.sortEventCards(eventCards);


        assertEquals(EventType.HUNT, stack.pop().getEventType());
        assertEquals(EventType.SHAMANIC_RITUAL, stack.pop().getEventType());
        assertEquals(EventType.SUSTENANCE, stack.pop().getEventType());
        assertEquals(EventType.SUSTENANCE, stack.pop().getEventType());

        // Test null
        assertThrows(IllegalArgumentException.class, () -> ruleSet.sortEventCards(null));
    }

    @Test
    void createOfferBoxes() {
        // Test numPlayers == 2
        List<OfferBox> boxes2 = ruleSet.createOfferBoxes(2);
        assertEquals(4, boxes2.size()); // B, C, E, F (no A, no D, no G)
        assertEquals(OFFER_BOX_ID.B, boxes2.get(0).getBoxId());
        assertEquals(OFFER_BOX_ID.C, boxes2.get(1).getBoxId());
        assertEquals(OFFER_BOX_ID.E, boxes2.get(2).getBoxId());
        assertEquals(OFFER_BOX_ID.F, boxes2.get(3).getBoxId());

        // Test numPlayers == 3
        List<OfferBox> boxes3 = ruleSet.createOfferBoxes(3);
        assertEquals(5, boxes3.size()); // B, C, D, E, F (no A, no G)
        assertEquals(OFFER_BOX_ID.B, boxes3.get(0).getBoxId());
        assertEquals(OFFER_BOX_ID.C, boxes3.get(1).getBoxId());
        assertEquals(OFFER_BOX_ID.D, boxes3.get(2).getBoxId());
        assertEquals(OFFER_BOX_ID.E, boxes3.get(3).getBoxId());
        assertEquals(OFFER_BOX_ID.F, boxes3.get(4).getBoxId());

        // Test numPlayers == 4
        List<OfferBox> boxes4 = ruleSet.createOfferBoxes(4);
        assertEquals(6, boxes4.size()); // B, C, D, E, F, G (no A)
        assertEquals(OFFER_BOX_ID.B, boxes4.get(0).getBoxId());
        assertEquals(OFFER_BOX_ID.C, boxes4.get(1).getBoxId());
        assertEquals(OFFER_BOX_ID.D, boxes4.get(2).getBoxId());
        assertEquals(OFFER_BOX_ID.E, boxes4.get(3).getBoxId());
        assertEquals(OFFER_BOX_ID.F, boxes4.get(4).getBoxId());
        assertEquals(OFFER_BOX_ID.G, boxes4.get(5).getBoxId());

        // Test numPlayers == 5
        List<OfferBox> boxes5 = ruleSet.createOfferBoxes(5);
        assertEquals(7, boxes5.size()); // A, B, C, D, E, F, G
        assertEquals(OFFER_BOX_ID.A, boxes5.get(0).getBoxId());
        assertEquals(OFFER_BOX_ID.B, boxes5.get(1).getBoxId());
        assertEquals(OFFER_BOX_ID.C, boxes5.get(2).getBoxId());
        assertEquals(OFFER_BOX_ID.D, boxes5.get(3).getBoxId());
        assertEquals(OFFER_BOX_ID.E, boxes5.get(4).getBoxId());
        assertEquals(OFFER_BOX_ID.F, boxes5.get(5).getBoxId());
        assertEquals(OFFER_BOX_ID.G, boxes5.get(6).getBoxId());

        // Test numPlayers not valid
        assertThrows(IllegalArgumentException.class, () -> ruleSet.createOfferBoxes(1));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.createOfferBoxes(6));
    }


    @Test
    void validatePlayerCount_BoundsTesting() {
        // Test min (2)
        assertDoesNotThrow(() -> ruleSet.getUpperRowSize(2));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(1));

        // Test max (5)
        assertDoesNotThrow(() -> ruleSet.getUpperRowSize(5));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(6));

        // Test extreme values
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(0));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(-10));
        assertThrows(IllegalArgumentException.class, () -> ruleSet.getUpperRowSize(100));

        // test good value
        assertDoesNotThrow(() -> ruleSet.getUpperRowSize(3));
    }
}