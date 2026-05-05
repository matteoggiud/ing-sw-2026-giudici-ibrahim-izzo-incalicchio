package it.polimi.ingsw.server.model.states;

import it.polimi.ingsw.server.model.core.Game;
import it.polimi.ingsw.server.model.core.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class EndedState implements IGameState {
    private final Map<Player, Integer> finalRanking;

    public EndedState(Map<Player, Integer> finalRanking) {
        if (finalRanking == null) {
            throw new IllegalArgumentException("Final ranking cannot be null.");
        }

        this.finalRanking = new LinkedHashMap<>(finalRanking);
    }

    @Override
    public void nextState(Game game) {
        // Terminal state: no next state.
    }

    @Override
    public Map<Player, Integer> getFinalRanking(Game game) {
        return new LinkedHashMap<>(finalRanking);
    }

    @Override
    public boolean isEnded() {
        return true;
    }
}
