package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;

public class PlayerInfoDTOMapper {
    private PlayerInfoDTOMapper() {
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
}
