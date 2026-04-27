package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.observer.dto.GameInfoDTO;

import java.util.List;

public final class GameInfoDTOMapper {
    private GameInfoDTOMapper() {
    }

    /**
     * Maps a game to a GameInfoDTO, containing only the game ID and the number of players.
     *
     * @param game the game to map
     * @return the corresponding GameInfoDTO
     */
    public static GameInfoDTO toGameInfoDTO(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game is null");
        }

        return new GameInfoDTO(game.getGameId(), game.getNumberOfPlayers(), game.getPlayers().getPlayerNicknames());
    }

    public static List<GameInfoDTO> toGameInfoDTOs(List<Game> games) {
        if (games == null || games.contains(null)) {
            throw new IllegalArgumentException("Games list is/contains null");
        }

        return games.stream()
                .map(GameInfoDTOMapper::toGameInfoDTO)
                .toList();
    }
}
