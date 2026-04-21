package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.core.*;
import it.polimi.ingsw.model.gamelogic.ScoreCalculator;

import java.util.Map;

public class ComputeFinalScoreState implements IGameState {
    @Override
    public void nextState(Game game) {
        game.setGameState(new EndedState());
    }

    @Override
    public void executePhase(Game game) {
        Map<String, Integer> finalRanking = ScoreCalculator.getFinalRanking(game);
    }
}
