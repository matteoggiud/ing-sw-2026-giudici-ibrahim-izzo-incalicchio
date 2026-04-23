package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.gamelogic.ScoreCalculator;

import java.util.Map;

/**
 * The ComputeFinalScoreState class represents the state of the game where the final scores
 * are calculated and the final ranking of players is determined.
 */

public class ComputeFinalScoreState implements IGameState {

    /**
     * Transitions the game to the EndedState after computing the final scores.
     * @param game the current game context
     */
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndedState());
    }

    /**
     * Executes the phase of computing final scores and determining player rankings.
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

}
