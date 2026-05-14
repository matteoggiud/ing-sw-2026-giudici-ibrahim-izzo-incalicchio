package it.polimi.ingsw.server.model.observer.dto.mapper;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.observer.dto.BoardDTO;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.model.observer.dto.PlayerInfoDTO;

import java.util.List;

/**
 * Mapper class to convert a Game object into a GameSnapshotDTO, which contains all the necessary information to reconstruct the game state.
 */
public class GameSnapshotDTOMapper {
    public static GameSnapshotDTO toGameSnapshotDTO(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game is null");
        }
        int gameId = game.getGameId();
        int expectedPlayerCount = game.getExpectedPlayerCount();
        boolean lastRoundTriggered = game.isLastRoundTriggered();
        String currentStateName = game.getGameState().getClass().getSimpleName();

        List<PlayerInfoDTO> players = PlayerInfosDTOMapper.toPlayerInfoDTOList(game.getPlayers().getPlayers());
        BoardDTO boardSnapshot = BoardDTOMapper.toBoardDTO(game);
        List<Integer> remainingTribeCardIds = game.getTribeDeck().getTribeDeck().stream().map(card -> card.getCardId()).toList();
        List<Integer> remainingEra1BuildingIds = game.getBuildingDecks().getBuildingDecksEra1().getBuildingCards().stream().map(card -> card.getCardId()).toList();
        List<Integer> remainingEra2BuildingIds = game.getBuildingDecks().getBuildingDecksEra2().getBuildingCards().stream().map(card -> card.getCardId()).toList();
        List<Integer> remainingEra3BuildingIds = game.getBuildingDecks().getBuildingDecksEra3().getBuildingCards().stream().map(card -> card.getCardId()).toList();

        return new GameSnapshotDTO(gameId, expectedPlayerCount, lastRoundTriggered, currentStateName, players, boardSnapshot, remainingTribeCardIds, remainingEra1BuildingIds, remainingEra2BuildingIds, remainingEra3BuildingIds);

    }
}
