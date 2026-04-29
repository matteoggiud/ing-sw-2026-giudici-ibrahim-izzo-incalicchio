package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.dto.BuildingCardDTO;
import it.polimi.ingsw.server.model.observer.dto.CharacterCardDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerDeckDTO;

import java.util.List;

/**
 * Utility class responsible for mapping a player's personal deck into observer DTOs.
 */
public final class PlayerDeckDTOMapper {

    private PlayerDeckDTOMapper() {
    }

    /**
     * Maps the current personal deck of a player to a deck DTO.
     *
     * @param player the player whose deck must be exported
     * @return the DTO representing the player's personal deck
     */
    public static PlayerDeckDTO toPlayerDeckDTO(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        return new PlayerDeckDTO(
                toCharacterCardDTOs(player.getPlayerDeck().getCharacters()),
                toBuildingCardDTOs(player.getPlayerDeck().getBuildings())
        );
    }

    /**
     * Maps a list of character cards to their DTO representations.
     *
     * @param cards the character cards to map
     * @return the corresponding character card DTOs
     */
    private static List<CharacterCardDTO> toCharacterCardDTOs(List<CharacterCard> cards) {
        return cards.stream()
                .map(card -> new CharacterCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().getName(),
                        ""
                ))
                .toList();
    }

    /**
     * Maps a list of building cards to their DTO representations.
     *
     * @param cards the building cards to map
     * @return the corresponding building card DTOs
     */
    private static List<BuildingCardDTO> toBuildingCardDTOs(List<BuildingCard> cards) {
        return cards.stream()
                .map(card -> new BuildingCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().getName(),
                        ""
                ))
                .toList();
    }
}
