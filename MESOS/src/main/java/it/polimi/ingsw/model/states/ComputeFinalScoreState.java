package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.Game;
import it.polimi.ingsw.model.core.Player;
import it.polimi.ingsw.model.gamelogic.ScoreCalculator;

import java.util.Map;

public class ComputeFinalScoreState implements IGameState {

    @Override
    public void nextState(Game game) {
        game.setGameState(new EndedState());
    }

    @Override
    public Map<Player, Integer> getFinalRanking(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null.");
        }

        return ScoreCalculator.rankPlayer(game);
    }

}
