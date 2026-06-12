package it.polimi.ingsw.common.network.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing the visible board state of a game.
 */
public record BoardDTO(
        String currentEra,
        List<CharacterCardDTO> upperCharacterCards,
        List<EventCardDTO> upperEventCards,
        List<BuildingCardDTO> upperBuildingCards,
        List<CharacterCardDTO> lowerCharacterCards,
        List<EventCardDTO> lowerEventCards,
        List<BuildingCardDTO> lowerBuildingCards,
        List<OfferBoxDTO> offerTrackBoxes,
        List<TurnOrderSlotDTO> turnOrderSlots
) implements Serializable {

    public BoardDTO {
        if (currentEra == null || currentEra.isBlank()) {
            throw new IllegalArgumentException("Current era cannot be null or blank.");
        }

        upperCharacterCards = List.copyOf(upperCharacterCards);
        upperEventCards = List.copyOf(upperEventCards);
        upperBuildingCards = List.copyOf(upperBuildingCards);
        lowerCharacterCards = List.copyOf(lowerCharacterCards);
        lowerEventCards = List.copyOf(lowerEventCards);
        lowerBuildingCards = List.copyOf(lowerBuildingCards);
        offerTrackBoxes = List.copyOf(offerTrackBoxes);
        turnOrderSlots = List.copyOf(turnOrderSlots);
    }
}
