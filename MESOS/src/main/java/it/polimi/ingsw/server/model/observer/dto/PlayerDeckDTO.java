package it.polimi.ingsw.server.model.observer.dto;

import it.polimi.ingsw.server.model.card.BuildingCard;
import it.polimi.ingsw.server.model.card.CharacterCard;
import it.polimi.ingsw.server.model.core.Player;

import java.util.List;

/**
 * DTO representing the visible personal deck of one player.
 */
public record PlayerDeckDTO(
        List<CharacterCardDTO> characterCards,
        List<BuildingCardDTO> buildingCards
) {

    public PlayerDeckDTO {
        characterCards = List.copyOf(characterCards);
        buildingCards = List.copyOf(buildingCards);
    }

    /**
     * Builds a player deck DTO from the current state of a player.
     *
     * @param player the player whose deck must be exported
     * @return a DTO representing the player's current deck
     */
    public static PlayerDeckDTO fromPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        return new PlayerDeckDTO(
                toCharacterDTOs(player.getPlayerDeck().getCharacters()),
                toBuildingDTOs(player.getPlayerDeck().getBuildings())
        );
    }

    private static List<CharacterCardDTO> toCharacterDTOs(List<CharacterCard> cards) {
        return cards.stream()
                .map(card -> new CharacterCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }

    private static List<BuildingCardDTO> toBuildingDTOs(List<BuildingCard> cards) {
        return cards.stream()
                .map(card -> new BuildingCardDTO(
                        card.getCardId(),
                        card.getName(),
                        card.getEra().name(),
                        ""
                ))
                .toList();
    }
}
