package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.common.network.dto.mapper.EventCardDTOMapper;
import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.effect.EffectEventResult;
import it.polimi.ingsw.server.model.effect.building.modifiers.IEventModifier;
import it.polimi.ingsw.server.model.effect.event.IEventEffect;
import it.polimi.ingsw.server.model.era.Era1;
import it.polimi.ingsw.common.network.dto.EventCardDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventCardDTOMapperTest {

    /**
     * Tests mapping a populated {@link EventCard} to an {@link EventCardDTO}.
     * Verifies that the base properties such as ID, name, era, and image key are translated properly.
     */
    @Test
    void toEventCardDTO_mapsFieldsCorrectly() {
        // Arrange
        Era1 era1 = new Era1();
        IEventEffect effect = new DummyEventEffect();
        EventCard eventCard = new EventCard(
                1,
                "EventA",
                era1,
                2,
                false,
                effect,
                3,
                1,
                2,
                1,
                4,
                "event_image"
        );

        // Act
        EventCardDTO dto = EventCardDTOMapper.toEventCardDTO(eventCard);

        // Assert
        assertEquals(1, dto.cardId());
        assertEquals("EventA", dto.name());
        assertEquals("Era I", dto.era());
        assertEquals("event_image", dto.imageKey());
    }

    /**
     * Tests the error handling of {@link EventCardDTOMapper#toEventCardDTO(EventCard)}.
     * Expects an {@link IllegalArgumentException} when the provided event card is null.
     */
    @Test
    void toEventCardDTO_nullEventCard_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> EventCardDTOMapper.toEventCardDTO(null));
    }

    private static final class DummyEventEffect implements IEventEffect {
        @Override
        public List<EffectEventResult> calculateBaseEffect(Game game, EventCard eventCard) {
            return List.of();
        }

        @Override
        public void acceptModifier(IEventModifier eventModifier, EffectEventResult effectEventResult) {
            // No-op for tests
        }
    }
}
