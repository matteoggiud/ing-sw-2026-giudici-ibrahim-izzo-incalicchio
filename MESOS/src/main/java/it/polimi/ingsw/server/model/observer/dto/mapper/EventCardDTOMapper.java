package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.card.EventCard;
import it.polimi.ingsw.server.model.observer.dto.EventCardDTO;

public class EventCardDTOMapper {
    private EventCardDTOMapper() {
    }

    public static EventCardDTO toEventCardDTO(EventCard eventCard) {
        if (eventCard == null) {
            throw new IllegalArgumentException("EventCard cannot be null");
        }

        return new EventCardDTO(
                eventCard.getCardId(),
                eventCard.getName(),
                eventCard.getEra().getName(),
                eventCard.getImageKey()
        );
    }
}

