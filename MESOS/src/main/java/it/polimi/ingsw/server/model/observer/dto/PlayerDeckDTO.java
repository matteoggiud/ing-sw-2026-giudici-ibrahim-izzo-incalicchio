package it.polimi.ingsw.server.model.observer.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO representing the visible personal deck of one player.
 */
public record PlayerDeckDTO(
        List<CharacterCardDTO> characterCards,
        List<BuildingCardDTO> buildingCards
) implements Serializable {


    public PlayerDeckDTO {
        characterCards = List.copyOf(characterCards);
        buildingCards = List.copyOf(buildingCards);
    }
}
