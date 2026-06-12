package it.polimi.ingsw.common.network.dto.mapper;

import it.polimi.ingsw.server.model.card.CharacterType;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.deck.PlayerDeck;
import it.polimi.ingsw.common.network.dto.PlayerInfoDTO;
import it.polimi.ingsw.common.network.dto.PlayerStatsDTO;

import java.util.List;

public class PlayerInfosDTOMapper {
    private PlayerInfosDTOMapper() {
    }

    /**
     * Converts a Player object to a PlayerInfoDTO.
     *
     * @param player the Player object to be converted
     * @return a PlayerInfoDTO containing the player's information
     */
    public static PlayerInfoDTO toPlayerInfoDTO(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null.");
        }

        PlayerDeck deck = player.getPlayerDeck();
        PlayerStatsDTO stats = new PlayerStatsDTO(
                player.getPP(),
                player.getFood(),
                deck.getCharacters().size(),
                deck.getTotalGathererDiscount(),
                deck.getTotalShamanStars(),
                deck.getTotalBuilderDiscount(),
                deck.getDifferentInventorItem(),
                deck.countCharactersByType(CharacterType.GATHERER),
                deck.countCharactersByType(CharacterType.HUNTER),
                deck.countCharactersByType(CharacterType.BUILDER),
                deck.countCharactersByType(CharacterType.SHAMAN),
                deck.countCharactersByType(CharacterType.ARTIST),
                deck.countCharactersByType(CharacterType.INVENTOR)
        );

        String totem = player.getTotem() == null ? "" : player.getTotem().toString();

        return new PlayerInfoDTO(
                player.getNickname(),
                player.getFood(),
                player.getPP(),
                totem,
                PlayerDeckDTOMapper.toPlayerDeckDTO(player),
                stats
        );
    }



    /**
     * Converts a list of Player objects to a list of PlayerInfoDTO
     *
     * @param players the list of Player objects to be converted
     * @return a list of PlayerInfoDTO containing the information of each player
     */
    public static List<PlayerInfoDTO> toPlayerInfoDTOList(List<Player> players) {
        return players.stream()
                .map(PlayerInfosDTOMapper::toPlayerInfoDTO)
                .toList();
    }
}
