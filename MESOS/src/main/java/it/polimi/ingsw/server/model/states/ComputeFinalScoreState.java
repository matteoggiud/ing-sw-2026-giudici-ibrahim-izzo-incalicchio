package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.database.DataWriting;
import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;
import it.polimi.ingsw.server.model.gamelogic.ScoreCalculator;
import it.polimi.ingsw.server.model.observer.dto.GameSnapshotDTO;
import it.polimi.ingsw.server.model.observer.dto.mapper.GameSnapshotDTOMapper;
import it.polimi.ingsw.server.model.observer.event.GameEndedEvent;
import it.polimi.ingsw.server.database.DataDepository;
import it.polimi.ingsw.server.model.observer.dto.LeadBoardDTO;
import java.util.List;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The ComputeFinalScoreState class represents the state of the game where the final scores
 * are calculated and the final ranking of players is determined.
 */
public class ComputeFinalScoreState implements IGameState {

    private boolean finalRankingNotified = false;

    /**
     * Transitions the game to the EndedState after computing the final scores.
     *
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndedState(getFinalRanking(game)));
    }

    /**
     * Executes the final scoring phase.
     * The final ranking is computed and notified to observers only once.
     *
     * @param game the current game context
     */
    @Override
    public void executePhase(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        // Set the game ad finished
        new DataWriting().updateGameInProgress(game.getGameId(), false);

        if (finalRankingNotified) {
            return;
        }

        notifyGameEnded(game);
        finalRankingNotified = true;
        nextState(game);
    }

    /**
     * Returns the final ranking of the players.
     *
     * @param game the current game context
     * @return a map of players and their corresponding final scores, sorted by rank
     */
    @Override
    public Map<Player, Integer> getFinalRanking(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        return ScoreCalculator.rankPlayer(game);
    }

    /**
     * Notifies observers that the game ended and provides the final ranking.
     *
     * @param game the current game context
     */
    private void notifyGameEnded(Game game) {
        Map<String, Integer> rankingByNickname = new LinkedHashMap<>();
        DataWriting dw = new DataWriting();

        for (Map.Entry<Player, Integer> entry : getFinalRanking(game).entrySet()) {
            rankingByNickname.put(entry.getKey().getNickname(), entry.getValue());
        }

        dw.rankingsUpdate(game);
        DataDepository dataDepository = new DataDepository();
        List<LeadBoardDTO> historicalLeaderboard = dataDepository.getLeadBoard(game.getExpectedPlayerCount());
        game.getObserverManager().notifyObservers(
                new GameEndedEvent(game.getGameId(), rankingByNickname, game.getExpectedPlayerCount(), historicalLeaderboard)
        );
    }

    @Override
    public boolean isEnded() {
        return false;
    }
}
