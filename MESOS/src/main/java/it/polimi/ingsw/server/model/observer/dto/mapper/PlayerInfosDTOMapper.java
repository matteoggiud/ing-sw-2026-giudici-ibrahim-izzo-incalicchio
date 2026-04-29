package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;

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
        return new PlayerInfoDTO(
                player.getNickname(),
                player.getFood(),
                player.getPP(),
                player.getTotem().toString(),
                PlayerDeckDTOMapper.toPlayerDeckDTO(player)
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
